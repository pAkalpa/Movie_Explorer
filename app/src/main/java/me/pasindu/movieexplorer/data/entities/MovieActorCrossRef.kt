package me.pasindu.movieexplorer.data.entities

import androidx.room.Entity

@Entity(primaryKeys = ["title", "name"], tableName = "movie_actor_cross_ref")
data class MovieActorCrossRef(
//    movie title
    val title: String,
//    actor name
    val name: String
)
