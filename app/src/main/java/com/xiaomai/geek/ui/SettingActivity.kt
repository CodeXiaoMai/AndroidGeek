package com.xiaomai.geek.ui

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.TextView
import com.suke.widget.SwitchButton
import com.xiaomai.geek.R
import com.xiaomai.geek.data.pref.ThemePref
import com.xiaomai.geek.event.ThemeEvent
import com.xiaomai.geek.ui.base.BaseActivity
import org.greenrobot.eventbus.EventBus

/**
 * Created by XiaoMai on 2017/6/9.
 */
class SettingActivity : BaseActivity() {

    var toolBar: Toolbar? = null
    var tvNightMode: TextView? = null
    var switchButton: SwitchButton? = null

    companion object {
        var flag: Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        initViews()
    }

    private fun initViews() {
        setTitle("设置")
        toolBar = findViewById(R.id.tool_bar) as Toolbar
        toolBar?.let {
            setSupportActionBar(toolBar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        tvNightMode = findViewById(R.id.tv_night_mode) as TextView

        switchButton = findViewById(R.id.switch_button) as SwitchButton
        switchButton?.isChecked = ThemePref.getTheme(this) == R.style.NightTheme
        switchButton?.setOnCheckedChangeListener { view, isChecked ->
            flag = !flag
            if (isChecked) {
                ThemePref.saveTheme(this, R.style.NightTheme)
                setTheme(R.style.NightTheme)
            } else {
                ThemePref.saveTheme(this, R.style.AppTheme)
                setTheme(R.style.AppTheme)
            }
            EventBus.getDefault().post(ThemeEvent())
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}