package com.idanatz.oneadapter.internal.utils.extensions

import com.idanatz.oneadapter.external.interfaces.Diffable
import java.util.*

internal fun <T : Diffable> List<T>.createMutableCopy(): MutableList<T> {
    return LinkedList(this)
}

internal fun <T : Diffable> List<T>.createMutableCopyAndApply(block: MutableList<T>.() -> Unit): MutableList<T> {
    return LinkedList(this).apply(block)
}

internal fun <T : Diffable, M : Diffable> MutableList<T>.findIndexOfClass(classToFind: Class<M>): Int? {
    return indexOfFirst { classToFind.isInstance(it) }.takeIf { it != -1 }
}

internal fun <T : Diffable, M : Diffable> MutableList<T>.isClassExists(classToFind: Class<M>): Boolean {
    return indexOfFirst { classToFind.isInstance(it) } != -1
}

internal fun <T : Diffable> MutableList<T>.removeAllItems(itemsToRemove: List<T>) {
    itemsToRemove.forEach { removeAt(getIndexOfItem(it)) }
}

internal fun <T : Diffable> List<T>.getIndexOfItem(itemToFind: T): Int {
    return indexOfFirst { item ->
        when (item.javaClass == itemToFind.javaClass) {
            true -> item.getUniqueIdentifier() == itemToFind.getUniqueIdentifier()
            false -> false
        }
    }
}