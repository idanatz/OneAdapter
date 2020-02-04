package com.idanatz.oneadapter.external.holders

import com.idanatz.oneadapter.external.interfaces.Diffable
import java.util.*

sealed class OneInternalHolderModel : Diffable {

    private val uniqueId = UUID.randomUUID().mostSignificantBits

    override fun getUniqueIdentifier() = uniqueId
    override fun areContentTheSame(other: Any) = true
}

object LoadingIndicator : OneInternalHolderModel()
object EmptyIndicator : OneInternalHolderModel()