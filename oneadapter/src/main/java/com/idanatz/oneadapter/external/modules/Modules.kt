package com.idanatz.oneadapter.external.modules

class Modules {
    var itemModules = mutableMapOf<Class<*>, ItemModule<*>>()
    var emptinessModule: EmptinessModule? = null
    var pagingModule: PagingModule? = null
    var itemSelectionModule: ItemSelectionModule? = null
}