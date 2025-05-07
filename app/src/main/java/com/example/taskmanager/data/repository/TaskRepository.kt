package com.example.taskmanager.data.repository

import com.example.taskmanager.data.model.Task
import java.time.LocalDate

object TaskRepository {
    private val taskList = mutableListOf<Task>()

    fun getTasksFor(date: LocalDate): List<Task> {
        return taskList.filter { it.date == date }
    }

    fun addTask(task: Task) {
        taskList.add(task)
    }
}
