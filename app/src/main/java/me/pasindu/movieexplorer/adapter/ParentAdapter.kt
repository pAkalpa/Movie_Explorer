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
        //        declare ui components
        val actorTv: TextView = itemView.findViewById(R.id.actorTitleTV)
        val childRv: RecyclerView = itemView.findViewById(R.id.childRV)
    }

    /**
     * This override [onCreateViewHolder] method inflate recycler view
     *
     * @param parent view group
     * @param viewType view type
     * @return [WebViewAdapter.ViewHolder]
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParentAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val actorView = inflater.inflate(R.layout.parent_item, parent, false)
        return ViewHolder(actorView)
    }

    /**
     * This overridden [onBindViewHolder] method add items to recycler view
     *
     * @param holder [WebViewAdapter.ViewHolder]
     * @param position position on each respective item
     */
    override fun onBindViewHolder(holder: ParentAdapter.ViewHolder, position: Int) {
//        add data to each item in the recycler view
        holder.actorTv.text = list[position].actor.name

        val movieList = list[position].movies
//        parse list
        val itemAdapter = ChildAdapter(movieList)
//        assign adapter
        holder.childRv.adapter = itemAdapter
//        assign Linearlayout manager to recycler view
        holder.childRv.layoutManager = LinearLayoutManager(holder.childRv.context)
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