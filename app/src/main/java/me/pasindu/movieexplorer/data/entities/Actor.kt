package me.pasindu.movieexplorer.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actor_table")
data class Actor(
    @PrimaryKey(autoGenerate = false)
//    Actor name
    val name: String = ""
)
