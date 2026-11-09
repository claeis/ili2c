package ch.interlis.ilirepository.impl;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

public class FetchFromHttpServerTest {
    private static final long ONE_DAY=86400000L;
    private static final String ROOT_FILE="data.xtf";
    private static final String SUBFOLDER_FILE="a/b/data.xtf";

    @Rule
    public TemporaryFolder cache=new TemporaryFolder();

    private HttpServer server=null;
    private String repository=null;
    private final Map<String,Integer> requestCount=new HashMap<String,Integer>();
    private RepositoryAccess repoAccess=null;

    @Before
    public void setUp() throws IOException
    {
        server=HttpServer.create(new InetSocketAddress("127.0.0.1",0),0);
        server.createContext("/",new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                String path=exchange.getRequestURI().getPath();
                synchronized(requestCount) {
                    Integer count=requestCount.get(path);
                    requestCount.put(path,count==null ? 1 : count+1);
                }
                byte[] body=content(path).getBytes("US-ASCII");
                exchange.sendResponseHeaders(200,body.length);
                OutputStream out=exchange.getResponseBody();
                out.write(body);
                out.close();
            }
        });
        server.start();
        repository="http://127.0.0.1:"+server.getAddress().getPort()+"/";
        repoAccess=new RepositoryAccess();
        repoAccess.setCache(cache.getRoot());
    }

    @After
    public void tearDown()
    {
        if(server!=null) {
            server.stop(0);
        }
    }

    @Test
    public void simple() throws Exception
    {
        RepositoryAccess repos=new RepositoryAccess();
        File localFile=repos.getLocalFileLocation("https://models.interlis.ch/refhb23/MiniCoordSysData-20200320.xml");
        Assert.assertTrue(localFile.exists());
    }

    @Test
    public void rootFileFromCache() throws Exception
    {
        File first=repoAccess.getLocalFileLocation(repository,ROOT_FILE,ONE_DAY,null);
        File second=repoAccess.getLocalFileLocation(repository,ROOT_FILE,ONE_DAY,null);
        Assert.assertEquals(first,second);
        Assert.assertEquals(1,requestCount("/"+ROOT_FILE));
    }

    @Test
    public void subfolderFileFromCache() throws Exception
    {
        File first=repoAccess.getLocalFileLocation(repository,SUBFOLDER_FILE,ONE_DAY,null);
        File second=repoAccess.getLocalFileLocation(repository,SUBFOLDER_FILE,ONE_DAY,null);
        Assert.assertEquals(first,second);
        Assert.assertEquals(RepositoryAccess.calcMD5(content("/"+SUBFOLDER_FILE)),RepositoryAccess.calcMD5(second));
        Assert.assertEquals(1,requestCount("/"+SUBFOLDER_FILE));
    }

    @Test
    public void subfolderFileMaxTTLZeroRefetch() throws Exception
    {
        repoAccess.getLocalFileLocation(repository,SUBFOLDER_FILE,0L,null);
        repoAccess.getLocalFileLocation(repository,SUBFOLDER_FILE,0L,null);
        Assert.assertEquals(2,requestCount("/"+SUBFOLDER_FILE));
    }

    @Test
    public void subfolderFileMd5MatchesFromCache() throws Exception
    {
        String md5=RepositoryAccess.calcMD5(content("/"+SUBFOLDER_FILE));
        repoAccess.getLocalFileLocation(repository,SUBFOLDER_FILE,ONE_DAY,md5);
        repoAccess.getLocalFileLocation(repository,SUBFOLDER_FILE,ONE_DAY,md5);
        Assert.assertEquals(1,requestCount("/"+SUBFOLDER_FILE));
    }

    @Test
    public void subfolderFileMd5DiffersRefetch() throws Exception
    {
        String md5=RepositoryAccess.calcMD5("other content");
        repoAccess.getLocalFileLocation(repository,SUBFOLDER_FILE,ONE_DAY,md5);
        repoAccess.getLocalFileLocation(repository,SUBFOLDER_FILE,ONE_DAY,md5);
        Assert.assertEquals(2,requestCount("/"+SUBFOLDER_FILE));
    }

    @Test
    public void subfolderFileServerDownUseCachedCopy() throws Exception
    {
        File first=repoAccess.getLocalFileLocation(repository,SUBFOLDER_FILE,ONE_DAY,null);
        server.stop(0);
        server=null;
        File second=repoAccess.getLocalFileLocation(repository,SUBFOLDER_FILE,0L,null);
        Assert.assertEquals(first,second);
    }

    private static String content(String path)
    {
        return "content of "+path;
    }

    private int requestCount(String path)
    {
        synchronized(requestCount) {
            Integer count=requestCount.get(path);
            return count==null ? 0 : count;
        }
    }
}
