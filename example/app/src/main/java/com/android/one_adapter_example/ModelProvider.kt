package com.android.one_adapter_example

import com.android.one_adapter_example.models.MessageModel
import java.util.*

class ModelProvider {

    var models: MutableList<MessageModel> = LinkedList()
    
    init {
        models.add(MessageModel(models.size, 0, R.drawable.one, "Person " + (models.size + 1), "is simply dummy text of the printing and typesetting industry."))
        models.add(MessageModel(models.size, 0, R.drawable.two, "Person " + (models.size + 1), "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."))
        models.add(MessageModel(models.size, 0, R.drawable.three, "Person " + (models.size + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        models.add(MessageModel(models.size, 1, R.drawable.four, "Person " + (models.size + 1), "Contrary to popular belief, Lorem Ipsum is not simply random text."))
        models.add(MessageModel(models.size, 1, R.drawable.five, "Person " + (models.size + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        models.add(MessageModel(models.size, 1, R.drawable.one, "Person " + (models.size + 1), "is simply dummy text of the printing and typesetting industry."))
        models.add(MessageModel(models.size, 2, R.drawable.two, "Person " + (models.size + 1), "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."))
        models.add(MessageModel(models.size, 2, R.drawable.three, "Person " + (models.size + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        models.add(MessageModel(models.size, 2, R.drawable.four, "Person " + (models.size + 1), "Contrary to popular belief, Lorem Ipsum is not simply random text."))
        models.add(MessageModel(models.size, 2, R.drawable.five, "Person " + (models.size + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
    }

    fun addMessage(): MessageModel {
        val messageModel = MessageModel(models.size, 0, R.drawable.six, "Person Added", "This Message is added!")
        models.add(2, messageModel)
        return messageModel
    }

    fun updateMessage(index: Int): MessageModel {
        val messageModel = MessageModel(2,0 ,R.drawable.two, "Person Updated", "This Message is updated!")
        models.removeAt(index)
        models.add(index, messageModel)
        return messageModel
    }

    fun removeIndex(index: Int): MessageModel {
        return models.removeAt(index)
    }

    fun removeItem(): MessageModel {
        return models.removeAt(1)
    }

    fun createLoadMoreItems(): List<MessageModel> {
        val moreModels: MutableList<MessageModel> = LinkedList()
        moreModels.add(MessageModel(models.size + moreModels.size, 2, R.drawable.two, "Person " + (models.size + moreModels.size + 1), "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."))
        moreModels.add(MessageModel(models.size + moreModels.size, 2, R.drawable.three, "Person " + (models.size + moreModels.size + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        moreModels.add(MessageModel(models.size + moreModels.size, 2, R.drawable.four, "Person " + (models.size + moreModels.size + 1), "Contrary to popular belief, Lorem Ipsum is not simply random text."))
        moreModels.add(MessageModel(models.size + moreModels.size, 2, R.drawable.five, "Person " + (models.size + moreModels.size + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        moreModels.add(MessageModel(models.size + moreModels.size, 2, R.drawable.two, "Person " + (models.size + moreModels.size + 1), "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book."))
        moreModels.add(MessageModel(models.size + moreModels.size, 2, R.drawable.three, "Person " + (models.size + moreModels.size + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        moreModels.add(MessageModel(models.size + moreModels.size, 2, R.drawable.four, "Person " + (models.size + moreModels.size + 1), "Contrary to popular belief, Lorem Ipsum is not simply random text."))
        moreModels.add(MessageModel(models.size + moreModels.size, 2, R.drawable.five, "Person " + (models.size + moreModels.size + 1), "It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."))
        models.addAll(moreModels)
        return moreModels
    }

    fun getMessagesWithHeaderId(id: Int): List<MessageModel> {
        return models.filter { it.headerId == id }
    }
}