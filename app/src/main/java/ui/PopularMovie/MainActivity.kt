package ui.PopularMovie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.netflix_statz.R
import data.api.TheMovieDBClient
import data.api.TheMovieDBInterface
import data.repository.NetworkState
import data.vo.Movie
import kotlinx.android.synthetic.main.activity_main.*
import ui.Watchlist


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    lateinit var movieRepository: MoviePagedListRepository

    lateinit var etSearch : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val uid: String = intent.getStringExtra("userId").toString()
        val movieAdapter = PopularMoviePagedListAdapter(this,uid )
        etSearch = findViewById(R.id.search)

        btnwatchlist.setOnClickListener {
            val intent = Intent(this, Watchlist::class.java)
            intent.putExtra("userId", uid)
            this.startActivity(intent)
        }

       btnSearch.setOnClickListener {
           var searchText : String
           searchText = etSearch.text.toString()
           if (searchText.isNotEmpty()){
                   viewModel.moviePagedList.observe(this@MainActivity,{
                       it.forEach { movie: Movie? ->
                           if (movie!!.title.toLowerCase().contains(searchText.toLowerCase()) && !etSearch.equals("")){
                               movieAdapter.searchMovie(it,movie.id)
                           }else{
                               Toast.makeText(this,"Não temos esse filme no momento, Tente outro", Toast.LENGTH_SHORT).show()
                           }
                       }
                   })
           }else{
               Toast.makeText(this,"O campo está vazio!!", Toast.LENGTH_SHORT).show()
           }
       }
        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        movieRepository = MoviePagedListRepository(apiService)
        viewModel = getViewModel()
        val gridLayoutManager = GridLayoutManager(this, 3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                if (viewType == movieAdapter.MOVIE_VIEW_TYPE) return  1    // Movie_VIEW_TYPE will occupy 1 out of 3 span
                else return 3                                              // NETWORK_VIEW_TYPE will occupy all 3 span
            }
        };


        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter



        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if (viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if (!viewModel.listIsEmpty()) {
                movieAdapter.setNetworkState(it)
            }
        })

    }


    private fun getViewModel(): MainActivityViewModel {
        return ViewModelProviders.of(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainActivityViewModel(movieRepository) as T
            }
        })[MainActivityViewModel::class.java]
    }

}
