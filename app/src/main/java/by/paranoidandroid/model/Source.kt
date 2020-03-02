package by.paranoidandroid.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="source")
class Source(
    @PrimaryKey @ColumnInfo(name="name") val name: String,
    @ColumnInfo(name="enabled") val enabled: Boolean
)