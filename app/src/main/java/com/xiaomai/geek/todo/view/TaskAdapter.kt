package com.xiaomai.geek.todo.view

import com.xiaomai.geek.R
import com.xiaomai.geek.base.BaseAdapter
import com.xiaomai.geek.databinding.TaskItemBinding
import com.xiaomai.geek.db.Task


/**
 * Created by wangce on 2018/2/23.
 */
class TaskAdapter : BaseAdapter<Task, TaskItemBinding> (R.layout.task_item) {

    override fun onBindViewHolder(holder: Holder<TaskItemBinding>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener {
            AddEditTaskActivity.launch(holder.itemView.context, getItem(position))
        }
    }
}