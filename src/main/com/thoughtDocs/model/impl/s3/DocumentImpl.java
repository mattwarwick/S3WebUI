package com.thoughtDocs.model.impl.s3;

import com.thoughtDocs.model.Document;
import com.thoughtDocs.model.Repository;
import com.thoughtDocs.model.Folder;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Kailuo "Kai" Wang
 * Date: Jul 10, 2009
 * Time: 6:29:24 PM
 */
public class DocumentImpl extends AbstractItem implements Document, Serializable {

    private static final String PUBLIC_PASSWORD_META_KEY = "public-password";

    private DocumentImpl(S3Object obj, Repository repo) {
        super(obj, repo);
    }

    private static Document createTransientDocument(Repository repo, String key) {
        S3Object obj = S3Object.createNewTransient(((RepositoryImpl) repo).getBucket(), key);
        return new DocumentImpl(obj, repo);
    }

    public static Document createTransientDocument(Folder parentFolder, String name) {
          String key = createKey(parentFolder, name);
          return createTransientDocument( parentFolder.getRepository(), key);
      }


    public String getKey() {
        return s3Object.getKey();
    }

    /**
     * get the document by name from server
     *
     * @param repo
     * @param key
     * @return null if no such file found on server
     * @throws IOException
     */
    public static Document loadedFromRepository(Repository repo, String key) throws IOException {
        S3Object obj = S3Object.loadedFromServer(((RepositoryImpl) repo).getBucket(), key);
        return loadedFromS3Object(repo, obj);
    }

    static Document loadedFromS3Object(Repository repo, S3Object obj) throws IOException {
        obj.updateMeta();
        if (obj.isTransient())
            return null;
        return new DocumentImpl(obj,repo );
    }

 

    public String getSignedURL() {
        return s3Object.getSignedURL();
    }

    public byte[] getData() throws IOException {
        if (s3Object.getData() == null && !isTransient())
            update();
        return s3Object.getData();
    }

    public void setData(byte[] data) {
        s3Object.setData(data);
    }

    public String getContentType() {
        return s3Object.getContentType();
    }

    public void setContentType(String contentType) {
        s3Object.setContentType(contentType);
    }

    public void delete() throws IOException {
        s3Object.delete();
    }

    public String getPassword() throws IOException {
        Object passwordsMeta = s3Object.getMeta().get(PUBLIC_PASSWORD_META_KEY);
        if (passwordsMeta != null)
            return (String) ((List) passwordsMeta).get(0);
        else
            return null;
    }


    public void setPassword(String password) throws IOException {
        s3Object.getMeta().put(PUBLIC_PASSWORD_META_KEY, Arrays.asList(password));
    }


    /**
     * @return public url for external user to download (password needed)
     */
    public String getPublicUrl() {
        return "http://thoughtdocs.com/dl/" + getKey();
    }

}
