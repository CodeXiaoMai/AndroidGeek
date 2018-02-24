package com.xiaomai.geek.todo.model

import com.xiaomai.geek.db.Task
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by wangce on 2018/2/23.
 */
class TasksRepository(private val taskDataSource: TaskDataSource) : TaskDataSource {

    override fun getTasks(): Single<List<Task>> = taskDataSource.getTasks()

    override fun getTask(taskId: Long): Single<Task> = taskDataSource.getTask(taskId)

    override fun saveTask(task: Task): Completable = taskDataSource.saveTask(task)

    override fun deleteTask(taskId: Long): Completable = taskDataSource.deleteTask(taskId)

    override fun deleteAllTasks(): Completable = taskDataSource.deleteAllTasks()
}