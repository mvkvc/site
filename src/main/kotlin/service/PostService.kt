package com.example.com

import org.commonmark.ext.front.matter.YamlFrontMatterExtension
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.File
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class PostService(
    private val post_dir: String = "posts",
) {
    private val parser =
        Parser
            .builder()
            .extensions(listOf(YamlFrontMatterExtension.create()))
            .build()
    private val renderer = HtmlRenderer.builder().build()

    fun getAllPosts(): List<Post> =
        File(post_dir)
            .listFiles()
            ?.filter { it.extension == "md" }
            ?.mapNotNull { file -> parsePostFile(file) }
            ?: emptyList()

    fun getPost(slug: String): Post? =
        File(post_dir)
            .listFiles()
            ?.firstOrNull {
                it.extension == "md" &&
                it.nameWithoutExtension.split("-").let { parts ->
                    parts.size >= 2 && parts.last() == slug
                }
            }?.let { parsePostFile(it) }

    private fun parsePostFile(file: File): Post? {
        val parts = file.nameWithoutExtension.split("-")
        if (parts.size < 2) return null

        try {
            val fileContent = file.readText()
            val document = parser.parse(fileContent)

            val visitor = YamlFrontMatterVisitor()
            document.accept(visitor)
            val frontMatter = visitor.data

            val title = frontMatter["title"]?.firstOrNull() ?: return null

            val datePart = parts[0]
            val slug = parts.drop(1).joinToString("_")
            val date = LocalDate.parse(
                datePart.replace("_", "-"),
                DateTimeFormatter.ofPattern("yy-MM-dd")
            )
            return Post(
                title = title,
                slug = slug,
                date = date.atStartOfDay().toInstant(ZoneOffset.UTC),
                content = renderer.render(document),
            )
        } catch (e: Exception) {
            return null
        }
    }
}
