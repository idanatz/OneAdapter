package com.idanatz.sample.examples

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.examples.simple_example.SimpleActionsDialog
import com.idanatz.sample.models.ModelGenerator

@SuppressLint("Registered")
open class BaseExampleActivity : AppCompatActivity(), SimpleActionsDialog.ActionsListener {

    private val actionsDialog = SimpleActionsDialog.getInstance()

    protected lateinit var recyclerView: RecyclerView
    protected val modelGenerator = ModelGenerator()
    protected val handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        recyclerView = findViewById(R.id.recycler)
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    protected fun initActionsDialog(vararg actions: SimpleActionsDialog.Action) {
        actionsDialog.setListener(this)
        actionsDialog.setActions(*actions)

        val actionButton = findViewById<Button>(R.id.show_options_button)
        actionButton.visibility = View.VISIBLE
        actionButton.setOnClickListener { actionsDialog.show(supportFragmentManager, SimpleActionsDialog::class.java.simpleName) }
    }

    override fun onAddItemClicked(id: Int) {}
    override fun onClearAllClicked() {}
    override fun onSetAllClicked() {}
    override fun onUpdatedItemClicked(id: Int) {}
    override fun onDeleteItemClicked(id: Int) {}
    override fun onDeleteIndexClicked(index: Int) {}
    override fun largeDiff() {}
}