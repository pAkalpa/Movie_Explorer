package me.pasindu.movieexplorer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class Movie(
    @PrimaryKey(autoGenerate = false)
    val title: String = "",
    val year: String = "",
    val rated: String = "",
    val released: String = "",
    val runtime: String = "",
    val genre: String = "",
    val director: String = "",
    val writer: String = "",
    val plot: String = "",
    val posterURL: String = ""
)
