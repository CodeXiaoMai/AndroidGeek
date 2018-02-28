package com.xiaomai.geek.todo.viewmodel

import android.app.Application
import android.databinding.ObservableField
import android.support.annotation.StringDef
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

    var priority: ObservableField<Int> = ObservableField(0)

    var completed: ObservableField<Boolean> = ObservableField(false)

    private lateinit var tasks: MutableList<Task>

    private var priorityAsc = true
    private var timeAsc = true

    private val taskRepository: TasksRepository = TasksRepository(TasksLocalDataSource())

    fun saveTask(taskId: Long? = null, completableObserver: CompletableObserver) {
        val task = Task().apply {
            id = taskId
            title = this@TaskViewModel.title.get()
            content = this@TaskViewModel.content.get()
            priority = this@TaskViewModel.priority.get()
            complete = this@TaskViewModel.completed.get()
            createTime = System.currentTimeMillis()
        }

        if (task.isEmpty) {
            showSnackBar("标题和内容不能都为空")
            return
        }

        if (task.priority == 0) {
            showSnackBar("请选择优先级")
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

    fun getTasks(singleObserver: SingleObserver<MutableList<Task>>) {
        taskRepository.getTasks()
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    pageStatus.value = PageStatus.LOADING
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterSuccess {
                    tasks = it
                    if (it.isNotEmpty()) {
                        pageStatus.value = PageStatus.NORMAL
                    } else {
                        pageStatus.value = PageStatus.EMPTY
                    }
                }
                .subscribe(singleObserver)
    }

    fun deleteTasks(tasks: MutableList<Task>, completableObserver: CompletableObserver) {
        if (tasks.isEmpty()) {
            snackMessage.value = "请选择要删除的任务"
            return
        }
        taskRepository.deleteTasks(tasks)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    pageStatus.value = PageStatus.LOADING
                }
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate {
                    pageStatus.value = PageStatus.NORMAL
                    snackMessage.value = "删除成功"
                }
                .subscribe(completableObserver)
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

    fun sortTasks(@TaskSortType sortType: String): MutableList<Task> {
        when (sortType) {
            SORT_TYPE_PRIORITY -> {
                priorityAsc = !priorityAsc
                if (priorityAsc) {
                    tasks.sortBy {
                        it.priority
                    }
                } else {
                    tasks.sortByDescending {
                        it.priority
                    }
                }
            }
            SORT_TYPE_TIME -> {
                timeAsc = !timeAsc
                if (timeAsc) {
                    tasks.sortBy {
                        it.id
                    }
                } else {
                    tasks.sortByDescending {
                        it.id
                    }
                }
            }
        }
        return tasks
    }

    fun filterTasks(@FilterType filterType: String): MutableList<Task> {
        when (filterType) {
            ALL -> {
                return tasks
            }
            COMPLETED -> {
                return tasks.filter {
                    it.complete
                }.toMutableList()
            }
            UNCOMPLETED -> {
                return tasks.filter {
                    !it.complete
                }.toMutableList()
            }
        }
        return tasks
    }
}


@StringDef(SORT_TYPE_PRIORITY, SORT_TYPE_TIME)
annotation class TaskSortType

@StringDef(ALL, COMPLETED, UNCOMPLETED)
annotation class FilterType

const val SORT_TYPE_PRIORITY: String = "PRIORITY"
const val SORT_TYPE_TIME: String = "TIME"

const val ALL: String = "ALL"
const val COMPLETED: String = "COMPLETED"
const val UNCOMPLETED: String = "UNCOMPLETED"