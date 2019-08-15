package com.idanatz.sample.models

import com.idanatz.oneadapter.sample.R
import java.util.*

class ModelGenerator {

    private var modelNumber = 0

    fun generateFirstMessages(): MutableList<MessageModel> {
        val models: MutableList<MessageModel> = mutableListOf()
        addModel(models, MessageModel(modelNumber, 0, R.drawable.person_one, "Oscar Wilde", "Be yourself; everyone else is already taken."))
        addModel(models, MessageModel(modelNumber, 0, R.drawable.person_two, "Alfred A. Montapert", "Expect problems and eat them for breakfast."))
        addModel(models, MessageModel(modelNumber, 0, R.drawable.person_three, "Thomas A. Edison", "Our greatest weakness lies in giving up. The most certain way to succeed is always to try just one more time."))
        addModel(models, MessageModel(modelNumber, 1, R.drawable.person_four, "Isaac Asimov", "People who think they know everything are a great annoyance to those of us who do."))
        addModel(models, MessageModel(modelNumber, 1, R.drawable.person_five, "Ellen DeGeneres", "My grandmother started walking five miles a day when she was sixty. She's ninety-seven now, and we don't know where the hell she is."))
        addModel(models, MessageModel(modelNumber, 1, R.drawable.person_one, "George Carlin", "Atheism is a non-prophet organization."))
        addModel(models, MessageModel(modelNumber, 2, R.drawable.person_two, "Ayn Rand", "A creative man is motivated by the desire to achieve, not by the desire to beat others."))
        addModel(models, MessageModel(modelNumber, 2, R.drawable.person_three, "Arthur Ashe", "Start where you are. Use what you have. Do what you can."))
        addModel(models, MessageModel(modelNumber, 2, R.drawable.person_four, "Mark Twain", "Get your facts first, then you can distort them as you please."))
        addModel(models, MessageModel(modelNumber, 2, R.drawable.person_five, "Confucius", "It does not matter how slowly you go as long as you do not stop."))
        return models
    }

    fun generateMessage(id: Int): MessageModel {
        val messageModel = MessageModel(id, 2, R.drawable.person_six, "Person Added", "This Message is added!")
        modelNumber++
        return messageModel
    }

    fun generateUpdatedMessage(id: Int, headerId: Int): MessageModel {
        return MessageModel(id, headerId, R.drawable.person_two, "Person Updated", "This Message is updated!")
    }

    fun generateLoadMoreMessages(): List<MessageModel> {
        val moreModels: MutableList<MessageModel> = LinkedList()
        addModel(moreModels, MessageModel(modelNumber, 2, R.drawable.person_two, "Samuel Beckett", "Ever tried. Ever failed. No matter. Try Again. Fail again. Fail better."))
        addModel(moreModels, MessageModel(modelNumber, 2, R.drawable.person_three, "Theodore Roosevelt", "Believe you can and you're halfway there."))
        addModel(moreModels, MessageModel(modelNumber, 2, R.drawable.person_four, "Francis of Assisi", "Start by doing what's necessary; then do what's possible; and suddenly you are doing the impossible."))
        addModel(moreModels, MessageModel(modelNumber, 2, R.drawable.person_five, "Margaret Mead", "Always remember that you are absolutely unique. Just like everyone else."))
        addModel(moreModels, MessageModel(modelNumber, 2, R.drawable.person_two, "Confucius", "The will to win, the desire to succeed, the urge to reach your full potential... these are the keys that will unlock the door to personal excellence."))
        addModel(moreModels, MessageModel(modelNumber, 2, R.drawable.person_three, "Sam Levenson", "Don't watch the clock; do what it does. Keep going."))
        addModel(moreModels, MessageModel(modelNumber, 2, R.drawable.person_four, "Elbert Hubbard", "Do not take life too seriously. You will never get out of it alive."))
        addModel(moreModels, MessageModel(modelNumber, 2, R.drawable.person_five, "Dalai Lama", "Hapiness is not something ready made. It comes from your own actions."))
        return moreModels
    }

    fun generateStories(): StoriesModel {
        return StoriesModel(0, R.drawable.story_one, R.drawable.story_two, R.drawable.story_three)
    }

    fun addHeadersFromMessages(messages: List<MessageModel>, checkable: Boolean): MutableList<Any> {
        val list = mutableListOf<Any>()

        messages.groupBy { it.headerId }.forEach { (headerIndex, messages) ->
            list.add(HeaderModel(headerIndex, "Section " + (headerIndex + 1), checkable))
            list.addAll(messages.sortedBy { it.id })
        }

        return list
    }

    private fun addModel(models: MutableList<MessageModel>, model: MessageModel) {
        models.add(model)
        modelNumber += 11
    }
}