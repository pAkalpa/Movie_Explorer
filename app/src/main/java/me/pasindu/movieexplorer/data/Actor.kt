package me.pasindu.movieexplorer.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "actor_table")
data class Actor(
    @PrimaryKey(autoGenerate = false)
    val name: String = ""
)
