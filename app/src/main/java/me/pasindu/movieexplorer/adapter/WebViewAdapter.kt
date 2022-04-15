package me.pasindu.movieexplorer.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import me.pasindu.movieexplorer.model.MovieItem
import me.pasindu.movieexplorer.R

class WebViewAdapter(var list: ArrayList<MovieItem>) :
    RecyclerView.Adapter<WebViewAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv: TextView = itemView.findViewById(R.id.movieTitleTV)
        val yearTv: TextView = itemView.findViewById(R.id.movieYearTV)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebViewAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val movieItemView = inflater.inflate(R.layout.movie_item, parent, false)
        return ViewHolder(movieItemView)
    }

    override fun onBindViewHolder(holder: WebViewAdapter.ViewHolder, position: Int) {
        holder.titleTv.text = list[position].title
        holder.yearTv.text = list[position].year
    }

    override fun getItemCount(): Int {
        return list.size
    }
}