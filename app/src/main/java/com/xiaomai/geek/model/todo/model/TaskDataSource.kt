package com.xiaomai.geek.model.todo.model

import com.xiaomai.geek.db.Task
import io.reactivex.Completable
import io.reactivex.Single


/**
 * Created by wangce on 2018/2/23.
 */
interface TaskDataSource {

    fun getTasks(): Single<MutableList<Task>>

    fun getTask(taskId: Long): Single<Task>

    fun saveTask(task: Task): Completable

    fun deleteTask(taskId: Long): Completable

    fun deleteTasks(tasks: MutableList<Task>): Completable

    fun deleteAllTasks(): Completable

    fun backup(): Completable

    fun import(path: String): Completable
}