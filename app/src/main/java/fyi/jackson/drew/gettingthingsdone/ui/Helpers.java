package fyi.jackson.drew.gettingthingsdone.ui;

import java.util.Collections;
import java.util.List;

import fyi.jackson.drew.gettingthingsdone.data.entities.Bucket;

public class Helpers {
    public static void organizeBuckets(List<Bucket> buckets) {
        for (int i = buckets.size() - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                String a = buckets.get(j).getName();
                String b = buckets.get(j + 1).getName();
                if (b.equals("Inbox") || a.equals("Trash") || a.compareTo(b) > 0) {
                    Collections.swap(buckets, j, j + 1);
                }
            }
        }
    }
}
