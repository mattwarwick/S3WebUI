package com.thoughtDocs.viewModel;

import com.thoughtDocs.model.Document;
import com.thoughtDocs.model.Repository;
import com.thoughtDocs.model.impl.s3.DocumentImpl;
import com.thoughtDocs.viewModel.DocumentDisplayItem;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.annotations.web.RequestParameter;
import org.jboss.seam.international.StatusMessages;
import org.jboss.seam.log.Log;

import java.io.IOException;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ThoughtWorks
 * Date: Jul 23, 2009
 * Time: 7:06:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Scope(ScopeType.SESSION)
@Name("documentPublicDownloadAction")
public class DocumentPublicDownloadAction implements Serializable {
    private String key;
    @Logger
    private Log log;

    @In
    private StatusMessages statusMessages;


    @In
    private Repository defaultRepository;

    @RequestParameter
    void setKey(String newkey) {
        if (newkey != null)
            key = newkey;
    }

    private String password;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public String getFilename() {
        return key;
    }

    public void download() throws IOException {

        Document doc = DocumentImpl.loadedFromRepository(defaultRepository, key);
        if (doc == null || password == null || !password.equals(doc.getPassword())) {
            statusMessages.add("File and password does not match, please try again");
            return;
        }

        new DocumentDisplayItem(doc).open();
        //test url http://localhost:8080/thoughtDocs/documentPublicDownload.seam?key=restws.pdf
    }
}