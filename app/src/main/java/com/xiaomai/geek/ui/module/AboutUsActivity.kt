package com.xiaomai.geek.ui.module

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import com.xiaomai.geek.BuildConfig
import com.xiaomai.geek.R
import com.xiaomai.geek.ui.base.BaseActivity

/**
 * Created by XiaoMai on 2017/6/13.
 */
class AboutUsActivity : BaseActivity() {

    var toolBar: Toolbar? = null
    var tvVersion: TextView? = null
    var tvDownloadUrl: TextView? = null

    private var mVersionName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aboutus)
        initViews()
        initData()
    }

    private fun initData() {
        try {
            mVersionName = packageManager.getPackageInfo(packageName, 0).versionName
            var versionValue: String = "版本号:$mVersionName"
            if (BuildConfig.DEBUG)
                versionValue = "$versionValue-debug"
            tvVersion?.text = versionValue
            tvDownloadUrl?.text = getString(R.string.lite_version, mVersionName)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun initViews() {
        toolBar = findViewById(R.id.tool_bar) as Toolbar
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        tvVersion = findViewById(R.id.tv_version) as TextView
        tvDownloadUrl = findViewById(R.id.tv_download_url) as TextView
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.about_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            R.id.menu_share -> {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.app_share_info, getString(R.string.pro_version, mVersionName)))
                startActivity(Intent.createChooser(intent, "分享到"))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}