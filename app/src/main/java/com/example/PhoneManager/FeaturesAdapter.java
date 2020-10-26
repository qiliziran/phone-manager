package com.example.PhoneManager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class FeaturesAdapter extends RecyclerView.Adapter<FeaturesAdapter.ViewHolder> {
    private List<Features> mFeaturesList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View featuresView;
        ImageView featuresImage;
        TextView featuresName;

        public ViewHolder(View view) {
            super(view);
            featuresView = view;
            featuresImage = (ImageView) view.findViewById(R.id.features_image);
            featuresName = (TextView) view.findViewById(R.id.features_name);
        }
    }

    public FeaturesAdapter(Context context, List<Features> featuresList) {
        mFeaturesList = featuresList;
        this.mContext = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View view = LayoutInflater.from((parent.getContext())).inflate(R.layout.features_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.featuresView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();

                switch (position) {
                    case 0:
                        Intent intent= new Intent();
                        intent.setClass(mContext, ApplicationListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        break;
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Features features = mFeaturesList.get((position));
        holder.featuresImage.setImageResource(features.getImageId());
        holder.featuresName.setText(features.getName());
    }

    @Override
    public int getItemCount() {
        return mFeaturesList.size();
    }

}
