package com.msaproject.doctor.ui.appointments.appointment_view_edit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseRecyclerAdapter;
import com.msaproject.doctor.base.ItemClickListener;
import com.msaproject.doctor.databinding.ItemAppointmentImageBinding;
import com.msaproject.doctor.model.AppointmentImageModel;
import com.msaproject.doctor.utils.PicassoHelper;
import com.msaproject.doctor.utils.UIUtils;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.ArrayList;
import java.util.List;

public class AppointmentImagesAdapter extends RecyclerView.Adapter<AppointmentImagesAdapter.ViewHolder>
        implements BaseRecyclerAdapter<AppointmentImageModel> {

    public final static int MODE_VIEW = 0;
    public final static int MODE_EDIT = 1;

    private final ItemClickListener<AppointmentImageModel> onRemoveClickListener;
    private final ArrayList<AppointmentImageModel> list;
    private final FragmentActivity context;
    private final int mode;
    private final int cardSideLength, layoutMargin;

    public AppointmentImagesAdapter(int mode, FragmentActivity context, ItemClickListener<AppointmentImageModel> onRemoveClickListener) {
        this.mode = mode;
        this.context = context;
        this.onRemoveClickListener = onRemoveClickListener;
        list = new ArrayList<>();
        int netWidth = UIUtils.getScreenDisplayMetrics(context).widthPixels - (context.getResources().getDimensionPixelSize(R.dimen.padding) * 4);
        cardSideLength = (int) (netWidth / 3.0);
        layoutMargin = context.getResources().getDimensionPixelSize(R.dimen.half_padding);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemAppointmentImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
    public void add(AppointmentImageModel item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<AppointmentImageModel> items) {
        list.clear();
        list.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemAppointmentImageBinding viewBinding;

        ViewHolder(ItemAppointmentImageBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
            this.viewBinding.getRoot().getLayoutParams().width = cardSideLength;
            this.viewBinding.getRoot().getLayoutParams().height = cardSideLength;
        }

        private void bind(int position) {
            CardView.LayoutParams layoutParams = new CardView.LayoutParams(cardSideLength, cardSideLength);
            layoutParams.setMargins(layoutMargin, layoutMargin, layoutMargin, layoutMargin);
            viewBinding.getRoot().setLayoutParams(layoutParams);
            AppointmentImageModel model = list.get(position);
            PicassoHelper.loadImageWithCache(model.getDownloadLink(), viewBinding.ivAppointmentImage, PicassoHelper.MODE.JUST_INTO, null, null, null);
            viewBinding.ivAppointmentImage.setOnClickListener(v ->
                    new StfalconImageViewer.Builder<>(context, new String[]{model.getDownloadLink()}, (iv, image) ->
                            Picasso.get().load(image).into(iv))
                            .withTransitionFrom(viewBinding.ivAppointmentImage)
                            .withBackgroundColorResource(R.color.colorBlack).show(true));
            viewBinding.ivRemove.setOnClickListener(v -> onRemoveClickListener.onItemClicked(model));
            viewBinding.ivRemove.setVisibility(mode == MODE_VIEW ? View.GONE : View.VISIBLE);
        }
    }
}