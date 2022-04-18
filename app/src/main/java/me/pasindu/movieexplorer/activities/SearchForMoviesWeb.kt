package me.pasindu.movieexplorer.activities

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.pasindu.movieexplorer.R
import me.pasindu.movieexplorer.adapter.WebViewAdapter
import me.pasindu.movieexplorer.model.MovieItem
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class SearchForMoviesWeb : AppCompatActivity() {
    //    define ui components
    private lateinit var movieEt: EditText
    private lateinit var searchBtn: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageLayout: LinearLayout
    private lateinit var messageIv: ImageView
    private lateinit var notFoundMessage: TextView

    //    define variable to store JSON Object
    private lateinit var responseObject: JSONObject

    //    define adapter for recycler view
    private lateinit var movieItemAdapter: WebViewAdapter

    //    define list for parse data into adapter
    private var dataList = arrayListOf<MovieItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_movies_web)

        //        hide action bar
        this.supportActionBar!!.hide()

        //    define ui components
        movieEt = findViewById(R.id.webMovieEt)
        searchBtn = findViewById(R.id.movieSearchButton)
        recyclerView = findViewById(R.id.recyclerView)
        messageLayout = findViewById(R.id.messageLl)
        messageIv = findViewById(R.id.messageIv)
        notFoundMessage = findViewById(R.id.messageTv)

        //    define adapter for recycler view
        movieItemAdapter = WebViewAdapter(dataList)

//        assign adapter
        recyclerView.adapter = movieItemAdapter
//        assign Linearlayout manager to recycler view
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (savedInstanceState != null) { // check savedInstanceState for null
//            assign saved data from shared preference
            val response = savedInstanceState.getString("response", "no")
            if (response != "no") {
//                assign converted string from shared preference
                responseObject = JSONObject(response)
//                invoke setData method and parse restored JSON object
                setData(responseObject)
            }
        }

//        set click listener for search button
        searchBtn.setOnClickListener {
//            hide keyboard on search button press
            movieEt.hideKeyboard()
            if (movieEt.text.isNotEmpty()) { // check edit text emptiness
//        declare api url
                val request =
                    URL("https://www.omdbapi.com/?apikey=39f8221&type=movie&s=*${movieEt.text}*")
//        create connection
                val connection = request.openConnection() as HttpURLConnection
//        start lifecycleScope coroutine with Input Output dispatcher
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
//                read response as string
                        val response = connection.inputStream.bufferedReader().use { it.readLine() }
//                convert received string response to json object
                        responseObject = JSONTokener(response).nextValue() as JSONObject
                        if (responseObject.getBoolean("Response")) {
//                invoke setData method and parse converted json object
                            setData(responseObject)
                        } else {
//        start ui thread
                            runOnUiThread {
                                //                    show Toast if movie data not available
                                Toast.makeText(
                                    applicationContext,
                                    "Movies Not Available",
                                    Toast.LENGTH_SHORT
                                ).show()
                                recyclerView.visibility = View.GONE
                                messageLayout.visibility = View.VISIBLE
                                notFoundMessage.visibility = View.VISIBLE
                                messageIv.setImageResource(R.drawable.seek_not)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                //                    show Toast if EditText empty
                Toast.makeText(this, "Please Enter Movie Name", Toast.LENGTH_SHORT).show()
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
        if (dataList.isNotEmpty() && responseObject.getBoolean("Response")) {
            outState.putString("response", responseObject.toString())
        }
    }

    /**
     * This [setData] method add received data to list and show in UI
     *
     * @param responseObject JSONObject of the response
     */
    private fun setData(responseObject: JSONObject) {
        dataList.clear()
        runOnUiThread {
            recyclerView.visibility = View.VISIBLE
            messageLayout.visibility = View.GONE
            notFoundMessage.visibility = View.GONE
            messageIv.setImageResource(R.drawable.seek)
        }
        val arrayJSON: JSONArray = responseObject.getJSONArray("Search")
        for (i in 0 until arrayJSON.length()) {
            val item: JSONObject = arrayJSON[i] as JSONObject
            dataList.add(
                MovieItem(
                    item.getString("Title"),
                    item.getString("Year"),
                    item.getString("Poster")
                )
            )
        }
        runOnUiThread {
            updateRecyclerView()
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    /**
     * This [updateRecyclerView] method update recycler view with newest data
     */
    private fun updateRecyclerView() {
        recyclerView.apply {
            movieItemAdapter.list = dataList
            movieItemAdapter.notifyDataSetChanged()
        }
    }

    /**
     * This [EditText.hideKeyboard] is [hideKeyboard] function extension of [EditText]
     */
    private fun EditText.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}