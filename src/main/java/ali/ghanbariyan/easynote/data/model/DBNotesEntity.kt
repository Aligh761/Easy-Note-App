package ali.ghanbariyan.easynote.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notesTable")
data class DBNotesEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "note_id") var id: Int = 0,
    @ColumnInfo(name = "note_title") var title: String,
    @ColumnInfo(name = "note_detail") var detail: String,
    @ColumnInfo(name = "note_date") var date: String,
    @ColumnInfo(name = "note_delete_state") var deleteState: String
)
