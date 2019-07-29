package com.android.one_adapter_example.simple_example;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.one_adapter_example.R;
import com.android.one_adapter_example.advanced_example.view.AdvancedActionsDialog;
import com.android.one_adapter_example.models.MessageModel;
import com.android.one_adapter_example.models.ModelGenerator;
import com.android.oneadapter.OneAdapter;
import com.android.oneadapter.external.modules.ItemModule;
import com.android.oneadapter.external.modules.ItemModuleConfig;
import com.android.oneadapter.internal.holders.ViewBinder;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SimpleJavaExampleActivity extends AppCompatActivity implements SimpleActionsDialog.ActionsListener {

    private RecyclerView recyclerView;
    private OneAdapter oneAdapter;
    private List<MessageModel> items = new ArrayList<>();
    private ModelGenerator modelGenerator = new ModelGenerator();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        initViews();

        oneAdapter = new OneAdapter()
                .attachItemModule(messageItem())
                .attachTo(recyclerView);

        onSetAllClicked();
    }

    private void initViews() {
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        SimpleActionsDialog actionsDialog = SimpleActionsDialog.getInstance();
        actionsDialog.setListener(this);
        Button actionButton = findViewById(R.id.show_options_button);
        actionButton.setVisibility(View.VISIBLE);
        actionButton.setOnClickListener(v -> actionsDialog.show(getSupportFragmentManager(), SimpleActionsDialog.class.getSimpleName()));
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
                ImageView image = viewBinder.findViewById(R.id.avatarImage);

                id.setText(String.format(getString(R.string.message_model_id), model.id));
                title.setText(model.title);
                body.setText(model.body);
                Glide.with(SimpleJavaExampleActivity.this).load(model.avatarImageId).into(image);
            }
        };
    }

    @Override
    public void onSetAllClicked() {
        items.addAll(modelGenerator.generateFirstModels());
        oneAdapter.setItems(items);
    }

    @Override
    public void onClearAllClicked() {
        items.clear();
        oneAdapter.clear();
    }

    @Override
    public void onAddItemClicked(int id) {
        MessageModel newItem = modelGenerator.generateMessage(id);
        items.add(newItem);
        oneAdapter.add(newItem); // will add new item to the end of the list
    }

    @Override
    public void onUpdatedItemClicked(int id) {
        MessageModel itemToUpdate = getMessageModelWithId(id);
        if (itemToUpdate != null) {
            MessageModel updatedItem = modelGenerator.generateUpdatedMessage(itemToUpdate.id, itemToUpdate.headerId);
            replaceMessageModel(id, updatedItem);
            oneAdapter.update(updatedItem);
        }
    }

    @Override
    public void onDeleteItemClicked(int id) {
        MessageModel itemToDelete = getMessageModelWithId(id);
        if (itemToDelete != null) {
            oneAdapter.remove(itemToDelete);
        }
    }

    @Override
    public void onDeleteIndexClicked(int index) {
        items.remove(index);
        oneAdapter.remove(index);
    }

    @Override
    public void largeDiff() {
        for (MessageModel item : new ArrayList<>(items)) {
            if (item.id % 2 == 0) {
                items.remove(item);
            }
        }
        oneAdapter.setItems(items);
    }

    @Nullable
    private MessageModel getMessageModelWithId(int id) {
        for (MessageModel item : items) {
            if (item.id == id) {
                return item;
            }
        }
        return null;
    }

    private int getMessageModelIndex(int id) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).id == id) {
                return i;
            }
        }
        return -1;
    }

    private void replaceMessageModel(int originalModelId, MessageModel newModel) {
        int indexToReplace = getMessageModelIndex(originalModelId);
        items.remove(indexToReplace);
        items.add(indexToReplace, newModel);
    }
}