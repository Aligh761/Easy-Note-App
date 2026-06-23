package ali.ghanbariyan.easynote.ui.activity

import ali.ghanbariyan.easynote.data.local.db.DBHandler
import ali.ghanbariyan.easynote.data.local.db.dao.NotesDao
import ali.ghanbariyan.easynote.data.model.DBNotesEntity
import ali.ghanbariyan.easynote.databinding.ActivityAddNoteBinding
import ali.ghanbariyan.easynote.utils.PersianDate
import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AddNoteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNoteBinding
    private lateinit var dao: NotesDao
    private var isNewNote: Boolean = false
    private var noteId: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddNoteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = DBHandler.getDatabase(this).notesDao()
        isNewNote = intent.getBooleanExtra("newNotes", false)
        noteId = intent.getIntExtra("noteId", 0)

        lifecycleScope.launch(Dispatchers.Main) {
            if (isNewNote) {
                binding.textDate.text = getDate()
            } else {
                dao.getNotesById(noteId).collect { listNotes ->
                    if (listNotes.isNotEmpty()) {
                        val note = listNotes[0]
                        val edit = Editable.Factory()
                        binding.edtTitleNote.text = edit.newEditable(note.title)
                        binding.edtTextNote.text = edit.newEditable(note.detail)
                        binding.textDate.text = note.date
                    }
                }
            }
        }

        binding.btnSave.setOnClickListener {
            val title = binding.edtTitleNote.text.toString()
            val detail = binding.edtTextNote.text.toString()

            if (title.isNotEmpty()) {
                lifecycleScope.launch(Dispatchers.IO) {
                    val result = if (isNewNote) {
                        val newNote =
                            DBNotesEntity(0, title, detail,getDate() , DBHandler.FALSE_STATE)
                        dao.insertNote(newNote) > 0
                    } else {
                        val noteToUpdate = DBNotesEntity(
                            noteId,
                            title,
                            detail,
                            binding.textDate.text.toString() ,
                            DBHandler.FALSE_STATE
                        )
                        dao.updateNote(noteToUpdate) > 0
                    }
                    withContext(Dispatchers.Main) {
                        if (result) {
                            showText("یادداشت شما با موفقیت ذخیره شد")
                            finish()
                        } else {
                            showText("یادداشت شما در ذخیره سازی با مشکل مواجه شد")
                        }
                    }
                }
            } else {
                showText("عنوان متن را وارد کنید")
            }
        }

        binding.btnCansel.setOnClickListener {
            finish()
        }
    }

    private fun getDate(): String {
        val date = PersianDate()
        val currentDate = "${date.year}/${date.month}/${date.day}"
        val currentTime = "${date.hour}:${date.min}"
        return "$currentDate | $currentTime"
    }

    private fun showText(text: String) {
        Toast.makeText(
            this,
            text,
            Toast.LENGTH_SHORT
        ).show()
    }
}