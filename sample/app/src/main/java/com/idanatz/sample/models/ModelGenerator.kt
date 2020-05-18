package com.idanatz.sample.models

import com.idanatz.oneadapter.external.interfaces.Diffable
import com.idanatz.oneadapter.sample.R
import java.util.*

class ModelGenerator {

	private var modelNumber = 0
	private var headerNumber = 0
	private var headerCount = 0
	private val images = listOf(
			R.drawable.person_one,
			R.drawable.person_two,
			R.drawable.person_three,
			R.drawable.person_four,
			R.drawable.person_four
	)
	private val quotes = listOf(
			"Oscar Wilde" to "Be yourself; everyone else is already taken.",
			"Alfred A. Montapert" to "Expect problems and eat them for breakfast.",
			"Thomas A. Edison" to "Our greatest weakness lies in giving up. The most certain way to succeed is always to try just one more time.",
			"Isaac Asimov" to "People who think they know everything are a great annoyance to those of us who do.",
			"Ellen DeGeneres" to "My grandmother started walking five miles a day when she was sixty. She's ninety-seven now, and we don't know where the hell she is.",
			"George Carlin" to "Atheism is a non-prophet organization.",
			"Ayn Rand" to "A creative man is motivated by the desire to achieve, not by the desire to beat others.",
			"Arthur Ashe" to "Start where you are. Use what you have. Do what you can.",
			"Mark Twain" to "Get your facts first, then you can distort them as you please.",
			"Confucius" to "It does not matter how slowly you go as long as you do not stop.",
			"Samuel Beckett" to "Ever tried. Ever failed. No matter. Try Again. Fail again. Fail better.",
			"Theodore Roosevelt" to "Believe you can and you're halfway there.",
			"Francis of Assisi" to "Start by doing what's necessary; then do what's possible; and suddenly you are doing the impossible.",
			"Margaret Mead" to "Always remember that you are absolutely unique. Just like everyone else.",
			"Confucius" to "The will to win, the desire to succeed, the urge to reach your full potential... these are the keys that will unlock the door to personal excellence.",
			"Sam Levenson" to "Don't watch the clock; do what it does. Keep going.",
			"Elbert Hubbard" to "Do not take life too seriously. You will never get out of it alive.",
			"Dalai Lama" to "Hapiness is not something ready made. It comes from your own actions."
	)

	fun generateMessages(count: Int): MutableList<MessageModel> {
		val models: MutableList<MessageModel> = mutableListOf()
		for (i in 0 until count) {
			models.add(generateMessage())
		}
		return models
	}

	private fun generateMessage(): MessageModel {
		val (person, quote) = quotes[modelNumber % quotes.size]
		val messageModel = MessageModel(modelNumber * 3, headerNumber, images[modelNumber % images.size], person, quote)
		modelNumber++

		headerCount = (headerCount + 1) % 5
		if (headerCount == 0) {
			headerNumber++
		}

		return messageModel
	}

	fun generateAddedMessage(id: Int, headerId: Int): MessageModel {
		val messageModel = MessageModel(id, headerId, R.drawable.person_six, "Person Added", "This Message is added!")
		modelNumber++
		return messageModel
	}

	fun generateUpdatedMessage(id: Int, headerId: Int): MessageModel {
		return MessageModel(id, headerId, R.drawable.person_six, "Person Updated", "This Message is updated!")
	}

	fun generateStories(): StoriesModel {
		val storyModels: MutableList<StoryModel> = LinkedList()
		storyModels.add(StoryModel(0, R.drawable.story_one))
		storyModels.add(StoryModel(1, R.drawable.story_two))
		storyModels.add(StoryModel(2, R.drawable.story_three))
		storyModels.add(StoryModel(3, R.drawable.story_four))
		storyModels.add(StoryModel(4, R.drawable.story_five))
		storyModels.add(StoryModel(5, R.drawable.story_one))
		storyModels.add(StoryModel(6, R.drawable.story_two))
		storyModels.add(StoryModel(7, R.drawable.story_three))
		storyModels.add(StoryModel(8, R.drawable.story_four))
		storyModels.add(StoryModel(9, R.drawable.story_five))
		return StoriesModel(storyModels)
	}

	fun addHeadersFromMessages(messages: List<MessageModel>, checkable: Boolean): MutableList<Diffable> {
		val list = mutableListOf<Diffable>()

		messages.groupBy { it.headerId }.forEach { (headerIndex, messages) ->
			list.add(HeaderModel(headerIndex, "Section " + (headerIndex + 1), checkable))
			list.addAll(messages.sortedBy { it.id })
		}

		return list
	}
}