package fyi.jackson.drew.gettingthingsdone.data.entities;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Bucket {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String name;
    private Integer iconId;

    public Bucket(String name, Integer iconId) {
        setName(name);
        setIconId(iconId);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getIconId() {
        return iconId;
    }

    public void setIconId(Integer iconId) {
        this.iconId = iconId;
    }
}
