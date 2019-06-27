package com.android.one_adapter_example.advanced_example.view;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.one_adapter_example.R;
import com.android.one_adapter_example.models.HeaderModel;
import com.android.one_adapter_example.models.MessageModel;
import com.android.one_adapter_example.advanced_example.view_model.AdvancedExampleViewModel;
import com.android.one_adapter_example.models.StoriesModel;
import com.android.oneadapter.OneAdapter;
import com.android.oneadapter.external.events.ClickEventHook;
import com.android.oneadapter.external.modules.EmptinessModuleConfig;
import com.android.oneadapter.external.modules.ItemModuleConfig;
import com.android.oneadapter.external.modules.ItemSelectionActions;
import com.android.oneadapter.external.modules.PagingModuleConfig;
import com.android.oneadapter.external.modules.ItemSelectionModuleConfig;
import com.android.oneadapter.external.modules.EmptinessModule;
import com.android.oneadapter.external.modules.PagingModule;
import com.android.oneadapter.external.modules.ItemModule;
import com.android.oneadapter.internal.holders.ViewBinder;
import com.android.oneadapter.external.modules.ItemSelectionModule;
import com.android.oneadapter.external.states.SelectionState;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

public class AdvancedJavaExampleActivity extends AppCompatActivity {

    private AdvancedExampleViewModel viewModel;
    private OneAdapter oneAdapter;
    private CompositeDisposable compositeDisposable;
    private Menu toolbarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);
        compositeDisposable = new CompositeDisposable();
        viewModel = ViewModelProviders.of(this).get(AdvancedExampleViewModel.class);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        oneAdapter = new OneAdapter()
                .attachItemModule(storyItem())
                .attachItemModule(headerItem())
                .attachItemModule(messageItem().addState(selectionState()).addEventHook(clickEventHook()))
                .attachEmptinessModule(emptinessModule())
                .attachPagingModule(pagingModule())
                .attachItemSelectionModule(itemSelectionModule())
                .attachTo(recyclerView);

        Button optionsButton = findViewById(R.id.show_options_button);
        optionsButton.setOnClickListener(v -> AdvancedActionsDialog.getInstance().show(getSupportFragmentManager(), AdvancedActionsDialog.class.getSimpleName()));

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
    private ItemModule<StoriesModel> storyItem() {
        return new ItemModule<StoriesModel>() {
            @NotNull @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() { return R.layout.stories_model; }
                };
            }

            @Override
            public void onBind(@NotNull StoriesModel model, @NotNull ViewBinder viewBinder) {
                ImageView story1 = viewBinder.findViewById(R.id.story1);
                ImageView story2 = viewBinder.findViewById(R.id.story2);
                ImageView story3 = viewBinder.findViewById(R.id.story3);

                Glide.with(AdvancedJavaExampleActivity.this).load(model.storyImageId1).into(story1);
                Glide.with(AdvancedJavaExampleActivity.this).load(model.storyImageId2).into(story2);
                Glide.with(AdvancedJavaExampleActivity.this).load(model.storyImageId3).into(story3);
            }
        };
    }

    @NotNull
    private ItemModule<HeaderModel> headerItem() {
        return new ItemModule<HeaderModel>() {
            @NotNull @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() { return R.layout.header_model; }
                };
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
    private ItemModule<MessageModel> messageItem() {
        return new ItemModule<MessageModel>() {
            @NotNull @Override
            public ItemModuleConfig provideModuleConfig() {
                return new ItemModuleConfig() {
                    @Override
                    public int withLayoutResource() { return R.layout.message_model; }
                };
            }

            @Override
            public void onBind(@NotNull MessageModel model, @NotNull ViewBinder viewBinder) {
                TextView title = viewBinder.findViewById(R.id.title);
                TextView body  = viewBinder.findViewById(R.id.body);
                ImageView avatarImage = viewBinder.findViewById(R.id.avatarImage);
                ImageView selectedLayer = viewBinder.findViewById(R.id.selected_layer);

                title.setText(model.title);
                body.setText(model.body);
                Glide.with(AdvancedJavaExampleActivity.this).load(model.avatarImageId).into(avatarImage);

                // selected UI
                avatarImage.setAlpha(model.isSelected ? 0.5f : 1f);
                selectedLayer.setVisibility(model.isSelected? View.VISIBLE : View.GONE);
                viewBinder.getRootView().setBackgroundColor(model.isSelected ? ContextCompat.getColor(AdvancedJavaExampleActivity.this, R.color.light_gray) : Color.TRANSPARENT);
            }
        };
    }

    @NotNull
    private SelectionState<MessageModel> selectionState() {
        return new SelectionState<MessageModel>() {
            @Override
            public boolean selectionEnabled(@NonNull MessageModel model) {
                return true;
            }

            @Override
            public void onSelected(@NonNull MessageModel model, boolean selected) {
                model.isSelected = selected;
            }
        };
    }

    @NotNull
    private ClickEventHook<MessageModel> clickEventHook() {
        return new ClickEventHook<MessageModel>() {
            @Override
            public void onClick(@NonNull MessageModel model, @NonNull ViewBinder viewBinder) {
                Toast.makeText(AdvancedJavaExampleActivity.this, model.title + " clicked", Toast.LENGTH_SHORT).show();
            }
        };
    }

    @NotNull
    private EmptinessModule emptinessModule() {
        return new EmptinessModule() {
            @NotNull @Override
            public EmptinessModuleConfig provideModuleConfig() {
                return new EmptinessModuleConfig() {
                    @Override
                    public int withLayoutResource() { return R.layout.empty_state; }
                };
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
    private PagingModule pagingModule() {
        return new PagingModule() {
            @NotNull @Override
            public PagingModuleConfig provideModuleConfig() {
                return new PagingModuleConfig() {
                    @Override
                    public int withLayoutResource() { return R.layout.load_more; }

                    @Override
                    public int withVisibleThreshold() { return 3; }
                };
            }

            @Override
            public void onLoadMore(int currentPage) {
                viewModel.loadMore();
            }
        };
    }

    @NotNull
    private ItemSelectionModule itemSelectionModule() {
        return new ItemSelectionModule() {
            @NotNull @Override
            public ItemSelectionModuleConfig provideModuleConfig() {
                return new ItemSelectionModuleConfig() {
                    @NotNull @Override
                    public SelectionType withSelectionType() { return SelectionType.Multiple; }
                };
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
            ItemSelectionActions itemSelectionActions = oneAdapter.getItemSelectionActions();
            if (itemSelectionActions != null) {
                viewModel.onDeleteItemsClicked(itemSelectionActions.getSelectedItems());
                itemSelectionActions.clearSelection();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}