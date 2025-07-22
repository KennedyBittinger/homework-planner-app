package com.bignerdranch.android.homeworkplannerapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class AddTaskActivity : AppCompatActivity() {

    private lateinit var titleEditText: EditText
    private lateinit var subjectEditText: EditText
    private lateinit var dueDateEditText: EditText
    private lateinit var notesEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var priorityGroup: RadioGroup

    private var isEditMode = false
    private var taskIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        titleEditText = findViewById(R.id.editTextTitle)
        subjectEditText = findViewById(R.id.editTextSubject)
        dueDateEditText = findViewById(R.id.editTextDueDate)
        notesEditText = findViewById(R.id.editTextNotes)
        saveButton = findViewById(R.id.buttonSave)
        priorityGroup = findViewById(R.id.radioGroupPriority)

        // Check if this is an edit
        isEditMode = intent.getBooleanExtra("editMode", false)
        if (isEditMode) {
            taskIndex = intent.getIntExtra("taskIndex", -1)
            titleEditText.setText(intent.getStringExtra("title"))
            subjectEditText.setText(intent.getStringExtra("subject"))
            dueDateEditText.setText(intent.getStringExtra("dueDate"))
            notesEditText.setText(intent.getStringExtra("notes"))
            when (intent.getStringExtra("priority")) {
                "High" -> priorityGroup.check(R.id.radioHigh)
                "Medium" -> priorityGroup.check(R.id.radioMedium)
                "Low" -> priorityGroup.check(R.id.radioLow)
            }
        }

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val subject = subjectEditText.text.toString()
            val dueDate = dueDateEditText.text.toString()
            val notes = notesEditText.text.toString()
            val priority = when (priorityGroup.checkedRadioButtonId) {
                R.id.radioHigh -> "High"
                R.id.radioMedium -> "Medium"
                R.id.radioLow -> "Low"
                else -> "Low"
            }

            val resultIntent = Intent().apply {
                putExtra("title", title)
                putExtra("subject", subject)
                putExtra("dueDate", dueDate)
                putExtra("priority", priority)
                putExtra("notes", notes)
                putExtra("editMode", isEditMode)
                putExtra("taskIndex", taskIndex)
            }

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
