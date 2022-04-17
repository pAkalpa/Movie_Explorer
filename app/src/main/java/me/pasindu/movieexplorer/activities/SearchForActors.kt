package me.pasindu.movieexplorer.activities

import android.annotation.SuppressLint
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.*
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
    private lateinit var messageLayout: LinearLayout
    private lateinit var messageIv: ImageView
    private lateinit var messageTv: TextView

    private lateinit var itemAdapter: ParentAdapter

    private lateinit var fadeInAnim: Animation
    private lateinit var fadeOutAnim: Animation

    private var emptyDataList = arrayListOf<ActorWithMovies>()
    private var dataList = arrayListOf<ActorWithMovies>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_actors)

        actorET = findViewById(R.id.actorEt)
        searchButton = findViewById(R.id.actorSearchButton)
        recyclerView = findViewById(R.id.parentRv)
        messageLayout = findViewById(R.id.messageLl)
        messageIv = findViewById(R.id.messageIv)
        messageTv = findViewById(R.id.messageTv)

        itemAdapter = ParentAdapter(emptyDataList)

        recyclerView.adapter = itemAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

//        hide action bar
        this.supportActionBar!!.hide()

        //        define data access object
        val movieDao = (application as MovieApp).db.movieDao()

        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fadein)
        fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fadeout)

        searchButton.setOnClickListener {
            actorET.hideKeyboard()
            if (actorET.text.isNotEmpty()) {
                messageLayout.startAnimation(fadeOutAnim)
                messageLayout.visibility = View.GONE
                recyclerView.startAnimation(fadeInAnim)
                recyclerView.visibility = View.VISIBLE
                getData(actorET.text.toString(), movieDao)
            }
        }

        if (savedInstanceState != null) {
            if (savedInstanceState.getBoolean("data")) {
                getData(savedInstanceState.getString("keyword")!!,movieDao)
            }
        }
    }

    private fun getData(query: String, movieDao: MovieDao) {
        lifecycleScope.launch {
            val data = movieDao.fetchMovieByActor(query)
            data.collect {
                dataList = ArrayList(it)
                if (dataList.isEmpty()) {
                    messageTv.visibility = View.VISIBLE
                    messageLayout.startAnimation(fadeInAnim)
                    messageLayout.visibility = View.VISIBLE
                    recyclerView.startAnimation(fadeOutAnim)
                    recyclerView.visibility = View.GONE
                    messageIv.setImageResource(R.drawable.seek_not)
                } else {
                    messageTv.visibility = View.GONE
                    messageIv.setImageResource(R.drawable.seek)
                }
                runOnUiThread {
                    updateRecyclerView()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (actorET.text.isNotEmpty() && dataList.isNotEmpty()) {
            outState.putBoolean("data", true)
            outState.putString("keyword", actorET.text.toString())
            Log.i("MD data", "true")
        } else {
            outState.putBoolean("data", false)
            Log.i("MD data", "false")
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