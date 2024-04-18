package com.example.elderlycare.board.vo

import User
import java.util.Date

data class BoardDTO(
    val num: Long = 0,
    var title: String = "",
    var writer: String = "",
    var content: String = "",
    var regdate: Date = Date(),
    var hitcount: Long = 0,
    var replycnt: Long = 0,
    var regdateStr: String = "",
    var userId: Long = 0
)