package me.pasindu.movieexplorer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import me.pasindu.movieexplorer.R
import me.pasindu.movieexplorer.data.entities.ActorWithMovies

class ParentAdapter(var list: ArrayList<ActorWithMovies>) :
    RecyclerView.Adapter<ParentAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val actorTv: TextView = itemView.findViewById(R.id.actorTitleTV)
        val childRv: RecyclerView = itemView.findViewById(R.id.childRV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val actorView = inflater.inflate(R.layout.parent_item, parent, false)
        return ViewHolder(actorView)
    }

    override fun onBindViewHolder(holder: ParentAdapter.ViewHolder, position: Int) {
        holder.actorTv.text = list[position].actor.name

        val movieList = list[position].movies
        val itemAdapter = ChildAdapter(movieList)
        holder.childRv.adapter = itemAdapter
        holder.childRv.layoutManager = LinearLayoutManager(holder.childRv.context)
    }

    override fun getItemCount(): Int {
        return list.size
    }

}