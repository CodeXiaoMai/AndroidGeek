package com.xiaomai.geek.todo.model

import com.xiaomai.geek.db.Task
import io.reactivex.Completable
import io.reactivex.Single


/**
 * Created by wangce on 2018/2/23.
 */
interface TaskDataSource {

    fun getTasks(): Single<List<Task>>

    fun getTask(taskId: Long): Single<Task>

    fun saveTask(task: Task): Completable

    fun deleteTask(taskId: Long): Completable

    fun deleteAllTasks(): Completable
}