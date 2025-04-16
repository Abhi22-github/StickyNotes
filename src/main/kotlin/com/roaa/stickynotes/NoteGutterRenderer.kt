package com.roaa.stickynotes

import com.intellij.openapi.actionSystem.*
import com.intellij.openapi.editor.markup.GutterIconRenderer
import com.intellij.icons.AllIcons
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.DumbAwareAction
import javax.swing.Icon

class NoteGutterRenderer(
    private val note: Note,
    private val editor: Editor
) : GutterIconRenderer() {
    override fun getIcon(): Icon = AllIcons.Actions.AddToDictionary
    override fun getTooltipText(): String = note.content
    override fun hashCode(): Int = note.hashCode()
    override fun equals(other: Any?): Boolean = other is NoteGutterRenderer && other.note == note
    override fun getPopupMenuActions(): ActionGroup {
        return DefaultActionGroup(object : DumbAwareAction("Delete Note") {
            override fun actionPerformed(e: AnActionEvent) {
                val project = e.project ?: return
                NoteService.getInstance(project).removeNote(note)
                // Remove the highlighter from the editor immediately
                editor.markupModel.allHighlighters.forEach {
                    if (it.gutterIconRenderer == this@NoteGutterRenderer) {
                        editor.markupModel.removeHighlighter(it)
                    }
                }
            }
        })
    }
}