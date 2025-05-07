package com.example.taskmanager.ui.editTask

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.R

class EditTaskActivity : AppCompatActivity() {
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var etDate: TextView
    private var taskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        etTitle = findViewById(R.id.etTaskName)
        etDescription = findViewById(R.id.etTaskDescription)
        etDate = findViewById(R.id.etDate)

        // Load data from intent
        taskId = intent.getIntExtra("task_id", -1)
        etTitle.setText(intent.getStringExtra("title"))
        etDescription.setText(intent.getStringExtra("description"))
        etDate.setText(intent.getStringExtra("date"))

        findViewById<Button>(R.id.btnSimpan).setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("task_id", taskId)
                putExtra("title", etTitle.text.toString())
                putExtra("description", etDescription.text.toString())
                putExtra("date", etDate.text.toString())
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}
