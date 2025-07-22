package com.bignerdranch.android.homeworkplannerapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()
    private lateinit var btnDelete: Button
    private var isInDeleteMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val titleView = findViewById<TextView>(R.id.textViewTitle)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerViewTasks)
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        btnDelete = findViewById(R.id.btnDelete)

        taskAdapter = TaskAdapter(
            taskList,
            onClick = { task, position ->
                if (isInDeleteMode) {
                    task.isSelected = !task.isSelected
                    taskAdapter.notifyItemChanged(position)
                } else {
                    val intent = Intent(this, AddTaskActivity::class.java).apply {
                        putExtra("editMode", true)
                        putExtra("taskIndex", position)
                        putExtra("title", task.title)
                        putExtra("subject", task.subject)
                        putExtra("dueDate", task.dueDate)
                        putExtra("priority", task.priority)
                        putExtra("notes", task.notes)
                    }
                    startActivityForResult(intent, REQUEST_CODE_EDIT_TASK)
                }
            },
            onLongClick = { position ->
                if (!isInDeleteMode) {
                    isInDeleteMode = true
                    taskList[position].isSelected = true
                    btnDelete.visibility = Button.VISIBLE
                    taskAdapter.notifyDataSetChanged()
                }
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = taskAdapter

        fab.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADD_TASK)
        }

        btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Tasks")
                .setMessage("Are you sure you want to delete selected tasks?")
                .setPositiveButton("Yes") { _, _ ->
                    taskList.removeAll { it.isSelected }
                    isInDeleteMode = false
                    btnDelete.visibility = Button.VISIBLE
                    taskAdapter.notifyDataSetChanged()
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if ((requestCode == REQUEST_CODE_ADD_TASK || requestCode == REQUEST_CODE_EDIT_TASK)
            && resultCode == RESULT_OK && data != null
        ) {
            val title = data.getStringExtra("title") ?: return
            val subject = data.getStringExtra("subject") ?: return
            val dueDate = data.getStringExtra("dueDate") ?: return
            val priority = data.getStringExtra("priority") ?: "Low"
            val notes = data.getStringExtra("notes") ?: ""

            val newTask = Task(title, subject, dueDate, priority, notes)

            if (requestCode == REQUEST_CODE_EDIT_TASK) {
                val index = data.getIntExtra("taskIndex", -1)
                if (index != -1) {
                    taskList[index] = newTask
                    sortTasks()
                    taskAdapter.notifyDataSetChanged()
                }
            } else {
                taskList.add(newTask)
                sortTasks()
                taskAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun sortTasks() {
        val dateFormat = SimpleDateFormat("MMMM dd yyyy", Locale.getDefault())
        taskList.sortWith(compareByDescending<Task> {
            when (it.priority.lowercase(Locale.getDefault())) {
                "high" -> 3
                "medium" -> 2
                "low" -> 1
                else -> 0
            }
        }.thenBy {
            try {
                dateFormat.parse(it.dueDate)
            } catch (e: Exception) {
                null
            }
        })
    }

    companion object {
        private const val REQUEST_CODE_ADD_TASK = 100
        private const val REQUEST_CODE_EDIT_TASK = 101
    }
}
