package fyi.jackson.drew.gettingthingsdone.data.entities;

public class Task {

    private String name, bucket;
    private Long created;
    private Boolean done;

    public Task() {}

    public Task(String name, String bucket, Long created, Long completed, Boolean done) {
        setName(name);
        setBucket(bucket);
        setCreated(created);
        setDone(done);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }
}
