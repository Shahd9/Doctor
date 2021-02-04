package com.msaproject.doctor.ui.medical_files;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.msaproject.doctor.R;
import com.msaproject.doctor.base.BaseRecyclerAdapter;
import com.msaproject.doctor.databinding.ItemMedicalFileBinding;
import com.msaproject.doctor.model.MedicalFileModel;
import com.msaproject.doctor.utils.PicassoHelper;
import com.msaproject.doctor.utils.UIUtils;
import com.squareup.picasso.Picasso;
import com.stfalcon.imageviewer.StfalconImageViewer;

import java.util.ArrayList;
import java.util.List;

public class MedicalFilesRecyclerAdapter extends RecyclerView.Adapter<MedicalFilesRecyclerAdapter.ViewHolder>
        implements BaseRecyclerAdapter<MedicalFileModel> {

    private final ArrayList<MedicalFileModel> list;
    private final FragmentActivity context;
    private final int cardSideLength, layoutMargin;

    public MedicalFilesRecyclerAdapter(FragmentActivity context) {
        this.context = context;
        list = new ArrayList<>();
        int netWidth = UIUtils.getScreenDisplayMetrics(context).widthPixels - (context.getResources().getDimensionPixelSize(R.dimen.padding) * 4);
        cardSideLength = (int) (netWidth / 3.0);
        layoutMargin = context.getResources().getDimensionPixelSize(R.dimen.half_padding);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemMedicalFileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
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
    public void add(MedicalFileModel item) {
        list.add(item);
        notifyDataSetChanged();
    }

    @Override
    public void addAll(List<MedicalFileModel> items) {
        list.addAll(items);
        notifyDataSetChanged();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final ItemMedicalFileBinding viewBinding;

        ViewHolder(ItemMedicalFileBinding viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
            this.viewBinding.getRoot().getLayoutParams().width = cardSideLength;
            this.viewBinding.getRoot().getLayoutParams().height = cardSideLength;
        }

        private void bind(int position) {
            CardView.LayoutParams layoutParams = new CardView.LayoutParams(cardSideLength, cardSideLength);
            layoutParams.setMargins(layoutMargin, layoutMargin, layoutMargin, layoutMargin);
            viewBinding.getRoot().setLayoutParams(layoutParams);
            MedicalFileModel model = list.get(position);
            PicassoHelper.loadImageWithCache(model.getDownloadLink(), viewBinding.ivMedicalFileImage, PicassoHelper.MODE.JUST_INTO, null, null, null);
            viewBinding.ivMedicalFileImage.setOnClickListener(v ->
                    new StfalconImageViewer.Builder<>(context, new String[]{model.getDownloadLink()}, (iv, image) ->
                            Picasso.get().load(image).into(iv))
                            .withTransitionFrom(viewBinding.ivMedicalFileImage)
                            .withBackgroundColorResource(R.color.colorBlack).show(true));
        }
    }
}
