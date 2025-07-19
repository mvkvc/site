package com.example.com.service

import com.example.com.Page
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer
import java.io.File

class PagesService(
    private val pagesDir: String = "pages",
) {
    private val parser = Parser.builder().build()
    private val renderer = HtmlRenderer.builder().build()

    fun getPage(filename: String): Page? {
        val file =
            when {
                filename == "README.md" -> File(filename)
                else -> File(pagesDir, filename)
            }

        return file.takeIf { it.exists() }?.let { file ->
            val content = file.readText()
            Page(
                name = file.nameWithoutExtension.replaceFirstChar { it.uppercase() },
                href = "/${file.nameWithoutExtension}",
                content = renderer.render(parser.parse(content)),
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
                    name = file.nameWithoutExtension.replaceFirstChar { it.uppercase() },
                    href = "/${file.nameWithoutExtension}",
                    content = renderer.render(parser.parse(content)),
                )
            } ?: emptyList()
}
