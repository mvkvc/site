package vc.mvk.site.templates

import kotlinx.html.FlowContent
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.h2
import kotlinx.html.p
import vc.mvk.site.Post
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun FlowContent.postPreview(post: Post) {
    a(href = "/posts/${post.slug}", classes = "block group") {
        div(classes = "card bg-base-100 border border-base-300 hover:border-primary/20 hover:shadow-md transition-all duration-200 cursor-pointer") {
            div(classes = "card-body p-6") {
                h2(classes = "card-title text-base-content text-lg mb-2 group-hover:text-primary transition-colors") {
                    +post.title
                }
                post.description?.let { description ->
                    p(classes = "text-base-content/70 text-sm mb-3 line-clamp-2") {
                        +description
                    }
                }
                p(classes = "text-base-content/60 text-sm font-medium") {
                    +"Published on ${post.date.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}"
                }
            }
        }
    }
}
