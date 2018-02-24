package com.xiaomai.geek.todo.model

import com.xiaomai.geek.application.GeekApplication
import com.xiaomai.geek.db.Task
import io.reactivex.Completable
import io.reactivex.Single

/**
 * Created by wangce on 2018/2/23.
 */
class TasksLocalDataSource : TaskDataSource {

    override fun getTasks(): Single<List<Task>> {
        return Single.create {
            val list = GeekApplication.DAO_SESSION.taskDao.loadAll()
            if (list != null) {
                it.onSuccess(list)
            } else {
                it.onError(Throwable("no task"))
            }
        }
    }

    override fun getTask(taskId: Long): Single<Task> {
        return Single.create<Task> {
            val list = GeekApplication.DAO_SESSION.taskDao.queryRaw("where id = ?", taskId.toString())
            if (list.isNotEmpty()) {
                it.onSuccess(list[0])
            } else {
                it.onError(Throwable("no task"))
            }
        }
    }

    override fun saveTask(task: Task): Completable {
        return Completable.fromAction {
            GeekApplication.DAO_SESSION.taskDao.save(task)
        }
    }

    override fun deleteTask(taskId: Long): Completable {
        return Completable.fromAction {
            GeekApplication.DAO_SESSION.taskDao.deleteByKey(taskId)
        }
    }

    override fun deleteAllTasks(): Completable {
        return Completable.fromAction {
            GeekApplication.DAO_SESSION.taskDao.deleteAll()
        }
    }
}