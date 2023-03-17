package com.example.templatecrudproject.entity.domain

import com.example.templatecrudproject.utils.toUnixTimestamp

data class PopularType(
    val type: String,
    val count: Int
)