package com.msaproject.doctor.ui.specializations_selection;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.msaproject.doctor.R;
import com.msaproject.doctor.base.ItemClickListener;
import com.msaproject.doctor.databinding.DialogSelectSpecializationBinding;
import com.msaproject.doctor.model.SpecializationModel;

import java.util.List;

public class SelectSpecializationDialog extends BottomSheetDialogFragment {

    private DialogSelectSpecializationBinding viewBinding;
    private final Context context;
    private final List<SpecializationModel> specializationModels;
    private String selectedId;
    @Nullable
    private final ItemClickListener<SpecializationModel> itemClickListener;

    public SelectSpecializationDialog(Context context, List<SpecializationModel> specializationModels, String selectedId, @Nullable ItemClickListener<SpecializationModel> itemClickListener) {
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        this.context = context;
        this.specializationModels = specializationModels;
        this.selectedId = selectedId;
        this.itemClickListener = itemClickListener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBinding = DialogSelectSpecializationBinding.inflate(LayoutInflater.from(context), null, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvBottomSheet = viewBinding.rvBottomSheet;

        SpecializationsAdapter specializationsAdapter = new SpecializationsAdapter(selectedId, specializationModels, specializationModel -> {
            selectedId = specializationModel.getId();
            if (itemClickListener != null)
                itemClickListener.onItemClicked(specializationModel);
            dismiss();
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);

        rvBottomSheet.setLayoutManager(linearLayoutManager);
        rvBottomSheet.setAdapter(specializationsAdapter);
        rvBottomSheet.addItemDecoration(new DividerItemDecoration(context,
                DividerItemDecoration.VERTICAL));
    }

    @Nullable
    public String getSelectedId() {
        return selectedId;
    }

}