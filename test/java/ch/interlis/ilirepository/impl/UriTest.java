package ch.interlis.ilirepository.impl;

import org.junit.Assert;
import org.junit.Test;

public class UriTest {
    @Test
    public void simple() throws Exception
    {
        java.net.URI uri=RepositoryAccess.makeURI(new java.net.URI("http://server/sub/"),"test");
        Assert.assertEquals("http://server/sub/test",uri.toString());
    }
    @Test
    public void simpleSpace() throws Exception
    {
        java.net.URI uri=RepositoryAccess.makeURI(new java.net.URI("http://server/sub/"),"test more");
        Assert.assertEquals("http://server/sub/test%20more",uri.toString());
    }
    @Test
    public void simpleSlashSpace() throws Exception
    {
        java.net.URI uri=RepositoryAccess.makeURI(new java.net.URI("http://server/sub/"),"/test more");
        Assert.assertEquals("http://server/sub/test%20more",uri.toString());
    }
    @Test
    public void simpleBackSlashSpace() throws Exception
    {
        java.net.URI uri=RepositoryAccess.makeURI(new java.net.URI("http://server/sub/"),"\\test more");
        Assert.assertEquals("http://server/sub/test%20more",uri.toString());
    }
        
}
