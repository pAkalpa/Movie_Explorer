package me.pasindu.movieexplorer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.pasindu.movieexplorer.data.*

class MainActivity : AppCompatActivity() {

    private lateinit var addMToDBButton: Button
    private lateinit var sFMButton: Button
    private lateinit var sFAButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val movieDao = (application as MovieApp).db.movieDao()

        addMToDBButton = findViewById(R.id.amtDBButton)
        sFMButton = findViewById(R.id.sfmButton)
        sFAButton = findViewById(R.id.sfaButton)

        addMToDBButton.setOnClickListener {
            addDefaultMoviesToDB(movieDao)
        }

        sFMButton.setOnClickListener {
            lifecycleScope.launch {
                movieDao.fetchMovieByActor("El").collect {
                    Log.i("NO", it.toString())
                }
            }
        }
    }

    private fun addDefaultMoviesToDB(movieDao: MovieDao) {
        val listOfMovies = listOf(
            Movie(
                "The Shawshank Redemption",
                "1994",
                "R",
                "14 Oct 1994",
                "142 min",
                "Drama",
                "Frank Darabont",
                "Stephen King, Frank Darabont",
                "Two imprisoned men bond over a number of years, finding solace and eventual redemption through acts of common decency.",
                "https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_SX300.jpg"
            ),
            Movie(
                "Batman: The Dark Knight Returns, Part 1",
                "2012",
                "PG-13",
                "25 Sep 2012",
                "76 min",
                "Animation, Action, Crime, Drama, Thriller",
                "Jay Oliva",
                "Bob Kane (character created by: Batman), Frank Miller (comic book), Klaus Janson (comic book), Bob Goodman",
                "Batman has not been seen for ten years. A new breed of criminal ravages Gotham City, forcing 55-year-old Bruce Wayne back into the cape and cowl. But, does he still have what it takes to fight crime in a new era?",
                "https://m.media-amazon.com/images/M/MV5BMzIxMDkxNDM2M15BMl5BanBnXkFtZTcwMDA5ODY1OQ@@._V1_SX300.jpg"
            ),
            Movie(
                "The Lord of the Rings: The Return of the King",
                "2003",
                "PG-13",
                "17 Dec 2003",
                "201 min",
                "Action, Adventure, Drama",
                "Peter Jackson",
                "J.R.R. Tolkien, Fran Walsh, Philippa Boyens",
                "Gandalf and Aragorn lead the World of Men against Sauron's army to draw his gaze from Frodo and Sam as they approach Mount Doom with the One Ring.",
                "https://m.media-amazon.com/images/M/MV5BNzA5ZDNlZWMtM2NhNS00NDJjLTk4NDItYTRmY2EwMWZlMTY3XkEyXkFqcGdeQXVyNzkwMjQ5NzM@._V1_SX300.jpg"
            ),
            Movie(
                "Inception",
                "2010",
                "PG-13",
                "16 Jul 2010",
                "148 min",
                "Action, Adventure, Sci-Fi",
                "Christopher Nolan",
                "Christopher Nolan",
                "A thief who steals corporate secrets through the use of dream-sharing technology is given the inverse task of planting an idea into the mind of a C.E.O., but his tragic past may doom the project and his team to disaster.",
                "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_SX300.jpg"
            ),
            Movie(
                "The Matrix",
                "1999",
                "R",
                "31 Mar 1999",
                "136 min",
                "Action, Sci-Fi",
                "Lana Wachowski, Lilly Wachowski",
                "Lilly Wachowski, Lana Wachowski",
                "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence.",
                "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg"
            )
        )

        val listOfActors = listOf(
            Actor(
                "Tim Robbins"
            ),
            Actor(
                "Morgan Freeman"
            ),
            Actor(
                "Bob Gunton"
            ),
            Actor(
                "Peter Weller"
            ),
            Actor(
                "Ariel Winter"
            ),
            Actor(
                "David Selby"
            ),
            Actor(
                "Wade Williams"
            ),
            Actor(
                "Elijah Wood"
            ),
            Actor(
                "Viggo Mortensen"
            ),
            Actor(
                "Ian McKellen"
            ),
            Actor(
                "Leonardo DiCaprio"
            ),
            Actor(
                "Joseph Gordon-Levitt"
            ),
            Actor(
                "Elliot Page"
            ),
            Actor(
                "Keanu Reeves"
            ),
            Actor(
                "Laurence Fishburne"
            ),
            Actor(
                "Carrie-Anne Moss"
            )
        )

        val listOfCrossRef = listOf(
            MovieActorCrossRef(
                "The Shawshank Redemption",
                "Tim Robbins"
            ),
            MovieActorCrossRef(
                "The Shawshank Redemption",
                "Morgan Freeman"
            ),
            MovieActorCrossRef(
                "The Shawshank Redemption",
                "Bob Gunton"
            ),
            MovieActorCrossRef(
                "Batman: The Dark Knight Returns, Part 1",
                "Peter Weller"
            ),
            MovieActorCrossRef(
                "Batman: The Dark Knight Returns, Part 1",
                "Ariel Winter"
            ),
            MovieActorCrossRef(
                "Batman: The Dark Knight Returns, Part 1",
                "David Selby"
            ),
            MovieActorCrossRef(
                "Batman: The Dark Knight Returns, Part 1",
                "Wade Williams"
            ),
            MovieActorCrossRef(
                "The Lord of the Rings: The Return of the King",
                "Elijah Wood"
            ),
            MovieActorCrossRef(
                "The Lord of the Rings: The Return of the King",
                "Viggo Mortensen"
            ),
            MovieActorCrossRef(
                "The Lord of the Rings: The Return of the King",
                "Ian McKellen"
            ),
            MovieActorCrossRef(
                "Inception",
                "Leonardo DiCaprio"
            ),
            MovieActorCrossRef(
                "Inception",
                "Joseph Gordon-Levitt"
            ),
            MovieActorCrossRef(
                "Inception",
                "Elliot Page"
            ),
            MovieActorCrossRef(
                "The Matrix",
                "Keanu Reeves"
            ),
            MovieActorCrossRef(
                "The Matrix",
                "Laurence Fishburne"
            ),
            MovieActorCrossRef(
                "The Matrix",
                "Carrie-Anne Moss"
            )
        )
        lifecycleScope.launch {

            listOfMovies.forEach {
                movieDao.insert(it)
            }

            listOfActors.forEach {
                movieDao.insert(it)
            }

            listOfCrossRef.forEach {
                movieDao.insertMovieActorCrossRef(it)
            }

            Toast.makeText(applicationContext, "Movies Added to Database!", Toast.LENGTH_SHORT).show()
        }
    }
}