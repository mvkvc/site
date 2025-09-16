package vc.mvk.site.utils

import org.commonmark.ext.front.matter.YamlFrontMatterExtension
import org.commonmark.ext.gfm.tables.TablesExtension
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension
import org.commonmark.ext.image.attributes.ImageAttributesExtension
import org.commonmark.ext.task.list.items.TaskListItemsExtension
import org.commonmark.node.Node
import org.commonmark.parser.Parser
import org.commonmark.renderer.html.HtmlRenderer

class MarkdownRenderer {
    private val extensions =
        listOf(
            YamlFrontMatterExtension.create(),
            TaskListItemsExtension.create(),
            ImageAttributesExtension.create(),
            HeadingAnchorExtension.create(),
            TablesExtension.create(),
        )

    private val parser =
        Parser
            .builder()
            .extensions(extensions)
            .build()
    private val renderer =
        HtmlRenderer
            .builder()
            .extensions(extensions)
            .build()

    fun renderMarkdown(content: String): String {
        val document = parser.parse(content)
        return renderer.render(document)
    }

    fun parse(content: String): Node = parser.parse(content)

    fun render(document: Node): String = renderer.render(document)
}
