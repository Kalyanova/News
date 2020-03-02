package by.paranoidandroid.model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName="article")
data class Article(
    @ColumnInfo(name="title") val title: String,
    @ColumnInfo(name="description") val description: String,
    @ColumnInfo(name="url_to_image") val urlToImage: String,
    @ColumnInfo(name="time") val time: String
)