package com.rifki.kotlin.mynotesroom

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.rifki.kotlin.mynotesroom.adapter.NoteAdapter
import com.rifki.kotlin.mynotesroom.db.NoteDatabase
import com.rifki.kotlin.mynotesroom.model.Note
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadNotesData()

        fab_add.setOnClickListener {
            startActivity(Intent(this, NoteAddUpdateActivity::class.java))
        }
    }

    private fun loadNotesData(){
        val database = NoteDatabase.getDatabase(applicationContext)
        val dao = database.getNoteDao()
        val listNotes = arrayListOf<Note>()
        listNotes.addAll(dao.getAll())
        showRecyclerList(listNotes)
        if (listNotes.isEmpty()){
            Snackbar.make(rv_notes, "Tidak Ada Catatan", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun showRecyclerList(listNotes: ArrayList<Note>){
        rv_notes.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = NoteAdapter(listNotes, object : NoteAdapter.NoteListener{
                override fun onItemClicked(note: Note) {
                    val intent = Intent(this@MainActivity, NoteAddUpdateActivity::class.java)
                    intent.putExtra(NoteAddUpdateActivity.EXTRA_NOTE, note)
                    startActivity(intent)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        loadNotesData()
    }
}