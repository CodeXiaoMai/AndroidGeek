package com.xiaomai.geek.setting.viewmodel

import android.app.Application
import android.os.Environment
import com.xiaomai.geek.base.BaseViewModel
import com.xiaomai.geek.todo.model.TaskDataSource
import com.xiaomai.geek.todo.model.TasksLocalDataSource
import com.xiaomai.geek.todo.model.TasksRepository
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by wangce on 2018/2/27.
 */
class SettingViewModel(context: Application) : BaseViewModel(context) {

    private val tasksRepository: TaskDataSource by lazy {
        TasksRepository(TasksLocalDataSource())
    }

    fun clearAllTasks(completableObserver: CompletableObserver) {
        tasksRepository.deleteAllTasks()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(completableObserver)
    }

    fun backup(completableObserver: CompletableObserver) {
        tasksRepository.backup()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(completableObserver)
    }

    fun import(completableObserver: CompletableObserver) {
        tasksRepository.import("${Environment.getExternalStorageDirectory()}/backup.xml")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(completableObserver)
    }
}