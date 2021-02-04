package com.msaproject.doctor.ui.specializations_selection;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msaproject.doctor.base.ItemClickListener;
import com.msaproject.doctor.databinding.ItemSpecializationSelectionBinding;
import com.msaproject.doctor.model.SpecializationModel;

import java.util.List;

public class SpecializationsAdapter extends RecyclerView.Adapter<SpecializationsAdapter.ViewHolder> {

    private String selectedId;
    private List<SpecializationModel> specializationModels;
    private final ItemClickListener<SpecializationModel> onSpecializationSelected;

    public SpecializationsAdapter(String selectedId, List<SpecializationModel> specializationModels, ItemClickListener<SpecializationModel> onSpecializationSelected) {
        this.selectedId = selectedId;
        this.specializationModels = specializationModels;
        this.onSpecializationSelected = onSpecializationSelected;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemSpecializationSelectionBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(specializationModels.get(position));
    }

    @Override
    public int getItemCount() {
        return specializationModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemSpecializationSelectionBinding viewBinding;

        public ViewHolder(ItemSpecializationSelectionBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        public void bind(SpecializationModel model) {
            viewBinding.tvSpecialization.setText(model.getName());

            if (selectedId != null && selectedId.equals(model.getId())) {
                viewBinding.tvSpecialization.setTypeface(null, Typeface.BOLD);
                viewBinding.ivIsSelected.setVisibility(View.VISIBLE);
            } else {
                viewBinding.tvSpecialization.setTypeface(null, Typeface.NORMAL);
                viewBinding.ivIsSelected.setVisibility(View.INVISIBLE);
            }

            itemView.setOnClickListener(v -> {
                selectedId = model.getId();
                onSpecializationSelected.onItemClicked(model);
            });
        }
    }
}