package fyi.jackson.drew.gettingthingsdone.data.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import fyi.jackson.drew.gettingthingsdone.data.entities.Bucket;

@Dao
public interface BucketDao {

    @Query("SELECT * FROM bucket")
    LiveData<List<Bucket>> getBuckets();

    @Query("SELECT * FROM bucket WHERE id=:id")
    LiveData<Bucket> getBucket(long id);

    @Insert
    long insert(Bucket... bucket);

    @Delete
    void delete(Bucket bucket);

}
