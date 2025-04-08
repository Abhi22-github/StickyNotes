package com.roaa.stickynotes

data class Note(
    var filePath: String = "",
    var line: Int = 0,
    var content: String = ""
)