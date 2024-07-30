package com.myapps.note.Activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.myapps.note.DataClasses.NoteDataClass
import com.myapps.note.R
import com.myapps.note.Utils.Constant

class AddNoteActivity : AppCompatActivity() {
    lateinit var titleEDT : TextInputEditText
    lateinit var contentEDT : TextInputEditText
    lateinit var saveButton :Button
    lateinit var deleteButton :Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        init()
        val saveOrUpdate = saveOrUpdate()
        saveButton.setOnClickListener {
            if (saveOrUpdate=="save"){
                onSaveButtonClicked()
            }
            else{
                onUpdateButtonClicked()
            }
            finish()
        }
        deleteButton.setOnClickListener { onDeleteButtonClicked() }
    }

    private fun init(){
        titleEDT = findViewById(R.id.titleEDT)
        contentEDT = findViewById(R.id.contentEDT)
        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)
    }
    private fun saveOrUpdate() : String {
        val query = intent.getStringExtra("clicked_on")?:"Add Button"
        val jsonItem = intent.getStringExtra("item")?:""
        if (query=="Add Button"){
            deleteButton.visibility = View.GONE
            return "save"
        }
        else{
            val item = Gson().fromJson(jsonItem, NoteDataClass::class.java)
            titleEDT.setText(item.title)
            contentEDT.setText(item.content)
            deleteButton.visibility = View.VISIBLE
            return "update"
        }
    }
    private fun onSaveButtonClicked(){
        val title = titleEDT.text.toString()
        val content = contentEDT.text.toString()
        if (title.isNotEmpty() && content.isNotEmpty()){
            val note = NoteDataClass(title,content)
            Constant.notes.add(note)

            showToast("Added ${Constant.notes.size}")
        }
        else{
            showToast("title & content must not empty")
        }
    }
    private fun onUpdateButtonClicked(){
        val title = titleEDT.text.toString()
        val content = contentEDT.text.toString()
        val position = intent.getIntExtra("position",0)

        if (title.isNotEmpty() && content.isNotEmpty()){
            val note = NoteDataClass(title,content)
            Constant.notes[position] = note
            showToast("Updated ${Constant.notes.size}")
        }
        else{
            showToast("title & content must not empty")
        }
    }
    private fun onDeleteButtonClicked(){
        val position = intent.getIntExtra("position",-1)
        Constant.notes.removeAt(position)
        showToast("Deleted")
        finish()
    }
    private fun showToast(msg:String){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}