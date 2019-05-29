package com.android.one_adapter_example

import androidx.lifecycle.ViewModel
import android.os.Handler
import com.android.one_adapter_example.models.HeaderModel
import com.android.one_adapter_example.models.MessageModel
import com.android.one_adapter_example.models.ModelGenerator
import com.android.one_adapter_example.persistence.RoomDB
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject

class Presenter : ViewModel() {

    private val modelProvider = ModelGenerator()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    var itemsSubject: BehaviorSubject<List<Any>> = BehaviorSubject.createDefault(listOf())

    init {
        compositeDisposable.add(
                Single.fromCallable { RoomDB.instance.clearAllTables() }
                        .flatMapObservable { RoomDB.instance.messageDao().observeTable().toObservable() }
                        .subscribeOn(Schedulers.io())
                        .subscribe { itemsSubject.onNext(createHeaders(it)) }
        )
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    fun setAll() {
        compositeDisposable.add(
                Single.fromCallable { RoomDB.instance.messageDao().insert(modelProvider.generateFirstModels()) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    fun clearAll() {
        compositeDisposable.add(
                Single.fromCallable { RoomDB.instance.messageDao().deleteAll() }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    fun addOne() {
        compositeDisposable.add(
                Single.fromCallable { RoomDB.instance.messageDao().insert(modelProvider.generateMessage()) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    fun setOne() {
        compositeDisposable.add(
                Single.fromCallable { RoomDB.instance.messageDao().update(modelProvider.generateUpdatedMessage()) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    fun onDeleteItemsClicked(items: List<Any>) {
        compositeDisposable.add(
                Single.fromCallable { RoomDB.instance.messageDao().delete(items as List<MessageModel>) }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    fun cutItems() {
        compositeDisposable.add(
                Single.fromCallable { RoomDB.instance.messageDao().deleteEvenIds() }
                        .subscribeOn(Schedulers.io())
                        .subscribe()
        )
    }

    fun loadMore() {
        Handler().postDelayed({
            val loadMoreItems = modelProvider.generateLoadMoreItems()
            compositeDisposable.add(
                    Single.fromCallable { RoomDB.instance.messageDao().insert(loadMoreItems) }
                            .subscribeOn(Schedulers.io())
                            .subscribe()
            )
        }, 2500)
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
                items.removeIf { it is MessageModel && it.headerId == model.id }
            }

            itemsSubject.onNext(items)
        }
    }

    private fun createHeaders(models: List<MessageModel>): MutableList<Any> {
        val list = mutableListOf<Any>()

        models.groupBy { it.headerId }.forEach { (headerIndex, messages) ->
            list.add(HeaderModel(headerIndex, "Header " + (headerIndex + 1)))
            list.addAll(messages)
        }

        return list
    }

    private inline fun <T> List<T>.indexOfFirstAsNullable(predicate: (T) -> Boolean) = indexOfFirst(predicate).takeIf { it != -1 }
}