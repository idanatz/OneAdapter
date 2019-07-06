package com.android.one_adapter_example.simple_example;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.android.one_adapter_example.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.Nullable;

public class SimpleActionsDialog extends BottomSheetDialogFragment {

    private ActionsListener listener;

    public static SimpleActionsDialog getInstance() {
        return new SimpleActionsDialog();
    }

    public void setListener(ActionsListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.bottom_dialog_fragment, container, false);

        rootView.findViewById(R.id.add_item).setOnClickListener(view -> {
            String id = ((EditText)rootView.findViewById(R.id.add_item_edit_text)).getText().toString();
            if (!id.isEmpty()) {
                listener.onAddItemClicked(Integer.parseInt(id));
            }
            dismiss();
        });

        rootView.findViewById(R.id.clear).setOnClickListener(view -> {
            listener.onClearAllClicked();
            dismiss();
        });

        rootView.findViewById(R.id.set_all).setOnClickListener(view -> {
            listener.onSetAllClicked();
            dismiss();
        });

        rootView.findViewById(R.id.update_item).setOnClickListener(view -> {
            String id = ((EditText)rootView.findViewById(R.id.update_item_edit_text)).getText().toString();
            if (!id.isEmpty()) {
                listener.onUpdatedItemClicked(Integer.parseInt(id));
            }
            dismiss();
        });

        rootView.findViewById(R.id.delete_item).setOnClickListener(view -> {
            String id = ((EditText)rootView.findViewById(R.id.delete_item_edit_text)).getText().toString();
            if (!id.isEmpty()) {
                listener.onDeleteItemClicked(Integer.parseInt(id));
            }
            dismiss();
        });

        rootView.findViewById(R.id.delete_index).setOnClickListener(view -> {
            String id = ((EditText)rootView.findViewById(R.id.delete_index_edit_text)).getText().toString();
            if (!id.isEmpty()) {
                listener.onDeleteIndexClicked(Integer.parseInt(id));
            }
            dismiss();
        });

        rootView.findViewById(R.id.large_diff).setOnClickListener(view -> {
            listener.largeDiff();
            dismiss();
        });

        return rootView;
    }

    public interface ActionsListener {

        void onAddItemClicked(int id);

        void onClearAllClicked();

        void onSetAllClicked();

        void onUpdatedItemClicked(int id);

        void onDeleteItemClicked(int id);

        void onDeleteIndexClicked(int index);

        void largeDiff();
    }
}