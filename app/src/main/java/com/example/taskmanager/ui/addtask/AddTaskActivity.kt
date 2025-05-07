package com.example.taskmanager.ui.addtask

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.taskmanager.R
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.*

class AddTaskActivity : AppCompatActivity() {
    private lateinit var editTextTanggal: EditText
    private lateinit var iconDatePicker: ImageView
    private val calendar = Calendar.getInstance()
    private lateinit var editTextTask: EditText
    private lateinit var editTextDeskripsi: EditText
    private lateinit var btnSimpan: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        editTextTanggal = findViewById(R.id.editTextTanggal)
        iconDatePicker = findViewById(R.id.iconDatePicker)

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, day)
            updateDateInView()
        }

        iconDatePicker.setOnClickListener {
            DatePickerDialog(this, dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        editTextTask = findViewById(R.id.editTextTask)
        editTextDeskripsi = findViewById(R.id.editTextDeskripsi)
        btnSimpan = findViewById(R.id.buttonSimpan)

        btnSimpan.setOnClickListener {
            simpanTugas()
        }
    }

    private fun simpanTugas() {
        val title = editTextTask.text.toString()
        val description = editTextDeskripsi.text.toString()
        val dateStr = editTextTanggal.text.toString()

        if (title.isBlank() || dateStr.isBlank()) {
            Toast.makeText(this, "Judul dan tanggal harus diisi", Toast.LENGTH_SHORT).show()
            return
        }

        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = sdf.parse(dateStr)
        val localDate = date?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()

        val resultIntent = Intent().apply {
            putExtra("title", title)
            putExtra("description", description)
            putExtra("date", localDate.toString())
        }

        setResult(RESULT_OK, resultIntent)
        finish()
    }

    private fun updateDateInView() {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        editTextTanggal.setText(sdf.format(calendar.time))
    }

}