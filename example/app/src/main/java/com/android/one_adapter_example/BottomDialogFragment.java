package com.android.one_adapter_example;

import androidx.lifecycle.ViewModelProviders;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.jetbrains.annotations.NotNull;

public class BottomDialogFragment extends BottomSheetDialogFragment {

    private AdvancedExampleViewModel viewModel;

    public static BottomDialogFragment getInstance() {
        return new BottomDialogFragment();
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

        rootView.findViewById(R.id.add_one).setOnClickListener(view -> {
            viewModel.addOne();
            dismiss();
        });

        rootView.findViewById(R.id.clear).setOnClickListener(view -> {
            viewModel.clearAll();
            dismiss();
        });

        rootView.findViewById(R.id.set_all).setOnClickListener(view -> {
            viewModel.setAll();
            dismiss();
        });

        rootView.findViewById(R.id.set_one_item).setOnClickListener(view -> {
            viewModel.setOne();
            dismiss();
        });

        rootView.findViewById(R.id.delete_one_item).setOnClickListener(view -> {
//            viewModel.removeItem();
//            dismiss();
        });

        rootView.findViewById(R.id.delete_one_index).setOnClickListener(view -> {
//            viewModel.removeIndex();
//            dismiss();
        });

        rootView.findViewById(R.id.cut).setOnClickListener(view -> {
            viewModel.cutItems();
            dismiss();
        });

        return rootView;
    }
}