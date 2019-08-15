package com.idanatz.sample.examples.complete.view;

import org.jetbrains.annotations.NotNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.idanatz.oneadapter.external.events.SwipeEventHook;
import com.idanatz.sample.examples.BaseExampleActivity;
import com.idanatz.sample.models.HeaderModel;
import com.idanatz.sample.models.MessageModel;
import com.idanatz.sample.examples.complete.view_model.CompleteExampleViewModel;
import com.idanatz.sample.models.StoriesModel;
import com.idanatz.oneadapter.OneAdapter;
import com.idanatz.oneadapter.external.events.ClickEventHook;
import com.idanatz.oneadapter.external.modules.EmptinessModuleConfig;
import com.idanatz.oneadapter.external.modules.ItemModuleConfig;
import com.idanatz.oneadapter.external.modules.ItemSelectionActions;
import com.idanatz.oneadapter.external.modules.PagingModuleConfig;
import com.idanatz.oneadapter.external.modules.ItemSelectionModuleConfig;
import com.idanatz.oneadapter.external.modules.EmptinessModule;
import com.idanatz.oneadapter.external.modules.PagingModule;
import com.idanatz.oneadapter.external.modules.ItemModule;
import com.idanatz.oneadapter.internal.holders.ViewBinder;
import com.idanatz.oneadapter.external.modules.ItemSelectionModule;
import com.idanatz.oneadapter.external.states.SelectionState;
import com.idanatz.oneadapter.sample.R;
import com.bumptech.glide.Glide;

import static com.idanatz.sample.examples.ActionsDialog.*;

public class CompleteJavaExampleActivity extends BaseExampleActivity {

    private static final int ICON_MARGIN = 50;

    private CompleteExampleViewModel viewModel;
    private OneAdapter oneAdapter;
    private CompositeDisposable compositeDisposable;
    private Menu toolbarMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        viewModel = ViewModelProviders.of(this).get(CompleteExampleViewModel.class);

        oneAdapter = new OneAdapter()
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
                .attachTo(recyclerView);

        initActionsDialog(Action.values()).setListener(viewModel);

        observeViewModel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    private void observeViewModel() {
        compositeDisposable.add(
                viewModel.getItemsSubject()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(items -> oneAdapter.setItems(items))
        );
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

                Glide.with(CompleteJavaExampleActivity.this).load(model.storyImageId1).into(story1);
                Glide.with(CompleteJavaExampleActivity.this).load(model.storyImageId2).into(story2);
                Glide.with(CompleteJavaExampleActivity.this).load(model.storyImageId3).into(story3);
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
                headerSwitch.setVisibility(model.checkable ? View.VISIBLE : View.GONE);
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
                TextView id = viewBinder.findViewById(R.id.id);
                TextView title = viewBinder.findViewById(R.id.title);
                TextView body  = viewBinder.findViewById(R.id.body);
                ImageView avatarImage = viewBinder.findViewById(R.id.avatarImage);
                ImageView selectedLayer = viewBinder.findViewById(R.id.selected_layer);

                id.setText(String.format(getString(R.string.message_model_id), model.id));
                title.setText(model.title);
                body.setText(model.body);
                Glide.with(CompleteJavaExampleActivity.this).load(model.avatarImageId).into(avatarImage);

                // selected UI
                avatarImage.setAlpha(model.isSelected ? 0.5f : 1f);
                selectedLayer.setVisibility(model.isSelected? View.VISIBLE : View.GONE);
                viewBinder.getRootView().setBackgroundColor(model.isSelected ? ContextCompat.getColor(CompleteJavaExampleActivity.this, R.color.light_gray) : Color.WHITE);
            }
        };
    }

    @NotNull
    private SelectionState<MessageModel> selectionState() {
        return new SelectionState<MessageModel>() {
            @Override
            public boolean selectionEnabled(@NotNull MessageModel model) {
                return true;
            }

            @Override
            public void onSelected(@NotNull MessageModel model, boolean selected) {
                model.isSelected = selected;
            }
        };
    }

    @NotNull
    private ClickEventHook<MessageModel> clickEventHook() {
        return new ClickEventHook<MessageModel>() {
            @Override
            public void onClick(@NotNull MessageModel model, @NotNull ViewBinder viewBinder) {
                Toast.makeText(CompleteJavaExampleActivity.this, model.title + " clicked", Toast.LENGTH_SHORT).show();
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
                viewModel.onLoadMore();
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

    private SwipeEventHook<MessageModel> swipeEventHook()  {
        return new SwipeEventHook<MessageModel>() {
            @Override
            public void onSwipe(@NotNull Canvas canvas, float xAxisOffset, @NotNull ViewBinder viewBinder) {
                if (xAxisOffset < 0) {
                    paintSwipeLeft(canvas, xAxisOffset, viewBinder.getRootView());
                } else if (xAxisOffset > 0) {
                    paintSwipeRight(canvas, xAxisOffset, viewBinder.getRootView());
                }
            }

            @Override
            public void onSwipeComplete(@NotNull MessageModel model, @NotNull SwipeDirection direction, @NotNull ViewBinder viewBinder) {
                switch (direction) {
                    case Right:
                        Toast.makeText(CompleteJavaExampleActivity.this, model.title + " snoozed", Toast.LENGTH_SHORT).show();
                        oneAdapter.update(model);
                        break;
                    case Left:
                        viewModel.onSwipeToDeleteItem(model);
                        break;
                }
            }
        };
    }

    private void paintSwipeRight(Canvas canvas, float xAxisOffset, View rootView) {
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.ic_snooze_white_24dp);
        ColorDrawable colorDrawable = new ColorDrawable(Color.DKGRAY);

        if (icon != null) {
            int middle = rootView.getBottom() - rootView.getTop();
            int top = rootView.getTop();
            int bottom = rootView.getBottom();
            int right = rootView.getLeft() + (int)xAxisOffset;
            int left = rootView.getLeft();
            colorDrawable.setBounds(left, top, right, bottom);
            colorDrawable.draw(canvas);

            top = rootView.getTop() + (middle / 2) - (icon.getIntrinsicHeight() / 2);
            bottom = top + icon.getIntrinsicHeight();
            left = rootView.getLeft() + ICON_MARGIN;
            right = left + icon.getIntrinsicWidth();
            icon.setBounds(left, top, right, bottom);
            icon.draw(canvas);
        }
    }

    private void paintSwipeLeft(Canvas canvas, float xAxisOffset, View rootView) {
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.ic_delete_white_24dp);
        ColorDrawable colorDrawable = new ColorDrawable(Color.RED);

        if (icon != null) {
            int middle = rootView.getBottom() - rootView.getTop();
            int top = rootView.getTop();
            int bottom = rootView.getBottom();
            int right = rootView.getRight();
            int left = rootView.getRight() + (int)xAxisOffset;
            colorDrawable.setBounds(left, top, right, bottom);
            colorDrawable.draw(canvas);

            top = rootView.getTop() + (middle / 2) - (icon.getIntrinsicHeight() / 2);
            bottom = top + icon.getIntrinsicHeight();
            right = rootView.getRight() - ICON_MARGIN;
            left = right - icon.getIntrinsicWidth();
            icon.setBounds(left, top, right, bottom);
            icon.draw(canvas);
        }
    }
}