package me.pasindu.movieexplorer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.pasindu.movieexplorer.R
import me.pasindu.movieexplorer.data.entities.Movie

class ChildAdapter(private var movieList: List<Movie>) :
    RecyclerView.Adapter<ChildAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv: TextView = itemView.findViewById(R.id.movieTitleTV)
        val yearTv: TextView = itemView.findViewById(R.id.movieYearTV)
        val runtimeTv: TextView = itemView.findViewById(R.id.movieRuntimeTV)
        val genreTv: TextView = itemView.findViewById(R.id.movieGenreTV)
        val actorsTv: TextView = itemView.findViewById(R.id.movieActorTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val movieDataView = inflater.inflate(R.layout.child_item, parent, false)
        return ViewHolder(movieDataView)
    }

    override fun onBindViewHolder(holder: ChildAdapter.ViewHolder, position: Int) {
        holder.titleTv.text = movieList[position].title
        holder.yearTv.text = movieList[position].year
        holder.runtimeTv.text = movieList[position].runtime
        holder.genreTv.text = movieList[position].genre
        holder.actorsTv.text = movieList[position].actors
    }

    override fun getItemCount(): Int {
        return movieList.size
    }
}