package com.idanatz.oneadapter.internal.holders

import com.idanatz.oneadapter.external.interfaces.Diffable
import java.util.*

internal abstract class InternalHolderModel : Diffable {

    private val uniqueId = UUID.randomUUID().mostSignificantBits

    override fun getUniqueIdentifier() = uniqueId
    override fun areContentTheSame(other: Any) = true
}

internal object LoadingIndicator : InternalHolderModel()
internal object EmptyIndicator : InternalHolderModel()