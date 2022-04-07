package me.pasindu.movieexplorer.data

import androidx.room.Entity

@Entity(primaryKeys = ["title","name"], tableName = "movie_actor_cross_ref")
data class MovieActorCrossRef(
    val title: String,
    val name: String
)
