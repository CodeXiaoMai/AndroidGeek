package com.xiaomai.geek.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.suke.widget.SwitchButton
import com.tencent.bugly.beta.Beta
import com.xiaomai.geek.R
import com.xiaomai.geek.data.pref.ThemePref
import com.xiaomai.geek.event.ThemeEvent
import com.xiaomai.geek.ui.base.BaseActivity
import com.xiaomai.geek.ui.module.AboutUsActivity
import org.greenrobot.eventbus.EventBus

/**
 * Created by XiaoMai on 2017/6/9.
 */
class SettingActivity : BaseActivity() {

    var toolBar: Toolbar? = null
    var tvNightMode: TextView? = null
    var switchButton: SwitchButton? = null
    var llUpgrade: View? = null
    var llAboutUs: View? = null

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

        llUpgrade = findViewById(R.id.ll_upGrade)
        llUpgrade?.setOnClickListener { view ->
            // 第一个参数:是否为用户手动点击，第二个参数：是否提示用户正在检查更新
            Beta.checkUpgrade(true, false)
        }
        llAboutUs = findViewById(R.id.ll_about)
        llAboutUs?.setOnClickListener { view ->
            startActivity(Intent(this, AboutUsActivity::class.java))
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