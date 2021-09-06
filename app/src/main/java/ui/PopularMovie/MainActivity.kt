package ui.PopularMovie

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
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


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    lateinit var movieRepository: MoviePagedListRepository

    lateinit var rvMovie : RecyclerView
    lateinit var etSearch : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val uid: String = intent.getStringExtra("userId").toString()
        val movieAdapter = PopularMoviePagedListAdapter(this,uid )
        rvMovie = findViewById(R.id.rv_movie_list) as RecyclerView
        etSearch = findViewById(R.id.search)

       btnSearch.setOnClickListener {
           val searchText : String
           searchText = etSearch.text.toString()
           viewModel.moviePagedList.observe(this@MainActivity,{
               it.forEach { movie: Movie? ->
                   if (movie!!.title.toLowerCase().contains(searchText.toLowerCase())){
                       movieAdapter.searchMovie(it,movie.id)
                       println(movie.title)
                   }else{
                       println("nada ainda")
                   }

               }
           })
       }

        val apiService : TheMovieDBInterface = TheMovieDBClient.getClient()
        println(apiService)
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


        rvMovie!!.layoutManager = gridLayoutManager
        rvMovie!!.setHasFixedSize(true)
        rvMovie!!.adapter = movieAdapter



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
