package com.idanatz.oneadapter.internal.holders_creators

import com.idanatz.oneadapter.external.interfaces.Diffable

internal class ViewHolderCreatorsStore {

    private val dataTypes = mutableListOf<Class<*>>() // maps T.class to unique indexes for adapter's getCreatorUniqueIndex
    private val holderCreators = mutableMapOf<Class<*>, ViewHolderCreator<Diffable>>() // maps T.class -> ViewHolderCreator<T>

    fun addCreator(clazz: Class<*>, creator: ViewHolderCreator<Diffable>) {
        holderCreators[clazz] = creator
    }

    fun getCreatorUniqueIndex(clazz: Class<Diffable>): Int {
        if (dataTypes.indexOf(clazz) == -1) {
            dataTypes.add(clazz)
        }
        return dataTypes.indexOf(clazz)
    }

    fun getClassDataType(viewType: Int) = dataTypes[viewType]

    fun getCreator(viewType: Int): ViewHolderCreator<Diffable>? = holderCreators[getClassDataType(viewType)]
}