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
                    it.nameWithoutExtension.endsWith("_$slug")
            }?.let { parsePostFile(it) }

    private fun parsePostFile(file: File): Post? {
        val parts = file.nameWithoutExtension.split("_")
        if (parts.size != 2) return null

        try {
            val fileContent = file.readText()
            val document = parser.parse(fileContent)

            val visitor = YamlFrontMatterVisitor()
            document.accept(visitor)
            val frontMatter = visitor.data

            val title = frontMatter["title"]?.firstOrNull() ?: return null
            val description = frontMatter["description"]?.firstOrNull() ?: return null

            val date = LocalDate.parse(parts[0], DateTimeFormatter.ISO_DATE)
            return Post(
                title = title,
                description = description,
                slug = parts[1],
                date = date.atStartOfDay().toInstant(ZoneOffset.UTC),
                content = renderer.render(document),
            )
        } catch (e: Exception) {
            return null
        }
    }
}
