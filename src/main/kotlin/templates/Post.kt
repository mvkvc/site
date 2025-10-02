package vc.mvk.site.templates

import kotlinx.html.BODY
import kotlinx.html.HTML
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.p
import kotlinx.html.unsafe
import vc.mvk.site.Page
import vc.mvk.site.Post
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun HTML.post(
    post: Post,
    pages: List<Page>,
    content: BODY.() -> Unit,
) {
    root(title = post.title, pages = pages) {
        h1(classes = "text-3xl font-bold text-base-content mb-6") { +post.title }
        post.description?.let { description ->
            p(classes = "text-base-content/80 text-base mb-4") {
                +description
            }
        }
        p(classes = "text-base-content/70 text-sm mb-8") {
            +"Published on ${post.date.atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))}"
        }
        div(classes = "prose prose-neutral max-w-none prose-pre:bg-base-200 prose-pre:border prose-pre:border-base-300") {
            unsafe {
                +post.content
            }
        }
    }
}
