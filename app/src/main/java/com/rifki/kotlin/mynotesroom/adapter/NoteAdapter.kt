package com.rifki.kotlin.mynotesroom.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rifki.kotlin.mynotesroom.R
import com.rifki.kotlin.mynotesroom.model.Note

class NoteAdapter(private val listNotes: ArrayList<Note>, private val listener: NoteListener) : RecyclerView.Adapter<NoteAdapter.NoteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listNotes.size
    }

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val note = listNotes[position]
        holder.tvTitle.text = note.title
        holder.tvDescription.text = note.description
        holder.tvDate.text = note.date
        holder.itemView.setOnClickListener {
            listener.onItemClicked(note)
        }
    }

    class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tv_item_title)
        var tvDescription: TextView = itemView.findViewById(R.id.tv_item_description)
        var tvDate: TextView = itemView.findViewById(R.id.tv_item_date)
    }

    interface NoteListener{
        fun onItemClicked(note: Note)
    }
}