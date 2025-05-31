package uz.alimov.notesapp.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
data class Home(
    val categoryId: Long? = null
)

@Serializable
data class Note(
    val noteId: Long? = null,
    val categoryId: Long? = null,
)

@Serializable
object Settings

@Serializable
data class Category(
    val categoryId: Long? = null
)