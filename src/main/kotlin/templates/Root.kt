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
        div(classes = "navbar bg-base-100 border-b border-base-300 px-6") {
            div(classes = "flex-1") {
                a(classes = "text-xl font-bold text-base-content hover:text-primary transition-colors", href = "/") {
                    +"mvkvc"
                }
            }
            div(classes = "flex-none") {
                div(classes = "flex gap-6") {
                    a(classes = "link link-hover text-base-content/80 hover:text-base-content transition-colors", href = "/") { +"Home" }
                    a(classes = "link link-hover text-base-content/80 hover:text-base-content transition-colors", href = "/posts") { +"Posts" }
                    pages.forEach { page ->
                        a(classes = "link link-hover text-base-content/80 hover:text-base-content transition-colors", href = page.href) { +page.name }
                    }
                }
            }
        }

        main(classes = "container mx-auto px-6 py-16 max-w-4xl min-h-screen") {
            div(classes = "prose prose-neutral max-w-none prose-headings:text-base-content prose-p:text-base-content/90 prose-a:text-primary hover:prose-a:text-primary/80 prose-strong:text-base-content prose-img:rounded-lg prose-img:shadow-md") {
                this@body.content()
            }
        }
    }
}
