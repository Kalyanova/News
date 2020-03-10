package by.paranoidandroid.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="source")
class Source(
    @PrimaryKey @ColumnInfo(name="id") val id: String,
    @ColumnInfo(name="name") val name: String,
    @ColumnInfo(name="description") val description: String,
    @ColumnInfo(name="enabled") var enabled: Boolean = false
)