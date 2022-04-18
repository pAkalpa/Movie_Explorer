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

    //    define ui components
    private lateinit var actorET: EditText
    private lateinit var searchButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var messageLayout: LinearLayout
    private lateinit var messageIv: ImageView
    private lateinit var messageTv: TextView

    //    define adapter for recycler view
    private lateinit var itemAdapter: ParentAdapter

    //    define fade in and out animations
    private lateinit var fadeInAnim: Animation
    private lateinit var fadeOutAnim: Animation

    //    define list for parse data into adapter
    private var dataList = arrayListOf<ActorWithMovies>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_for_actors)

//        bind ui components with id's
        actorET = findViewById(R.id.actorEt)
        searchButton = findViewById(R.id.actorSearchButton)
        recyclerView = findViewById(R.id.parentRv)
        messageLayout = findViewById(R.id.messageLl)
        messageIv = findViewById(R.id.messageIv)
        messageTv = findViewById(R.id.messageTv)

//        parse list
        itemAdapter = ParentAdapter(dataList)

//        assign adapter
        recyclerView.adapter = itemAdapter
//        assign Linearlayout manager to recycler view
        recyclerView.layoutManager = LinearLayoutManager(this)

//        hide action bar
        this.supportActionBar!!.hide()

        //        define data access object
        val movieDao = (application as MovieApp).db.movieDao()

//        load fade in and out animations
        fadeInAnim = AnimationUtils.loadAnimation(this, R.anim.fadein)
        fadeOutAnim = AnimationUtils.loadAnimation(this, R.anim.fadeout)

//        set click listener for search button
        searchButton.setOnClickListener {
//            hide keyboard on search button press
            actorET.hideKeyboard()
            if (actorET.text.isNotEmpty()) { // check edit text emptiness
//                start fadeout animation
                messageLayout.startAnimation(fadeOutAnim)
//                remove component from view
                messageLayout.visibility = View.GONE
//                start fade in animation
                recyclerView.startAnimation(fadeInAnim)
//                remove component from view
                recyclerView.visibility = View.VISIBLE
//                invoke getData method and parse text and data access object
                getData(actorET.text.toString(), movieDao)
            } else {
                //                    show Toast if EditText empty
                Toast.makeText(this, "Please Enter Actor Name", Toast.LENGTH_SHORT).show()
            }
        }

        if (savedInstanceState != null) { // check savedInstanceState for null
            if (savedInstanceState.getBoolean("data")) { // check data stored
//                start fadeout animation
                messageLayout.startAnimation(fadeOutAnim)
//                remove component from view
                messageLayout.visibility = View.GONE
//                start fade in animation
                recyclerView.startAnimation(fadeInAnim)
//                add component to the view
                recyclerView.visibility = View.VISIBLE
//                invoke getData method and parse text and data access object
                getData(savedInstanceState.getString("keyword")!!, movieDao)
            }
        }
    }

    /**
     * This [getData] Method fetch data from database and show in the ui
     *
     * @param query search keyword
     * @param movieDao data access object
     */
    private fun getData(query: String, movieDao: MovieDao) {
//        start lifecycleScope coroutine
        lifecycleScope.launch {
//            fetch data from database and store in variable
            val data = movieDao.fetchMovieByActor(query)
//            datatype is flow so use collect method to get data
            data.collect {
//                assign collected data to variable
                dataList = ArrayList(it)
                if (dataList.isEmpty()) { // check dataList emptiness
                    messageTv.visibility = View.VISIBLE
//                start fade in animation
                    messageLayout.startAnimation(fadeInAnim)
//                add component to the view
                    messageLayout.visibility = View.VISIBLE
//                start fade out animation
                    recyclerView.startAnimation(fadeOutAnim)
//                remove component from view
                    recyclerView.visibility = View.GONE
//                set not found image to image view
                    messageIv.setImageResource(R.drawable.seek_not)
                    //                    show Toast if movie data not available
                    Toast.makeText(
                        applicationContext,
                        "Movies Not Available",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
//                remove component from view
                    messageTv.visibility = View.GONE
//                set found image to image view
                    messageIv.setImageResource(R.drawable.seek)
                }
//                start ui thread
                runOnUiThread {
//                    invoke updateRecyclerView method inside ui thread
                    updateRecyclerView()
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
    /**
     * This [updateRecyclerView] method update recycler view with newest data
     */
    private fun updateRecyclerView() {
//        apply changes to recycler view
        recyclerView.apply {
//            assign list to recycler view adapter
            itemAdapter.list = dataList
//            notify recycler view adapter to update
            itemAdapter.notifyDataSetChanged()
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