package vc.mvk.site

import io.ktor.http.ContentType
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationCall
import io.ktor.server.html.respondHtml
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import kotlinx.css.Color
import kotlinx.css.CssBuilder
import kotlinx.css.Margin
import kotlinx.css.backgroundColor
import kotlinx.css.body
import kotlinx.css.color
import kotlinx.css.margin
import kotlinx.css.px
import kotlinx.html.body
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.li
import kotlinx.html.link
import kotlinx.html.ul
import kotlinx.html.unsafe
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

fun Application.configureTemplating() {
    val markdownParser = Parser.builder().build()
    val htmlRenderer = HtmlRenderer.builder().build()

    fun renderMarkdown(content: String): String = htmlRenderer.render(markdownParser.parse(content))

    routing {
        get("/markdown") {
            val markdown =
                """
                # Hello Markdown!
                
                This is **simple** markdown rendering.
                
                ```kotlin
                fun main() {
                    println("Hello World!")
                }
                ```
                """.trimIndent()

            call.respondHtml {
                body {
                    unsafe {
                        +renderMarkdown(markdown)
                    }
                }
            }
        }
        get("/html-dsl") {
            call.respondHtml {
                body {
                    h1 { +"HTML" }
                    ul {
                        for (n in 1..10) {
                            li { +"$n" }
                        }
                    }
                }
            }
        }
        get("/styles.css") {
            call.respondCss {
                body {
                    backgroundColor = Color.darkBlue
                    margin = Margin(0.px)
                }
                "h1.page-title" {
                    color = Color.white
                }
            }
        }

        get("/html-css-dsl") {
            call.respondHtml {
                head {
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                }
                body {
                    h1(classes = "page-title") {
                        +"Hello from Ktor!"
                    }
                }
            }
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
