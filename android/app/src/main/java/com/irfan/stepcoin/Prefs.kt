package com.irfan.stepcoin

import android.content.Context

object Prefs {
    private const val P = "stepcoin"
    private fun sp(c: Context) = c.getSharedPreferences(P, Context.MODE_PRIVATE)

    fun getInt(c: Context, k: String, d: Int) = sp(c).getInt(k, d)
    fun setInt(c: Context, k: String, v: Int) = sp(c).edit().putInt(k, v).apply()

    fun getLong(c: Context, k: String, d: Long) = sp(c).getLong(k, d)
    fun setLong(c: Context, k: String, v: Long) = sp(c).edit().putLong(k, v).apply()

    fun getStr(c: Context, k: String, d: String) = sp(c).getString(k, d) ?: d
    fun setStr(c: Context, k: String, v: String) = sp(c).edit().putString(k, v).apply()

    fun getBool(c: Context, k: String, d: Boolean) = sp(c).getBoolean(k, d)
    fun setBool(c: Context, k: String, v: Boolean) = sp(c).edit().putBoolean(k, v).apply()
}
