package com.roaa.stickynotes

import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.icons.AllIcons
import javax.swing.Icon

class NoteGutterRenderer(private val note: Note) : GutterIconRenderer() {
    override fun getIcon(): Icon = AllIcons.Nodes.EmptyNode
    override fun getTooltipText(): String = note.content
    override fun hashCode(): Int = note.hashCode()
    override fun equals(other: Any?): Boolean = other is NoteGutterRenderer && other.note == note
}