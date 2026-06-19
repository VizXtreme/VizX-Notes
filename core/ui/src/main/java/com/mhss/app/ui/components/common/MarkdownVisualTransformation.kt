package com.mhss.app.ui.components.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.em

class MarkdownVisualTransformation(
    private val color: Color = Color.Unspecified
) : VisualTransformation {

    override fun filter(text: AnnotatedString): TransformedText {
        val annotatedString = buildAnnotatedStringWithMarkdown(text.text, color)
        return TransformedText(annotatedString, OffsetMapping.Identity)
    }

    private fun buildAnnotatedStringWithMarkdown(text: String, color: Color): AnnotatedString {
        val builder = AnnotatedString.Builder(text)
        if (color != Color.Unspecified) {
            builder.addStyle(SpanStyle(color = color), 0, text.length)
        }

        // Bold: **text**
        val boldRegex = Regex("\\*\\*(.*?)\\*\\*")
        boldRegex.findAll(text).forEach { matchResult ->
            builder.addStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold),
                start = matchResult.range.first,
                end = matchResult.range.last + 1
            )
        }

        // Bold 2: __text__
        val boldRegex2 = Regex("__(.*?)__")
        boldRegex2.findAll(text).forEach { matchResult ->
            builder.addStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold),
                start = matchResult.range.first,
                end = matchResult.range.last + 1
            )
        }

        // Italic: *text*
        val italicRegex = Regex("(?<!\\*)\\*(?!\\*)(.*?)(?<!\\*)\\*(?!\\*)")
        italicRegex.findAll(text).forEach { matchResult ->
            builder.addStyle(
                style = SpanStyle(fontStyle = FontStyle.Italic),
                start = matchResult.range.first,
                end = matchResult.range.last + 1
            )
        }

        // Italic 2: _text_
        val italicRegex2 = Regex("(?<!_)_(?!_)(.*?)(?<!_)_(?!_)")
        italicRegex2.findAll(text).forEach { matchResult ->
            builder.addStyle(
                style = SpanStyle(fontStyle = FontStyle.Italic),
                start = matchResult.range.first,
                end = matchResult.range.last + 1
            )
        }

        // Strikethrough: ~~text~~
        val strikeRegex = Regex("~~(.*?)~~")
        strikeRegex.findAll(text).forEach { matchResult ->
            builder.addStyle(
                style = SpanStyle(textDecoration = TextDecoration.LineThrough),
                start = matchResult.range.first,
                end = matchResult.range.last + 1
            )
        }

        // Underline: <u>text</u>
        val underlineRegex = Regex("<u>(.*?)</u>")
        underlineRegex.findAll(text).forEach { matchResult ->
            builder.addStyle(
                style = SpanStyle(textDecoration = TextDecoration.Underline),
                start = matchResult.range.first,
                end = matchResult.range.last + 1
            )
        }

        // Headers
        val headerRegex = Regex("^(#{1,6}) (.*)$", RegexOption.MULTILINE)
        headerRegex.findAll(text).forEach { matchResult ->
            val level = matchResult.groups[1]?.value?.length ?: 1
            val scale = when (level) {
                1 -> 1.5f
                2 -> 1.4f
                3 -> 1.3f
                4 -> 1.2f
                5 -> 1.1f
                else -> 1.0f
            }
            builder.addStyle(
                style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = scale.em),
                start = matchResult.range.first,
                end = matchResult.range.last + 1
            )
        }

        return builder.toAnnotatedString()
    }
}
