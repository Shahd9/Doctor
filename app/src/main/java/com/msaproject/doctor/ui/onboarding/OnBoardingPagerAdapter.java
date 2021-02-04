package com.msaproject.doctor.ui.onboarding;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.msaproject.doctor.R;
import com.msaproject.doctor.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class OnBoardingPagerAdapter extends RecyclerView.Adapter<OnBoardingPagerAdapter.OnBoardingViewHolder> {

    private final Context context;
    private final List<TutorialPage> list;
    private final OnGetStartedClick listener;

    public OnBoardingPagerAdapter(Context context, OnGetStartedClick listener) {
        this.context = context;
        this.listener = listener;
        list = new ArrayList<>();

        list.add(new TutorialPage(R.drawable.pic_doctor, R.color.onboarding_tansparent1, StringUtils.getString(R.string.doctors), StringUtils.getString(R.string.find_the_best_doctor_msg)));
        list.add(new TutorialPage(R.drawable.pic_clinic_appointmnt, R.color.onboarding_tansparent2, StringUtils.getString(R.string.appointments), StringUtils.getString(R.string.monitor_appointment_data)));
        list.add(new TutorialPage(R.drawable.pic_medication, R.color.onboarding_tansparent3, StringUtils.getString(R.string.prescriptions), StringUtils.getString(R.string.never_lose_prescriptions)));
        list.add(new TutorialPage(R.drawable.pic_health_recommendation_system, R.color.onboarding_tansparent4, StringUtils.getString(R.string.hrs), StringUtils.getString(R.string.health_recommendation_system)));
    }

    @NonNull
    @Override
    public OnBoardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_onboarding, parent, false);
        return new OnBoardingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OnBoardingViewHolder holder, int position) {
        TutorialPage page = list.get(position);

        holder.constraint.setBackgroundColor(context.getResources().getColor(page.getColorId()));
        holder.image.setImageResource(page.getImageId());
        holder.textViewTitle.setText(page.getTitle());
        holder.descriptionContent.setText(page.getDescription());

        holder.btnGetStarted.setOnClickListener(view1 -> {
            if (this.listener != null)
                listener.onClick();
        });

        if(position == list.size()-1){
            holder.btnGetStarted.setVisibility(View.VISIBLE);
        }
        else {
            holder.btnGetStarted.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnGetStartedClick {
        void onClick();
    }

    public static class OnBoardingViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout constraint;
        ImageView image;
        TextView textViewTitle;
        TextView descriptionContent;
        Button btnGetStarted;

        public OnBoardingViewHolder(@NonNull View itemView) {
            super(itemView);
            constraint = itemView.findViewById(R.id.constrain_trans);
            image = itemView.findViewById(R.id.iv_background);
            textViewTitle = itemView.findViewById(R.id.tv_title);
            descriptionContent = itemView.findViewById(R.id.tv_desc);
            btnGetStarted = itemView.findViewById(R.id.btn_get_started);
        }
    }

    public static class TutorialPage{
        private final String title;
        private final int imageId;
        private final int colorId;
        private final String description;

        public TutorialPage(int imageId, int colorId, String title, String description) {
            this.title = title;
            this.imageId = imageId;
            this.colorId = colorId;
            this.description = description;
        }

        public String getTitle() {
            return title;
        }

        public int getImageId() {
            return imageId;
        }

        public String getDescription() {
            return description;
        }

        public int getColorId() {
            return colorId;
        }
    }
}
