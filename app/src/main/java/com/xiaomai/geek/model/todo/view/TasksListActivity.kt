package com.xiaomai.geek.model.todo.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import android.widget.AdapterView
import com.xiaomai.geek.R
import com.xiaomai.geek.base.BaseAdapter
import com.xiaomai.geek.base.BaseListActivity
import com.xiaomai.geek.base.observer.BaseCompletableObserver
import com.xiaomai.geek.base.observer.BaseSingleObserver
import com.xiaomai.geek.databinding.TaskItemBinding
import com.xiaomai.geek.db.Task
import com.xiaomai.geek.model.todo.viewmodel.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.tasks_list_activity.*

/**
 * Created by wangce on 2018/2/23.
 */
class TasksListActivity : BaseListActivity<Task, TaskItemBinding, TaskViewModel>() {

    override fun loadList() {
        viewModel.getTasks(object : BaseSingleObserver<MutableList<Task>>() {
            override fun onSuccess(t: MutableList<Task>) {
                mAdapter.values = t
            }
        })
    }

    override fun getLayoutId(): Int = R.layout.tasks_list_activity

    override fun getLayoutManager(): RecyclerView.LayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    override fun getAdapter(): BaseAdapter<Task, TaskItemBinding> = TaskAdapter()

    override fun getViewModelClazz(): Class<TaskViewModel> = TaskViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title_view.setTitle("TODOs")

        title_view.setMenu("新建", listener = View.OnClickListener {
            startActivity(Intent(this@TasksListActivity, AddEditTaskActivity::class.java))
            (mAdapter as TaskAdapter).isEdit = false
        })

        (mAdapter as TaskAdapter).callback = object : TaskAdapter.Callback {
            override fun onClick(isEdit: Boolean, task: Task) {
                if (isEdit) {
                    task.isChecked = !task.isChecked
                    mAdapter.notifyDataSetChanged()
                } else {
                    AddEditTaskActivity.launch(this@TasksListActivity, task)
                }
            }

            override fun onLongClick(task: Task): Boolean {
                task.isChecked = true
                (mAdapter as TaskAdapter).isEdit = true
                edit_layout.visibility = View.VISIBLE
                return true
            }
        }

        delete.setOnClickListener {
            AlertDialog.Builder(this@TasksListActivity)
                    .setMessage("确认删除？")
                    .setPositiveButton("确认", { _, _ ->
                        deletTasks()
                    })
                    .setNegativeButton("取消", null)
                    .create().show()
        }

        select_all.setOnClickListener {
            mAdapter.values.forEach {
                it.isChecked = true
            }
            mAdapter.notifyDataSetChanged()
        }

        select_invert.setOnClickListener {
            mAdapter.values.forEach {
                it.isChecked = !it.isChecked
            }
            mAdapter.notifyDataSetChanged()
        }

        priority.setOnClickListener {
            mAdapter.values = viewModel.sortTasks(SORT_TYPE_PRIORITY)
        }

        create_time.setOnClickListener {
            mAdapter.values = viewModel.sortTasks(SORT_TYPE_TIME)
        }

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> mAdapter.values = viewModel.filterTasks(ALL)
                    1 -> mAdapter.values = viewModel.filterTasks(COMPLETED)
                    2 -> mAdapter.values = viewModel.filterTasks(UNCOMPLETED)
                }
            }
        }
    }

    private fun deletTasks() {
        val deleteList = mutableListOf<Task>()
        mAdapter.values.forEach {
            if (it.isChecked) {
                deleteList.add(it)
            }
        }
        viewModel.deleteTasks(deleteList, object : BaseCompletableObserver() {
            override fun onComplete() {
                onBackPressed()
                loadList()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadList()
    }

    override fun onBackPressed() {
        if ((mAdapter as TaskAdapter).isEdit) {
            (mAdapter as TaskAdapter).isEdit = false
            edit_layout.visibility = View.GONE
            return
        }
        super.onBackPressed()
    }
}