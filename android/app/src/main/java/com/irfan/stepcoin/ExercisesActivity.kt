package com.irfan.stepcoin

import android.graphics.Typeface
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class ExercisesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercises)

        val container = findViewById<LinearLayout>(R.id.container)
        val d = resources.displayMetrics.density
        val pad = (14 * d).toInt()

        for ((category, list) in Exercises.data) {
            val header = TextView(this).apply {
                text = category
                textSize = 20f
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(ContextCompat.getColor(this@ExercisesActivity, R.color.primary))
                setPadding(0, pad + pad / 2, 0, pad / 2)
            }
            container.addView(header)

            for (ex in list) {
                val row = TextView(this).apply {
                    text = "${ex.name}   ·   ${ex.sets}"
                    textSize = 16f
                    setPadding(pad / 2, pad, pad / 2, pad)
                    isClickable = true
                    setBackgroundResource(android.R.drawable.list_selector_background)
                    setOnClickListener { showDetail(ex) }
                }
                container.addView(row)

                val divider = View(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1
                    )
                    setBackgroundColor(0x22000000)
                }
                container.addView(divider)
            }
        }
    }

    private fun showDetail(ex: Exercise) {
        AlertDialog.Builder(this)
            .setTitle(ex.name)
            .setMessage("Target: ${ex.muscle}\n\nHow to do it:\n${ex.how}\n\nSuggested: ${ex.sets}")
            .setPositiveButton("Got it", null)
            .show()
    }
}
