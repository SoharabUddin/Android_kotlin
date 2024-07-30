package com.myapps.moviesapp.dataClass

import java.util.Locale.Category

data class MoviesData(
    var page:Int = 0,
    var results: ArrayList<Results>
)

data class Results(
    var _id: String,
    var titleText: TitleText,
    var primaryImage: PrimaryImage,
    var releaseYear: ReleaseYear,
    var releaseDate : ReleaseDate,
    var titleType : TitleType
)
data class TitleText(
    var text: String=""
)
data class PrimaryImage(
    var url : String =" ",
    var width : Int =0,
    var height : Int =0
)
data class  ReleaseYear(
    var year: Int =0
)
data class ReleaseDate(
    var day : Int,
    var month : Int,
    var year : Int
)
data class TitleType(
    var categories: List<Categories>
)

data class Categories(
    var value: String
)
