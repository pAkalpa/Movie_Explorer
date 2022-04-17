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
        //        declare ui components
        val titleTv: TextView = itemView.findViewById(R.id.movieTitleTV)
        val yearTv: TextView = itemView.findViewById(R.id.movieYearTV)
        val runtimeTv: TextView = itemView.findViewById(R.id.movieRuntimeTV)
        val genreTv: TextView = itemView.findViewById(R.id.movieGenreTV)
        val actorsTv: TextView = itemView.findViewById(R.id.movieActorTV)
    }

    /**
     * This override [onCreateViewHolder] method inflate recycler view
     *
     * @param parent view group
     * @param viewType view type
     * @return [WebViewAdapter.ViewHolder]
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChildAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val movieDataView = inflater.inflate(R.layout.child_item, parent, false)
        return ViewHolder(movieDataView)
    }

    /**
     * This overridden [onBindViewHolder] method add items to recycler view
     *
     * @param holder [WebViewAdapter.ViewHolder]
     * @param position position on each respective item
     */
    override fun onBindViewHolder(holder: ChildAdapter.ViewHolder, position: Int) {
//        add data to each item in the recycler view
        holder.titleTv.text = movieList[position].title
        holder.yearTv.text = movieList[position].year
        holder.runtimeTv.text = movieList[position].runtime
        holder.genreTv.text = movieList[position].genre
        holder.actorsTv.text = movieList[position].actors
    }

    /**
     * This overridden [getItemCount] method return item count
     *
     * @return [Int] no of items in given list
     */
    override fun getItemCount(): Int {
        return movieList.size
    }
}