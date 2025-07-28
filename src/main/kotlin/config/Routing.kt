package vc.mvk.site

import vc.mvk.site.PostService
import vc.mvk.site.service.PagesService
import vc.mvk.site.templates.post
import vc.mvk.site.templates.postPreview
import vc.mvk.site.templates.root
import vc.mvk.site.templates.home
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.resources.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.compression.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.css.*
import kotlinx.html.*
import kotlinx.serialization.Serializable
import java.io.File

fun Application.configureRouting() {
    install(Resources)
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(text = "404: Page Not Found", status = status)
        }
    }

    val pagesService = PagesService()
    val postService = PostService()

    routing {
        staticResources("/static", "static") {
            enableAutoHeadResponse()
        }

        get("/posts/{slug}") {
            val slug = call.parameters["slug"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            postService.getPost(slug)?.let { post ->
                call.respondHtml {
                    post(post = post, pages = pagesService.getAllPages()) {}
                }
            } ?: call.respond(HttpStatusCode.NotFound)
        }

        get("/{slug?}") {
            val slug = call.parameters["slug"] ?: ""
            val allPages = pagesService.getAllPages()
            
            when (slug) {
                "" -> {
                    pagesService.getPage("README.md")?.let { page ->
                        call.respondHtml {
                            home(
                                latestPost = postService.getLatestPost(),
                                readmeContent = page.content,
                                pages = allPages
                            )
                        }
                    } ?: call.respond(HttpStatusCode.NotFound)
                }
                "posts" -> {
                    val posts = postService.getAllPosts()
                    call.respondHtml {
                        root(title = "Posts", pages = allPages) {
                            h1(classes = "text-3xl font-bold text-base-content mb-8 text-center") { +"Posts" }
                            div(classes = "not-prose space-y-8") {
                                div(classes = "space-y-6") {
                                    posts.forEach { post ->
                                        postPreview(post)
                                    }
                                }
                            }
                        }
                    }
                }
                else -> {
                    pagesService.getPage("$slug.md")?.let { page ->
                        call.respondHtml {
                            root(title = page.name, pages = allPages) {
                                h1(classes = "text-3xl font-bold text-base-content mb-8 text-center") { +page.name }
                                unsafe { +page.content }
                            }
                        }
                    } ?: call.respond(HttpStatusCode.NotFound)
                }
            }
        }


    }
}

@Serializable
@Resource("/articles")
class Articles(
    val sort: String? = "new",
)
