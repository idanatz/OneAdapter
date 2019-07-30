package com.idanatz.oneadapter.external.interfaces

interface Diffable {
    fun getUniqueIdentifier(): Long
    fun areContentTheSame(other: Any): Boolean
}