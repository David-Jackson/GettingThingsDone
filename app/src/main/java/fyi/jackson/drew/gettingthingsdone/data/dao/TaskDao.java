package fyi.jackson.drew.gettingthingsdone.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import fyi.jackson.drew.gettingthingsdone.data.entities.Task;

@Dao
public interface TaskDao {

    @Query("SELECT * FROM task")
    LiveData<List<Task>> getTasks();

    @Query("SELECT * FROM task WHERE id=:id")
    LiveData<Task> getTask(long id);

    @Query("SELECT * FROM task WHERE bucket=:bucketName")
    LiveData<List<Task>> getTasksFromBucket(String bucketName);

    @Query("SELECT t.id, t.name, b.name AS bucket, t.created, t.done FROM Bucket AS b LEFT OUTER JOIN Task AS t ON t.bucket = b.name")
    LiveData<List<Task>> getItemsAndBuckets();

    @Insert
    long insert(Task task);


}
