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
    private lateinit var movieEt: EditText
    private lateinit var searchBtn: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageLayout: LinearLayout
    private lateinit var messageIv: ImageView
    private lateinit var notFoundMessage: TextView

    private lateinit var responseObject: JSONObject

    private lateinit var movieItemAdapter: WebViewAdapter

    private var emptyDataList = arrayListOf<MovieItem>()
    private var dataList = arrayListOf<MovieItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_movies_web)

        movieEt = findViewById(R.id.webMovieEt)
        searchBtn = findViewById(R.id.movieSearchButton)
        recyclerView = findViewById(R.id.recyclerView)
        messageLayout = findViewById(R.id.messageLl)
        messageIv = findViewById(R.id.messageIv)
        notFoundMessage = findViewById(R.id.messageTv)

        movieItemAdapter = WebViewAdapter(emptyDataList)

        recyclerView.adapter = movieItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (savedInstanceState != null) {
            val response = savedInstanceState.getString("response", "no")
            if (response != "no") {
                responseObject = JSONObject(response)
                setData(responseObject)
            }
        }

        searchBtn.setOnClickListener {
            movieEt.hideKeyboard()
            if (movieEt.text.isNotEmpty()) {
                val request =
                    URL("https://www.omdbapi.com/?apikey=39f8221&type=movie&s=${movieEt.text}")
                val connection = request.openConnection() as HttpURLConnection
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val response = connection.inputStream.bufferedReader().use { it.readLine() }
                        responseObject = JSONTokener(response).nextValue() as JSONObject
                        if (responseObject.getBoolean("Response")) {
                            setData(responseObject)
                        } else {
                            runOnUiThread {
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
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (dataList.isNotEmpty()) {
            outState.putString("response", responseObject.toString())
        }
    }

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
    private fun updateRecyclerView() {
        recyclerView.apply {
            movieItemAdapter.list = dataList
            movieItemAdapter.notifyDataSetChanged()
        }
    }

    private fun EditText.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}