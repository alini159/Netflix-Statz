package ui

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MoviesViewHolder(movieView : View, context: Context, uid: String): RecyclerView.ViewHolder(movieView) {

    lateinit var cv_iv_movie_poster : ImageView
    lateinit var cv_movie_title: TextView
    lateinit var cv_movie_release_date: TextView


}