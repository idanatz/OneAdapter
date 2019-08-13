package com.idanatz.sample.examples.advanced_example.view;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.idanatz.sample.examples.advanced_example.view_model.AdvancedExampleViewModel;
import com.idanatz.oneadapter.sample.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class AdvancedActionsDialog extends BottomSheetDialogFragment {

    private AdvancedExampleViewModel viewModel;

    public static AdvancedActionsDialog newInstance() {
        return new AdvancedActionsDialog();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(AdvancedExampleViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.bottom_dialog_fragment, container, false);

        rootView.findViewById(R.id.add_item).setOnClickListener(view -> {
            String id = ((EditText)rootView.findViewById(R.id.add_item_edit_text)).getText().toString();
            if (!id.isEmpty()) {
                viewModel.onAddItemClicked(Integer.parseInt(id));
            }
            dismiss();
        });

        rootView.findViewById(R.id.clear).setOnClickListener(view -> {
            viewModel.onClearAllClicked();
            dismiss();
        });

        rootView.findViewById(R.id.set_all).setOnClickListener(view -> {
            viewModel.onSetAllClicked();
            dismiss();
        });

        rootView.findViewById(R.id.update_item).setOnClickListener(view -> {
            String id = ((EditText)rootView.findViewById(R.id.update_item_edit_text)).getText().toString();
            if (!id.isEmpty()) {
                viewModel.onUpdatedItemClicked(Integer.parseInt(id));
            }
            dismiss();
        });

        rootView.findViewById(R.id.delete_item).setOnClickListener(view -> {
            String id = ((EditText)rootView.findViewById(R.id.delete_item_edit_text)).getText().toString();
            if (!id.isEmpty()) {
                viewModel.onDeleteItemClicked(Integer.parseInt(id));
            }
            dismiss();
        });

        rootView.findViewById(R.id.delete_index).setOnClickListener(view -> {
            String id = ((EditText)rootView.findViewById(R.id.delete_index_edit_text)).getText().toString();
            if (!id.isEmpty()) {
                viewModel.onDeleteIndexClicked(Integer.parseInt(id));
            }
            dismiss();
        });

        rootView.findViewById(R.id.large_diff).setOnClickListener(view -> {
            viewModel.onLargeDiffClicked();
            dismiss();
        });

        return rootView;
    }
}