package com.idanatz.oneadapter.external.holders

import com.idanatz.oneadapter.external.interfaces.Diffable
import java.util.*

sealed class OneInternalHolderModel : Diffable {

    override val uniqueIdentifier: Long = UUID.randomUUID().mostSignificantBits
    override fun areContentTheSame(other: Any) = true
}

object LoadingIndicator : OneInternalHolderModel()
object EmptyIndicator : OneInternalHolderModel()