package vc.mvk.site.templates

import vc.mvk.site.Page
import vc.mvk.site.Post
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
        div(classes = "prose prose-lg max-w-none") {
            unsafe {
                +post.content
            }
        }
    }
}
