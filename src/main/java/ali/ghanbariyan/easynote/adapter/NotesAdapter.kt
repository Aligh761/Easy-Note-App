package ali.ghanbariyan.easynote.adapter

import ali.ghanbariyan.easynote.R
import ali.ghanbariyan.easynote.data.model.RecyclerBinNotesEntity
import ali.ghanbariyan.easynote.databinding.ListItemNoteBinding
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(
    private val context: Context,
    private val onItemClick: (RecyclerBinNotesEntity) -> Unit,
    private val onDeleteClick: (RecyclerBinNotesEntity) -> Unit
) : RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    private var allData = ArrayList<RecyclerBinNotesEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(
            ListItemNoteBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.setData(allData[position])
    }

    override fun getItemCount(): Int = allData.size

    inner class NotesViewHolder(private val binding: ListItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(note: RecyclerBinNotesEntity) {
            binding.titleText.text = note.title

            binding.deleteIcon.setOnClickListener {
                AlertDialog.Builder(ContextThemeWrapper(context,com.google.android.material.R.style.MaterialAlertDialog_Material3_Animation))
                    .setTitle("حذف یادداشت")
                    .setMessage("آیا می خواهید یادداشت به سطل زباله منتقل شود ؟ ")
                    .setIcon(R.drawable.icons8_delete_64)
                    .setPositiveButton("خیر") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNegativeButton("بله") { dialog, _ ->
                        onDeleteClick(note)
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }

            binding.root.setOnClickListener {
                onItemClick(note)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun changeData(data: List<RecyclerBinNotesEntity>) {
        allData.clear()
        allData.addAll(data)
        notifyDataSetChanged()
    }
}