package com.idanatz.oneadapter.internal.holders_creators

import com.idanatz.oneadapter.external.UnsupportedClassException
import com.idanatz.oneadapter.external.interfaces.Diffable

internal class ViewHolderCreatorsStore {

    private val dataTypes = mutableListOf<Class<Diffable>>() // maps T.class to unique indexes for adapter's getCreatorUniqueIndex
    private val holderCreators = mutableMapOf<Class<Diffable>, ViewHolderCreator<Diffable>>() // maps T.class to its corresponding ViewHolderCreator<T>

    fun addCreator(clazz: Class<Diffable>, creator: ViewHolderCreator<Diffable>) {
        holderCreators[clazz] = creator
        if (!dataTypes.contains(clazz)) dataTypes.add(clazz)
    }

    fun getCreatorUniqueIndex(clazz: Class<Diffable>): Int {
        if (!dataTypes.contains(clazz)) {
            throw UnsupportedClassException("${clazz.simpleName} not registered as an Module data type.")
        }
        return dataTypes.indexOf(clazz)
    }

    fun getClassDataType(viewType: Int): Class<Diffable> = dataTypes[viewType]

    fun getCreator(viewType: Int): ViewHolderCreator<Diffable>? = holderCreators[getClassDataType(viewType)]
}