package com.jmflaherty.client

import kotlin.js.ExperimentalJsExport
import kotlinx.serialization.Serializable

@ExperimentalJsExport
@Serializable
data class SomeDataClass(
    val id: Int,
    var name: String,
    var age: Int,
)
