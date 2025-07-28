package vc.mvk.site.templates

import vc.mvk.site.Post
import kotlinx.html.*

fun HTML.home(
    title: String = "mvkvc",
    pages: List<vc.mvk.site.Page>,
    latestPost: Post?,
    readmeContent: String,
) {
    root(title = title, pages = pages) {
        div(classes = "not-prose space-y-8") {
            latestPost?.let { post ->
                h1(classes = "text-3xl font-bold text-base-content mb-8 text-center") { +"Latest Post" }
                div(classes = "space-y-6") {
                    postPreview(post)
                }
            }
            unsafe {
                +readmeContent
            }
        }
    }
}
