package fyi.jackson.drew.gettingthingsdone.data;


import java.util.ArrayList;
import java.util.List;

import fyi.jackson.drew.gettingthingsdone.R;
import fyi.jackson.drew.gettingthingsdone.data.entities.Bucket;
import fyi.jackson.drew.gettingthingsdone.data.entities.Task;

public class DummyData {
    public static List<Bucket> buckets = new ArrayList<Bucket>() {{
        add(new Bucket("Inbox", R.drawable.ic_inbox_black_24dp));
        add(new Bucket("Work"));
        add(new Bucket("Home"));
        add(new Bucket("Projects"));
        add(new Bucket("Trash", R.drawable.ic_delete_black_24dp));
    }};

    public static List<Task> tasks = new ArrayList<Task>(){{
        add(new Task("Do work", "Work", 1523628945L, null, false));
        add(new Task("Laundry", "Home", 1523618945L, 1523623545L, true));
        add(new Task("A neat idea!", "Inbox", 1523627945L, null, false));
        add(new Task("Do taxes", "Inbox", 1523624925L, null, false));
    }};
}