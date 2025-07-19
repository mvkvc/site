package com.example.com.templates

import com.example.com.Page
import com.example.com.Post
import kotlinx.html.*
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun HTML.postListLayout(
    posts: List<Post>,
    pages: List<Page>,
) {
    rootLayout(title = "Posts", pages = pages) {
        div(classes = "not-prose space-y-8") {
            h1(classes = "text-3xl font-bold text-base-content mb-8") { +"Posts" }
            div(classes = "space-y-6") {
                posts.forEach { post ->
                    a(href = "/posts/${post.slug}", classes = "block") {
                        div(classes = "card bg-base-200 shadow hover:shadow-lg transition-shadow cursor-pointer") {
                            div(classes = "card-body") {
                                h2(classes = "card-title text-base-content") {
                                    +post.title
                                }
                                p(classes = "text-base-content/70 text-sm") {
                                    +"Published on ${post.date.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}"
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
