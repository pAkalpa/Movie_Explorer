package me.pasindu.movieexplorer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import me.pasindu.movieexplorer.data.*

class MainActivity : AppCompatActivity() {

    //    Add Movies to Database Button Declarations
    private lateinit var addMToDBButton: Button

    //    Search for movies Button Declaration
    private lateinit var sFMButton: Button

    //    Search for actor Button Declaration
    private lateinit var sFAButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        define data access object
        val movieDao = (application as MovieApp).db.movieDao()

        addMToDBButton = findViewById(R.id.amtDBButton)
        sFMButton = findViewById(R.id.sfmButton)
        sFAButton = findViewById(R.id.sfaButton)

//        Set click listener for add movies to database button
        addMToDBButton.setOnClickListener {
//            Invoke addDefaultMoviesToDB method and parse data access object
            addDefaultMoviesToDB(movieDao)
        }

//        Set click listener for search for movies button
        sFMButton.setOnClickListener {
            val searchForMoviesActivity = Intent(this, SearchForMovies::class.java)
            startActivity(searchForMoviesActivity)
        }

//        Set click listener for search for actors button
        sFAButton.setOnClickListener {
            val searchForActorsActivity = Intent(this, SearchForActors::class.java)
            startActivity(searchForActorsActivity)
        }
    }

    /**
     * This [addDefaultMoviesToDB] Method Add Hardcoded movies to database
     * @param movieDao Data Access Object
     */
    private fun addDefaultMoviesToDB(movieDao: MovieDao) {
//        list of Movie objects
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
                "Tim Robbins, Morgan Freeman, Bob Gunton",
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
                "Peter Weller, Ariel Winter, David Selby, Wade Williams",
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
                "Elijah Wood, Viggo Mortensen, Ian McKellen",
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
                "Leonardo DiCaprio, Joseph Gordon-Levitt, Elliot Page",
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
                "Keanu Reeves, Laurence Fishburne, Carrie-Anne Moss",
                "When a beautiful stranger leads computer hacker Neo to a forbidding underworld, he discovers the shocking truth--the life he knows is the elaborate deception of an evil cyber-intelligence.",
                "https://m.media-amazon.com/images/M/MV5BNzQzOTk3OTAtNDQ0Zi00ZTVkLWI0MTEtMDllZjNkYzNjNTc4L2ltYWdlXkEyXkFqcGdeQXVyNjU0OTQ0OTY@._V1_SX300.jpg"
            )
        )

//        list of Actor objects
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

//        list of MovieActorCrossRef objects
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
//        define lifecycleScope routine for add data to database
        lifecycleScope.launch {

//            add movies to movie_table
            listOfMovies.forEach {
                movieDao.insert(it)
            }

//            add actors to actor_table
            listOfActors.forEach {
                movieDao.insert(it)
            }

//            add MovieActorCrossRef to movie_actor_cross_ref table
            listOfCrossRef.forEach {
                movieDao.insertMovieActorCrossRef(it)
            }

//            display toast notification after operation ends
            Toast.makeText(applicationContext, "Movies Added to Database!", Toast.LENGTH_SHORT)
                .show()
        }
    }
}