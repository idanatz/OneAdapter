package com.idanatz.sample.examples;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.idanatz.oneadapter.OneAdapter;
import com.idanatz.oneadapter.external.event_hooks.ClickEventHook;
import com.idanatz.oneadapter.external.modules.EmptinessModule;
import com.idanatz.oneadapter.external.modules.ItemModule;
import com.idanatz.oneadapter.sample.R;
import com.idanatz.sample.models.MessageModel;

import androidx.annotation.Nullable;

class BasicJavaExampleActivity
		extends BaseExampleActivity {

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		OneAdapter oneAdapter = new OneAdapter(recyclerView)
				.attachItemModule(new MessageItem())
                .attachEmptinessModule(new EmptinessModuleImpl());

		oneAdapter.setItems(getModelGenerator().generateMessages(10));
	}

	private static class MessageItem extends ItemModule<MessageModel> {

		public MessageItem() {
			config(builder -> {
				builder.setLayoutResource(R.layout.message_model);
				return null;
			});
			onBind((model, viewBinder, metadata) -> {
				TextView title = viewBinder.findViewById(R.id.title);
				TextView body = viewBinder.findViewById(R.id.body);
				ImageView image = viewBinder.findViewById(R.id.avatarImage);

				title.setText(model.title);
				body.setText(model.body);
				Glide.with(viewBinder.getRootView())
				     .load(model.avatarImageId)
				     .into(image);
				return null;
			});
			eventHooks.add(new ClickEventHook<MessageModel>() {{
				onClick((model, viewBinder, metadata) -> {
					Toast.makeText(viewBinder.getRootView().getContext(), model.title + " clicked", Toast.LENGTH_SHORT).show();
					return null;
				});
			}});
		}
	}

	private static class EmptinessModuleImpl extends EmptinessModule {

		public EmptinessModuleImpl() {
			config(builder -> {
				builder.setLayoutResource(R.layout.empty_state);
				return null;
			});
			onBind((viewBinder, metadata) -> {
				LottieAnimationView animation = viewBinder.findViewById(R.id.animation_view);
				animation.setAnimation(R.raw.empty_list);
				animation.playAnimation();
				return null;
			});
			onUnbind((viewBinder, metadata) -> {
				LottieAnimationView animation = viewBinder.findViewById(R.id.animation_view);
				animation.pauseAnimation();
				return null;
			});
		}
	}
}