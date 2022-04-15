package me.pasindu.movieexplorer.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class Movie(
    @PrimaryKey(autoGenerate = false)
//    Movie Data
    val title: String = "",
    val year: String = "",
    val rated: String = "",
    val released: String = "",
    val runtime: String = "",
    val genre: String = "",
    val director: String = "",
    val writer: String = "",
    val actors: String = "",
    val plot: String = "",
    val posterURL: String = ""
)
