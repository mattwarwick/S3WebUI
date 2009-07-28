package com.thoughtDocs.model.impl.s3;

import com.thoughtDocs.model.impl.s3.DocumentImpl;
import com.thoughtDocs.model.impl.s3.FixtureBase;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ThoughtWorks
 * Date: Jul 20, 2009
 * Time: 4:16:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class DocumentFixture extends FixtureBase {
    private static final String TEST_DATA = "testData";


    public DocumentFixture() throws IOException {
    }

    @Test
    public void testAddDocumentTest() throws IOException {
        int oldSize = defaultRepository.getDocuments().size();
        DocumentImpl doc = new DocumentImpl();
        String randomString = randomString();
        doc.setName(randomString);
        doc.setData(TEST_DATA.getBytes());
        doc.upload( defaultRepository);
        Assert.assertEquals(defaultRepository.getDocuments().size(), oldSize + 1);
        doc.refresh();
        Assert.assertEquals(new String(doc.getData()), TEST_DATA);
        try{
            doc.delete();
        }catch( Exception e) {
           //delete is not tested here.
        }
    }

    private String randomString()  {
        SecureRandom random = null;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new UnknownError(e.getMessage());
        }
        random.setSeed(new Date().getTime());
        String randomString = String.valueOf(random.nextLong());
        return randomString;
    }

    @Test
    public void testDeleteDocument() throws IOException {
        DocumentImpl doc = new DocumentImpl();
        doc.setName(randomString());
        doc.setData(TEST_DATA.getBytes());
        doc.upload( defaultRepository);
        int oldSize = defaultRepository.getDocuments().size();
        doc.delete();
        Assert.assertEquals(defaultRepository.getDocuments().size(), oldSize - 1);
    }

    @Test
    public void testDocumentPassword() throws IOException {
        DocumentImpl doc = new DocumentImpl();
        doc.setName(randomString());
        doc.setData(TEST_DATA.getBytes());
        String pass = "pass";
        doc.setPassword(pass);
        doc.upload(defaultRepository);
        doc.setPassword("");
        doc.refresh();
        Assert.assertEquals(doc.getPassword(), pass);
        Assert.assertEquals(new String(doc.getData()), TEST_DATA);
        doc.delete();
    }
}
