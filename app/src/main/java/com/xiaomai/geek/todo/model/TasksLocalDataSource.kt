package com.xiaomai.geek.todo.model

import android.os.Environment
import android.text.TextUtils
import com.xiaomai.geek.application.GeekApplication
import com.xiaomai.geek.base.BaseSingleObserver
import com.xiaomai.geek.common.wrapper.GeeKLog
import com.xiaomai.geek.db.Task
import io.reactivex.Completable
import io.reactivex.Single
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.*

/**
 * Created by wangce on 2018/2/23.
 */
class TasksLocalDataSource : TaskDataSource {

    override fun getTasks(): Single<MutableList<Task>> {
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

    private fun saveTasks(tasks: MutableList<Task>) {
        GeekApplication.DAO_SESSION.taskDao.saveInTx(tasks)
    }

    override fun deleteTask(taskId: Long): Completable {
        return Completable.fromAction {
            GeekApplication.DAO_SESSION.taskDao.deleteByKey(taskId)
        }
    }

    override fun deleteTasks(tasks: MutableList<Task>): Completable {
        return Completable.fromAction {
            GeekApplication.DAO_SESSION.taskDao.deleteInTx(tasks)
        }
    }

    override fun deleteAllTasks(): Completable {
        return Completable.fromAction {
            GeekApplication.DAO_SESSION.taskDao.deleteAll()
        }
    }

    override fun backup(): Completable {
        return Completable.fromAction {
            getTasks().subscribe(object : BaseSingleObserver<MutableList<Task>>() {
                override fun onSuccess(t: MutableList<Task>) {
                    backup(t, "${Environment.getExternalStorageDirectory().absolutePath}/backup.xml")
                }
            })
        }
    }

    override fun import(path: String): Completable {
        return Completable.fromAction {
            val fileInputStream = FileInputStream(path)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            var line: String? = ""
            val stringBuilder = StringBuilder()
            while (line != null) {
                line = bufferedReader.readLine()
                line?.apply {
                    stringBuilder.append(line)
                }
            }
            bufferedReader.close()
            inputStreamReader.close()
            fileInputStream.close()

            val stringReader = StringReader(stringBuilder.toString())

            val xmlPullParser = XmlPullParserFactory.newInstance().newPullParser()
            xmlPullParser.setInput(stringReader)
            var eventType: Int = xmlPullParser.eventType

            var task: Task? = null
            val taskList = mutableListOf<Task>()

            while (eventType != XmlPullParser.END_DOCUMENT) {
                val nodeName = xmlPullParser.name
                when (eventType) {
                    XmlPullParser.START_TAG -> {
                        when (nodeName) {
                            TAG_TASK -> task = Task()
                            TAG_TITLE -> task?.title = xmlPullParser.nextText()
                            TAG_CONTENT -> task?.content = xmlPullParser.nextText()
                            TAG_PRIORITY -> task?.priority = xmlPullParser.nextText().toInt()
                            TAG_COMPLETE -> task?.complete = xmlPullParser.nextText().toBoolean()
                            TAG_CREATE_TIME -> task?.createTime = xmlPullParser.nextText().toLong()
                        }
                    }
                    XmlPullParser.END_TAG -> {
                        if (TextUtils.equals(TAG_TASK, nodeName) && task != null) {
                            taskList.add(task)
                            task = null
                        }
                    }
                }
                eventType = xmlPullParser.next()
            }

            saveTasks(taskList)
        }
    }

    private fun backup(tasks: MutableList<Task>, path: String) {
        if (tasks.isEmpty()) return

        val stringWriter = StringWriter()
        val xmlPullParserFactory = XmlPullParserFactory.newInstance()
        val xmlSerializer = xmlPullParserFactory.newSerializer()

        xmlSerializer.apply {
            setOutput(stringWriter)
            startDocument("UTF-8", true)
            startTag("", TAG_TASK_LIST)
            tasks.forEach { task ->
                startTag("", TAG_TASK)

                startTag("", TAG_TITLE)
                text(task.title ?: "")
                endTag("", TAG_TITLE)

                startTag("", TAG_CONTENT)
                text(task.content ?: "")
                endTag("", TAG_CONTENT)

                startTag("", TAG_PRIORITY)
                text(task.priority.toString())
                endTag("", TAG_PRIORITY)

                startTag("", TAG_COMPLETE)
                text(task.complete.toString())
                endTag("", TAG_COMPLETE)

                startTag("", TAG_CREATE_TIME)
                text(task.createTime.toString())
                endTag("", TAG_CREATE_TIME)

                endTag("", TAG_TASK)
            }
            endTag("", TAG_TASK_LIST)
            endDocument()

            GeeKLog.e(stringWriter.toString())

            File(path).apply {
                if (exists()) {
                    delete()
                }
            }
            val fileOutputStream = FileOutputStream(path)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            outputStreamWriter.write(stringWriter.toString())
            outputStreamWriter.close()
            fileOutputStream.close()
        }
    }
}

const val TAG_TASK_LIST = "task_list"
const val TAG_TASK = "task"
const val TAG_TITLE = "title"
const val TAG_CONTENT = "content"
const val TAG_PRIORITY = "priority"
const val TAG_COMPLETE = "complete"
const val TAG_CREATE_TIME = "create_time"