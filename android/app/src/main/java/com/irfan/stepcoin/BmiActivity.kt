package com.irfan.stepcoin

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.appbar.MaterialToolbar

class BmiActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmi)

        setSupportActionBar(findViewById<MaterialToolbar>(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val heightUnit = findViewById<RadioGroup>(R.id.heightUnit)
        val heightCm = findViewById<EditText>(R.id.heightCm)
        val ftinRow = findViewById<LinearLayout>(R.id.ftinRow)
        val feet = findViewById<EditText>(R.id.feet)
        val inch = findViewById<EditText>(R.id.inch)
        val weightUnit = findViewById<RadioGroup>(R.id.weightUnit)
        val weightInput = findViewById<EditText>(R.id.weightInput)
        val result = findViewById<TextView>(R.id.result)

        heightUnit.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rbCm) {
                heightCm.visibility = View.VISIBLE
                ftinRow.visibility = View.GONE
            } else {
                heightCm.visibility = View.GONE
                ftinRow.visibility = View.VISIBLE
            }
        }

        weightUnit.setOnCheckedChangeListener { _, checkedId ->
            weightInput.hint = if (checkedId == R.id.rbKg) "Weight (kg)" else "Weight (lb)"
        }

        findViewById<Button>(R.id.calcBtn).setOnClickListener {
            // ----- height in cm -----
            val heightInCm: Double? = if (heightUnit.checkedRadioButtonId == R.id.rbCm) {
                heightCm.text.toString().toDoubleOrNull()
            } else {
                val ft = feet.text.toString().toDoubleOrNull()
                val inchVal = inch.text.toString().toDoubleOrNull() ?: 0.0
                if (ft == null) null else (ft * 30.48) + (inchVal * 2.54)
            }

            // ----- weight in kg -----
            val w = weightInput.text.toString().toDoubleOrNull()
            val weightInKg: Double? = if (w == null) null else {
                if (weightUnit.checkedRadioButtonId == R.id.rbKg) w else w * 0.453592
            }

            if (heightInCm == null || weightInKg == null || heightInCm <= 0 || weightInKg <= 0) {
                result.text = "Please enter a valid height and weight."
                return@setOnClickListener
            }

            val m = heightInCm / 100.0
            val bmi = weightInKg / (m * m)
            val cat = when {
                bmi < 18.5 -> "Underweight"
                bmi < 25 -> "Normal — keep it up! 💪"
                bmi < 30 -> "Overweight"
                else -> "Obese"
            }
            result.text = "Your BMI: %.1f\n%s".format(bmi, cat)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
