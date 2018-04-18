package fyi.jackson.drew.gettingthingsdone.recycler.holders;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;

import fyi.jackson.drew.gettingthingsdone.R;
import fyi.jackson.drew.gettingthingsdone.recycler.helpers.ItemTouchHelperViewHolder;

public class TaskViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {

    public View itemView;
    public CheckBox cbTask;

    public TaskViewHolder(View v) {
        super(v);
        itemView = v;
        cbTask = v.findViewById(R.id.cb_task_status);
    }

    @Override
    public void onItemSelected() {

    }

    @Override
    public void onItemClear() {
        itemView.setBackgroundColor(0);
    }
}
