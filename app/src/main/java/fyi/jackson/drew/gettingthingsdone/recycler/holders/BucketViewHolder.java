package fyi.jackson.drew.gettingthingsdone.recycler.holders;


import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import fyi.jackson.drew.gettingthingsdone.R;

public class BucketViewHolder extends RecyclerView.ViewHolder{

    public TextView tvBucketName;

    public BucketViewHolder(View v) {
        super(v);
        tvBucketName = v.findViewById(R.id.tv_bucket_name);
    }
}
