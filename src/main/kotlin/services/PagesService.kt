package vc.mvk.site.service

import vc.mvk.site.Page
import vc.mvk.site.utils.MarkdownRenderer
import java.io.File

class PagesService(
    private val pagesDir: String = "pages",
) {
    private val markdownRenderer = MarkdownRenderer()

    fun getPage(filename: String): Page? {
        val file =
            when {
                filename == "README.md" -> File(filename)
                else -> File(pagesDir, filename)
            }

        return file.takeIf { it.exists() }?.let { file ->
            val content = file.readText()
            Page(
                name = if (file.name == "README.md") "Home" else file.nameWithoutExtension.replaceFirstChar { it.uppercase() },
                href = "/${file.nameWithoutExtension}",
                content = markdownRenderer.renderMarkdown(content),
            )
        }
    }

    fun getAllPages(): List<Page> =
        File(pagesDir)
            .listFiles()
            ?.filter { it.isFile && it.extension == "md" }
            ?.mapNotNull { file ->
                val content = file.readText()
                Page(
                    name = if (file.name == "README.md") "Home" else file.nameWithoutExtension.replaceFirstChar { it.uppercase() },
                    href = "/${file.nameWithoutExtension}",
                    content = markdownRenderer.renderMarkdown(content),
                )
            } ?: emptyList()
}
