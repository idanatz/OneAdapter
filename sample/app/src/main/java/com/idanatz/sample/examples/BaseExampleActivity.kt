package com.idanatz.sample.examples

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.models.ModelGenerator

@SuppressLint("Registered")
open class BaseExampleActivity : AppCompatActivity(), ActionsDialog.ActionsListener {

    protected lateinit var recyclerView: RecyclerView
    protected val modelGenerator = ModelGenerator()
    protected val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    protected fun initActionsDialog(vararg actions: ActionsDialog.Action): ActionsDialog {
        val actionsDialog = ActionsDialog.getInstance()
        actionsDialog.setActions(*actions)

        val actionButton = findViewById<Button>(R.id.show_options_button)
        actionButton.visibility = View.VISIBLE
        actionButton.setOnClickListener { actionsDialog.show(supportFragmentManager, ActionsDialog::class.java.simpleName) }

        return actionsDialog
    }

    protected fun setToolbarText(text: String) {
        supportActionBar?.title = text
    }

    protected fun setToolbarColor(color: ColorDrawable) {
        supportActionBar?.setBackgroundDrawable(color)
    }

    override fun onAddItemClicked(id: Int) {}
    override fun onClearAllClicked() {}
    override fun onSetAllClicked() {}
    override fun onUpdatedItemClicked(id: Int) {}
    override fun onDeleteItemClicked(id: Int) {}
    override fun onDeleteIndexClicked(index: Int) {}
    override fun onLargeDiffClicked() {}
}