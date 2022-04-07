package me.pasindu.movieexplorer.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actor: Actor)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieActorCrossRef(crossRef: MovieActorCrossRef)

    @Transaction
    @Query("SELECT * FROM actor_table WHERE name LIKE '%'|| :keyword || '%'")
    fun fetchMovieByActor(keyword: String): Flow<List<ActorWithMovies>>

    @Transaction
    @Query("SELECT * FROM movie_table WHERE title LIKE '%' || :keyword || '%'")
    fun fetchActorsByMovie(keyword: String): Flow<List<MovieWithActors>>
}