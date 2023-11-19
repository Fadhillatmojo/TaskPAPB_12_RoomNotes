package com.example.tskpapb_12_roomnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.example.tskpapb_12_roomnotes.database.Note
import com.example.tskpapb_12_roomnotes.database.NoteDao
import com.example.tskpapb_12_roomnotes.database.NoteRoomDatabase
import com.example.tskpapb_12_roomnotes.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        setContentView(binding.root)

        with(binding){
            // click listener dari btn add
            btnAdd.setOnClickListener{
                val intentToActionActivity = Intent(this@MainActivity, ActionActivity::class.java)
                var title = "Add Note"
                intentToActionActivity.putExtra("EXT_TITLE", title)
                startActivity(intentToActionActivity)
            }

            // lv onclick
            lvItem.setOnItemClickListener(){adapterView, view, position, id ->
                // idItem = item.id
                val item = adapterView.adapter.getItem(position) as Note
                val intentToActionActivity = Intent(this@MainActivity, ActionActivity::class.java)
                var title = "Edit/Delete Note"
                intentToActionActivity.putExtra("EXT_NOTE", item)
                intentToActionActivity.putExtra("EXT_TITLE", title)
                startActivity(intentToActionActivity)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        getAllNotes()
    }

    private fun getAllNotes(){
        mNotesDao.allNotes.observe(this){
                notes ->
            val adapter : ArrayAdapter<Note> = ArrayAdapter<Note>(
                this,
                android.R.layout.simple_list_item_1, notes
            )
            binding.lvItem.adapter = adapter
        }
    }
}