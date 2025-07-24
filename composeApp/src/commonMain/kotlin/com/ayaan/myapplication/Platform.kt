package com.ayaan.myapplication

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform