package com.example.com

import java.time.Instant

data class Post(
    val title: String,
    val slug: String,
    val date: Instant,
    val content: String,
)
