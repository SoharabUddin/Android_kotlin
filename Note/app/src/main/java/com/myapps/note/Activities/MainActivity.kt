package com.myapps.note.Activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.myapps.note.DataClasses.NoteDataClass
import com.myapps.note.NotesAdapterClass
import com.myapps.note.R
import com.myapps.note.Utils.Constant

class MainActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var addButton: ImageButton
    lateinit var adapter: NotesAdapterClass
    lateinit var preferences: SharedPreferences

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: Initializing SharedPreferences")
        preferences = getSharedPreferences("notes", Context.MODE_PRIVATE)

        Log.d(TAG, "onCreate: Loading notes from preferences")
        val loadedNotes = loadNotesFromPreferences()
        Constant.notes.addAll(loadedNotes)

        Log.d(TAG, "onCreate: Loaded ${loadedNotes.size} notes $loadedNotes")

        initViews()
        setupRecyclerView()
        addButton.setOnClickListener { navigateToAddNote() }
    }

    private fun initViews() {
        Log.d(TAG, "initViews: Initializing views")
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        addButton = findViewById(R.id.addImageButton)
    }
    private fun setupRecyclerView() {
        Log.d(TAG, "setupRecyclerView: Setting up RecyclerView")
        adapter = NotesAdapterClass(Constant.notes, ::onNoteItemClick)
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
        Log.d(TAG, "setupRecyclerView: Adapter set and notified")
    }

    private fun navigateToAddNote() {
        Log.d(TAG, "navigateToAddNote: Navigating to AddNoteActivity")
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("clicked_on", "Add Button")
        startActivity(intent)
    }

    private fun onNoteItemClick(item: NoteDataClass, position: Int) {
        Log.d(TAG, "onNoteItemClick: Item clicked at position $position")
        val intent = Intent(this, AddNoteActivity::class.java)
        val jsonItem = Gson().toJson(item)
        intent.putExtra("clicked_on", "Item Clicked")
        intent.putExtra("item", jsonItem)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Log.d(TAG, "showToast: $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    override fun onStart() {
        super.onStart()
        adapter.notifyDataSetChanged()
    }

    override fun onStop() {
        super.onStop()
        saveNotesToPreferences(Constant.notes)
    }

    private fun saveNotesToPreferences(notes: ArrayList<NoteDataClass>) {
        Log.d(TAG, "Saving notes: ${notes.size} $notes")
        val json = Gson().toJson(notes)
        Log.d(TAG, "JSON to save: $json")
        preferences.edit().putString("notes", json).apply()
    }

    private fun loadNotesFromPreferences(): ArrayList<NoteDataClass> {
        Log.d(TAG, "Loading notes")
        val json = preferences.getString("notes", "[]") // Default to empty JSON array
        Log.d(TAG, "JSON loaded: $json")
        val type = object : TypeToken<ArrayList<NoteDataClass>>() {}.type
        return Gson().fromJson(json, type) ?: arrayListOf()
    }
}
