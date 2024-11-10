package com.example.recycleview.model

import java.time.LocalDate

data class Todo(val id:Int, val title:String, val subTitle:String, val day:LocalDate, val status:Status)
