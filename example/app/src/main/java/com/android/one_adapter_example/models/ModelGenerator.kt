package com.android.one_adapter_example.models

import com.android.one_adapter_example.R
import java.util.*

class ModelGenerator {

    private var modelNumber = 0

    fun generateFirstModels(): MutableList<MessageModel> {
        val models: MutableList<MessageModel> = mutableListOf()
        models.add(MessageModel(modelNumber, 0, R.drawable.one, "Oscar Wilde", "Be yourself; everyone else is already taken."))
        modelNumber++
        models.add(MessageModel(modelNumber, 0, R.drawable.two, "Alfred A. Montapert", "Expect problems and eat them for breakfast."))
        modelNumber++
        models.add(MessageModel(modelNumber, 0, R.drawable.three, "Thomas A. Edison", "Our greatest weakness lies in giving up. The most certain way to succeed is always to try just one more time."))
        modelNumber++
        models.add(MessageModel(modelNumber, 1, R.drawable.four, "Isaac Asimov", "People who think they know everything are a great annoyance to those of us who do."))
        modelNumber++
        models.add(MessageModel(modelNumber, 1, R.drawable.five, "Ellen DeGeneres", "My grandmother started walking five miles a day when she was sixty. She's ninety-seven now, and we don't know where the hell she is."))
        modelNumber++
        models.add(MessageModel(modelNumber, 1, R.drawable.one, "George Carlin", "Atheism is a non-prophet organization."))
        modelNumber++
        models.add(MessageModel(modelNumber, 2, R.drawable.two, "Ayn Rand", "A creative man is motivated by the desire to achieve, not by the desire to beat others."))
        modelNumber++
        models.add(MessageModel(modelNumber, 2, R.drawable.three, "Arthur Ashe", "Start where you are. Use what you have. Do what you can."))
        modelNumber++
        models.add(MessageModel(modelNumber, 2, R.drawable.four, "Mark Twain", "Get your facts first, then you can distort them as you please."))
        modelNumber++
        models.add(MessageModel(modelNumber, 2, R.drawable.five, "Confucius", "It does not matter how slowly you go as long as you do not stop."))
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
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.two, "Confucius", "The will to win, the desire to succeed, the urge to reach your full potential... these are the keys that will unlock the door to personal excellence."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.three, "Theodore Roosevelt", "Believe you can and you're halfway there."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.four, "Francis of Assisi", "Start by doing what's necessary; then do what's possible; and suddenly you are doing the impossible."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.five, "Margaret Mead", "Always remember that you are absolutely unique. Just like everyone else."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.two, "Samuel Beckett", "Ever tried. Ever failed. No matter. Try Again. Fail again. Fail better."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.three, "Sam Levenson", "Don't watch the clock; do what it does. Keep going."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.four, "Elbert Hubbard", "Do not take life too seriously. You will never get out of it alive."))
        modelNumber++
        moreModels.add(MessageModel(modelNumber, 2, R.drawable.five, "Dalai Lama", "Hapiness is not something ready made. It comes from your own actions."))
        modelNumber++
        return moreModels
    }
}