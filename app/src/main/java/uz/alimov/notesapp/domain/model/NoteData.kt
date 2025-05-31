package uz.alimov.notesapp.domain.model

data class NoteData(
    val id: Long? = null,
    var title: String = "",
    var body: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    var charCount: Long = 0,
    var lineCount: Long = 0,
    var wordCount: Long = 0,
    var isPinned: Boolean = false,
    var isTrashed: Boolean = false,
    var categoryId: Long = 0
)