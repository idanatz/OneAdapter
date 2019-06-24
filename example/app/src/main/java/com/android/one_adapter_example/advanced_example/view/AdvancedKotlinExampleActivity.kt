package com.android.one_adapter_example.advanced_example.view

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.one_adapter_example.R
import com.android.one_adapter_example.models.HeaderModel
import com.android.one_adapter_example.models.MessageModel
import com.android.one_adapter_example.advanced_example.view_model.AdvancedExampleViewModel
import com.android.oneadapter.OneAdapter
import com.android.oneadapter.external.events.ClickEventHook
import com.android.oneadapter.external.modules.*
import com.android.oneadapter.external.states.SelectionState
import com.android.oneadapter.internal.holders.ViewBinder
import com.bumptech.glide.Glide
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class AdvancedKotlinExampleActivity : AppCompatActivity() {

    private lateinit var viewModel: AdvancedExampleViewModel
    private lateinit var oneAdapter: OneAdapter
    private lateinit var compositeDisposable: CompositeDisposable
    private var toolbarMenu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.advanced_example_activity)
        compositeDisposable = CompositeDisposable()
        viewModel = ViewModelProviders.of(this).get(AdvancedExampleViewModel::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)

        oneAdapter = OneAdapter()
                .attachItemModule(headerItem())
                .attachItemModule(messageItem().addState(selectionState()).addEventHook(clickEventHook()))
                .attachEmptinessModule(emptinessModule())
                .attachPagingModule(pagingModule())
                .attachItemSelectionModule(itemSelectionModule())
                .attachTo(recyclerView)

        val optionsButton = findViewById<Button>(R.id.show_options_button)
        optionsButton.setOnClickListener { ActionsDialogFragment.getInstance().show(supportFragmentManager, ActionsDialogFragment::class.java.simpleName) }

        compositeDisposable.add(
                viewModel.itemsSubject
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { items -> oneAdapter.setItems(items) }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }

    private fun headerItem(): ItemModule<HeaderModel> = object : ItemModule<HeaderModel>() {
        override fun provideModuleConfig(): ItemModuleConfig<HeaderModel> = object : ItemModuleConfig<HeaderModel>() {
            override fun withLayoutResource() = R.layout.header_model
        }

        override fun onBind(model: HeaderModel, viewBinder: ViewBinder) {
            val headerTitle = viewBinder.findViewById<TextView>(R.id.header_title)
            val headerSwitch = viewBinder.findViewById<SwitchCompat>(R.id.header_switch)

            headerTitle.text = model.name
            headerSwitch.isChecked = model.checked
            headerSwitch.setOnCheckedChangeListener { _, isChecked -> viewModel.headerCheckedChanged(model, isChecked) }
        }
    }

    private fun messageItem(): ItemModule<MessageModel> = object : ItemModule<MessageModel>() {
        override fun provideModuleConfig(): ItemModuleConfig<MessageModel> = object : ItemModuleConfig<MessageModel>() {
            override fun withLayoutResource() = R.layout.message_model
        }

        override fun onBind(model: MessageModel, viewBinder: ViewBinder) {
            val title = viewBinder.findViewById<TextView>(R.id.title)
            val body = viewBinder.findViewById<TextView>(R.id.body)
            val image = viewBinder.findViewById<ImageView>(R.id.image)
            val selectedLayer = viewBinder.findViewById<ImageView>(R.id.selected_layer)

            title.text = model.title
            body.text = model.body
            Glide.with(this@AdvancedKotlinExampleActivity).load(model.imageId).into(image)

            // selected UI
            image.alpha = if (model.isSelected) 0.5f else 1f
            selectedLayer.visibility = if (model.isSelected) View.VISIBLE else View.GONE
            viewBinder.getRootView().setBackgroundColor(if (model.isSelected) ContextCompat.getColor(this@AdvancedKotlinExampleActivity, R.color.light_gray) else Color.TRANSPARENT)
        }
    }

    private fun selectionState(): SelectionState<MessageModel> = object : SelectionState<MessageModel>() {
        override fun selectionEnabled(model: MessageModel): Boolean = true

        override fun onSelected(model: MessageModel, selected: Boolean) {
            model.isSelected = selected
        }
    }

    private fun clickEventHook(): ClickEventHook<MessageModel> = object : ClickEventHook<MessageModel>() {
        override fun onClick(model: MessageModel) {
            Toast.makeText(this@AdvancedKotlinExampleActivity, model.title + "clicked", Toast.LENGTH_SHORT).show()
        }
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
            viewModel.loadMore()
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
            viewModel.onDeleteItemsClicked(oneAdapter.getSelectedItems())
            oneAdapter.clearSelection()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}