package me.pasindu.movieexplorer.activities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.pasindu.movieexplorer.R
import me.pasindu.movieexplorer.data.MovieApp
import me.pasindu.movieexplorer.data.entities.Actor
import me.pasindu.movieexplorer.data.entities.Movie
import me.pasindu.movieexplorer.data.entities.MovieActorCrossRef
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class SearchForMovies : AppCompatActivity(), View.OnClickListener {

    //    define ui components
    private lateinit var movieET: EditText
    private lateinit var retrieveButton: Button
    private lateinit var saveToDbButton: Button
    private lateinit var messageLayout: LinearLayout
    private lateinit var messageIv: ImageView
    private lateinit var notFoundMessage: TextView
    private lateinit var movieDataLayout: LinearLayout

    private lateinit var posterIv: ImageView
    private lateinit var titleTv: TextView
    private lateinit var yearTv: TextView
    private lateinit var ratedTv: TextView
    private lateinit var releasedTv: TextView
    private lateinit var runtimeTv: TextView
    private lateinit var genreTv: TextView
    private lateinit var directorsTv: TextView
    private lateinit var writerTv: TextView
    private lateinit var actorsTv: TextView
    private lateinit var plotTv: TextView

    //    define variable for store data availability
    private var isDataAvailable = false

    //    define variable for store JSONObject
    private lateinit var responseObject: JSONObject

    //    define fade in and out animations
    private lateinit var fadeInAnim: Animation
    private lateinit var fadeOutAnim: Animation

    //    define variable for store Movie object
    private lateinit var movieObj: Movie

    //    define List for store Actor objects
    private var actorObjList = mutableListOf<Actor>()

    //    define List for store MovieActorCrossRef objects
    private var movieActorCrossRefObjList = mutableListOf<MovieActorCrossRef>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_movies)

//        hide action bar
        this.supportActionBar!!.hide()

//        bind ui components with id's
        movieET = findViewById(R.id.movieEt)
        retrieveButton = findViewById(R.id.retrieveButton)
        saveToDbButton = findViewById(R.id.saveButton)
        messageLayout = findViewById(R.id.messageLl)
        messageIv = findViewById(R.id.messageIv)
        notFoundMessage = findViewById(R.id.messageTvA1)
        movieDataLayout = findViewById(R.id.movieDataLl)

//        bind ui components with id's
        posterIv = findViewById(R.id.posterIv)
        titleTv = findViewById(R.id.titleTv)
        yearTv = findViewById(R.id.yearTv)
        ratedTv = findViewById(R.id.ratedTv)
        releasedTv = findViewById(R.id.releasedTv)
        runtimeTv = findViewById(R.id.runtimeTv)
        genreTv = findViewById(R.id.genreTv)
        directorsTv = findViewById(R.id.directorsTv)
        writerTv = findViewById(R.id.writersTv)
        actorsTv = findViewById(R.id.actorsTv)
        plotTv = findViewById(R.id.plotTv)

//        load fade in and out animations
        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fadein)
        fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fadeout)

