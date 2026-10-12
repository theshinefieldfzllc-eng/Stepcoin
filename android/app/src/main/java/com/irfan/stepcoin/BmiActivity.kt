package com.irfan.stepcoin

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BmiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        val height = findViewById<EditText>(R.id.height)
        val weight = findViewById<EditText>(R.id.weight)
        val result = findViewById<TextView>(R.id.result)

        findViewById<Button>(R.id.calcBtn).setOnClickListener {
            val hc = height.text.toString().toDoubleOrNull()
            val wk = weight.text.toString().toDoubleOrNull()
            if (hc == null || wk == null || hc <= 0 || wk <= 0) {
                result.text = "Sahi height (cm) aur weight (kg) daalein."
                return@setOnClickListener
            }
            val m = hc / 100.0
            val bmi = wk / (m * m)
            val cat = when {
                bmi < 18.5 -> "Underweight"
                bmi < 25 -> "Normal — keep it up! 💪"
                bmi < 30 -> "Overweight"
                else -> "Obese"
            }
            result.text = "Your BMI: %.1f\n%s".format(bmi, cat)
        }
    }
}
