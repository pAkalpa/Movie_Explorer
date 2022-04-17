package me.pasindu.movieexplorer.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.pasindu.movieexplorer.data.entities.Movie
import me.pasindu.movieexplorer.data.entities.Actor
import me.pasindu.movieexplorer.data.entities.MovieActorCrossRef

@Database(
//    parse entities
    entities = [Movie::class, Actor::class, MovieActorCrossRef::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase: RoomDatabase() {

    /**
     * This [movieDao] method access database
     *
     * @return [MovieDao] Data Access Object
     */
    abstract fun movieDao(): MovieDao

    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        /**
         * This [getInstance] method build database
         *
         * @param context current context
         * @return [MovieDatabase] object
         */
        fun getInstance(context: Context): MovieDatabase {
//            singleton design pattern
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        MovieDatabase::class.java,
                        "movie_database"
                    ).fallbackToDestructiveMigration().build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}