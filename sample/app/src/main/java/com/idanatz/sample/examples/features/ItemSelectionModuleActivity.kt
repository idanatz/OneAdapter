package com.idanatz.sample.examples.features

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat

import com.bumptech.glide.Glide
import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.interfaces.Item
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
                .attachItemModule(MessageItem().addState(MessageSelectionState()))
                .attachItemSelectionModule(ItemSelectionModuleImpl())

        oneAdapter.setItems(modelGenerator.generateFirstMessages())
    }

    private inner class MessageItem : ItemModule<MessageModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource(): Int = R.layout.message_model
        }

        override fun onBind(item: Item<MessageModel>, viewBinder: ViewBinder) {
            val title = viewBinder.findViewById<TextView>(R.id.title)
            val body = viewBinder.findViewById<TextView>(R.id.body)
            val avatarImage = viewBinder.findViewById<ImageView>(R.id.avatarImage)
            val selectedLayer = viewBinder.findViewById<ImageView>(R.id.selected_layer)

            title.text = item.model.title
            body.text = item.model.body
            Glide.with(viewBinder.rootView).load(item.model.avatarImageId).into(avatarImage)

            // selected UI
            avatarImage.alpha = if (item.metadata.isSelected) 0.5f else 1f
            selectedLayer.visibility = if (item.metadata.isSelected) View.VISIBLE else View.GONE
            viewBinder.rootView.setBackgroundColor(if (item.metadata.isSelected) ContextCompat.getColor(this@ItemSelectionModuleActivity, R.color.light_gray) else Color.WHITE)
        }
    }

    private inner class MessageSelectionState : SelectionState<MessageModel>() {
        override fun isSelectionEnabled(model: MessageModel): Boolean = true

        override fun onSelected(model: MessageModel, selected: Boolean) {
            val message = "${model.title} " + if (selected) "selected" else "unselected"
            Toast.makeText(this@ItemSelectionModuleActivity, message, Toast.LENGTH_SHORT).show()
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
            oneAdapter.modules.itemSelectionModule?.actions?.removeSelectedItems()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}