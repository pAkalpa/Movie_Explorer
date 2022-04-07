package me.pasindu.movieexplorer.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    /**
     * This method [insert] inserts movies to movie_table
     * @param movie Movie object
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(movie: Movie)

    /**
     * This method [insert] inserts actors to actor_table
     * @param actor Actor object
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(actor: Actor)

    /**
     * This method [insertMovieActorCrossRef] inserts MovieActorCrossRef to movie_actor_cross_ref table
     * @param crossRef MovieActorCrossRef objects
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieActorCrossRef(crossRef: MovieActorCrossRef)

    /**
     * This method [fetchMovieByActor] returns list of ActorWithMovies objects for given keyword
     * @param keyword search keyword
     * @return [Flow] flow listOf ActorWithMovies objects
     */
    @Transaction
    @Query("SELECT * FROM actor_table WHERE name LIKE '%'|| :keyword || '%'")
    fun fetchMovieByActor(keyword: String): Flow<List<ActorWithMovies>>

    @Transaction
    @Query("SELECT * FROM movie_table WHERE title LIKE '%' || :keyword || '%'")
    fun fetchActorsByMovie(keyword: String): Flow<List<MovieWithActors>>
}