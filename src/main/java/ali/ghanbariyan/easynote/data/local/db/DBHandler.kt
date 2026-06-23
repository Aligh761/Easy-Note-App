package ali.ghanbariyan.easynote.data.local.db

import ali.ghanbariyan.easynote.data.local.db.dao.NotesDao
import ali.ghanbariyan.easynote.data.model.DBNotesEntity
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DBNotesEntity::class],
    version = DBHandler.DB_VERSION
)
abstract class DBHandler : RoomDatabase() {

    abstract fun notesDao(): NotesDao

    companion object {

        const val DB_NAME = "note.db"
        const val DB_VERSION = 1
        const val TRUE_STATE = "1"
        const val FALSE_STATE = "0"


        private var INSTANCE: DBHandler? = null

        fun getDatabase(context: Context): DBHandler {
            INSTANCE = Room.databaseBuilder(
                context,
                DBHandler::class.java,
                DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()

            return INSTANCE!!
            }
        }
    }
