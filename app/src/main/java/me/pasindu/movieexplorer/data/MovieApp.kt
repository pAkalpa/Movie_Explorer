package me.pasindu.movieexplorer.data

import android.app.Application

class MovieApp: Application() {
    val db by lazy {
        MovieDatabase.getInstance(this)
    }
}