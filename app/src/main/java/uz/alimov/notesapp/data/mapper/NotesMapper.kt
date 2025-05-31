package uz.alimov.notesapp.data.mapper

import uz.alimov.notesapp.data.local.entity.NotesEntity
import uz.alimov.notesapp.domain.model.NoteData

fun NotesEntity.toNoteData(): NoteData {
    return NoteData(
        id = id,
        title = title,
        body = body,
        createdAt = createdAt,
        updatedAt = updatedAt,
        charCount = charCount,
        lineCount = lineCount,
        wordCount = wordCount,
        isPinned = isPinned,
        isTrashed = isTrashed,
        categoryId = categoryId
    )
}

fun NoteData.toNotesEntity(): NotesEntity {
    return NotesEntity(
        id = id ?: 0,
        title = title,
        body = body,
        createdAt = createdAt,
        updatedAt = updatedAt,
        charCount = charCount,
        lineCount = lineCount,
        wordCount = wordCount,
        isPinned = isPinned,
        isTrashed = isTrashed,
        categoryId = categoryId
    )
}