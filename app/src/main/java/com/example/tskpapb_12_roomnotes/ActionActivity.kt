package com.example.tskpapb_12_roomnotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.tskpapb_12_roomnotes.database.Note
import com.example.tskpapb_12_roomnotes.database.NoteDao
import com.example.tskpapb_12_roomnotes.database.NoteRoomDatabase
import com.example.tskpapb_12_roomnotes.databinding.ActivityActionBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class ActionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActionBinding
    private lateinit var mNotesDao: NoteDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActionBinding.inflate(layoutInflater)
        executorService = Executors.newSingleThreadExecutor()
        val db = NoteRoomDatabase.getDatabase(this)
        mNotesDao = db!!.noteDao()!!

        setContentView(binding.root)
        val intent =  intent
        val note = intent.getSerializableExtra("EXT_NOTE") as Note?

        with(binding){
            edtTitle.setText(note?.title)
            edtDesc.setText(note?.description)
            edtDate.setText(note?.date)

            //ketika button add di klik
            btnAdd.setOnClickListener(){
                insert(
                    Note(
                        title =edtTitle.text.toString(),
                        description = edtDesc.text.toString(),
                        date = edtDate.text.toString()
                    )
                )
                // menjalankan fungsi setempty field utk mengosongkan field
                setEmptyField()

                // membuat toast successfull
                Toast.makeText(this@ActionActivity, "Berhasil Menambahkan Data!!!", Toast.LENGTH_SHORT).show()
            }

            // ketika button updatedi click
            btnUpdate.setOnClickListener(){
                if (note != null) {
                    update(
                        Note(
                            id = note.id,
                            title = edtTitle.text.toString(),
                            description = edtDesc.text.toString(),
                            date = edtDate.text.toString()
                        )
                    )
                    // set edt text jadi kosong
                    setEmptyField()

                    // untuk menyelesaikan halaman update data
                    finish()

                    // membuat toast successfull
                    Toast.makeText(this@ActionActivity, "Berhasil Mengupdate Data!!!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ActionActivity, "Tak ada data yang valid!!!", Toast.LENGTH_SHORT).show()
                }
            }

            // ketika tn delete di klik
            btnDelete.setOnClickListener(){
                if (note != null) {
                    // menghapus data
                    delete(note)

                    // set edt text jadi kosong
                    setEmptyField()

                    // finish activity
                    finish()

                    // toast successfull
                    Toast.makeText(this@ActionActivity, "Berhasil Menghapus Data!!!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ActionActivity, "Tak ada data yang valid!!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun setEmptyField(){
        with(binding){
            edtTitle.setText("")
            edtDesc.setText("")
            edtDate.setText("")
        }
    }

    private fun insert(note: Note){
        executorService.execute{mNotesDao.insert(note)}
    }

    private fun update(note:Note){
        executorService.execute{mNotesDao.update(note)}
    }

    private fun delete(note:Note){
        executorService.execute{mNotesDao.delete(note)}
    }
}