package com.roaa.stickynotes

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.editor.Editor

class NoteEditorStartup : EditorFactoryListener {
    override fun editorCreated(event: EditorFactoryEvent) {
        val editor = event.editor
        val project = editor.project ?: return
        val file = FileDocumentManager.getInstance().getFile(editor.document) ?: return

        editor.addEditorMouseListener(NoteMouseListener()) // Register mouse listener for this editor

        val notes = NoteService.getInstance(project).getNotesForFile(file.path)
        notes.forEach { note ->
            ApplicationManager.getApplication().invokeLater {
                addGutterIcon(editor, note)
            }
        }
    }
}

private fun addGutterIcon(editor: Editor, note: Note) {
    editor.markupModel.addLineHighlighter(note.line, 0, null).apply {
        gutterIconRenderer = NoteGutterRenderer(note, editor)
    }
}

fun applyNotesToEditor(editor: com.intellij.openapi.editor.Editor) {
    val project = editor.project ?: return
    val file = com.intellij.openapi.fileEditor.FileDocumentManager.getInstance().getFile(editor.document) ?: return
    val notes = NoteService.getInstance(project).getNotesForFile(file.path)

    notes.forEach { note ->
        editor.markupModel.addLineHighlighter(note.line, 0, null).apply {
            gutterIconRenderer = NoteGutterRenderer(note)
        }
    }
}