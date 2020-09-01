package com.idanatz.oneadapter.external.interfaces

interface Diffable {
    val uniqueIdentifier: Long
    fun areContentTheSame(other: Any): Boolean
}