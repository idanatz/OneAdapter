package com.android.one_adapter_example.models

import com.android.one_adapter_example.R
import java.util.*

class ModelGenerator {

    private var modelNumber = 0

    fun generateFirstModels(): MutableList<MessageModel> {
        val models: MutableList<MessageModel> = mutableListOf()
        models.add(MessageModel(modelNumber, 0, R.drawable.one, "Person " + (modelNumber + 1), "is simply dummy text of the printing and typesetting industry."))
        modelNumber++
        models.add(MessageModel(modelNumber, 0, R.drawable.two, "Person " + (modelNumber + 1), "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."))
        modelNumber++
        models.add(MessageModel(modelNumber, 0, R.drawable.three, "Person " + (modelNumber + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        modelNumber++
        models.add(MessageModel(modelNumber, 1, R.drawable.four, "Person " + (modelNumber + 1), "Contrary to popular belief, Lorem Ipsum is not simply random text."))
        modelNumber++
        models.add(MessageModel(modelNumber, 1, R.drawable.five, "Person " + (modelNumber + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        modelNumber++
        models.add(MessageModel(modelNumber, 1, R.drawable.one, "Person " + (modelNumber + 1), "is simply dummy text of the printing and typesetting industry."))
        modelNumber++
        models.add(MessageModel(modelNumber, 2, R.drawable.two, "Person " + (modelNumber + 1), "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."))
        modelNumber++
        models.add(MessageModel(modelNumber, 2, R.drawable.three, "Person " + (modelNumber + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        modelNumber++
        models.add(MessageModel(modelNumber, 2, R.drawable.four, "Person " + (modelNumber + 1), "Contrary to popular belief, Lorem Ipsum is not simply random text."))
        modelNumber++
        models.add(MessageModel(modelNumber, 2, R.drawable.five, "Person " + (modelNumber + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        modelNumber++
        return models
    }

    fun generateMessage(): MessageModel {
        val messageModel = MessageModel(modelNumber, 0, R.drawable.six, "Person Added", "This Message is added!")
        modelNumber++
        return messageModel
    }

    fun generateUpdatedMessage(): MessageModel {
        return MessageModel(2,0 , R.drawable.two, "Person Updated", "This Message is updated!")
    }

    fun generateLoadMoreItems(): List<MessageModel> {
        val moreModels: MutableList<MessageModel> = LinkedList()
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.two, "Person " + (modelNumber + 1), "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.three, "Person " + (modelNumber + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.four, "Person " + (modelNumber + 1), "Contrary to popular belief, Lorem Ipsum is not simply random text."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.five, "Person " + (modelNumber + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.two, "Person " + (modelNumber + 1), "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.three, "Person " + (modelNumber + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.four, "Person " + (modelNumber + 1), "Contrary to popular belief, Lorem Ipsum is not simply random text."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.five, "Person " + (modelNumber + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        modelNumber++
        return moreModels
    }
}