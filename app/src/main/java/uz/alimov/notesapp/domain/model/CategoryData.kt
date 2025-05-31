package uz.alimov.notesapp.domain.model

data class CategoryData(
    val id: Long = 0,
    val name: String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val isDefault: Boolean = false
)