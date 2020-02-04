package com.idanatz.sample.examples.binding.data_binding

import android.os.Bundle

import com.idanatz.oneadapter.OneAdapter
import com.idanatz.oneadapter.external.modules.ItemModule
import com.idanatz.oneadapter.external.modules.ItemModuleConfig
import com.idanatz.oneadapter.internal.holders.ViewBinder
import com.idanatz.oneadapter.sample.BR
import com.idanatz.oneadapter.sample.R
import com.idanatz.sample.examples.BaseExampleActivity
import com.bumptech.glide.Glide
import androidx.databinding.BindingAdapter
import android.widget.ImageView
import androidx.lifecycle.ViewModelProviders
import com.idanatz.oneadapter.external.interfaces.Item
import com.idanatz.sample.examples.ActionsDialog
import com.idanatz.sample.models.ObservableMessageModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

class DataBindingActivity : BaseExampleActivity() {

    private lateinit var viewModel: DataBindingViewModel
    private lateinit var oneAdapter: OneAdapter
    private lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(DataBindingViewModel::class.java)
        compositeDisposable = CompositeDisposable()

        oneAdapter = OneAdapter(recyclerView)
                .attachItemModule(MessageItemModule())

        initActionsDialog(ActionsDialog.Action.UpdateItem).setListener(viewModel)

        observeViewModel()
    }

    private fun observeViewModel() {
        compositeDisposable.add(
                viewModel.itemsSubject
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { items -> oneAdapter.setItems(items) }
        )
    }

    inner class MessageItemModule : ItemModule<ObservableMessageModel>() {
        override fun provideModuleConfig(): ItemModuleConfig = object : ItemModuleConfig() {
            override fun withLayoutResource(): Int = R.layout.message_model_data_binding
        }

        override fun onBind(item: Item<ObservableMessageModel>, viewBinder: ViewBinder) {
            viewBinder.dataBinding?.run {
                setVariable(BR.messageModel, item.model)
                lifecycleOwner = this@DataBindingActivity
                executePendingBindings()
            }
        }
    }
}

class BindingAdapters {
    companion object {
        @BindingAdapter("avatarImage")
        @JvmStatic fun loadImage(view: ImageView, resourceId: Int) {
            Glide.with(view.context).load(resourceId).into(view)
        }
    }
}