package com.idanatz.sample.examples.features

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.external.modules.ItemSelectionModule
import com.idanatz.oneadapter.external.modules.ItemSelectionModuleConfig
import com.idanatz.oneadapter.external.states.SelectionState
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.MessageModel
import com.idanatz.sample.examples.BaseExampleActivity

class ItemSelectionModuleActivity : BaseExampleActivity() {

    private var toolbarMenu: Menu? = null
    lateinit var oneAdapter: OneAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        oneAdapter = OneAdapter(recyclerView)
                .attachItemModule(messageItem())
                .attachItemSelectionModule(itemSelectionModule())

        oneAdapter.setItems(modelGenerator.generateFirstMessages())
    }

    private fun messageItem(): ItemModule<MessageModel> = object : ItemModule<MessageModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource(): Int = R.layout.message_model
        }

        override fun onBind(model: MessageModel, viewBinder: ViewBinder) {
            val title = viewBinder.findViewById<TextView>(R.id.title)
            val body = viewBinder.findViewById<TextView>(R.id.body)
            val avatarImage = viewBinder.findViewById<ImageView>(R.id.avatarImage)
            val selectedLayer = viewBinder.findViewById<ImageView>(R.id.selected_layer)

            title.text = model.title
            body.text = model.body
            Glide.with(this@ItemSelectionModuleActivity).load(model.avatarImageId).into(avatarImage)

            // selected UI
            avatarImage.alpha = if (model.isSelected) 0.5f else 1f
            selectedLayer.visibility = if (model.isSelected) View.VISIBLE else View.GONE
            viewBinder.getRootView().setBackgroundColor(if (model.isSelected) ContextCompat.getColor(this@ItemSelectionModuleActivity, R.color.light_gray) else Color.WHITE)
        }
    }.addState(object : SelectionState<MessageModel>() {
        override fun isSelectionEnabled(model: MessageModel): Boolean = true

        override fun onSelected(model: MessageModel, selected: Boolean) {
            model.isSelected = selected
        }
    })

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
            oneAdapter.modulesActions.itemSelectionActions?.removeSelectedItems()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}