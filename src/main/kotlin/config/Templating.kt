package vc.mvk.site

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.*
import kotlinx.html.*
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
                rule("h1.page-title") {
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
