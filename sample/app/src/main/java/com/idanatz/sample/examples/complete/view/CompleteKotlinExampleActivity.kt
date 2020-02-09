package com.idanatz.sample.examples.complete.view

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.idanatz.sample.models.HeaderModel
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.complete.view_model.CompleteExampleViewModel
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook
import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.sample.R
import com.bumptech.glide.Glide
import com.idanatz.oneadapter.external.event_hooks.SwipeEventHook
import com.idanatz.oneadapter.external.event_hooks.SwipeEventHookConfig
import com.idanatz.oneadapter.external.holders.EmptyIndicator
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.oneadapter.external.modules.*
import com.idanatz.sample.examples.BaseExampleActivity
import com.idanatz.sample.examples.ActionsDialog.*
import com.idanatz.sample.models.StoriesModel
import com.idanatz.sample.models.StoryModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class CompleteKotlinExampleActivity : BaseExampleActivity() {

    companion object {
        const val ICON_MARGIN = 50
    }

    private lateinit var viewModel: CompleteExampleViewModel
    private lateinit var oneAdapter: OneAdapter
    private lateinit var compositeDisposable: CompositeDisposable
    private var toolbarMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        compositeDisposable = CompositeDisposable()
        viewModel = ViewModelProviders.of(this).get(CompleteExampleViewModel::class.java)

        oneAdapter = OneAdapter(recyclerView)
                .attachItemModule(StoriesItem())
                .attachItemModule(HeaderItem())
                .attachItemModule(MessageItem()
                        .addState(MessageSelectionState())
                        .addEventHook(MessageClickHook())
                        .addEventHook(MessageSwipeHook())
                )
                .attachEmptinessModule(EmptinessModuleImpl())
                .attachPagingModule(PagingModuleImpl())
                .attachItemSelectionModule(ItemSelectionModuleImpl())

        initActionsDialog(*Action.values()).setListener(viewModel)

        observeViewModel()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun observeViewModel() {
        compositeDisposable.add(
                viewModel.itemsSubject
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { items -> oneAdapter.setItems(items) }
        )
    }

    private inner class HeaderItem : ItemModule<HeaderModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource() = R.layout.header_model
            override fun withFirstBindAnimation(): Animator {
                // can be implemented by constructing ObjectAnimator
                return ObjectAnimator().apply {
                    propertyName = "translationX"
                    setFloatValues(-1080f, 0f)
                    duration = 750
                }
            }
        }

	    override fun onBind(item: Item<HeaderModel>, viewBinder: ViewBinder) {
		    val headerTitle = viewBinder.findViewById<TextView>(R.id.header_title)
		    val headerSwitch = viewBinder.findViewById<SwitchCompat>(R.id.header_switch)

		    headerTitle.text = item.model.name
		    headerSwitch.visibility = if (item.model.checkable) View.VISIBLE else View.GONE
		    headerSwitch.isChecked = item.model.checked
		    headerSwitch.setOnCheckedChangeListener { _, isChecked -> viewModel.headerCheckedChanged(item.model, isChecked) }
	    }
    }

    private inner class MessageItem : ItemModule<MessageModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource() = R.layout.message_model
            override fun withFirstBindAnimation(): Animator {
                // can be implemented by inflating Animator Xml
                return AnimatorInflater.loadAnimator(this@CompleteKotlinExampleActivity, R.animator.item_animation_example)
            }
        }

	    override fun onBind(item: Item<MessageModel>, viewBinder: ViewBinder) {
		    val id = viewBinder.findViewById<TextView>(R.id.id)
		    val title = viewBinder.findViewById<TextView>(R.id.title)
		    val body = viewBinder.findViewById<TextView>(R.id.body)
		    val avatarImage = viewBinder.findViewById<ImageView>(R.id.avatarImage)
		    val selectedLayer = viewBinder.findViewById<ImageView>(R.id.selected_layer)

		    id.text = getString(R.string.message_model_id).format(item.model.id)
		    title.text = item.model.title
		    body.text = item.model.body
		    Glide.with(viewBinder.rootView).load(item.model.avatarImageId).into(avatarImage)

		    // selected UI
		    avatarImage.alpha = if (item.metadata.isSelected) 0.5f else 1f
		    selectedLayer.visibility = if (item.metadata.isSelected) View.VISIBLE else View.GONE
		    viewBinder.rootView.setBackgroundColor(if (item.metadata.isSelected) ContextCompat.getColor(this@CompleteKotlinExampleActivity, R.color.light_gray) else Color.WHITE)	    }
    }

    private inner class MessageSelectionState : SelectionState<MessageModel>() {
        override fun isSelectionEnabled(model: MessageModel): Boolean = true

        override fun onSelected(model: MessageModel, selected: Boolean) {
            val message = "${model.title} " + if (selected) "selected" else "unselected"
            Toast.makeText(this@CompleteKotlinExampleActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    private class MessageClickHook : ClickEventHook<MessageModel>() {
        override fun onClick(model: MessageModel, viewBinder: ViewBinder) = Toast.makeText(viewBinder.rootView.context, "${model.title} clicked", Toast.LENGTH_SHORT).show()
    }

    private class StoriesItem : ItemModule<StoriesModel>() {
        private var oneAdapter: OneAdapter? = null

        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource(): Int = R.layout.recycler_view
        }

        override fun onCreated(viewBinder: ViewBinder) {
            val nestedRecyclerView = viewBinder.findViewById<RecyclerView>(R.id.recycler)
            val layoutManager = LinearLayoutManager(viewBinder.rootView.context, LinearLayoutManager.HORIZONTAL, false)
            nestedRecyclerView.layoutManager = layoutManager

            oneAdapter = OneAdapter(nestedRecyclerView)
                    .attachItemModule(StoryItem())
        }

        override fun onBind(item: Item<StoriesModel>, viewBinder: ViewBinder) {
            oneAdapter?.setItems(item.model.stories)

            // restore scroll state
            val nestedRecyclerView = viewBinder.findViewById<RecyclerView>(R.id.recycler)
            val layoutManager = nestedRecyclerView.layoutManager as LinearLayoutManager?
            layoutManager?.onRestoreInstanceState(item.model.scrollPosition)
        }

        override fun onUnbind(item: Item<StoriesModel>, viewBinder: ViewBinder) {
            // save scroll state
            val nestedRecyclerView = viewBinder.findViewById<RecyclerView>(R.id.recycler)
            val layoutManager = nestedRecyclerView.layoutManager as LinearLayoutManager?
            item.model.scrollPosition = layoutManager?.onSaveInstanceState()
        }

        private class StoryItem : ItemModule<StoryModel>() {
            override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
                override fun withLayoutResource(): Int = R.layout.story_model
            }

            override fun onBind(item: Item<StoryModel>, viewBinder: ViewBinder) {
                val story = viewBinder.findViewById<ImageView>(R.id.story)
                Glide.with(viewBinder.rootView).load(item.model.storyImageId).into(story)
            }
        }
    }

    private class EmptinessModuleImpl : EmptinessModule() {
        override fun provideModuleConfig(): EmptinessModuleConfig = object : EmptinessModuleConfig() {
            override fun withLayoutResource() = R.layout.empty_state
        }

        override fun onBind(item: Item<EmptyIndicator>, viewBinder: ViewBinder) {
            val animation = viewBinder.findViewById<LottieAnimationView>(R.id.animation_view)
            animation.setAnimation(R.raw.empty_list)
            animation.playAnimation()
        }

        override fun onUnbind(item: Item<EmptyIndicator>, viewBinder: ViewBinder) {
            val animation = viewBinder.findViewById<LottieAnimationView>(R.id.animation_view)
            animation.pauseAnimation()
        }
    }

    private inner class PagingModuleImpl : PagingModule() {
        override fun provideModuleConfig(): PagingModuleConfig = object : PagingModuleConfig() {
            override fun withLayoutResource() = R.layout.load_more
            override fun withVisibleThreshold() = 3
        }

        override fun onLoadMore(currentPage: Int) {
            viewModel.onLoadMore()
        }
    }

    private inner class ItemSelectionModuleImpl : ItemSelectionModule() {
        override fun provideModuleConfig(): ItemSelectionModuleConfig = object : ItemSelectionModuleConfig() {
            override fun withSelectionType() = SelectionType.Multiple
        }

        override fun onSelectionUpdated(selectedCount: Int) {
            if (selectedCount == 0) {
                setToolbarText(getString(R.string.app_name))
                toolbarMenu?.findItem(R.id.action_delete)?.isVisible = false
            } else {
                setToolbarText("$selectedCount selected")
                toolbarMenu?.findItem(R.id.action_delete)?.isVisible = true
            }
        }
    }

    private fun setToolbarText(text: String) {
        supportActionBar?.title = text
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        toolbarMenu = menu
        menuInflater.inflate(R.menu.menu_main, toolbarMenu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_delete) {
            oneAdapter.modules.itemSelectionModule?.actions?.let { itemSelectionActions ->
                viewModel.onDeleteItemsClicked(itemSelectionActions.getSelectedItems())
                itemSelectionActions.clearSelection()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private inner class MessageSwipeHook : SwipeEventHook<MessageModel>() {
        override fun provideHookConfig(): SwipeEventHookConfig = object : SwipeEventHookConfig() {
            override fun withSwipeDirection() = listOf(SwipeDirection.Left, SwipeDirection.Right)
        }

        override fun onSwipe(canvas: Canvas, xAxisOffset: Float, viewBinder: ViewBinder) {
            when {
                xAxisOffset < 0 -> paintSwipeLeft(canvas, xAxisOffset, viewBinder.rootView)
                xAxisOffset > 0 -> paintSwipeRight(canvas, xAxisOffset, viewBinder.rootView)
            }
        }

        override fun onSwipeComplete(model: MessageModel, direction: SwipeDirection, viewBinder: ViewBinder) {
            when (direction) {
                SwipeDirection.Left -> viewModel.onSwipeToDeleteItem(model)
                SwipeDirection.Right -> {
                    Toast.makeText(this@CompleteKotlinExampleActivity, "${model.title} snoozed", Toast.LENGTH_SHORT).show()
                    oneAdapter.update(model)
                }
            }
        }
    }

    private fun paintSwipeRight(canvas: Canvas, xAxisOffset: Float, rootView: View) {
        val icon = ContextCompat.getDrawable(this@CompleteKotlinExampleActivity, R.drawable.ic_snooze_white_24dp)
        val colorDrawable = ColorDrawable(Color.DKGRAY)

        icon?.let {
            val middle = rootView.bottom - rootView.top
            var top = rootView.top
            var bottom = rootView.bottom
            var right = rootView.left + xAxisOffset.toInt()
            var left = rootView.left
            colorDrawable.setBounds(left, top, right, bottom)
            colorDrawable.draw(canvas)

            top = rootView.top + (middle / 2) - (it.intrinsicHeight / 2)
            bottom = top + it.intrinsicHeight
            left = rootView.left + ICON_MARGIN
            right = left + it.intrinsicWidth
            it.setBounds(left, top, right, bottom)
            it.draw(canvas)
        }
    }

    private fun paintSwipeLeft(canvas: Canvas, xAxisOffset: Float, rootView: View) {
        val icon = ContextCompat.getDrawable(this@CompleteKotlinExampleActivity, R.drawable.ic_delete_white_24dp)
        val colorDrawable = ColorDrawable(Color.RED)

        icon?.let {
            val middle = rootView.bottom - rootView.top
            var top = rootView.top
            var bottom = rootView.bottom
            var right = rootView.right
            var left = rootView.right + xAxisOffset.toInt()
            colorDrawable.setBounds(left, top, right, bottom)
            colorDrawable.draw(canvas)

            top = rootView.top + (middle / 2) - (it.intrinsicHeight / 2)
            bottom = top + it.intrinsicHeight
            right = rootView.right - ICON_MARGIN
            left = right - it.intrinsicWidth
            it.setBounds(left, top, right, bottom)
            it.draw(canvas)
        }
    }
}