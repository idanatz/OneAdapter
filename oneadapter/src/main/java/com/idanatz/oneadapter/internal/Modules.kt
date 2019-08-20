package com.idanatz.oneadapter.internal

import com.idanatz.oneadapter.external.modules.EmptinessModule
import com.idanatz.oneadapter.external.modules.PagingModule
import com.idanatz.oneadapter.internal.selection.OneItemSelection

internal class Modules(
        var emptinessModule: EmptinessModule? = null,
        var pagingModule: PagingModule? = null,
        var oneItemSelection: OneItemSelection? = null
)