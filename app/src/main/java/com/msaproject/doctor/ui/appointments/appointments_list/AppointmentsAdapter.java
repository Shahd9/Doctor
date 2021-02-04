package com.msaproject.doctor.ui.appointments.appointments_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msaproject.doctor.base.BaseRecyclerAdapter;
import com.msaproject.doctor.base.ItemClickListener;
import com.msaproject.doctor.databinding.ItemAppointmentBinding;
import com.msaproject.doctor.model.AppointmentModel;
import com.msaproject.doctor.model.UserModel;
import com.msaproject.doctor.utils.PicassoHelper;
import com.msaproject.doctor.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.ViewHolder>
        implements BaseRecyclerAdapter<AppointmentModel> {

    private final UserModel patientModel;
    private final ItemClickListener<AppointmentModel> itemClickListener;

    @NonNull
    private final ArrayList<AppointmentModel> list;

    public AppointmentsAdapter(UserModel patientModel, ItemClickListener<AppointmentModel> itemClickListener) {
        list = new ArrayList<>();
        this.patientModel = patientModel;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemAppointmentBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void clear(boolean notifyDataSetChanged) {
        list.clear();
        if (notifyDataSetChanged)
            notifyDataSetChanged();
    }

    @Override
    public void add(AppointmentModel item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<AppointmentModel> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemAppointmentBinding viewBinding;

        ViewHolder(ItemAppointmentBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }

        private void bind(int position) {
            AppointmentModel model = list.get(position);

            viewBinding.getRoot().setOnClickListener(v -> itemClickListener.onItemClicked(model));

            PicassoHelper.loadImageWithCache(patientModel.getProfilePicLink(), viewBinding.ivPatientDoctorPicture, PicassoHelper.MODE.FIT_AND_CENTER_CROP, null, null, null);

            viewBinding.tvAppointmentTitle.setText(model.getTitle());

            viewBinding.tvDate.setText(
                    StringUtils.formatDateToLocale(model.getCreatedAt(), "E dd/MM/yyyy", new Locale(StringUtils.getLanguage())));

            viewBinding.tvLastUpdated.setText(
                    StringUtils.formatDateToLocale(model.getUpdatedAt(), "E dd/MM/yyyy", new Locale(StringUtils.getLanguage())));
        }
    }
}