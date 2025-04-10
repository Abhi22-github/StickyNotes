package com.roaa.stickynotes

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@State(name = "StickyNotesState", storages = [Storage("sticky_notes.xml")])
@Service(Service.Level.PROJECT)
class NoteService : PersistentStateComponent<NoteService.State> {
    class State {
        var notes: MutableList<Note> = mutableListOf()
    }
    private var myState = State()

    override fun getState() = myState
    override fun loadState(state: State) { myState = state }

    fun addNote(note: Note) = myState.notes.add(note)
    fun removeNote(note: Note) = myState.notes.remove(note)
    fun getNotesForFile(path: String) = myState.notes.filter { it.filePath == path }

    companion object {
        fun getInstance(project: Project): NoteService = project.getService(NoteService::class.java)
    }
}