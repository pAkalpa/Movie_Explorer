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
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import me.pasindu.movieexplorer.R
import me.pasindu.movieexplorer.adapter.ParentAdapter
import me.pasindu.movieexplorer.data.*
import me.pasindu.movieexplorer.data.entities.ActorWithMovies

class SearchForActors : AppCompatActivity() {

    private lateinit var actorET: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView

    private lateinit var itemAdapter: ParentAdapter

    private var emptyDataList = arrayListOf<ActorWithMovies>()
    private var dataList = arrayListOf<ActorWithMovies>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_actors)

        actorET = findViewById(R.id.actorEt)
        searchButton = findViewById(R.id.actorSearchButton)
        recyclerView = findViewById(R.id.parentRv)

        itemAdapter = ParentAdapter(emptyDataList)

        recyclerView.adapter = itemAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

//        hide action bar
        this.supportActionBar!!.hide()

        //        define data access object
        val movieDao = (application as MovieApp).db.movieDao()

        searchButton.setOnClickListener {
            actorET.hideKeyboard()
            if (actorET.text.isNotEmpty()) {
                getData(actorET.text.toString(), movieDao)
                runOnUiThread {
                    updateRecyclerView()
                }
            }
        }
    }

    private fun getData(query: String, movieDao: MovieDao) {
        lifecycleScope.launch {
            val data = movieDao.fetchMovieByActor(query)
            data.collect {
                dataList = ArrayList(it)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateRecyclerView() {
        recyclerView.apply {
            itemAdapter.list = dataList
            itemAdapter.notifyDataSetChanged()
        }
    }

    private fun EditText.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as
                InputMethodManager
        imm.hideSoftInputFromWindow(this.windowToken, 0)
    }
}