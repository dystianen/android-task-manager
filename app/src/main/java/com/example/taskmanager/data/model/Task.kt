package com.example.taskmanager.data.model

import java.time.LocalDate

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    val date: LocalDate,
    val isStarred: Boolean = false
)
