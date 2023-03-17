package com.example.templatecrudproject.entity.dto

// TODO add DTO fields
@kotlinx.serialization.Serializable
data class EntityDTO(
    val id: Long = 0L,
    val date: String,
    val type: String,
    val duration: Int,
    val distance: Int,
    val calories: Int,
    val rate: Int
)
