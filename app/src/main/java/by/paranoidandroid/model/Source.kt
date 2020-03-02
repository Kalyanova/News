package by.paranoidandroid.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName="source")
class Source(
    @ColumnInfo(name="name") val name: String,
    @ColumnInfo(name="enabled") val enabled: Boolean
)