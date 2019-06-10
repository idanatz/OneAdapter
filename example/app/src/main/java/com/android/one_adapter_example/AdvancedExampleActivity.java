package com.android.one_adapter_example;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.one_adapter_example.models.HeaderModel;
import com.android.one_adapter_example.models.MessageModel;
import com.android.oneadapter.OneAdapter;
import com.android.oneadapter.modules.selection_state.SelectionModuleConfig;
import com.android.oneadapter.modules.selection_state.SelectionType;
import com.android.oneadapter.modules.empty_state.EmptyStateModule;
import com.android.oneadapter.modules.load_more.LoadMoreModule;
import com.android.oneadapter.modules.holder.HolderModule;
import com.android.oneadapter.internal.holders.ViewBinder;
import com.android.oneadapter.modules.empty_state.EmptyStateModuleConfig;
import com.android.oneadapter.modules.holder.HolderModuleConfig;
import com.android.oneadapter.modules.load_more.LoadMoreModuleConfig;
import com.android.oneadapter.modules.selection_state.SelectionStateModule;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

public class AdvancedExampleActivity extends AppCompatActivity {

    private AdvancedExampleViewModel viewModel;
    private OneAdapter oneAdapter;
    private CompositeDisposable compositeDisposable;
    private Menu toolbarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        compositeDisposable = new CompositeDisposable();
        viewModel = ViewModelProviders.of(this).get(AdvancedExampleViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        oneAdapter = new OneAdapter()
                .attachHolderModule(headerItem())
                .attachHolderModule(messageItem())
                .attachEmptyStateModule(emptyStateModule())
                .attachLoadMoreModule(loadMoreModule())
                .attachSelectionStateModule(selectionStateModule())
                .attachTo(recyclerView);

        Button optionsButton = findViewById(R.id.show_options_button);
        optionsButton.setOnClickListener(v -> BottomDialogFragment.getInstance().show(getSupportFragmentManager(), BottomDialogFragment.class.getSimpleName()));

        compositeDisposable.add(
                viewModel.getItemsSubject()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(items -> oneAdapter.setItems(items))
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @NotNull
    private HolderModule<HeaderModel> headerItem() {
        return new HolderModule<HeaderModel>() {
            @NotNull @Override
            public HolderModuleConfig<HeaderModel> provideModuleConfig(@NotNull HolderModuleConfig.Builder<HeaderModel> builder) {
                return builder
                        .withLayoutResource(R.layout.header_model)
                        .build();
            }

            @Override
            public void onBind(@NotNull HeaderModel model, @NotNull ViewBinder viewBinder) {
                TextView headerTitle = viewBinder.findViewById(R.id.header_title);
                SwitchCompat headerSwitch = viewBinder.findViewById(R.id.header_switch);

                headerTitle.setText(model.name);
                headerSwitch.setChecked(model.checked);
                headerSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> viewModel.headerCheckedChanged(model , isChecked));
            }
        };
    }

    @NotNull
    private HolderModule<MessageModel> messageItem() {
        return new HolderModule<MessageModel>() {
            @Override @NotNull
            public HolderModuleConfig<MessageModel> provideModuleConfig(@NotNull HolderModuleConfig.Builder<MessageModel> builder) {
                return builder
                        .withLayoutResource(R.layout.message_model)
                        .enableSelection()
                        .build();
            }

            @Override
            public void onBind(@NotNull MessageModel model, @NotNull ViewBinder viewBinder) {
                TextView title = viewBinder.findViewById(R.id.title);
                TextView body  = viewBinder.findViewById(R.id.body);
                ImageView image = viewBinder.findViewById(R.id.image);
                ImageView selectedLayer = viewBinder.findViewById(R.id.selected_layer);

                title.setText(model.title);
                body.setText(model.body);
                Glide.with(AdvancedExampleActivity.this).load(model.imageId).into(image);

                // selected UI
                image.setAlpha(model.isSelected ? 0.5f : 1f);
                selectedLayer.setVisibility(model.isSelected? View.VISIBLE : View.GONE);
                viewBinder.getRootView().setBackgroundColor(model.isSelected ? ContextCompat.getColor(AdvancedExampleActivity.this, R.color.light_gray) : Color.TRANSPARENT);
            }

            @Override
            public void onSelected(@NotNull MessageModel model, boolean selected) {
                model.isSelected = selected;
            }
        };
    }

    @NotNull
    private EmptyStateModule emptyStateModule() {
        return new EmptyStateModule() {
            @Override @NotNull
            public EmptyStateModuleConfig provideModuleConfig(@NotNull EmptyStateModuleConfig.Builder builder) {
                return builder
                        .withLayoutResource(R.layout.empty_state)
                        .build();
            }

            @Override
            public void onBind(@NotNull ViewBinder viewBinder) {
                LottieAnimationView animation = viewBinder.findViewById(R.id.animation_view);
                animation.setAnimation(R.raw.empty_list);
                animation.playAnimation();
            }

            @Override
            public void onUnbind(@NotNull ViewBinder viewBinder) {
                LottieAnimationView animation = viewBinder.findViewById(R.id.animation_view);
                animation.pauseAnimation();
            }
        };
    }

    @NotNull
    private LoadMoreModule loadMoreModule() {
        return new LoadMoreModule() {
            @Override @NotNull
            public LoadMoreModuleConfig provideModuleConfig(@NotNull LoadMoreModuleConfig.Builder builder) {
                return builder
                        .withLayoutResource(R.layout.load_more)
                        .withVisibleThreshold(3)
                        .build();
            }

            @Override
            public void onLoadMore(int currentPage) {
                viewModel.loadMore();
            }
        };
    }

    @NotNull
    private SelectionStateModule selectionStateModule() {
        return new SelectionStateModule() {
            @Override @NotNull
            public SelectionModuleConfig provideModuleConfig(@NotNull SelectionModuleConfig.Builder builder) {
                return builder
                        .withSelectionType(SelectionType.Multiple)
                        .build();
            }

            @Override
            public void onSelectionUpdated(int selectedCount) {
                if (selectedCount == 0) {
                    setToolbarText(getString(R.string.app_name));
                    toolbarMenu.findItem(R.id.action_delete).setVisible(false);
                } else {
                    setToolbarText(selectedCount + " selected");
                    toolbarMenu.findItem(R.id.action_delete).setVisible(true);
                }
            }
        };
    }

    private void setToolbarText(String text) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(text);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        toolbarMenu = menu;
        getMenuInflater().inflate(R.menu.menu_main, toolbarMenu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            viewModel.onDeleteItemsClicked(oneAdapter.getSelectedItems());
            oneAdapter.clearSelection();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}