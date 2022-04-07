package me.pasindu.movieexplorer.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieWithActors(
    @Embedded
//    Movie object
    val movie: Movie,
    @Relation(
//        Movie table title
        parentColumn = "title",
//        Actor table name
        entityColumn = "name",
//        JOIN using MovieActorCrossRef class
        associateBy = Junction(MovieActorCrossRef::class)
    )
//    List of actor objects
    val actors: List<Actor>
)
