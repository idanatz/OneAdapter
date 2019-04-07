package com.android.one_adapter_example;

import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.one_adapter_example.interfaces.ExampleView;
import com.android.one_adapter_example.models.HeaderModel;
import com.android.one_adapter_example.models.MessageModel;
import com.android.oneadapter.OneAdapter;
import com.android.oneadapter.interfaces.EmptyInjector;
import com.android.oneadapter.interfaces.LoadMoreInjector;
import com.android.oneadapter.interfaces.HolderInjector;
import com.android.oneadapter.internal.ViewFinder;
import com.android.oneadapter.internal.holder_config.EmptyHolderConfig;
import com.android.oneadapter.internal.holder_config.HolderConfig;
import com.android.oneadapter.internal.holder_config.LoadMoreHolderConfig;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JavaMainActivity extends AppCompatActivity implements ExampleView {

    private Presenter presenter;
    private OneAdapter oneAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        presenter = ViewModelProviders.of(this).get(Presenter.class);
        presenter.setView(this);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        oneAdapter = new OneAdapter();
        oneAdapter
                .injectHolder(new HolderInjector<HeaderModel>() {
                    @Override @NonNull
                    public HolderConfig<HeaderModel> provideHolderConfig() {
                        return new HolderConfig.HolderConfigBuilder<HeaderModel>()
                                .withLayoutResource(R.layout.header_model)
                                .withModelClass(HeaderModel.class)
                                .build();
                    }

                    @Override
                    public void onBind(HeaderModel model, @NonNull ViewFinder viewFinder) {
                        TextView headerTitle = viewFinder.findViewById(R.id.header_title);
                        SwitchCompat headerSwitch = viewFinder.findViewById(R.id.header_switch);

                        headerTitle.setText(model.name);
                        headerSwitch.setChecked(model.checked);
                        headerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> presenter.headerCheckedChanged(model , isChecked));
                    }
                })
                .injectHolder(new HolderInjector<MessageModel>() {
                    @Override @NonNull
                    public HolderConfig<MessageModel> provideHolderConfig() {
                        return new HolderConfig.HolderConfigBuilder<MessageModel>()
                                .withLayoutResource(R.layout.message_model)
                                .withModelClass(MessageModel.class)
                                .build();
                    }

                    @Override
                    public void onBind(MessageModel model, @NonNull ViewFinder viewFinder) {
                        TextView title = viewFinder.findViewById(R.id.title);
                        TextView body  = viewFinder.findViewById(R.id.body);
                        ImageView image = viewFinder.findViewById(R.id.image);

                        title.setText(model.title);
                        body.setText(model.body);
                        Glide.with(JavaMainActivity.this).load(model.imageId).into(image);
                    }
                })
                .injectEmptyHolder(new EmptyInjector() {
                    @Override @NonNull
                    public EmptyHolderConfig provideHolderConfig() {
                        return new EmptyHolderConfig.HolderConfigBuilder()
                                .withLayoutResource(R.layout.empty_state)
                                .build();
                    }
                })
                .injectLoadMoreHolder(new LoadMoreInjector() {
                    @Override @NonNull
                    public LoadMoreHolderConfig provideHolderConfig() {
                        return new LoadMoreHolderConfig.HolderConfigBuilder()
                                .withLayoutResource(R.layout.load_more)
                                .withVisibleThreshold(3)
                                .build();
                    }

                    @Override
                    public void onLoadMore(int currentPage) {
                        presenter.loadMore();
                        Toast.makeText(JavaMainActivity.this, "loading more...", Toast.LENGTH_SHORT).show();
                    }
                })
                .attachTo(recyclerView);

        findViewById(R.id.add_one).setOnClickListener(view -> presenter.addOne());
        findViewById(R.id.clear).setOnClickListener(view -> presenter.clearAll());
        findViewById(R.id.set_all).setOnClickListener(view -> presenter.setAll());
        findViewById(R.id.set_one_item).setOnClickListener(view -> presenter.setOne());
        findViewById(R.id.delete_one_item).setOnClickListener(view -> presenter.removeItem());
        findViewById(R.id.delete_one_index).setOnClickListener(view -> presenter.removeIndex());
        findViewById(R.id.cut).setOnClickListener(view -> presenter.cutItems());
    }

    public void setAll(@NotNull List<Object> items) {
        oneAdapter.setItems(items);
    }
    public void clearAll() {
        oneAdapter.clear();
    }
    public void addOne(int index, @NotNull Object item) {
        oneAdapter.add(index, item);
    }
    public void setOne(@NotNull Object item) {
        oneAdapter.update(item);
    }
    public void removeIndex(int index) {
        oneAdapter.remove(index);
    }
    public void removeItem(@NotNull Object item) {
        oneAdapter.remove(item);
    }
    public void addAll(@NotNull List<Object> items) {
        oneAdapter.add(items);
    }
}