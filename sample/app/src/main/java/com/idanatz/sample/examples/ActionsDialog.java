package com.idanatz.sample.examples;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.idanatz.oneadapter.sample.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import androidx.annotation.Nullable;

public class ActionsDialog extends BottomSheetDialogFragment {

    private Set<Action> actions = new HashSet<>();
    private ActionsListener listener;

    public static ActionsDialog getInstance() {
        return new ActionsDialog();
    }

    public void setActions(Action... actions) {
        this.actions.addAll(Arrays.asList(actions));
    }

    public void setListener(ActionsListener listener) {
        this.listener = listener;
    }

    @Nullable @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.bottom_dialog_fragment, container, false);

        if (actions.contains(Action.AddItem)) {
            View addView = rootView.findViewById(R.id.add_item);
            EditText editText = rootView.findViewById(R.id.add_item_edit_text);
            addView.setOnClickListener(view -> {
                String id = editText.getText().toString();
                if (!id.isEmpty()) {
                    listener.onAddItemClicked(Integer.parseInt(id));
                }
                dismiss();
            });
            addView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
        }

        if (actions.contains(Action.ClearAll)) {
            View clearView = rootView.findViewById(R.id.clear);
            clearView.setOnClickListener(view -> {
                listener.onClearAllClicked();
                dismiss();
            });
            clearView.setVisibility(View.VISIBLE);
        }

        if (actions.contains(Action.SetAll)) {
            View setView = rootView.findViewById(R.id.set_all);
            setView.setOnClickListener(view -> {
                listener.onSetAllClicked();
                dismiss();
            });
            setView.setVisibility(View.VISIBLE);
        }

        if (actions.contains(Action.UpdateItem)) {
            View updateView = rootView.findViewById(R.id.update_item);
            EditText editText = rootView.findViewById(R.id.update_item_edit_text);
            updateView.setOnClickListener(view -> {
                String id = editText.getText().toString();
                if (!id.isEmpty()) {
                    listener.onUpdatedItemClicked(Integer.parseInt(id));
                }
                dismiss();
            });
            updateView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
        }

        if (actions.contains(Action.DeleteItem)) {
            View deleteItemView = rootView.findViewById(R.id.delete_item);
            EditText editText = rootView.findViewById(R.id.delete_item_edit_text);
            deleteItemView.setOnClickListener(view -> {
                String id = editText.getText().toString();
                if (!id.isEmpty()) {
                    listener.onDeleteItemClicked(Integer.parseInt(id));
                }
                dismiss();
            });
            deleteItemView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
        }

        if (actions.contains(Action.DeleteIndex)) {
            View deleteIndexView = rootView.findViewById(R.id.delete_index);
            EditText editText = rootView.findViewById(R.id.delete_index_edit_text);
            deleteIndexView.setOnClickListener(view -> {
                String id = editText.getText().toString();
                if (!id.isEmpty()) {
                    listener.onDeleteIndexClicked(Integer.parseInt(id));
                }
                dismiss();
            });
            deleteIndexView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.VISIBLE);
        }

        if (actions.contains(Action.LargeDiff)) {
            View largeDiffView = rootView.findViewById(R.id.large_diff);
            largeDiffView.setOnClickListener(view -> {
                listener.onLargeDiffClicked();
                dismiss();
            });
            largeDiffView.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    public interface ActionsListener {
        void onAddItemClicked(int id);
        void onClearAllClicked();
        void onSetAllClicked();
        void onUpdatedItemClicked(int id);
        void onDeleteItemClicked(int id);
        void onDeleteIndexClicked(int index);
        void onLargeDiffClicked();
    }

    public enum Action {
        AddItem, ClearAll, SetAll, UpdateItem, DeleteItem, DeleteIndex, LargeDiff
    }
}