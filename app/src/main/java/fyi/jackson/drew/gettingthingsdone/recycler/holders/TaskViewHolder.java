package fyi.jackson.drew.gettingthingsdone.recycler.holders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import fyi.jackson.drew.gettingthingsdone.R;

public class TaskViewHolder extends RecyclerView.ViewHolder {

    public CheckBox cbTask;

    public TaskViewHolder(View v) {
        super(v);
        cbTask = v.findViewById(R.id.cb_task_status);
    }
}
