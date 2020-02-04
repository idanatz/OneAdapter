package com.idanatz.oneadapter.external.interfaces

import com.idanatz.oneadapter.internal.holders.Metadata

data class Item<M : Diffable>(
		val model: M,
		val metadata: Metadata
)