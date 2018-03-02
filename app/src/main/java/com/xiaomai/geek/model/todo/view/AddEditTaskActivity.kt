package com.xiaomai.geek.model.todo.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.xiaomai.geek.R
import com.xiaomai.geek.base.BaseViewModelActivity
import com.xiaomai.geek.base.observer.BaseCompletableObserver
import com.xiaomai.geek.common.Const
import com.xiaomai.geek.databinding.AddEditTaskActivityBinding
import com.xiaomai.geek.db.Task
import com.xiaomai.geek.model.todo.viewmodel.TaskViewModel
import kotlinx.android.synthetic.main.add_edit_task_activity.*
import kotlinx.android.synthetic.main.geek_base_activity.*

/**
 * Created by wangce on 2018/2/24.
 */
class AddEditTaskActivity : BaseViewModelActivity<TaskViewModel>() {

    companion object {
        fun launch(context: Context, task: Task?) {
            Intent(context, AddEditTaskActivity::class.java).apply {
                putExtra(Const.DATA, task)
                context.startActivity(this)
            }
        }
    }

    override fun getViewModelClazz(): Class<TaskViewModel> = TaskViewModel::class.java

    override fun getLayoutId(): Int = R.layout.add_edit_task_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        swipe_refresh_layout.isEnabled = false

        title_view.setTitle("新建")

        val task = intent.getSerializableExtra(Const.DATA) as? Task
        task?.apply {
            title_view.setTitle("编辑")
            viewModel.title.set(title)
            viewModel.content.set(content)
            viewModel.completed.set(complete)
            viewModel.priority.set(priority)
        }

        (dataBinding as AddEditTaskActivityBinding).value = viewModel

        title_view.setMenu("保存", listener = View.OnClickListener {
            viewModel.saveTask(task?.id, object : BaseCompletableObserver() {
                override fun onComplete() {
                    finish()
                }
            })
        })

        radio_group.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_low -> {
                    viewModel.priority.set(1)
                }
                R.id.rb_middle -> {
                    viewModel.priority.set(2)
                }
                R.id.rb_high -> {
                    viewModel.priority.set(3)
                }
                R.id.rb_highest -> {
                    viewModel.priority.set(4)
                }
            }
        }

        checkbox.setOnCheckedChangeListener { _, isChecked ->
            viewModel.completed.set(isChecked)
        }
    }
}