//        set click listeners for retrieve and save to database button
        retrieveButton.setOnClickListener(this)
        saveToDbButton.setOnClickListener(this)

        if (savedInstanceState != null) { // check savedInstanceState for null
//            assign saved data from shared preference
            val response = savedInstanceState.getString("response", "no")
            if (response != "no") { // check response availability
//                assign converted string from shared preference
                responseObject = JSONObject(response)
//                invoke setMovieData and parse response JSON Object
                setMovieData(responseObject)
            }
        }
    }

    /**
     * This Overridden Method [onClick] set click listeners for [Button]'s
     *
     * @param [view] - Activity View
     */
    override fun onClick(view: View?) {
//            hide keyboard on search button press
        movieET.hideKeyboard()
        when (view!!.id) { // check component id
            retrieveButton.id -> { // retrieve Button
                if (movieET.text.isNotEmpty()) { // check edit text emptiness
//                    invoke requestData method
                    requestData()
                } else {
//                    show Toast if EditText empty
                    Toast.makeText(this, "Please Enter Movie Name", Toast.LENGTH_SHORT).show()
                }
            }
            saveToDbButton.id -> { // saveToDb Button
                if (isDataAvailable) { // check data availability
//                    invoke insertToDatabase method
                    insertToDatabase()
                } else {
//                    show Toast if movie data not available
                    Toast.makeText(this, "Movie Data Not Available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * This Overridden Method [onSaveInstanceState] save data on activity state change
     *
     * @param [outState] - bundle in Instance
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (isDataAvailable) {
            outState.putString("response", responseObject.toString())
        }
    }

    /**
     * This [insertToDatabase] method insert movie data to database
     */
    private fun insertToDatabase() {
        //        define data access object
        val movieDao = (application as MovieApp).db.movieDao()

//        start lifecycleScope coroutine
        lifecycleScope.launch {
//            insert movie object to database
            movieDao.insert(movieObj)

//            insert actor objects to database
            actorObjList.forEach {
                movieDao.insert(it)
            }

//            insert movieActorCrossRef objects to database
            movieActorCrossRefObjList.forEach {
                movieDao.insertMovieActorCrossRef(it)
            }
        }

//        show Toast when data insertion done
        Toast.makeText(
            applicationContext,
            "${movieObj.title} Added to Database!",
            Toast.LENGTH_SHORT
        ).show()
    }

    /**
     * This [requestData] method fetch data from remote API
     */
    private fun requestData() {
//        declare api url
        val request = URL("https://www.omdbapi.com/?apikey=39f8221&type=movie&t=${movieET.text}")
//        create connection
        val connection = request.openConnection() as HttpURLConnection
//        start lifecycleScope coroutine with Input Output dispatcher
        lifecycleScope.launch(Dispatchers.IO) {
            try {
//                read response as string
                val response = connection.inputStream.bufferedReader().use { it.readLine() }
//                convert received string response to json object
                responseObject = JSONTokener(response).nextValue() as JSONObject
//                invoke setMovieData method and parse converted json object
                setMovieData(responseObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * This [setMovieData] method set received data to UI
     *
     * @param responseObject JSONObject of the response
     */
    private fun setMovieData(responseObject: JSONObject) {
//        define array to store actor names
        var actorArray: Array<String>
//        start ui thread
        runOnUiThread {
            when (responseObject.getBoolean("Response")) { // check response true or false
                true -> {
//                    clear text of the edit text
                    movieET.text.clear()
                    isDataAvailable = true
                    messageIv.setImageResource(R.drawable.seek)
                    movieDataLayout.startAnimation(fadeInAnim)
                    movieDataLayout.visibility = View.VISIBLE
                    messageLayout.startAnimation(fadeOutAnim)
                    messageLayout.visibility = View.GONE
                    notFoundMessage.visibility = View.GONE

// --------------------------- get data from json object and show in UI ----------------------------
                    val movieTitle = responseObject.getString("Title")
                    titleTv.text = movieTitle

                    val movieYear = responseObject.getString("Year")
                    yearTv.text = movieYear

                    val movieRated = responseObject.getString("Rated")
                    ratedTv.text = movieRated

                    val movieReleased = responseObject.getString("Released")
                    releasedTv.text = movieReleased

                    val movieRuntime = responseObject.getString("Runtime")
                    runtimeTv.text = movieRuntime

                    val movieGenre = responseObject.getString("Genre")
                    genreTv.text = movieGenre

                    val movieDirector = responseObject.getString("Director")
                    directorsTv.text = movieDirector

                    val movieWriter = responseObject.getString("Writer")
                    writerTv.text = movieWriter

                    val movieActor = responseObject.getString("Actors")
                    actorsTv.text = movieActor

                    val moviePlot = responseObject.getString("Plot")
                    plotTv.text = moviePlot
//--------------------------------------------------------------------------------------------------

                    if (responseObject.getString("Poster")
                            .equals("N/A")
                    ) { // check for poster availability
//                        assign placeholder to image view
                        posterIv.setImageResource(R.drawable.n_a)
                    } else {
//                        get image url
                        val imageURL = URL(responseObject.getString("Poster"))
//                        image download in separate thread
                        val image: Deferred<Bitmap?> = lifecycleScope.async(Dispatchers.IO) {
                            imageURL.toBitmap
                        }
//                        set image to image view after image downloaded in main thread
                        lifecycleScope.launch(Dispatchers.Main) {
                            posterIv.setImageBitmap(image.await())
                        }
                    }

//                    create Movie object with received data
                    movieObj = Movie(
                        responseObject.getString("Title"),
                        responseObject.getString("Year"),
                        responseObject.getString("Rated"),
                        responseObject.getString("Released"),
                        responseObject.getString("Runtime"),
                        responseObject.getString("Genre"),
                        responseObject.getString("Director"),
                        responseObject.getString("Writer"),
                        responseObject.getString("Actors"),
                        responseObject.getString("Plot"),
                        responseObject.getString("Poster")
                    )
//                    add actors to list
                    actorArray = responseObject.getString("Actors").split(",").toTypedArray()

                    for (i in actorArray) { // iterate through actor name list
//                        remove first space and add to Actor object list
                        actorObjList.add(Actor(i.trimStart()))
//                        add MovieActorCrossRef objects to list
                        movieActorCrossRefObjList.add(
                            MovieActorCrossRef(
                                movieTitle,
//                                trimStart() - remove first space
                                i.trimStart(),
                            )
                        )
                    }
                }
                false -> {
                    isDataAvailable = false
//                start fade out animation
                    movieDataLayout.startAnimation(fadeOutAnim)
//                remove component from view
                    movieDataLayout.visibility = View.GONE
//                start fade in animation
                    messageLayout.startAnimation(fadeInAnim)
//                add component to the view
                    messageLayout.visibility = View.VISIBLE
//                add component to the view
                    notFoundMessage.visibility = View.VISIBLE
//                set not found image to image view
                    messageIv.setImageResource(R.drawable.seek_not)
                }
            }
        }
    }
}

/**
 * This [URL.toBitmap] function is extension of [URL]
 */
val URL.toBitmap: Bitmap?
    get() {
        return try {
            BitmapFactory.decodeStream(openStream())
        } catch (e: IOException) {
            null
        }
    }

/**
 * This [EditText.hideKeyboard] is [hideKeyboard] function extension of [EditText]
 */
fun EditText.hideKeyboard(
) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}