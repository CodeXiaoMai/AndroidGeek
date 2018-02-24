package com.xiaomai.geek.todo.viewmodel

import android.app.Application
import android.databinding.ObservableField
import com.xiaomai.geek.base.BaseViewModel
import com.xiaomai.geek.common.PageStatus
import com.xiaomai.geek.db.Task
import com.xiaomai.geek.todo.model.TasksLocalDataSource
import com.xiaomai.geek.todo.model.TasksRepository
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangce on 2018/2/23.
 */
class TaskViewModel(context: Application) : BaseViewModel(context) {

    var title: ObservableField<String> = ObservableField()

    var content: ObservableField<String> = ObservableField()

    private val taskRepository: TasksRepository = TasksRepository(TasksLocalDataSource())

    fun saveTask(taskId: Long? = null, completableObserver: CompletableObserver) {
        val task = Task().apply {
            id = taskId
            title = this@TaskViewModel.title.get()
            content = this@TaskViewModel.content.get()
        }

        if (task.isEmtpy) {
            showSnackBar("标题和内容不能都为空")
            return
        }

        taskRepository
                .saveTask(task)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    pageStatus.value = PageStatus.LOADING
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(completableObserver)
    }

    fun getTasks(singleObserver: SingleObserver<List<Task>>) {
        taskRepository.getTasks()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    pageStatus.value = PageStatus.LOADING
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess {
                    if (it.isNotEmpty()) {
                        pageStatus.value = PageStatus.NORMAL
                    } else {
                        pageStatus.value = PageStatus.EMPTY
                    }
                }
                .subscribe(singleObserver)
    }

    fun deleteAllTasks(completableObserver: CompletableObserver) {
        taskRepository.deleteAllTasks()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    pageStatus.value = PageStatus.LOADING
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(completableObserver)
    }
}