package ali.ghanbariyan.easynote.adapter

import ali.ghanbariyan.easynote.R
import ali.ghanbariyan.easynote.data.model.RecyclerBinNotesEntity
import ali.ghanbariyan.easynote.databinding.ListItemRecycleBinBinding
import android.app.AlertDialog
import android.content.Context
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class RecycleBinAdapter(
    private val context: Context,
    private val onRestore: (RecyclerBinNotesEntity) -> Unit,
    private val onDelete: (RecyclerBinNotesEntity) -> Unit
) : RecyclerView.Adapter<RecycleBinAdapter.RecycleViewHolder>() {

    private var data = emptyList<RecyclerBinNotesEntity>()

    fun submitList(newData: List<RecyclerBinNotesEntity>) {
        data = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecycleViewHolder {
        return RecycleViewHolder(
            ListItemRecycleBinBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(
        holder: RecycleViewHolder,
        position: Int
    ) {
        holder.setData(data[position])
    }

    override fun getItemCount(): Int = data.size

    inner class RecycleViewHolder(private val binding: ListItemRecycleBinBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun setData(note: RecyclerBinNotesEntity)  {
            binding.titleText.text = note.title

            binding.deleteIcon.setOnClickListener {
                AlertDialog.Builder(ContextThemeWrapper(context, com.google.android.material.R.style.MaterialAlertDialog_Material3_Animation))
                    .setTitle("حذف یادداشت")
                    .setMessage("آیا می خواهید یادداشت برای همیشه حذف شود ؟")
                    .setIcon(R.drawable.icons8_delete_64)
                    .setPositiveButton("خیر") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNegativeButton("بله") { dialog, _ ->
                        onDelete(note)
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }

            binding.restoreIcon.setOnClickListener {
                AlertDialog.Builder(ContextThemeWrapper(context, com.google.android.material.R.style.MaterialAlertDialog_Material3_Animation))
                    .setTitle("بازیابی یادداشت")
                    .setMessage("آیا می خواهید یادداشت بازیابی شود ؟")
                    .setIcon(R.drawable.icons8_restore_64)
                    .setPositiveButton("خیر") { dialog, _ ->
                        dialog.dismiss()
                    }
                    .setNegativeButton("بله") { dialog, _ ->
                        onRestore(note)
                        dialog.dismiss()
                    }
                    .create()
                    .show()
            }
        }
    }
}