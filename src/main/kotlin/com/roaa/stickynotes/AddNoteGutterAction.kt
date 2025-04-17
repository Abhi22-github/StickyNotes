package com.roaa.stickynotes

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.ui.Messages
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.DataKey

class AddNoteGutterAction : AnAction("Add Sticky Note...", "Add a private note to this line", AllIcons.Actions.AddFile) {

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return
        val file = FileDocumentManager.getInstance().getFile(editor.document) ?: return

        // FIX: Use this DataKey to get the line number from the gutter context
        val lineKey = DataKey.create<IntArray>("EditorGutter.Lines")
        val lines = e.getData(lineKey)

        // Fallback: If the key returns null, we calculate it from the caret position
        val line = lines?.getOrNull(0) ?: editor.caretModel.logicalPosition.line

        val content = Messages.showInputDialog(
            project, "Enter Note Content:",
            "Sticky Note", AllIcons.Actions.AddFile
        )

        if (!content.isNullOrBlank()) {
            val note = Note(file.path, line, content)
            NoteService.getInstance(project).addNote(note)

            editor.markupModel.addLineHighlighter(line, 0, null).apply {
                gutterIconRenderer = NoteGutterRenderer(note, editor)
            }
        }
    }
}