package com.example.com.templates

import com.example.com.Page
import com.example.com.Post
import kotlinx.html.*
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun HTML.postLayout(
    post: Post,
    pages: List<Page>,
    content: BODY.() -> Unit,
) {
    rootLayout(title = post.title, pages = pages) {
        h1(classes = "text-3xl font-bold text-base-content mb-6") { +post.title }
        p(classes = "text-base-content/70 text-sm mb-8") {
            +"Published on ${post.date.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}"
        }
        div {
            unsafe {
                +post.content
            }
        }
    }
}
