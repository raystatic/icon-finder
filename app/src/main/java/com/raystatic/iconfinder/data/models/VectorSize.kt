package com.raystatic.iconfinder.data.models

data class VectorSize(
    val vectorFormats: List<VectorFormat>,
    val size: Int,
    val size_height: Int,
    val size_width: Int,
    val target_sizes: List<List<Int>>
)