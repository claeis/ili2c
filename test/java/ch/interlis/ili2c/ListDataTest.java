package ch.interlis.ili2c;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.gui.UserSettings;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.Model;
import ch.interlis.ili2c.metamodel.Table;
import ch.interlis.ili2c.metamodel.Topic;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.ilirepository.Dataset;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.XtfReader;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.ObjectEvent;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata;

public class ListDataTest {
    private static final String TEST_REPOS = "test/data/listdata/repos1";
    private static final String TEST_OUT=TEST_REPOS;
    private static final String DATASET_1 = "ch.interlis.ili2c.test.data1";
    private static final String DATASET_1_BID = "ch.interlis.ili2c.test.b1";
    private static final String DATASET_1_VERSION2 = "2";
    private static final String DATASET_2 = "ch.interlis.ili2c.test.data2";
    private static final String DATASET_2_VERSION1 = "1";
    private static final String DATASET_3 = "ch.interlis.ili2c.test.data3";
    private static final String DATASET_4 = "ch.interlis.ili2c.test.data4";

	@Test
	public void simpleList() throws Exception {
	    File data=new File(TEST_OUT,"simpleList-out.xtf");

		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_REPOS, FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		ili2cConfig.setOutputFile(data.getPath());
        UserSettings settings=new UserSettings();
		ListData listData=new ListData();
        listData.listData(ili2cConfig, settings);
        {
            XtfReader reader=new XtfReader(data);
            assertTrue(reader.read() instanceof StartTransferEvent);
            assertTrue(reader.read() instanceof StartBasketEvent);
            
            IoxEvent event=reader.read();
            assertTrue(event instanceof ObjectEvent);
            IomObject iomObj=((ObjectEvent)event).getIomObject();
            assertEquals(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag,iomObj.getobjecttag());
            assertEquals(DATASET_1,iomObj.getattrvalue(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_id));
            assertEquals(DATASET_1_VERSION2,iomObj.getattrvalue(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_version));
            
            for(int i=0;i<3;i++) {
                event=reader.read();
                assertTrue(event instanceof ObjectEvent);
                iomObj=((ObjectEvent)event).getIomObject();
                assertEquals(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag,iomObj.getobjecttag());
            }
            
            assertTrue(reader.read() instanceof EndBasketEvent);
            assertTrue(reader.read() instanceof EndTransferEvent);
        }
	}
    @Test
    public void queryById() throws Exception {

        ch.interlis.ilirepository.IliManager manager = new ch.interlis.ilirepository.IliManager();
        manager.setRepositories(new String[] {TEST_REPOS});
        List<Dataset> result=manager.getDatasetIndex(DATASET_1_BID, null);
        
        assertEquals(1,result.size());
        IomObject iomObj=result.get(0).getMetadata();
        assertEquals(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag,iomObj.getobjecttag());
        assertEquals(DATASET_1,iomObj.getattrvalue(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_id));
        assertEquals(DATASET_1_VERSION2,iomObj.getattrvalue(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_version));
        
    }
    @Test
    public void queryDatasetById() throws Exception {

        ch.interlis.ilirepository.IliManager manager = new ch.interlis.ilirepository.IliManager();
        manager.setRepositories(new String[] {TEST_REPOS});
        List<Dataset> result=manager.getDatasetIndex(DATASET_3, null);
        
        assertEquals(1,result.size());
        IomObject iomObj=result.get(0).getMetadata();
        assertEquals(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag,iomObj.getobjecttag());
        assertEquals(DATASET_3,iomObj.getattrvalue(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_id));
    }
    @Test
    public void queryDatasetWithoutBasketsById() throws Exception {

        ch.interlis.ilirepository.IliManager manager = new ch.interlis.ilirepository.IliManager();
        manager.setRepositories(new String[] {TEST_REPOS});
        List<Dataset> result=manager.getDatasetIndex(DATASET_4, null);
        
        assertEquals(1,result.size());
        IomObject iomObj=result.get(0).getMetadata();
        assertEquals(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag,iomObj.getobjecttag());
        assertEquals(DATASET_4,iomObj.getattrvalue(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_id));
    }
    @Test
    public void queryById_Fail() throws Exception {

        ch.interlis.ilirepository.IliManager manager = new ch.interlis.ilirepository.IliManager();
        manager.setRepositories(new String[] {TEST_REPOS});
        List<Dataset> result=manager.getDatasetIndex("x"+DATASET_1_BID, null);
        
        assertEquals(0,result.size());
    }
    @Test
    public void queryByTopic() throws Exception {

        ch.interlis.ilirepository.IliManager manager = new ch.interlis.ilirepository.IliManager();
        manager.setRepositories(new String[] {TEST_REPOS});
        List<Dataset> result=manager.getDatasetIndex(null, new String[] {"Ili2cModelListTest1.TopicA"});
        
        assertEquals(2,result.size());
        IomObject iomObj=result.get(0).getMetadata();
        assertEquals(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag,iomObj.getobjecttag());
        assertEquals(DATASET_1,iomObj.getattrvalue(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_id));
        assertEquals(DATASET_1_VERSION2,iomObj.getattrvalue(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_version));

        iomObj=result.get(1).getMetadata();
        assertEquals(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag,iomObj.getobjecttag());
        assertEquals(DATASET_2,iomObj.getattrvalue(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_id));
        assertEquals(DATASET_2_VERSION1,iomObj.getattrvalue(ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata.tag_version));
        
    }
    @Test
    public void queryFileById() throws Exception {

        ch.interlis.ilirepository.IliManager manager = new ch.interlis.ilirepository.IliManager();
        manager.setRepositories(new String[] {TEST_REPOS});
        List<Dataset> result=manager.getDatasetIndex(DATASET_1_BID, null);
        
        assertEquals(1,result.size());
        Dataset dataset=result.get(0);
        File[] files=manager.getLocalFileOfRemoteDataset(dataset, TransferDescription.MIMETYPE_XTF);
        assertEquals(1,files.length);
        assertTrue(files[0].exists());
    }

}
