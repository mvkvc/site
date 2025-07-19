package vc.mvk.site

import vc.mvk.site.PostService
import vc.mvk.site.service.PagesService
import vc.mvk.site.templates.postLayout
import vc.mvk.site.templates.postListLayout
import vc.mvk.site.templates.rootLayout
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

        get("/posts") {
            val posts = postService.getAllPosts()
            val allPages = pagesService.getAllPages()

            call.respondHtml {
                postListLayout(posts = posts, pages = allPages)
            }
        }

        get("/posts/{slug}") {
            val slug = call.parameters["slug"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val post = postService.getPost(slug)
            val allPages = pagesService.getAllPages()

            if (post != null) {
                call.respondHtml {
                    postLayout(post = post, pages = allPages) {}
                }
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/blog/{slug}") {
            val slug = call.parameters["slug"] ?: return@get call.respond(HttpStatusCode.BadRequest)
            val post = postService.getPost(slug)
            val allPages = pagesService.getAllPages()

            if (post != null) {
                call.respondHtml {
                    postLayout(post = post, pages = allPages) {}
                }
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }

        get("/{slug?}") {
            val slug = call.parameters["slug"] ?: "README"
            val page = pagesService.getPage("$slug.md")
            val allPages = pagesService.getAllPages()

            if (page != null) {
                call.respondHtml {
                    rootLayout(title = page.name, pages = allPages) {
                        unsafe {
                            +page.content
                        }
                    }
                }
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }


    }
}

@Serializable
@Resource("/articles")
class Articles(
    val sort: String? = "new",
)
