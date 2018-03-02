package com.xiaomai.geek.model.todo.view

import android.view.View
import com.xiaomai.geek.R
import com.xiaomai.geek.base.BaseAdapter
import com.xiaomai.geek.databinding.TaskItemBinding
import com.xiaomai.geek.db.Task
import kotlinx.android.synthetic.main.task_item.view.*


/**
 * Created by wangce on 2018/2/23.
 */
class TaskAdapter : BaseAdapter<Task, TaskItemBinding>(R.layout.task_item) {

    var isEdit = false
        set(value) {
            field = value
            if (!value) {
                values.forEach {
                    it.isChecked = false
                }
            }
            notifyDataSetChanged()
        }

    lateinit var callback: Callback

    override fun onBindViewHolder(holder: Holder<TaskItemBinding>, position: Int) {
        super.onBindViewHolder(holder, position)

        val task = getItem(position)

        if (isEdit) {
            holder.itemView.checkbox.isChecked = task.isChecked
            holder.itemView.checkbox.visibility = View.VISIBLE
        } else {
            holder.itemView.checkbox.visibility = View.GONE
        }

        holder.itemView.setOnClickListener {
            callback.onClick(isEdit, task)
        }

        holder.itemView.setOnClickListener {
            if (isEdit) {
                task.isChecked = !task.isChecked
                holder.itemView.checkbox.isChecked = task.isChecked
            } else {
                AddEditTaskActivity.launch(holder.itemView.context, task)
            }
        }

        holder.itemView.setOnLongClickListener {
            return@setOnLongClickListener callback.onLongClick(task)
        }
    }

    interface Callback {
        fun onClick(isEdit: Boolean, task: Task)
        fun onLongClick(task: Task): Boolean
    }
}