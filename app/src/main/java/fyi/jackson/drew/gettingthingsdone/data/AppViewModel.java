package fyi.jackson.drew.gettingthingsdone.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import fyi.jackson.drew.gettingthingsdone.data.dao.BucketDao;
import fyi.jackson.drew.gettingthingsdone.data.dao.TaskDao;
import fyi.jackson.drew.gettingthingsdone.data.entities.Bucket;
import fyi.jackson.drew.gettingthingsdone.data.entities.Task;

public class AppViewModel extends ViewModel {
    private static final String TAG = AppViewModel.class.getSimpleName();

    public AppViewModel() {
    }

    public LiveData<Task> getTask(TaskDao taskDao, long id) {
        return taskDao.getTask(id);
    }

    public LiveData<List<Task>> getTasksFromBucket(TaskDao taskDao, String bucketName) {
        return taskDao.getTasksFromBucket(bucketName);
    }

    public LiveData<List<Task>> getItems(TaskDao taskDao) {
        return taskDao.getTasks();
    }

    public LiveData<Bucket> getBucket(BucketDao bucketDao, long id) {
        return bucketDao.getBucket(id);
    }

    public LiveData<List<Bucket>> getBuckets(BucketDao bucketDao) {
        return bucketDao.getBuckets();
    }

    public void createBucket(final Bucket bucket, final BucketDao bucketDao) {
        // AsyncTask won't leak memory when used within the ViewModel
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                bucketDao.insert(bucket);
                return null;
            }
        }.execute();
    }

    public void createTask(final Task task, final TaskDao taskDao) {
        // AsyncTask won't leak memory when used within the ViewModel
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                taskDao.insert(task);
                return null;
            }
        }.execute();
    }

}
