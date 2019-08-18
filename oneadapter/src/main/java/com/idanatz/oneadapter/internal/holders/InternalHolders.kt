package com.idanatz.oneadapter.internal.holders

import com.idanatz.oneadapter.external.interfaces.Diffable
import java.util.*

internal object LoadingIndicator : Diffable {

    private val uniqueId = UUID.randomUUID().mostSignificantBits

    override fun getUniqueIdentifier() = uniqueId
    override fun areContentTheSame(other: Any) = true
}

internal object EmptyIndicator : Diffable {

    private val uniqueId = UUID.randomUUID().mostSignificantBits

    override fun getUniqueIdentifier() = uniqueId
    override fun areContentTheSame(other: Any) = true
}