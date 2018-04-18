package fyi.jackson.drew.gettingthingsdone.recycler;

import android.graphics.Paint;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import fyi.jackson.drew.gettingthingsdone.R;
import fyi.jackson.drew.gettingthingsdone.data.entities.Bucket;
import fyi.jackson.drew.gettingthingsdone.data.entities.Task;
import fyi.jackson.drew.gettingthingsdone.recycler.helpers.ItemTouchHelperAdapter;
import fyi.jackson.drew.gettingthingsdone.recycler.helpers.OnStartDragListener;
import fyi.jackson.drew.gettingthingsdone.recycler.holders.BucketBottomViewHolder;
import fyi.jackson.drew.gettingthingsdone.recycler.holders.BucketViewHolder;
import fyi.jackson.drew.gettingthingsdone.recycler.holders.TaskViewHolder;

public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements ItemTouchHelperAdapter {

    private static final String TAG = "TaskAdapter";

    public static final int BUCKET_TOP = 1, TASK = 2, BUCKET_BOTTOM = 3;

    List<Task> rawTaskList;

    List<Object> sortedTaskList;

    private OnStartDragListener onStartDragListener;

    public TaskAdapter(OnStartDragListener onStartDragListener){
        this.onStartDragListener = onStartDragListener;
    }

    public TaskAdapter(List<Task> taskList, OnStartDragListener onStartDragListener) {
        setTaskList(taskList);
        this.onStartDragListener = onStartDragListener;
        Log.d(TAG, "TaskAdapter: making list of length: " + taskList.size());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        View v;
        switch (viewType) {
            case BUCKET_TOP:
                v = inflater.inflate(R.layout.view_holder_bucket, parent, false);
                viewHolder = new BucketViewHolder(v);
                break;
            case TASK:
                v = inflater.inflate(R.layout.view_holder_task, parent, false);
                viewHolder = new TaskViewHolder(v);
                break;
            default: // BUCKET_BOTTOM
                v = inflater.inflate(R.layout.view_holder_bucket_bottom, parent, false);
                viewHolder = new BucketBottomViewHolder(v);
                break;
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return (sortedTaskList == null) ? 0 : sortedTaskList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = sortedTaskList.get(position);
        if (item instanceof Bucket) {
            return BUCKET_TOP;
        } else if (item instanceof Task) {
            return TASK;
        } else if (item instanceof BucketBottom) {
            return BUCKET_BOTTOM;
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case BUCKET_TOP:
                onBindBucketViewHolder((BucketViewHolder) holder, position);
                break;
            case TASK:
                onBindTaskViewHolder((TaskViewHolder) holder, position);
                break;
            default: // BUCKET_BOTTOM
                break;
        }
    }

    private void onBindBucketViewHolder(BucketViewHolder holder, int position) {
        Bucket bucket = (Bucket) sortedTaskList.get(position);
        holder.tvBucketName.setText(bucket.getName());
    }

    private void onBindTaskViewHolder(final TaskViewHolder holder, int position) {
        Task task = (Task) sortedTaskList.get(position);
        holder.cbTask.setText(task.getName());
        holder.cbTask.setChecked(task.getDone());
        int paintFlags = holder.cbTask.getPaintFlags();
        if (task.getDone()) {
            paintFlags |= Paint.STRIKE_THRU_TEXT_FLAG;
        } else {
            paintFlags &= (~ Paint.STRIKE_THRU_TEXT_FLAG);
        }
        holder.cbTask.setPaintFlags(paintFlags);

        // Start a drag whenever the handle view it touched
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    onStartDragListener.onStartDrag(holder);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(sortedTaskList, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        sortedTaskList.remove(position);
        notifyItemRemoved(position);
    }

    private class BucketBottom {
        public BucketBottom(){}
    }

    public void updateTaskList(List<Task> taskList) {
        setTaskList(taskList);
        notifyDataSetChanged();
    }

    public void setTaskList(List<Task> taskList) {
        this.rawTaskList = taskList;
        processTaskList();
    }

    private void processTaskList() {
        sortedTaskList = new ArrayList<>();
        try {
            JSONObject dict = new JSONObject();
            // put Inbox first
            dict.put("Inbox", new JSONArray());
            for (Task task : rawTaskList) {
                if (!dict.has(task.getBucket())) {
                    dict.put(task.getBucket(), new JSONArray());
                }
                dict.accumulate(task.getBucket(), task);
            }

            Iterator<?> keys = dict.keys();

            while(keys.hasNext()) {
                String key = (String)keys.next();
                JSONArray taskListForBucket = dict.getJSONArray(key);
                sortedTaskList.add(new Bucket(key, null));
                for (int i = 0; i < taskListForBucket.length(); i++) {
                    Task task = (Task) taskListForBucket.get(i);
                    if (task.getDone() == null) task.setDone(false);
                    sortedTaskList.add(task);
                }
                sortedTaskList.add(new BucketBottom());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
