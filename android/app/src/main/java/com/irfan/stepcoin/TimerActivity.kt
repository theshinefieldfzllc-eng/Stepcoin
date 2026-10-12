package com.irfan.stepcoin

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TimerActivity : AppCompatActivity() {

    private var timer: CountDownTimer? = null
    private var remaining = 60_000L
    private var running = false

    private lateinit var timeTv: TextView
    private lateinit var startBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        timeTv = findViewById(R.id.timeTv)
        startBtn = findViewById(R.id.startBtn)

        findViewById<Button>(R.id.p30).setOnClickListener { setTime(30_000) }
        findViewById<Button>(R.id.p60).setOnClickListener { setTime(60_000) }
        findViewById<Button>(R.id.p90).setOnClickListener { setTime(90_000) }
        findViewById<Button>(R.id.p120).setOnClickListener { setTime(120_000) }
        startBtn.setOnClickListener { if (running) pause() else start() }
        findViewById<Button>(R.id.resetBtn).setOnClickListener { reset() }

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
        val s = (remaining / 1000).toInt()
        timeTv.text = "%02d:%02d".format(s / 60, s % 60)
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

    override fun onDestroy() {
        super.onDestroy()
        timer?.cancel()
    }
}
