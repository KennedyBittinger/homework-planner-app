package com.bignerdranch.android.homeworkplannerapp

data class Task(
    val title: String,
    val subject: String,
    val dueDate: String, // e.g., "July 31 2025"
    val priority: String, // "High", "Medium", or "Low"
    val notes: String,
    var isSelected: Boolean = false
)
