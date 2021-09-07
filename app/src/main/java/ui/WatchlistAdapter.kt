package ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.netflix_statz.databinding.MovieListItemBinding
import data.api.POSTER_BASE_URL
import data.repository.NetworkState
import data.vo.Movie
import data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

import kotlinx.android.synthetic.main.watchlist_item.view.*
import ui.PopularMovie.PopularMoviePagedListAdapter
import ui.SingleMovie.MovieDetailsRepository
import ui.SingleMovie.SingleMovie
import ui.WatchlistAdapter.*

class WatchlistAdapter(public val context: Context, val userId: String)
    : ListAdapter<Movie, WatchlistAdapterViewHolder>(DIFF_CALLBACK) {

    companion object{
        private val DIFF_CALLBACK = object  :   DiffUtil.ItemCallback<Movie>(){
            override fun areItemsTheSame(
                oldItem: Movie,
                newItem: Movie): Boolean {
                return  oldItem.id == newItem.id
            }
            override fun areContentsTheSame(
                oldItem: Movie,
                newItem: Movie): Boolean {
            return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WatchlistAdapterViewHolder {
        return WatchlistAdapterViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WatchlistAdapterViewHolder, position: Int) {
        (holder as PopularMoviePagedListAdapter.MovieItemViewHolder).bind(getItem(position),context, userId)
    }
    class WatchlistAdapterViewHolder(
        private val itemBinding: MovieListItemBinding
    ) : RecyclerView.ViewHolder(itemBinding.root){

        fun bind(movie: Movie?, context: Context, uid: String?) {
            itemBinding.run {
                itemView.wl_movie_title.text = movie?.title
                itemView.wl_movie_release_date.text =  movie?.releaseDate

                val moviePosterURL = POSTER_BASE_URL + movie?.posterPath
                Glide.with(itemView.context)
                    .load(moviePosterURL)
                    .into(itemView.wl_iv_movie_poster);

                itemView.setOnClickListener{
                    val intent = Intent(context, SingleMovie::class.java)
                    intent.putExtra("id", movie?.id)
                    intent.putExtra("userId", uid)
                    context.startActivity(intent)
                }
            }
        }

        companion object {
            fun create(parent : ViewGroup): WatchlistAdapterViewHolder{
                val itemBinding = MovieListItemBinding
                    .inflate(LayoutInflater.from(parent.context),parent,false)

                return WatchlistAdapterViewHolder(itemBinding)
            }
        }
    }
}
class WatchlistMovieViewModel (private val movieRepository : MovieDetailsRepository, movieId: Int)  : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val  movieDetails : LiveData<MovieDetails> by lazy {
        movieRepository.fetchSingleMovieDetails(compositeDisposable,movieId)
    }

    val networkState : LiveData<NetworkState> by lazy {
        movieRepository.getMovieDetailsNetworkState()
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }



}