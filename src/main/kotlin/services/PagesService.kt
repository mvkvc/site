package vc.mvk.site.service

import vc.mvk.site.Page
import vc.mvk.site.utils.MarkdownRenderer
import java.io.File

class PagesService(
    private val pagesDir: String = "pages",
) {
    private val markdownRenderer = MarkdownRenderer()

    fun getPage(filename: String): Page? {
        val file = when {
            filename.startsWith("header/") -> File(pagesDir, filename)
            else -> File(pagesDir, filename)
        }
        return if (file.exists()) fileToPage(file) else null
    }

    fun getAllPages(): List<Page> =
        File(pagesDir, "header")
            .listFiles()
            ?.filter { it.isFile && it.extension == "md" }
            ?.mapNotNull { fileToPage(it) }
            ?: emptyList()

    private fun fileToPage(file: File): Page {
        val content = file.readText()
        return Page(
            name = file.nameWithoutExtension.replaceFirstChar { it.uppercase() },
            href = "/${file.nameWithoutExtension}",
            content = markdownRenderer.renderMarkdown(content),
        )
    }
}
