package com.bignerdranch.android.homeworkplannerapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val taskList: List<Task>,
    private val onClick: (Task, Int) -> Unit,
    private val onLongClick: (Int) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.taskTitle)
        val subjectView: TextView = itemView.findViewById(R.id.taskSubject)
        val dueDateView: TextView = itemView.findViewById(R.id.taskDueDate)
        val priorityView: TextView = itemView.findViewById(R.id.taskPriority)
        val cardView: CardView = itemView.findViewById(R.id.taskCard)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]

        holder.titleView.text = task.title
        holder.subjectView.text = task.subject
        holder.dueDateView.text = task.dueDate

        // Set priority text and background color
        when (task.priority.lowercase()) {
            "high" -> {
                holder.priorityView.text = "High Priority"
                holder.priorityView.setBackgroundColor(Color.parseColor("#F4A7A7")) // pastel red
            }
            "medium" -> {
                holder.priorityView.text = "Medium Priority"
                holder.priorityView.setBackgroundColor(Color.parseColor("#FFD1A4")) // pastel orange
            }
            "low" -> {
                holder.priorityView.text = "Low Priority"
                holder.priorityView.setBackgroundColor(Color.parseColor("#B4E7B2")) // pastel green
            }
            else -> {
                holder.priorityView.text = "Priority"
                holder.priorityView.setBackgroundColor(Color.LTGRAY)
            }
        }

        // Selection highlight
        holder.cardView.alpha = if (task.isSelected) 0.5f else 1.0f

        holder.itemView.setOnClickListener {
            onClick(task, position)
        }

        holder.itemView.setOnLongClickListener {
            onLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int = taskList.size
}

