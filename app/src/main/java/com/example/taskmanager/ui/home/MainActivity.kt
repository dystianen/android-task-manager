package com.example.taskmanager.ui.home

import VerticalSpaceItemDecoration
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.data.model.Task
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.taskmanager.ui.editTask.EditTaskActivity
import com.example.taskmanager.ui.addtask.AddTaskActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private lateinit var taskList: List<Task>
    private lateinit var btnKemarin: Button
    private lateinit var btnHariIni: Button
    private lateinit var btnBesok: Button

    enum class FilterType { KEMARIN, HARI_INI, BESOK }
    private var currentFilter: FilterType = FilterType.HARI_INI

    private val today: LocalDate = LocalDate.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale("id", "ID"))

        taskList = listOf(
            Task(1, "Beli bahan presentasi", "kdjkdjdkjkd", LocalDate.parse("3 Mei 2025", formatter), false),
            Task(2, "Kerjakan laporan KP", "kdjkdjdkjdd", LocalDate.parse("4 Mei 2025", formatter), false),
            Task(3, "Beli bahan baku", "kdjkdjdkjkd", LocalDate.parse("5 Mei 2025", formatter), false),
        )

        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(
            onItemClick = { task ->
                val intent = Intent(this, EditTaskActivity::class.java).apply {
                    putExtra("task_id", task.id)
                    putExtra("title", task.title)
                    putExtra("description", task.description)
                    putExtra("date", task.date.toString())
                }
                editTaskLauncher.launch(intent)
            },
            onStarClicked = { task -> toggleStar(task) }
        )
        recyclerView.adapter = taskAdapter

        val spacingInDp = 2
        val spacingInPx = (spacingInDp * resources.displayMetrics.density).toInt()
        recyclerView.addItemDecoration(VerticalSpaceItemDecoration(spacingInPx))

        taskAdapter.submitList(taskList)
        recyclerView.adapter = taskAdapter

        btnKemarin = findViewById(R.id.btnKemarin)
        btnHariIni = findViewById(R.id.btnHariIni)
        btnBesok = findViewById(R.id.btnBesok)

        btnKemarin.setOnClickListener {
            filterTasksBeforeToday()
        }

        btnHariIni.setOnClickListener {
            filterTasksForToday()
        }

        btnBesok.setOnClickListener {
            filterTasksAfterToday()
        }

        // Default filter saat buka app
        filterTasksForToday()

        val fabAddTask: FloatingActionButton = findViewById(R.id.fabAddTask)
        fabAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            addTaskLauncher.launch(intent)
        }
    }

    private fun updateButtonState(activeButton: Button) {
        val buttons = listOf(btnKemarin, btnHariIni, btnBesok)
        for (btn in buttons) {
            btn.setBackgroundResource(
                if (btn == activeButton) R.drawable.bg_filter_button_active
                else R.drawable.bg_filter_button
            )
        }
    }

    private fun filterTasksBeforeToday() {
        currentFilter = FilterType.KEMARIN
        val filtered = taskList.filter { it.date < today }
            .sortedWith(compareByDescending<Task> { it.isStarred }.thenBy { it.date })
        taskAdapter.submitList(filtered)
        updateButtonState(btnKemarin)
    }

    private fun filterTasksForToday() {
        currentFilter = FilterType.HARI_INI
        val filtered = taskList.filter { it.date == today }
            .sortedWith(compareByDescending<Task> { it.isStarred }.thenBy { it.date })
        taskAdapter.submitList(filtered)
        updateButtonState(btnHariIni)
    }

    private fun filterTasksAfterToday() {
        currentFilter = FilterType.BESOK
        val filtered = taskList.filter { it.date > today }
            .sortedWith(compareByDescending<Task> { it.isStarred }.thenBy { it.date })
        taskAdapter.submitList(filtered)
        updateButtonState(btnBesok)
    }

    private val addTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            val title = data.getStringExtra("title") ?: return@registerForActivityResult
            val description = data.getStringExtra("description") ?: ""
            val dateStr = data.getStringExtra("date") ?: return@registerForActivityResult

            val newTask = Task(
                id = taskList.size + 1,
                title = title,
                description = description,
                date = LocalDate.parse(dateStr),
            )

            taskList = taskList + newTask

            when {
                newTask.date < today -> filterTasksBeforeToday()
                newTask.date == today -> filterTasksForToday()
                else -> filterTasksAfterToday()
            }
        }
    }

    private val editTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            val id = data.getIntExtra("task_id", -1)
            val title = data.getStringExtra("title") ?: return@registerForActivityResult
            val description = data.getStringExtra("description") ?: ""
            val dateStr = data.getStringExtra("date") ?: return@registerForActivityResult
            val date = LocalDate.parse(dateStr)

            taskList = taskList.map {
                if (it.id == id) it.copy(title = title, description = description, date = date)
                else it
            }

            // Otomatis filter berdasarkan tanggal update
            when {
                date < today  -> filterTasksBeforeToday()
                date == today  -> filterTasksForToday()
                else -> filterTasksAfterToday()
            }
        }
    }

    private fun toggleStar(task: Task) {
        taskList = taskList.map {
            if (it.id == task.id) it.copy(isStarred = !it.isStarred) else it
        }

        // Apply filter ulang sesuai filter aktif
        when (currentFilter) {
            FilterType.KEMARIN -> filterTasksBeforeToday()
            FilterType.HARI_INI -> filterTasksForToday()
            FilterType.BESOK -> filterTasksAfterToday()
        }
    }
}
