package me.pasindu.movieexplorer.data.entities

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ActorWithMovies(
    @Embedded
//    Actor object
    val actor: Actor,
    @Relation(
//        Actor table name
        parentColumn = "name",
//        Movie table title
        entityColumn = "title",
//        JOIN using MovieActorCrossRef class
        associateBy = Junction(MovieActorCrossRef::class)
    )
//    List of movie objects
    val movies: List<Movie>
)
