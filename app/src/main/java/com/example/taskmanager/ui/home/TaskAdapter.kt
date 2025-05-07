package com.example.taskmanager.ui.home

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanager.R
import com.example.taskmanager.data.model.Task
import java.time.LocalDate

class TaskAdapter(
    private val onItemClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    private var taskList: List<Task> = emptyList()

    fun submitList(list: List<Task>) {
        taskList = list
        notifyDataSetChanged()
    }

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        val container: LinearLayout = itemView.findViewById(R.id.containerCard)
        val star: ImageView = itemView.findViewById(R.id.ivStar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun getItemCount(): Int = taskList.size

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = taskList[position]
        holder.tvTitle.text = task.title
        holder.tvDescription.text = task.description
        holder.tvDate.text = task.date.toString()

        val today = LocalDate.now()
        val bgColor = when {
            task.date < today -> "#FBC7C7" // merah muda
            task.date == today -> "#AAF6D0" // hijau muda
            else -> "#FDF0C4" // kuning muda
        }
        holder.container.setBackgroundColor(Color.parseColor(bgColor))

        holder.star.setImageResource(
            if (task.isImportant) R.drawable.ic_star_filled else R.drawable.ic_star_border
        )

        holder.itemView.setOnClickListener {
            onItemClick(task)
        }
    }

    private fun onItemClick(task: Task) {
        taskAdapter = TaskAdapter(
            onItemClick = { task ->
                val intent = Intent(this, EditTaskActivity::class.java)
                intent.putExtra("task_id", task.id)
                intent.putExtra("title", task.title)
                intent.putExtra("description", task.description)
                intent.putExtra("date", task.date.toString())
                editTaskLauncher.launch(intent)
            }
        )

    }
}
