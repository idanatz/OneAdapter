package com.idanatz.oneadapter.internal.utils

import com.idanatz.oneadapter.external.interfaces.Diffable

internal fun getIndexOfItem(itemList: List<Any>, itemToFind: Any): Int {
    return itemList.indexOfFirst { item ->
        if (item.javaClass == itemToFind.javaClass) {
            when {
                item is Diffable && itemToFind is Diffable -> item.getUniqueIdentifier() == itemToFind.getUniqueIdentifier()
                else -> item == itemToFind
            }
        } else {
            false
        }
    }
}

internal fun removeAllItems(itemList: MutableList<Any>, itemsToRemove: List<Any>) {
    itemsToRemove.forEach { itemList.removeAt(getIndexOfItem(itemList, it)) }
}