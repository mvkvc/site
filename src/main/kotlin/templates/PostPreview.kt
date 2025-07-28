package vc.mvk.site.templates

import vc.mvk.site.Post
import kotlinx.html.*
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun FlowContent.postPreview(post: Post) {
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
