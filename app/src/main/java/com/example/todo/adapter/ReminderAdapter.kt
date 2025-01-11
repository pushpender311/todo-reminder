package com.example.todo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.todo.R
import com.example.todo.model.Reminder
import com.google.android.material.card.MaterialCardView

class ReminderAdapter(
    val context: Context,
    private val todos: List<Reminder>,
    private val clickListener: TodoClickListener
) : RecyclerView.Adapter<ReminderAdapter.MyHolder>() {
    class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.textTitle)
        val description: TextView = itemView.findViewById(R.id.textDescription)
        private val todoStatus: TextView = itemView.findViewById(R.id.todoStatus)
        val card: MaterialCardView = itemView.findViewById(R.id.card)

        fun bind(reminder: Reminder) {
            title.text = reminder.title
            description.text = reminder.description
            if (reminder.isLocal) {
                todoStatus.text = "LOCAL"
            } else {
                todoStatus.text = "API"
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false)
        return MyHolder(view)
    }

    override fun getItemCount(): Int {
        return todos.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val todo = todos[position]
        holder.bind(todo)

        holder.card.setOnClickListener {
            clickListener.onTodoClick(todo)
        }
    }

    interface TodoClickListener {
        fun onTodoClick(reminder: Reminder)
    }
}