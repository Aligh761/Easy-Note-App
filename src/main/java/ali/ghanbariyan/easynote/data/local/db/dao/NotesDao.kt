package ali.ghanbariyan.easynote.data.local.db.dao

import ali.ghanbariyan.easynote.data.model.DBNotesEntity
import ali.ghanbariyan.easynote.data.model.RecyclerBinNotesEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {

    @Insert
    fun insertNote(note: DBNotesEntity): Long

    @Update
    fun updateNote(note: DBNotesEntity): Int

    @Query("UPDATE notesTable SET note_delete_state = :state WHERE note_id = :id")
    fun updateDeleteState(id: Int, state: String): Int

    @Query("SELECT note_id, note_title FROM notesTable WHERE note_delete_state = :value")
    fun getNotesForRecyclerBin(value: String): Flow<List<RecyclerBinNotesEntity>>

    @Query("SELECT * FROM notesTable WHERE note_id = :id")
    fun getNotesById(id: Int): Flow<List<DBNotesEntity>>

    @Query("DELETE FROM notesTable WHERE note_id = :id")
    fun deleteNotes(id: Int): Int
}
