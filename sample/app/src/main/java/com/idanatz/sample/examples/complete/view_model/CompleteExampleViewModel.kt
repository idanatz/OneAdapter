package com.idanatz.sample.examples.complete.view_model

import androidx.lifecycle.ViewModel
import android.os.Handler
import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.sample.examples.ActionsDialog
import com.idanatz.sample.models.HeaderModel
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.models.ModelGenerator
import com.idanatz.sample.persistence.RoomDB
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import kotlin.math.absoluteValue

@Suppress("UNCHECKED_CAST")
class CompleteExampleViewModel : ViewModel(), ActionsDialog.ActionsListener {

    private val modelProvider = ModelGenerator()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var itemsSubject: BehaviorSubject<List<Diffable>> = BehaviorSubject.createDefault(listOf())

    init {
        compositeDisposable.add(
                Single.fromCallable { RoomDB.instance.clearAllTables() }
                        .flatMapObservable { RoomDB.instance.messageDao().observeTable().toObservable() }
                        .map {
                            when (it.size) {
                                0 -> it
                                else -> modelProvider.addHeadersFromMessages(messages = it, checkable = true).apply { add(0, modelProvider.generateStories()) }
                            }
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe { itemsSubject.onNext(it) }
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    override fun onSetAllClicked() {
        compositeDisposable.add(
                Single.fromCallable { RoomDB.instance.messageDao().insert(modelProvider.generateFirstMessages()) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    override fun onClearAllClicked() {
        compositeDisposable.add(
                Single.fromCallable { RoomDB.instance.messageDao().deleteAll() }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    override fun onAddItemClicked(id: Int) {
        compositeDisposable.add(
                Single.fromCallable {
                    val newItem = modelProvider.generateMessage(id)
                    newItem.headerId = findClosestHeaderForId(id)
                    RoomDB.instance.messageDao().insert(newItem)
                }
                .subscribeOn(Schedulers.io())
                .subscribe({}, {})
        )
    }

    private fun findClosestHeaderForId(id: Int): Int {
        var closestHeaderId = -1
        var minVal = -1
        itemsSubject.value?.filterIsInstance<MessageModel>()?.forEach {
            val newVal = (it.id - id).absoluteValue
            if (minVal == -1 || minVal > newVal) {
                minVal = newVal
                closestHeaderId = it.headerId
            }
        }
        return closestHeaderId
    }

    override fun onUpdatedItemClicked(id: Int) {
        compositeDisposable.add(
                RoomDB.instance.messageDao().getMessageWithId(id)
                        .flatMap {
                            Single.fromCallable {
                                RoomDB.instance.messageDao().update(modelProvider.generateUpdatedMessage(it.id, it.headerId))
                            }
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe({}, {})
        )
    }

    fun onDeleteItemsClicked(items: List<Any>) {
        compositeDisposable.add(
                Single.fromCallable { RoomDB.instance.messageDao().delete(items as List<MessageModel>) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    override fun onDeleteIndexClicked(index: Int) {
        val item = itemsSubject.value?.get(index) as? MessageModel

        compositeDisposable.add(
                Single.fromCallable {
                    item?.let {
                        RoomDB.instance.messageDao().delete(it)
                    }
                }
                .subscribeOn(Schedulers.io())
                .subscribe({}, {})
        )
    }

    override fun onDeleteItemClicked(id: Int) {
        compositeDisposable.add(
                RoomDB.instance.messageDao().getMessageWithId(id)
                        .flatMap {
                            Single.fromCallable {
                                RoomDB.instance.messageDao().delete(it)
                            }
                        }
                        .subscribeOn(Schedulers.io())
                        .subscribe({}, {})
        )
    }

    override fun onLargeDiffClicked() {
        compositeDisposable.add(
                Single.fromCallable { RoomDB.instance.messageDao().deleteEvenIds() }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    fun onLoadMore() {
        Handler().postDelayed({
            val loadMoreItems = modelProvider.generateLoadMoreMessages()
            compositeDisposable.add(
                    Single.fromCallable { RoomDB.instance.messageDao().insert(loadMoreItems) }
                            .subscribeOn(Schedulers.io())
                            .subscribe()
            )
        }, 1500)
    }

    fun headerCheckedChanged(model: HeaderModel, checked: Boolean) {
        model.checked = checked

        val items = itemsSubject.value?.toMutableList()
        items?.let {
            if (checked) {
                items.indexOfFirstAsNullable { it is HeaderModel && it.id == model.id }?.let { indexToInsert ->
                    compositeDisposable.add(
                        RoomDB.instance.messageDao().getMessageWithHeaderId(model.id)
                                .subscribeOn(Schedulers.io())
                                .subscribe({ messagesWithHeaderId ->
                                    items.addAll(indexToInsert + 1, messagesWithHeaderId)
                                }, {})
                    )
                }
            } else {
                items.removeAll { it is MessageModel && it.headerId == model.id }
            }

            itemsSubject.onNext(items)
        }
    }

    fun onSwipeToDeleteItem(item: MessageModel) {
        onDeleteItemClicked(item.id)
    }

    private inline fun <T> List<T>.indexOfFirstAsNullable(predicate: (T) -> Boolean) = indexOfFirst(predicate).takeIf { it != -1 }
}