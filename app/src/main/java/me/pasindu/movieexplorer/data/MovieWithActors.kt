package me.pasindu.movieexplorer.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class MovieWithActors(
    @Embedded
    val movie: Movie,
    @Relation(
        parentColumn = "title",
        entityColumn = "name",
        associateBy = Junction(MovieActorCrossRef::class)
    )
    val actors: List<Actor>
)
