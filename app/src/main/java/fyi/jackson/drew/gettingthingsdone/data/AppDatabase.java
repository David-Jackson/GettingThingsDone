package fyi.jackson.drew.gettingthingsdone.data;


import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

import fyi.jackson.drew.gettingthingsdone.R;
import fyi.jackson.drew.gettingthingsdone.data.dao.BucketDao;
import fyi.jackson.drew.gettingthingsdone.data.dao.TaskDao;
import fyi.jackson.drew.gettingthingsdone.data.entities.Bucket;
import fyi.jackson.drew.gettingthingsdone.data.entities.Task;

@Database(entities = {Task.class, Bucket.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private final String TAG = AppDatabase.class.getSimpleName();

    private static AppDatabase sInstance;

    public abstract TaskDao taskDao();
    public abstract BucketDao bucketDao();

    public static synchronized AppDatabase getInstance(final Context context) {
        if (null == sInstance) {
            sInstance = Room
                    .databaseBuilder(context, AppDatabase.class, "gtd_database")
                    .addCallback(getDatabaseCreationCallback())
                    .build();

        }
        return sInstance;
    }

    private static RoomDatabase.Callback getDatabaseCreationCallback() {
        return new RoomDatabase.Callback(){
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
                db.beginTransaction();
                try {
                    db.execSQL("INSERT INTO Bucket " +
                            "(name, iconId) " +
                            "VALUES " +
                            "('Inbox', " + R.drawable.ic_inbox_black_24dp + ")," +
                            "('Trash', " + R.drawable.ic_delete_black_24dp + ");"
                    );
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            }
        };
    }

}

