package com.thoughtDocs.model.impl.s3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ThoughtWorks
 * Date: Jul 27, 2009
 * Time: 11:28:12 PM
 * a bucket implementation that uses memory to store objects basically for testing purpose
 */
public class MemoryBucketImpl implements S3Bucket {
    private List<S3Object> objects;

    public MemoryBucketImpl(String name) {
        objects = new ArrayList<S3Object>();
        this.name = name;
    }

    private String name;


    public String getName() {
        return name;
    }

    public void saveObject(S3Object obj) throws IOException {
        S3Object toRemove = findByKey(obj.getKey());
        if (toRemove != null)
            objects.remove(obj);
        objects.add(obj);
    }

    private S3Object findByKey(String key) {
        for (S3Object object : objects)
            if (object.getKey().equals(key))
                return object;
        return null;
    }

    public void removeObject(S3Object obj) throws IOException {
        objects.remove(findByKey(obj.getKey()));
    }

    public List<S3Object> getObjects() throws IOException {

        List<S3Object> retVal = new ArrayList<S3Object>();
        for (S3Object obj : objects) {
            retVal.add(S3Object.loadedFromServer(this, obj.getKey()));
        }
        return retVal;
    }

    public void updateObject(S3Object object) throws IOException {
        for (S3Object obj : objects) {
            if (obj.getKey().equals(object.getKey())) {
                object.setMeta(obj.getMeta());
                object.setData(obj.getData());
                object.setTransient(false);
                return;
            }
        }
        object.setTransient(true);
    }

    public String getSignedUrl(S3Object object) {
        return "http://localhost:8080/thoughtDocs/memoryDL/" + object.getKey();
    }

    public void updateObjectMeta(S3Object object) throws IOException {
        updateObject(object);
    }


}
