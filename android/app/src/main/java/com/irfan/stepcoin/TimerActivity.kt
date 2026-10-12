package com.irfan.stepcoin

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class TimerActivity : AppCompatActivity() {

    private var timer: CountDownTimer? = null
    private var remaining = 60_000L
    private var running = false

    private lateinit var timeTv: TextView
    private lateinit var startBtn: Button
    private lateinit var minInput: EditText
    private lateinit var secInput: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        timeTv = findViewById(R.id.timeTv)
        startBtn = findViewById(R.id.startBtn)
        minInput = findViewById(R.id.minInput)
        secInput = findViewById(R.id.secInput)

        findViewById<Button>(R.id.setBtn).setOnClickListener { setCustom() }
        findViewById<Button>(R.id.p30).setOnClickListener { setTime(30_000) }
        findViewById<Button>(R.id.p60).setOnClickListener { setTime(60_000) }
        findViewById<Button>(R.id.p90).setOnClickListener { setTime(90_000) }
        findViewById<Button>(R.id.p120).setOnClickListener { setTime(120_000) }
        findViewById<Button>(R.id.p300).setOnClickListener { setTime(300_000) }
        startBtn.setOnClickListener { if (running) pause() else start() }
        findViewById<Button>(R.id.resetBtn).setOnClickListener { reset() }

        render()
    }

    private fun setCustom() {
        if (running) { toast("Pause first to change the time."); return }
        val min = minInput.text.toString().toLongOrNull() ?: 0L
        val sec = secInput.text.toString().toLongOrNull() ?: 0L
        val totalMs = (min * 60L + sec) * 1000L
        if (totalMs <= 0L) { toast("Enter minutes and/or seconds."); return }
        remaining = totalMs
        render()
    }

    private fun setTime(ms: Long) {
        if (!running) { remaining = ms; render() }
    }

    private fun start() {
        if (remaining <= 0) return
        timer?.cancel()
        timer = object : CountDownTimer(remaining, 1000) {
            override fun onTick(ms: Long) { remaining = ms; render() }
            override fun onFinish() { remaining = 0; running = false; startBtn.text = "Start"; render(); buzz() }
        }.start()
        running = true
        startBtn.text = "Pause"
    }

    private fun pause() {
        timer?.cancel()
        running = false
        startBtn.text = "Start"
    }

    private fun reset() {
        timer?.cancel()
        running = false
        remaining = 60_000
        startBtn.text = "Start"
        render()
    }

    private fun render() {
        val totalSec = (remaining / 1000).toInt()
        val h = totalSec / 3600
        val m = (totalSec % 3600) / 60
        val s = totalSec % 60
        timeTv.text = if (h > 0)
            "%d:%02d:%02d".format(h, m, s)
        else
            "%02d:%02d".format(m, s)
    }

    private fun buzz() {
        val v = getSystemService(VIBRATOR_SERVICE) as? Vibrator ?: return
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(600, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION") v.vibrate(600)
            }
        } catch (e: Exception) {
            // ignore
        }
    }

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_SHORT).show()

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
