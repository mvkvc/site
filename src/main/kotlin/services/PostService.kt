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
            ?.firstOrNull { it.nameWithoutExtension.endsWith(slug) }
            ?.let { parsePostFile(it) }

    private fun parsePostFile(file: File): Post? =
        runCatching {
            val fileContent = file.readText()
            val document = markdownRenderer.parse(fileContent)
            val visitor = YamlFrontMatterVisitor().apply { document.accept(this) }
            val frontMatter = visitor.data
            val parts = file.nameWithoutExtension.split("-")

            Post(
                title = frontMatter["title"]!!.first(),
                published = frontMatter["published"]?.firstOrNull()?.toBoolean() ?: true,
                slug = parts.drop(1).joinToString("_"),
                date =
                    LocalDate
                        .parse(parts[0].replace("_", "-"), DateTimeFormatter.ofPattern("yy-MM-dd"))
                        .atStartOfDay()
                        .toInstant(ZoneOffset.UTC),
                content = markdownRenderer.render(document),
                description = frontMatter["description"]?.firstOrNull(),
            )
        }.getOrNull()

    private fun isDev(): Boolean = System.getenv("KTOR_ENV") == "dev"

    fun getLatestPost(): Post? = getAllPosts().firstOrNull()
}
