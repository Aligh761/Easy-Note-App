package ali.ghanbariyan.easynote.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "recyclerBinNoteTable")
data class RecyclerBinNotesEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "note_id") val id: Int = 0,
    @ColumnInfo(name = "note_title") val title: String
)