package by.paranoidandroid.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.OnConflictStrategy
import androidx.room.Query
import by.paranoidandroid.model.Source

@Dao
interface SourceDao {

    @Query("SELECT * from source ORDER BY name ASC")
    fun getSources(): List<Source>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(sources: List<Source>)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(source: Source)

    @Query("DELETE FROM source")
    suspend fun deleteAll()
}