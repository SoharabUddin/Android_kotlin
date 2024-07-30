package com.myapps.note

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.myapps.note.DataClasses.NoteDataClass
import com.myapps.note.Utils.Constant

class NotesAdapterClass(private var notes:ArrayList<NoteDataClass>,private val onItemClick :(NoteDataClass,Int)->Unit):RecyclerView.Adapter<NotesAdapterClass.MyViewHolder>() {
    inner class MyViewHolder(view:View): ViewHolder(view) {
        var titleTV : TextView = view.findViewById(R.id.titleTextView)
        var contentTV : TextView = view.findViewById(R.id.contentTextView)
        var editButton : ImageButton = view.findViewById(R.id.editButton)
        var deleteButton : ImageButton = view.findViewById(R.id.deleteButton)
        fun bind(item: NoteDataClass, position: Int){
            titleTV.text = item.title
            contentTV.text = item.content
            itemView.setOnClickListener {
                onItemClick(item,position)
            }
            editButton.setOnClickListener {
                onItemClick(item,position)
            }
            deleteButton.setOnClickListener {
                showDeleteConfirmationDialog(itemView.context, position,item.title)
            }
        }
        private fun showDeleteConfirmationDialog(context: Context, position: Int,title:String) {
            AlertDialog.Builder(context).apply {
                setTitle("Delete Note")
                setMessage("Are you sure you want to delete this note \"$title\"?")
                setPositiveButton("Yes") { dialog, _ ->
                    Constant.notes.removeAt(position)
                    notifyItemRemoved(position)
                    dialog.dismiss()
                }
                setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                create()
                show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var view = LayoutInflater.from(parent.context).inflate(R.layout.note_card,parent,false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       var item = notes[position]
        holder.bind(item,position)
    }
}