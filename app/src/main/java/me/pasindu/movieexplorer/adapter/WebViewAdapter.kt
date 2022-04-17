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
        //        declare ui components
        val titleTv: TextView = itemView.findViewById(R.id.movieTitleTV)
        val yearTv: TextView = itemView.findViewById(R.id.movieYearTV)
    }

    /**
     * This override [onCreateViewHolder] method inflate recycler view
     *
     * @param parent view group
     * @param viewType view type
     * @return [WebViewAdapter.ViewHolder]
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WebViewAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val movieItemView = inflater.inflate(R.layout.movie_item, parent, false)
        return ViewHolder(movieItemView)
    }

    /**
     * This overridden [onBindViewHolder] method add items to recycler view
     *
     * @param holder [WebViewAdapter.ViewHolder]
     * @param position position on each respective item
     */
    override fun onBindViewHolder(holder: WebViewAdapter.ViewHolder, position: Int) {
//        add data to each item in the recycler view
        holder.titleTv.text = list[position].title
        holder.yearTv.text = list[position].year
    }

    /**
     * This overridden [getItemCount] method return item count
     *
     * @return [Int] no of items in given list
     */
    override fun getItemCount(): Int {
        return list.size
    }
}