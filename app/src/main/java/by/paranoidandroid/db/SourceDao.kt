package by.paranoidandroid.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Update
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.paranoidandroid.model.Source

@Dao
interface SourceDao {

    @Query("SELECT * from source ORDER BY name ASC")
    fun getSources(): LiveData<List<Source>>

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(source: Source)

    @Query("DELETE FROM source")
    suspend fun deleteAll()
}