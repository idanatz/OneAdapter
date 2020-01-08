package com.idanatz.oneadapter.external.holders

import com.idanatz.oneadapter.external.interfaces.Diffable
import java.util.*

abstract class ExternalHolderModel : Diffable {

    private val uniqueId = UUID.randomUUID().mostSignificantBits

    override fun getUniqueIdentifier() = uniqueId
    override fun areContentTheSame(other: Any) = true
}

object LoadingIndicator : ExternalHolderModel()
object EmptyIndicator : ExternalHolderModel()