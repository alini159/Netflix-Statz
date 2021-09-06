package realtimeDatabase

import androidx.lifecycle.LiveData
import data.vo.MovieDetails
import ui.SingleMovie.SingleMovieViewModel
import java.util.*


class Watchlist {

    private val watchlist : Stack<Int>? = null

    fun getWatchlist(): Stack<Int>? {
        return watchlist
    }

    fun addWatchlistItem(movie: Int){
        watchlist!!.add(movie)
    }

}