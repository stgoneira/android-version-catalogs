package cl.stgoneira.android.versioncatalogs

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.rememberCoroutineScope
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlinx.coroutines.launch

@Entity
data class Usuario(
    @PrimaryKey val uid:Long,
    val nombre:String?
)

@Dao
interface UsuarioDao {
    @Query("SELECT * FROM usuario")
    suspend fun getAll():List<Usuario>
    @Insert
    suspend fun insertAll(vararg usuarios:Usuario)
}

@Database(entities = [Usuario::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao() : UsuarioDao
}


class MainActivity : ComponentActivity() {
    lateinit var userDao: UsuarioDao

    override fun onCreate(savedInstanceState: Bundle?) {
        userDao = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "prueba-db").build().usuarioDao()

        super.onCreate(savedInstanceState)
        setContent {
            val scope = rememberCoroutineScope()
            Button(onClick = {
                scope.launch {
                    userDao.insertAll(Usuario(System.currentTimeMillis(), "Juan"))
                }
            }) {
                Text("Guardar")
            }
        }
    }
}

