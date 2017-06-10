package com.xiaomai.geek.data.pref

import android.content.Context
import android.content.SharedPreferences
import com.xiaomai.geek.R

/**
 * Created by XiaoMai on 2017/6/8.
 */
class ThemePref {
    companion object {
        val PREF_NAME = "com.xiaomai.geek.theme_preference.xml"
        val KEY_THEME = "theme"

        private fun getPreferences(context: Context): SharedPreferences {
            return context.applicationContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }

        public fun saveTheme(context: Context, theme: Int) {
            getPreferences(context).edit().putInt(KEY_THEME, theme).apply()
        }

        public fun getTheme(context: Context): Int {
            return getPreferences(context).getInt(KEY_THEME, R.style.AppTheme)
        }
    }
}