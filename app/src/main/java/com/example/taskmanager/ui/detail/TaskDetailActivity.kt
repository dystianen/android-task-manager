package com.example.taskdetail

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.R

class TaskDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_task)

        val taskName = findViewById<EditText>(R.id.etTaskName)
        val taskDescription = findViewById<EditText>(R.id.etTaskDescription)
        val saveButton = findViewById<Button>(R.id.btnSimpan)

        saveButton.setOnClickListener {
            val name = taskName.text.toString()
            val desc = taskDescription.text.toString()

            // Simulasi simpan data
            Toast.makeText(this, "Tugas '$name' disimpan!", Toast.LENGTH_SHORT).show()
        }
    }
}
