package by.paranoidandroid.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="article")
data class Article(
    @PrimaryKey @ColumnInfo(name="title") val title: String,
    @ColumnInfo(name="description") val description: String,
    @ColumnInfo(name="url_to_image") val urlToImage: String,
    @ColumnInfo(name="time") val publishedAt: String
)