package com.roaa.stickynotes

import com.intellij.openapi.editor.event.EditorFactoryEvent
import com.intellij.openapi.editor.event.EditorFactoryListener

class NoteEditorStartup : EditorFactoryListener {
    override fun editorCreated(event: EditorFactoryEvent) {
        val project = event.editor.project ?: return

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