package me.pasindu.movieexplorer.data

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class ActorWithMovies(
    @Embedded
    val actor: Actor,
    @Relation(
        parentColumn = "name",
        entityColumn = "title",
        associateBy = Junction(MovieActorCrossRef::class)
    )
    val movies: List<Movie>
)
