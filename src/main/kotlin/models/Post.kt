package vc.mvk.site

import java.time.Instant

data class Post(
    val title: String,
    val published: Boolean,
    val slug: String,
    val date: Instant,
    val content: String,
)
