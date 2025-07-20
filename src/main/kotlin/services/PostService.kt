package vc.mvk.site

import org.commonmark.ext.front.matter.YamlFrontMatterVisitor
import vc.mvk.site.utils.MarkdownRenderer
import java.io.File
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

class PostService(
    private val post_dir: String = "posts",
) {
    private val markdownRenderer = MarkdownRenderer()

    fun getAllPosts(): List<Post> =
        File(post_dir)
            .listFiles()
            ?.filter { it.extension == "md" }
            ?.mapNotNull { file -> parsePostFile(file) }
            ?.filter { isDev() || it.published }
            ?.sortedByDescending { it.date }
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
            val document = markdownRenderer.parse(fileContent)

            val visitor = YamlFrontMatterVisitor()
            document.accept(visitor)
            val frontMatter = visitor.data

            val title = frontMatter["title"]?.firstOrNull() ?: return null
            val published = frontMatter["published"]?.firstOrNull()?.toBoolean() ?: true
            val datePart = parts[0]
            val slug = parts.drop(1).joinToString("_")
            val date = LocalDate.parse(
                datePart.replace("_", "-"),
                DateTimeFormatter.ofPattern("yy-MM-dd")
            )
            return Post(
                title = title,
                published = published,
                slug = slug,
                date = date.atStartOfDay().toInstant(ZoneOffset.UTC),
                content = markdownRenderer.render(document),
            )
        } catch (e: Exception) {
            return null
        }
    }

    private fun isDev(): Boolean {
        return System.getenv("KTOR_ENV") == "dev"
    }
}
