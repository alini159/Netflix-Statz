package com.example.netflix_statz


class Movies {
    var id: Int? = null
    var posterPath: String? = null
    var releaseDate: String? = null

    constructor()

    constructor(id: Int?, posterPath: String?, releaseDate: String?, title: String?) {
        this.id = id
        this.posterPath = posterPath
        this.releaseDate = releaseDate
        this.title = title
    }

    var title: String? = null
}