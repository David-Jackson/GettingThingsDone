package fyi.jackson.drew.gettingthingsdone.recycler.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import fyi.jackson.drew.gettingthingsdone.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    public TextView tvTaskName;
    public CheckBox cbTaskStatus;

    public TaskViewHolder(View v) {
        super(v);
        tvTaskName = v.findViewById(R.id.tv_task_name);
        cbTaskStatus = v.findViewById(R.id.cb_task_status);
    }
}
