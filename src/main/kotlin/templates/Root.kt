package vc.mvk.site.templates

import kotlinx.html.BODY
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.head
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.script
import kotlinx.html.title
import vc.mvk.site.Page

fun HTML.root(
    title: String = "mvkvc",
    pages: List<Page>,
    content: BODY.() -> Unit,
) {
    head {
        meta(charset = "utf-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1")
        title(title)
        link(rel = "apple-touch-icon", href = "/static/apple-touch-icon.png")
        link(rel = "icon", type = "image/png", href = "/static/favicon-32x32.png")
        link(rel = "icon", type = "image/png", href = "/static/favicon-16x16.png")
        link(rel = "manifest", href = "/static/site.webmanifest")
        link(
            rel = "stylesheet",
            href = "/static/output.css",
        )
        link(
            rel = "stylesheet",
            href = "/static/prism-one-dark.css",
        )
        script(src = "/static/prism.js") {}
    }

    body(classes = "bg-base-100 text-base-content") {
        div(classes = "navbar bg-base-200") {
            div(classes = "flex-1") {
                a(classes = "text-xl font-bold text-base-content pl-4", href = "/") {
                    +"mvkvc"
                }
            }
            div(classes = "flex-none") {
                div(classes = "tabs") {
                    a(classes = "tab tab-bordered", href = "/") { +"Home" }
                    a(classes = "tab tab-bordered", href = "/posts") { +"Posts" }
                    pages.forEach { page ->
                        a(classes = "tab tab-bordered", href = page.href) { +page.name }
                    }
                }
            }
        }

        main(classes = "container mx-auto px-6 py-16 max-w-4xl") {
            div(classes = "prose prose-lg max-w-none") {
                this@body.content()
            }
        }
    }
}
