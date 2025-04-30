import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.app_computer_ecom.dack.data.AppDatabase

object DatabaseProvider {
    @Volatile
    private var instance: AppDatabase? = null

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS 'product_history' ('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'productId' TEXT NOT NULL, 'timestamp' INTEGER NOT NULL)")
        }
    }

    fun getDatabase(context: Context): AppDatabase {
        return instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "app_database"
            )
                .addMigrations(MIGRATION_1_2)
                .build().also { instance = it }
        }
    }
}
