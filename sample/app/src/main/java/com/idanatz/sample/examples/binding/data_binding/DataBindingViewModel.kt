package com.idanatz.sample.examples.binding.data_binding

import androidx.lifecycle.ViewModel
import com.idanatz.sample.examples.ActionsDialog
import com.idanatz.sample.models.ModelGenerator
import com.idanatz.sample.models.ObservableMessageModel
import io.reactivex.subjects.BehaviorSubject

class DataBindingViewModel : ViewModel(), ActionsDialog.ActionsListener {

    private val modelProvider = ModelGenerator()
    var itemsSubject: BehaviorSubject<List<ObservableMessageModel>> = BehaviorSubject.createDefault(listOf())

    init {
        val models = modelProvider.generateMessages(10).map { ObservableMessageModel(it) }
        itemsSubject.onNext(models)
    }

    override fun onUpdatedItemClicked(id: Int) {
        // there is no need to call the adapter setter methods
        // the view and the model are binded, just update the model
        itemsSubject.value?.find { it.id == id }?.let {
            it.setTitle("Person Updated")
            it.setBody("This Message is updated!")
        }
    }

    override fun onAddItemClicked(id: Int) {}
    override fun onClearAllClicked() {}
    override fun onSetAllClicked() {}
    override fun onDeleteItemClicked(id: Int) {}
    override fun onDeleteIndexClicked(index: Int) {}
    override fun onLargeDiffClicked() {}
}