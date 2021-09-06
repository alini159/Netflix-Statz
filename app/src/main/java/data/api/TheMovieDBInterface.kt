package data.api

import data.vo.MovieDetails
import data.vo.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBInterface {

    //https://api.themoviedb.org/3/movie/566525?api_key=ffbee15e415cfabc2ad4ffb21b21c781
    //https://api.themoviedb.org/3/movie/popular?api_key=ffbee15e415cfabc2ad4ffb21b21c781&page=1
    //https://api.themoviedb.org/3/

    @GET("movie/popular")
    fun getPopularMovie(@Query("page") page: Int) : Single<MovieResponse>//alteração

    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id: Int) : Single<MovieDetails>

}