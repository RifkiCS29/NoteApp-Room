package com.rifki.kotlin.mynotesroom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.rifki.kotlin.mynotesroom.db.NoteDao
import com.rifki.kotlin.mynotesroom.db.NoteDatabase
import com.rifki.kotlin.mynotesroom.model.Note
import kotlinx.android.synthetic.main.activity_note_add_update.*
import java.text.SimpleDateFormat
import java.util.*

class NoteAddUpdateActivity : AppCompatActivity() {
    private var isEdit = false
    private lateinit var note: Note
    private lateinit var database: NoteDatabase
    private lateinit var dao: NoteDao

    companion object {
        const val ALERT_DIALOG_CLOSE = 10
        const val ALERT_DIALOG_DELETE = 20
        const val EXTRA_NOTE = "extra_note"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_note_add_update)

        val actionBarTitle: String
        val btnTitle: String

        database = NoteDatabase.getDatabase(applicationContext)
        dao = database.getNoteDao()

        if (intent.getParcelableExtra<Note>(EXTRA_NOTE) != null){
            isEdit = true
            note = intent.getParcelableExtra(EXTRA_NOTE)!!
            edt_title.setText(note.title)
            edt_description.setText(note.description)

            actionBarTitle = "Ubah"
            btnTitle = "Ubah"
        } else {
            actionBarTitle = "Tambah"
            btnTitle = "Simpan"
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_submit.text = btnTitle

        btn_submit.setOnClickListener {
            val title = edt_title.text.toString()
            val description = edt_description.text.toString()

            if (title.isEmpty() && description.isEmpty()){
                Toast.makeText(applicationContext, "Note Tidak Boleh Kosong", Toast.LENGTH_SHORT).show()
            } else{
                if (isEdit){
                    //insert
                    saveNote(Note(id = note.id, title = title, description = description, date = getCurrentDate()))
                }
                else{
                    //update
                    saveNote(Note(title = title, description = description, date = getCurrentDate()))
                }
            }
            finish()
        }
    }

    private fun saveNote(note: Note){
        if (dao.getById(note.id).isEmpty()){
            dao.insert(note)
            Toast.makeText(applicationContext, "Catatan Berhasil Disimpan", Toast.LENGTH_SHORT).show()
        } else{
            dao.update(note)
            Toast.makeText(applicationContext, "Catatan Berhasil DiUbah", Toast.LENGTH_SHORT).show()
        }
    }

    private fun deleteNote(note: Note){
        dao.delete(note)
        Toast.makeText(applicationContext, "Catatan Berhasil Dihapus", Toast.LENGTH_SHORT).show()
    }

    private fun getCurrentDate(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
        val date = Date()

        return dateFormat.format(date)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (isEdit) {
            menuInflater.inflate(R.menu.menu_form, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
            android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        showAlertDialog(ALERT_DIALOG_CLOSE)
    }

    private fun showAlertDialog(type: Int) {
        val isDialogClose = type == ALERT_DIALOG_CLOSE
        val dialogTitle: String
        val dialogMessage: String

        if (isDialogClose) {
            dialogTitle = "Batal"
            dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
        } else {
            dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
            dialogTitle = "Hapus Note"
        }

        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { _, _ ->
                if (isDialogClose) {
                    finish()
                } else {
                    deleteNote(note)
                    finish()
                }
            }
            .setNegativeButton("Tidak") { dialog, _ -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }
}