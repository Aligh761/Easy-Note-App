package ali.ghanbariyan.easynote.ui.activity

import ali.ghanbariyan.easynote.adapter.RecycleBinAdapter
import ali.ghanbariyan.easynote.data.local.db.DBHandler
import ali.ghanbariyan.easynote.data.local.db.dao.NotesDao
import ali.ghanbariyan.easynote.data.model.RecyclerBinNotesEntity
import ali.ghanbariyan.easynote.databinding.ActivityRecycleBinBinding
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecycleBinActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecycleBinBinding
    private lateinit var dao: NotesDao
    private lateinit var adapter: RecycleBinAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecycleBinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = DBHandler.getDatabase(this).notesDao()


    }

    override fun onStart() {
        super.onStart()
        initRecycler()
        observeDeletedNotes()
    }

    private fun initRecycler() {
        adapter = RecycleBinAdapter(
            this,
            onRestore = { note -> restoreNote(note) },
            onDelete = { note -> deleteNote(note) }
        )
        binding.recyclerRecycleBin.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        binding.recyclerRecycleBin.adapter = adapter
    }

    private fun observeDeletedNotes() {
        lifecycleScope.launch {
            dao.getNotesForRecyclerBin(DBHandler.TRUE_STATE).collect { notes ->
                adapter.submitList(notes)
            }
        }
    }

    private fun restoreNote(note: RecyclerBinNotesEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = dao.updateDeleteState(note.id, DBHandler.FALSE_STATE)
            withContext(Dispatchers.Main) {
                if (result > 0) {
                    showText("یادداشت با موفقیت بازگردانی شد")
                } else {
                    showText("عملیات با مشکل مواجه شد")
                }
            }
        }
    }

    private fun deleteNote(note: RecyclerBinNotesEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = dao.deleteNotes(note.id)
            withContext(Dispatchers.Main) {
                if (result > 0) {
                    showText("یادداشت با موفقیت حذف شد")
                } else {
                    showText("عملیات با مشکل مواجه شد")
                }
            }
        }
    }

    private fun showText(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
}