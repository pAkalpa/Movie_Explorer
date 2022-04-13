package me.pasindu.movieexplorer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class SearchForMovies : AppCompatActivity(), View.OnClickListener {

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

    private var isDataAvailable = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_movies)

//        hide action bar
        this.supportActionBar!!.hide()

        movieET = findViewById(R.id.movieEt)
        retrieveButton = findViewById(R.id.retrieveButton)
        saveToDbButton = findViewById(R.id.saveButton)
        messageLayout = findViewById(R.id.messageLl)
        messageIv = findViewById(R.id.messageIv)
        notFoundMessage = findViewById(R.id.messageTvA1)
        movieDataLayout = findViewById(R.id.movieDataLl)

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

        retrieveButton.setOnClickListener(this)
        saveToDbButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        movieET.hideKeyboard()
        when (view!!.id) {
            retrieveButton.id -> {
                if (movieET.text.isNotEmpty()) {
                    requestData()
                } else {
                    Toast.makeText(this, "Please Enter Movie Name", Toast.LENGTH_SHORT).show()
                }
            }
            saveToDbButton.id -> {
                if (isDataAvailable) {
//                    TODO Implement this
                } else {
                    Toast.makeText(this, "Movie Data Not Available", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun requestData() {
        val request = URL("https://www.omdbapi.com/?apikey=39f8221&type=movie&t=${movieET.text}")
        val connection = request.openConnection() as HttpURLConnection
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = connection.inputStream.bufferedReader().use { it.readLine() }
                val responseObject = JSONTokener(response).nextValue() as JSONObject
                setMovieData(responseObject)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun setMovieData(responseObject: JSONObject) {
        runOnUiThread(Runnable {
            when (responseObject.getBoolean("Response")) {
                true -> {
                    movieET.text.clear()
                    isDataAvailable = true
                    movieDataLayout.visibility = View.VISIBLE
                    messageLayout.visibility = View.GONE
                    notFoundMessage.visibility = View.GONE

                    val movieTitle = ": ${responseObject.getString("Title")}"
                    titleTv.text = movieTitle

                    val movieYear = ": ${responseObject.getString("Year")}"
                    yearTv.text = movieYear

                    val movieRated = ": ${responseObject.getString("Rated")}"
                    ratedTv.text = movieRated

                    val movieReleased = ": ${responseObject.getString("Released")}"
                    releasedTv.text = movieReleased

                    val movieRuntime = ": ${responseObject.getString("Runtime")}"
                    runtimeTv.text = movieRuntime

                    val movieGenre = ": ${responseObject.getString("Genre")}"
                    genreTv.text = movieGenre

                    val movieDirector = ": ${responseObject.getString("Director")}"
                    directorsTv.text = movieDirector

                    val movieWriter = ": ${responseObject.getString("Writer")}"
                    writerTv.text = movieWriter

                    val movieActor = ": ${responseObject.getString("Actors")}"
                    actorsTv.text = movieActor

                    val moviePlot = ": ${responseObject.getString("Plot")}"
                    plotTv.text = moviePlot

                    if (responseObject.getString("Poster").equals("N/A")) {
                        posterIv.setImageResource(R.drawable.n_a)
                    } else {
                        val imageURL = URL(responseObject.getString("Poster"))
                        val image: Deferred<Bitmap?> = lifecycleScope.async(Dispatchers.IO) {
                            imageURL.toBitmap
                        }
                        lifecycleScope.launch(Dispatchers.Main) {
                            posterIv.setImageBitmap(image.await())
                        }
                    }
                }
                false -> {
                    isDataAvailable = false
                    movieDataLayout.visibility = View.GONE
                    messageLayout.visibility = View.VISIBLE
                    notFoundMessage.visibility = View.VISIBLE
                    messageIv.setImageResource(R.drawable.seek_not)
                }
            }
        })
    }
}

val URL.toBitmap: Bitmap?
get() {
    return try {
        BitmapFactory.decodeStream(openStream())
    }catch (e: IOException){null}
}

fun EditText.showKeyboard(
) {
    requestFocus()
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun EditText.hideKeyboard(
) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
            InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}