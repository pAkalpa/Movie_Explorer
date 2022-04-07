package me.pasindu.movieexplorer.data

import android.app.Application

class MovieApp: Application() {
//    Initialize MovieDatabase object lazily
    val db by lazy {
        MovieDatabase.getInstance(this)
    }
}