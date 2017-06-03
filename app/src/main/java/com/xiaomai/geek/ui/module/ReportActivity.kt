package com.xiaomai.geek.ui.module

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import com.xiaomai.geek.R
import com.xiaomai.geek.ui.base.BaseActivity

class ReportActivity : BaseActivity() {

    var toolBar: Toolbar? = null
    var etTitle: EditText? = null
    var etContent: EditText? = null
    var etContact: EditText? = null
    var btOk: Button? = null

    companion object {
        fun launch(context: Context): Unit {
            context.startActivity(Intent(context, ReportActivity::class.java))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_report)
        initViews()
    }

    private fun initViews() {
        toolBar = findViewById(R.id.tool_bar) as Toolbar
        etTitle = findViewById(R.id.et_title) as EditText
        etContent = findViewById(R.id.et_content) as EditText
        etContact = findViewById(R.id.et_contact) as EditText
        btOk = findViewById(R.id.bt_ok) as Button

        toolBar?.title = "意见反馈"
        setSupportActionBar(toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btOk?.setOnClickListener { v ->
            postReport()
        }
    }

    private fun postReport() {

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
