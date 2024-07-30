package com.myapps.note.DataClasses

data class NotesList(
    var notes : ArrayList<NoteDataClass>
)
data class NoteDataClass(
    var title :String = "",
    var content : String = ""
)
