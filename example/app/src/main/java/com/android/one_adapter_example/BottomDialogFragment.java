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

    private Presenter presenter;

    public static BottomDialogFragment getInstance() {
        return new BottomDialogFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = ViewModelProviders.of(getActivity()).get(Presenter.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.bottom_dialog_fragment, container, false);

        rootView.findViewById(R.id.add_one).setOnClickListener(view -> {
            presenter.addOne();
            dismiss();
        });

        rootView.findViewById(R.id.clear).setOnClickListener(view -> {
            presenter.clearAll();
            dismiss();
        });

        rootView.findViewById(R.id.set_all).setOnClickListener(view -> {
            presenter.setAll();
            dismiss();
        });

        rootView.findViewById(R.id.set_one_item).setOnClickListener(view -> {
            presenter.setOne();
            dismiss();
        });

        rootView.findViewById(R.id.delete_one_item).setOnClickListener(view -> {
//            presenter.removeItem();
//            dismiss();
        });

        rootView.findViewById(R.id.delete_one_index).setOnClickListener(view -> {
//            presenter.removeIndex();
//            dismiss();
        });

        rootView.findViewById(R.id.cut).setOnClickListener(view -> {
            presenter.cutItems();
            dismiss();
        });

        return rootView;
    }
}