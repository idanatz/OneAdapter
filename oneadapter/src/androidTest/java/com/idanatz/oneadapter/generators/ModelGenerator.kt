package com.idanatz.oneadapter.generators

import com.idanatz.oneadapter.models.TestModel
import com.idanatz.oneadapter.models.TestModel1
import com.idanatz.oneadapter.models.TestModel2
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

    fun generateDifferentModels(quantity: Int): List<TestModel> {
        val list: MutableList<TestModel> = LinkedList()
        for(i in 1..quantity) {
            if (i % 2 != 0) addModel(list, TestModel1(modelNumber, "Model #$modelNumber"))
            else addModel(list, TestModel2(modelNumber, "Model #$modelNumber"))

        }
        return list
    }

    private fun <T> addModel(models: MutableList<T>, model: T) {
        models.add(model)
        modelNumber += 1
    }
}