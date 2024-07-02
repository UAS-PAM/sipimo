package com.example.sipimo

data class User(val userId: String,val username: String, val email: String, val fullname: String)  {
    constructor(): this("", "", "", "")
}


