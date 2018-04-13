package fyi.jackson.drew.gettingthingsdone.data;


import java.util.ArrayList;
import java.util.List;

import fyi.jackson.drew.gettingthingsdone.R;
import fyi.jackson.drew.gettingthingsdone.data.entities.Bucket;

public class DummyData {
    public static List<Bucket> buckets = new ArrayList<Bucket>() {{
        add(new Bucket("Inbox", R.drawable.ic_inbox_black_24dp));
        add(new Bucket("Work"));
        add(new Bucket("Home"));
        add(new Bucket("Projects"));
        add(new Bucket("Trash", R.drawable.ic_delete_black_24dp));
    }};

}
