package com.xiaomai.geek.setting.view

import android.app.AlertDialog
import android.os.Bundle
import com.xiaomai.geek.R
import com.xiaomai.geek.base.BaseCompletableObserver
import com.xiaomai.geek.base.BaseViewModelActivity
import com.xiaomai.geek.setting.viewmodel.SettingViewModel
import kotlinx.android.synthetic.main.geek_base_activity.*
import kotlinx.android.synthetic.main.setting_activity.*

/**
 * Created by wangce on 2018/2/27.
 */
class SettingActivity : BaseViewModelActivity<SettingViewModel>() {
    override fun getViewModelClazz(): Class<SettingViewModel> = SettingViewModel::class.java

    override fun getLayoutId(): Int = R.layout.setting_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title_view.setTitle(R.string.setting)

        swipe_refresh_layout.isEnabled = false

        layout_clear_data.setOnClickListener {
            AlertDialog.Builder(this@SettingActivity)
                    .setMessage("确认删除所有任务吗？")
                    .setPositiveButton("确定", { _, _ ->
                        viewModel.clearAllTasks(object : BaseCompletableObserver() {
                            override fun onComplete() {
                                showSnackBar("删除成功")
                            }
                        })
                    })
                    .setNegativeButton("取消", null)
                    .create().show()
        }

        layout_backup.setOnClickListener {
            viewModel.backup(object : BaseCompletableObserver() {
                override fun onComplete() {
                    showSnackBar("备份成功")
                }
            })
        }

        layout_import.setOnClickListener {
            viewModel.import(object : BaseCompletableObserver() {
                override fun onComplete() {
                    showSnackBar("导入成功")
                }
            })
        }
    }
}