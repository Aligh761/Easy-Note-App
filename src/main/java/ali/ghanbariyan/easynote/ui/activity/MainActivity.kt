package ali.ghanbariyan.easynote.ui.activity

import ali.ghanbariyan.easynote.adapter.NotesAdapter
import ali.ghanbariyan.easynote.data.local.db.DBHandler
import ali.ghanbariyan.easynote.data.local.db.dao.NotesDao
import ali.ghanbariyan.easynote.data.model.RecyclerBinNotesEntity
import ali.ghanbariyan.easynote.databinding.ActivityMainBinding
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: NotesDao
    private lateinit var adapter: NotesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = DBHandler.getDatabase(this).notesDao()



        binding.txtRecyclerBin.setOnClickListener {
            startActivity(Intent(this, RecycleBinActivity::class.java))
        }

        binding.txtAddNote.setOnClickListener {
            val intent = Intent(this, AddNoteActivity::class.java)
            intent.putExtra("newNotes", true)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        initRecycler()
        observeNotes()
    }

    private fun initRecycler() {
        adapter = NotesAdapter(
            this,
            onItemClick = { note -> navigateToNoteDetail(note) },
            onDeleteClick = { note -> moveNoteToRecycleBin(note) }
        )
        binding.recyclerNotes.layoutManager = LinearLayoutManager(
            this,
            RecyclerView.VERTICAL,
            false
        )
        binding.recyclerNotes.adapter = adapter
    }

    private fun observeNotes() {
        lifecycleScope.launch {
            dao.getNotesForRecyclerBin(DBHandler.FALSE_STATE).collect { notes ->
                adapter.changeData(notes)
            }
        }
    }

    private fun navigateToNoteDetail(note: RecyclerBinNotesEntity) {
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("noteId", note.id)
        startActivity(intent)
    }

    private fun moveNoteToRecycleBin(note: RecyclerBinNotesEntity) {
        lifecycleScope.launch(Dispatchers.IO) {
            val result = dao.updateDeleteState(note.id, DBHandler.TRUE_STATE)
            withContext(Dispatchers.Main) {
                if (result > 0) {
                    showText("یادداشت به سطل زباله منتقل شد")
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