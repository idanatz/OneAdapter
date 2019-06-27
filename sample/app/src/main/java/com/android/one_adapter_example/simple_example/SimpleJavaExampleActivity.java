package com.android.one_adapter_example.simple_example;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.one_adapter_example.R;
import com.android.one_adapter_example.models.MessageModel;
import com.android.one_adapter_example.models.ModelGenerator;
import com.android.oneadapter.OneAdapter;
import com.android.oneadapter.external.modules.ItemModule;
import com.android.oneadapter.external.modules.ItemModuleConfig;
import com.android.oneadapter.internal.holders.ViewBinder;
import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimpleJavaExampleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        OneAdapter oneAdapter = new OneAdapter();
        oneAdapter
                .attachItemModule(messageItem())
                .attachTo(recyclerView);

        ModelGenerator modelGenerator = new ModelGenerator();
        List<MessageModel> items = modelGenerator.generateFirstModels();
        oneAdapter.setItems(items);
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
                ImageView image = viewBinder.findViewById(R.id.avatarImage);

                title.setText(model.title);
                body.setText(model.body);
                Glide.with(SimpleJavaExampleActivity.this).load(model.avatarImageId).into(image);
            }
        };
    }
}