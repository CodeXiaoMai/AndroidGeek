package com.xiaomai.geek.todo.view

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.xiaomai.geek.base.BaseAdapter
import com.xiaomai.geek.base.BaseListActivity
import com.xiaomai.geek.databinding.TaskItemBinding
import com.xiaomai.geek.db.Task
import com.xiaomai.geek.todo.viewmodel.TaskViewModel
import io.reactivex.SingleObserver
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.main_activity.*

/**
 * Created by wangce on 2018/2/23.
 */
class TasksListActivity : BaseListActivity<Task, TaskItemBinding, TaskViewModel>() {
    override fun loadList() {
        viewModel.getTasks(object : SingleObserver<List<Task>> {
            override fun onSuccess(t: List<Task>) {
                mAdapter?.values = t
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
            }
        })
//
//        viewModel.deleteAllTasks(object : CompletableObserver{
//            override fun onComplete() {
//                Snackbar.make(title_view, "删除成功", Snackbar.LENGTH_SHORT).show()
//            }
//
//            override fun onSubscribe(d: Disposable) {
//            }
//
//            override fun onError(e: Throwable) {
//            }
//        })
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    override fun getAdapter(): BaseAdapter<Task, TaskItemBinding> = TaskAdapter()

    override fun getViewModelClazz(): Class<TaskViewModel> = TaskViewModel::class.java

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        title_view.setTitle("TODOs")

        title_view.setMenu("新建", listener = View.OnClickListener {
            startActivity(Intent(this@TasksListActivity, AddEditTaskActivity::class.java))
        })
    }
}