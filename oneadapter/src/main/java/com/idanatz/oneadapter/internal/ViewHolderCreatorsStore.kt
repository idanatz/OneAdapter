package com.idanatz.oneadapter.internal

import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.internal.interfaces.ViewHolderCreator

internal class ViewHolderCreatorsStore {

    private val dataTypes = mutableListOf<Class<*>>() // maps T.class to unique indexes for adapter's getCreatorUniqueIndex
    private val holderCreators = mutableMapOf<Class<*>, ViewHolderCreator<Diffable>>() // maps T.class -> ViewHolderCreator<T>

    fun isCreatorExists(clazz: Class<*>) = holderCreators.containsKey(clazz)

    fun addCreator(clazz: Class<*>, creator: ViewHolderCreator<Diffable>) {
        holderCreators[clazz] = creator
    }

    fun getCreatorUniqueIndex(clazz: Class<Diffable>): Int {
        if (dataTypes.indexOf(clazz) == -1) {
            dataTypes.add(clazz)
        }
        return dataTypes.indexOf(clazz)
    }

    fun getCreator(viewType: Int): ViewHolderCreator<Diffable>? {
        val dataType = dataTypes[viewType]
        return holderCreators[dataType]
    }

    fun getCreator(clazz: Class<Diffable>): ViewHolderCreator<Diffable>? {
        return getCreator(getCreatorUniqueIndex(clazz))
    }
}