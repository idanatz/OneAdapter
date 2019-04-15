package com.android.one_adapter_example

import android.arch.lifecycle.ViewModel
import android.os.Handler
import com.android.one_adapter_example.interfaces.ExampleView
import com.android.one_adapter_example.models.HeaderModel
import com.android.one_adapter_example.models.MessageModel

class Presenter : ViewModel() {

    private var view: ExampleView? = null
    private val modelProvider = ModelProvider()
    private var items = mutableListOf<Any>()

    fun setView(view: ExampleView) {
        this.view = view
    }

    fun setAll() {
        items = createHeaders(modelProvider.models)
        view?.setAll(items)
    }

    @Suppress("UNCHECKED_CAST")
    private fun createHeaders(models: List<MessageModel> = items as List<MessageModel>): MutableList<Any> {
        val list = mutableListOf<Any>()

        models.groupBy { it.headerId }.forEach { headerIndex, messages ->
            list.add(HeaderModel(headerIndex, "Header " + (headerIndex + 1)))
            list.addAll(messages)
        }

        return list
    }

    fun clearAll() {
        items.clear()
        view?.clearAll()
    }

    fun addOne() {
        val newItem = modelProvider.addMessage()

        if (items.isEmpty()) {
            items.add(0, newItem)
            items = createHeaders()
            view?.setAll(items)
        } else {
            items.add(1, newItem)
            view?.addOne(1, newItem)
        }
    }

    fun setOne() {
        val updatedItem = modelProvider.updateMessage(2)

        items.indexOfFirstAsNullable { it is MessageModel && it.id == updatedItem.id }?.let {
            items.removeAt(it)
            items.add(it, updatedItem)
            view?.setOne(updatedItem)
        }
    }

    fun removeIndex() {
        val removedItem = modelProvider.removeIndex(1)

        items.indexOfFirstAsNullable { it is MessageModel && it.id == removedItem.id }?.let {
            items.removeAt(it)
            view?.removeIndex(it)
        }
    }

    fun removeItem() {
        val removedItem = modelProvider.removeItem()

        items.indexOfFirstAsNullable { it is MessageModel && it.id == removedItem.id }?.let {
            items.removeAt(it)
            view?.removeItem(removedItem)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun cutItems() {
        items.filter { it is MessageModel && it.id % 2 == 0 }.let {
            items = createHeaders(it as List<MessageModel>)
            view?.setAll(items)
        }
    }

    fun loadMore() {
        Handler().postDelayed({
            val loadMoreItems = modelProvider.createLoadMoreItems()
            val headerId = loadMoreItems.first().headerId

            items.find { it is HeaderModel && it.id == headerId }?.takeIf { it is HeaderModel && it.checked }?.let {
                items.addAll(loadMoreItems)
            }

            view?.setAll(items)
        }, 2500)
    }

    fun headerCheckedChanged(model: HeaderModel, checked: Boolean) {
        model.checked = checked

        if (checked) {
            items.indexOfFirstAsNullable { it is HeaderModel && it.id == model.id }?.let {
                items.addAll(it + 1, modelProvider.getMessagesWithHeaderId(model.id))
            }
        } else {
            items.removeIf { it is MessageModel && it.headerId == model.id }
        }

        view?.setAll(items)
    }

    private inline fun <T> List<T>.indexOfFirstAsNullable(predicate: (T) -> Boolean) = indexOfFirst(predicate).takeIf { it != -1 }
}