package com.example.tskpapb_12_roomnotes.database


import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    // fungsi notedao sebagai aks, setiap akses yang terjadi di database, ini harus melalui perantara interface ini
    // jadi setiap crud nya di harus melalui ini dulu

    // penambahan data note
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note:Note)

    // update data note
    @Update
    fun update(note:Note)

    // delete data note
    @Delete
    fun delete(note:Note)

    // get data list note, melakukan select list note melalui note_table dari data class Note
    @get:Query("SELECT * FROM note_table ORDER BY id ASC")
    val allNotes: LiveData<List<Note>>
}