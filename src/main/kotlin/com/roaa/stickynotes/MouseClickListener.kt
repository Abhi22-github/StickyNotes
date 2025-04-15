package com.roaa.stickynotes

import com.intellij.openapi.editor.event.EditorMouseEvent
import com.intellij.openapi.editor.event.EditorMouseListener
import com.intellij.openapi.editor.event.EditorMouseEventArea
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.application.ApplicationManager
import com.intellij.icons.AllIcons
import java.awt.event.InputEvent

class NoteMouseListener : EditorMouseListener {
    override fun mousePressed(event: EditorMouseEvent) {
        // 1. Check if Alt + Click happened in the Gutter area
        if (event.mouseEvent.modifiersEx and InputEvent.ALT_DOWN_MASK != 0 &&
            event.area == EditorMouseEventArea.LINE_MARKERS_AREA) {

            val editor = event.editor
            val project = editor.project ?: return
            val file = FileDocumentManager.getInstance().getFile(editor.document) ?: return

            // Calculate which line was clicked
            val logicalPosition = editor.xyToLogicalPosition(event.mouseEvent.point)
            val line = logicalPosition.line

            // 2. Show the Input Dialog (The "Voice")
            val content = Messages.showInputDialog(
                project,
                "Enter your sticky note context:",
                "New Sticky Note",
                AllIcons.Nodes.EmptyNode
            )

            if (!content.isNullOrBlank()) {
                // 3. Create and Save the Data (The "Brain")
                val note = Note(file.path, line, content)
                NoteService.getInstance(project).addNote(note)

                // 4. Update the UI Immediately
                ApplicationManager.getApplication().invokeLater {
                    // This triggers the markup model to redraw with the new GutterRenderer
                    editor.markupModel.addLineHighlighter(line, 0, null).apply {
                        gutterIconRenderer = NoteGutterRenderer(note,editor)
                    }
                }
            }
        }
    }
}