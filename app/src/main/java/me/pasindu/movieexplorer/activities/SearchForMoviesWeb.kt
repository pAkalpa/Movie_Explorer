package me.pasindu.movieexplorer.activities

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
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

    private lateinit var movieItemAdapter: WebViewAdapter

    private var emptyDataList = arrayListOf<MovieItem>()
    private var dataList = arrayListOf<MovieItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_movies_web)

        movieEt = findViewById(R.id.webMovieEt)
        searchBtn = findViewById(R.id.movieSearchButton)
        recyclerView = findViewById(R.id.recyclerView)

        movieItemAdapter = WebViewAdapter(emptyDataList)

        recyclerView.adapter = movieItemAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)


        searchBtn.setOnClickListener {
            movieEt.hideKeyboard()
            if (movieEt.text.isNotEmpty()) {
                val request =
                    URL("https://www.omdbapi.com/?apikey=39f8221&type=movie&s=${movieEt.text}")
                val connection = request.openConnection() as HttpURLConnection
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        val response = connection.inputStream.bufferedReader().use { it.readLine() }
                        val responseObject = JSONTokener(response).nextValue() as JSONObject
                        if (responseObject.getBoolean("Response")) {
                            getData(responseObject)
                            runOnUiThread {
                                updateRecyclerView()
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private fun getData(responseObject: JSONObject) {
        dataList.clear()
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