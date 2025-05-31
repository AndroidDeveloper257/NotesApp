package uz.alimov.notesapp.data.mapper

import uz.alimov.notesapp.data.local.entity.CategoryEntity
import uz.alimov.notesapp.domain.model.CategoryData

fun CategoryEntity.toCategoryData(): CategoryData {
    return CategoryData(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt,
        isDefault = isDefault
    )
}

fun CategoryData.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}