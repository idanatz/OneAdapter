package com.idanatz.sample.examples.complete.view

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
import com.airbnb.lottie.LottieAnimationView
import com.idanatz.sample.models.HeaderModel
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.complete.view_model.CompleteExampleViewModel
import com.idanatz.sample.models.StoriesModel
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.events.ClickEventHook
import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.sample.R
import com.bumptech.glide.Glide
import com.idanatz.oneadapter.external.events.SwipeEventHook
import com.idanatz.oneadapter.external.modules.*
import com.idanatz.sample.examples.BaseExampleActivity
import com.idanatz.sample.examples.ActionsDialog.*
import com.idanatz.sample.examples.features.SwipeEventHookActivity
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

        oneAdapter = OneAdapter()
                .attachItemModule(storyItem())
                .attachItemModule(headerItem())
                .attachItemModule(messageItem()
                        .addState(selectionState())
                        .addEventHook(clickEventHook())
                        .addEventHook(swipeEventHook())
                )
                .attachEmptinessModule(emptinessModule())
                .attachPagingModule(pagingModule())
                .attachItemSelectionModule(itemSelectionModule())
                .attachTo(recyclerView)

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

    private fun storyItem(): ItemModule<StoriesModel> = object : ItemModule<StoriesModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource(): Int = R.layout.stories_model
        }

        override fun onBind(model: StoriesModel, viewBinder: ViewBinder) {
            val story1 = viewBinder.findViewById<ImageView>(R.id.story1)
            val story2 = viewBinder.findViewById<ImageView>(R.id.story2)
            val story3 = viewBinder.findViewById<ImageView>(R.id.story3)

            Glide.with(this@CompleteKotlinExampleActivity).load(model.storyImageId1).into(story1)
            Glide.with(this@CompleteKotlinExampleActivity).load(model.storyImageId2).into(story2)
            Glide.with(this@CompleteKotlinExampleActivity).load(model.storyImageId3).into(story3)
        }
    }

    private fun headerItem(): ItemModule<HeaderModel> = object : ItemModule<HeaderModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource() = R.layout.header_model
        }

        override fun onBind(model: HeaderModel, viewBinder: ViewBinder) {
            val headerTitle = viewBinder.findViewById<TextView>(R.id.header_title)
            val headerSwitch = viewBinder.findViewById<SwitchCompat>(R.id.header_switch)

            headerTitle.text = model.name
            headerSwitch.visibility = if (model.checkable) View.VISIBLE else View.GONE
            headerSwitch.isChecked = model.checked
            headerSwitch.setOnCheckedChangeListener { _, isChecked -> viewModel.headerCheckedChanged(model, isChecked) }
        }
    }

    private fun messageItem(): ItemModule<MessageModel> = object : ItemModule<MessageModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource() = R.layout.message_model
        }

        override fun onBind(model: MessageModel, viewBinder: ViewBinder) {
            val id = viewBinder.findViewById<TextView>(R.id.id)
            val title = viewBinder.findViewById<TextView>(R.id.title)
            val body = viewBinder.findViewById<TextView>(R.id.body)
            val avatarImage = viewBinder.findViewById<ImageView>(R.id.avatarImage)
            val selectedLayer = viewBinder.findViewById<ImageView>(R.id.selected_layer)

            id.text = getString(R.string.message_model_id).format(model.id)
            title.text = model.title
            body.text = model.body
            Glide.with(this@CompleteKotlinExampleActivity).load(model.avatarImageId).into(avatarImage)

            // selected UI
            avatarImage.alpha = if (model.isSelected) 0.5f else 1f
            selectedLayer.visibility = if (model.isSelected) View.VISIBLE else View.GONE
            viewBinder.getRootView().setBackgroundColor(if (model.isSelected) ContextCompat.getColor(this@CompleteKotlinExampleActivity, R.color.light_gray) else Color.WHITE)
        }
    }

    private fun selectionState(): SelectionState<MessageModel> = object : SelectionState<MessageModel>() {
        override fun selectionEnabled(model: MessageModel): Boolean = true

        override fun onSelected(model: MessageModel, selected: Boolean) {
            model.isSelected = selected
        }
    }

    private fun clickEventHook(): ClickEventHook<MessageModel> = object : ClickEventHook<MessageModel>() {
        override fun onClick(model: MessageModel, viewBinder: ViewBinder) = Toast.makeText(this@CompleteKotlinExampleActivity, "${model.title} clicked", Toast.LENGTH_SHORT).show()
    }

    private fun emptinessModule(): EmptinessModule = object : EmptinessModule() {
        override fun provideModuleConfig(): EmptinessModuleConfig = object : EmptinessModuleConfig() {
            override fun withLayoutResource() = R.layout.empty_state
        }

        override fun onBind(viewBinder: ViewBinder) {
            val animation = viewBinder.findViewById<LottieAnimationView>(R.id.animation_view)
            animation.setAnimation(R.raw.empty_list)
            animation.playAnimation()
        }

        override fun onUnbind(viewBinder: ViewBinder) {
            val animation = viewBinder.findViewById<LottieAnimationView>(R.id.animation_view)
            animation.pauseAnimation()
        }
    }

    private fun pagingModule(): PagingModule = object : PagingModule() {
        override fun provideModuleConfig(): PagingModuleConfig = object : PagingModuleConfig() {
            override fun withLayoutResource() = R.layout.load_more
            override fun withVisibleThreshold() = 3
        }

        override fun onLoadMore(currentPage: Int) {
            viewModel.onLoadMore()
        }
    }

    private fun itemSelectionModule(): ItemSelectionModule = object : ItemSelectionModule() {
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
            oneAdapter.itemSelectionActions?.let { itemSelectionActions ->
                viewModel.onDeleteItemsClicked(itemSelectionActions.getSelectedItems())
                itemSelectionActions.clearSelection()
            }
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun swipeEventHook(): SwipeEventHook<MessageModel> = object : SwipeEventHook<MessageModel>() {
        override fun onSwipe(canvas: Canvas, xAxisOffset: Float, viewBinder: ViewBinder) {
            when {
                xAxisOffset < 0 -> paintSwipeLeft(canvas, xAxisOffset, viewBinder.getRootView())
                xAxisOffset > 0 -> paintSwipeRight(canvas, xAxisOffset, viewBinder.getRootView())
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