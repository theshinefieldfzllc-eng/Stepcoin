package com.irfan.stepcoin

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.google.android.material.progressindicator.CircularProgressIndicator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(), SensorEventListener {

    private val GOAL = 6000

    // ---- AdMob TEST ids. Apni real AdMob ids se replace karein earning ke liye. ----
    private val REWARDED_ID = "ca-app-pub-3940256099942544/5224354917"

    private lateinit var sensorManager: SensorManager
    private var stepSensor: Sensor? = null
    private var rewardedAd: RewardedAd? = null

    private lateinit var stepsTv: TextView
    private lateinit var coinsTv: TextView
    private lateinit var goalTv: TextView
    private lateinit var streakTv: TextView
    private lateinit var distTv: TextView
    private lateinit var calTv: TextView
    private lateinit var ring: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        stepsTv = findViewById(R.id.stepsTv)
        coinsTv = findViewById(R.id.coinsTv)
        goalTv = findViewById(R.id.goalTv)
        streakTv = findViewById(R.id.streakTv)
        distTv = findViewById(R.id.distTv)
        calTv = findViewById(R.id.calTv)
        ring = findViewById(R.id.ring)
        ring.max = GOAL
        goalTv.text = "Goal ${fmt(GOAL)}"

        MobileAds.initialize(this) {}
        findViewById<AdView>(R.id.adView).loadAd(AdRequest.Builder().build())
        loadRewarded()

        findViewById<Button>(R.id.watchBtn).setOnClickListener { showRewarded() }
        findViewById<Button>(R.id.dailyBtn).setOnClickListener { claimDaily() }

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        checkDayRollover()
        ensureActivityPermission()
        updateUI()
    }

    override fun onResume() {
        super.onResume()
        checkDayRollover()
        if (hasActivityPermission()) {
            stepSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        }
        updateUI()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    // ---------------- Steps ----------------
    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null || event.sensor.type != Sensor.TYPE_STEP_COUNTER) return
        checkDayRollover()
        val total = event.values[0].toLong()
        val last = Prefs.getLong(this, "lastTotal", 0L)
        var delta = total - last
        if (last == 0L) delta = 0          // first run -> set baseline, don't count history
        if (delta < 0) delta = 0           // device rebooted (counter reset)
        if (delta > 20000) delta = 0       // ignore absurd jumps
        Prefs.setLong(this, "lastTotal", total)
        if (delta > 0) {
            val steps = Prefs.getInt(this, "todaySteps", 0) + delta.toInt()
            Prefs.setInt(this, "todaySteps", steps)
            awardStepCoins(steps)
        }
        updateUI()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun awardStepCoins(steps: Int) {
        // 10 coins per 1,000 steps
        val milestone = steps / 1000
        val claimed = Prefs.getInt(this, "milestones", 0)
        if (milestone > claimed) {
            addCoins((milestone - claimed) * 10)
            Prefs.setInt(this, "milestones", milestone)
        }
        // One-time daily goal bonus + streak
        if (steps >= GOAL && !Prefs.getBool(this, "goalRewarded", false)) {
            Prefs.setBool(this, "goalRewarded", true)
            addCoins(100)
            val today = dateStr(0)
            val yesterday = dateStr(-1)
            val lastGoal = Prefs.getStr(this, "lastGoalDate", "")
            val streak = if (lastGoal == yesterday) Prefs.getInt(this, "streak", 0) + 1 else 1
            Prefs.setInt(this, "streak", streak)
            Prefs.setStr(this, "lastGoalDate", today)
            toast("🎉 Daily goal complete! +100 coins")
        }
    }

    // ---------------- Coins / rewards ----------------
    private fun addCoins(n: Int) {
        Prefs.setInt(this, "coins", Prefs.getInt(this, "coins", 0) + n)
        updateUI()
    }

    private fun claimDaily() {
        val today = dateStr(0)
        if (Prefs.getStr(this, "dailyClaim", "") == today) {
            toast("Aaj ka daily reward le liya. Kal phir aayein 😊")
            return
        }
        Prefs.setStr(this, "dailyClaim", today)
        addCoins(25)
        toast("🎁 +25 coins!")
    }

    // ---------------- Ads ----------------
    private fun loadRewarded() {
        RewardedAd.load(this, REWARDED_ID, AdRequest.Builder().build(),
            object : RewardedAdLoadCallback() {
                override fun onAdLoaded(ad: RewardedAd) { rewardedAd = ad }
                override fun onAdFailedToLoad(e: LoadAdError) { rewardedAd = null }
            })
    }

    private fun showRewarded() {
        val ad = rewardedAd
        if (ad == null) {
            toast("Ad tayyar nahi — thodi der me phir try karein.")
            loadRewarded()
            return
        }
        ad.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() { rewardedAd = null; loadRewarded() }
            override fun onAdFailedToShowFullScreenContent(e: AdError) { rewardedAd = null; loadRewarded() }
        }
        ad.show(this, OnUserEarnedRewardListener {
            addCoins(50)
            toast("📺 +50 coins!")
        })
    }

    // ---------------- UI ----------------
    private fun updateUI() {
        val steps = Prefs.getInt(this, "todaySteps", 0)
        stepsTv.text = fmt(steps)
        coinsTv.text = fmt(Prefs.getInt(this, "coins", 0))
        ring.setProgressCompat(if (steps > GOAL) GOAL else steps, true)
        distTv.text = "%.2f km".format(steps * 0.0007)
        calTv.text = "${(steps * 0.04).toInt()} kcal"
        val lastGoal = Prefs.getStr(this, "lastGoalDate", "")
        val streak = if (lastGoal == dateStr(0) || lastGoal == dateStr(-1)) Prefs.getInt(this, "streak", 0) else 0
        streakTv.text = "🔥 $streak"
    }

    // ---------------- Day / date ----------------
    private fun checkDayRollover() {
        val today = dateStr(0)
        if (Prefs.getStr(this, "lastDate", "") != today) {
            Prefs.setStr(this, "lastDate", today)
            Prefs.setInt(this, "todaySteps", 0)
            Prefs.setBool(this, "goalRewarded", false)
            Prefs.setInt(this, "milestones", 0)
            if (Prefs.getStr(this, "lastGoalDate", "") != dateStr(-1)) {
                Prefs.setInt(this, "streak", 0)
            }
        }
    }

    private fun dateStr(offsetDays: Int): String {
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_YEAR, offsetDays)
        return SimpleDateFormat("yyyy-MM-dd", Locale.US).format(c.time)
    }

    private fun fmt(n: Int): String = String.format(Locale.US, "%,d", n)

    private fun toast(s: String) = Toast.makeText(this, s, Toast.LENGTH_SHORT).show()

    // ---------------- Permission ----------------
    private fun hasActivityPermission(): Boolean {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) return true
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) ==
            PackageManager.PERMISSION_GRANTED
    }

    private fun ensureActivityPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && !hasActivityPermission()) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACTIVITY_RECOGNITION), 10)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 10 && hasActivityPermission()) {
            stepSensor?.let { sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI) }
        }
    }
}
