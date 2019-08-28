package com.idanatz.oneadapter.models

import java.util.*

class ModelGenerator {

    private var modelNumber = 0

    fun generateModel(): TestModel {
        val messageModel = TestModel(modelNumber, "Model #$modelNumber")
        modelNumber++
        return messageModel
    }

    fun generateModels(quantity: Int): List<TestModel> {
        val list: MutableList<TestModel> = LinkedList()
        for(i in 1..quantity) {
            addModel(list, TestModel(modelNumber, "Model #$modelNumber"))
        }
        return list
    }

    private fun addModel(models: MutableList<TestModel>, model: TestModel) {
        models.add(model)
        modelNumber += 1
    }
}