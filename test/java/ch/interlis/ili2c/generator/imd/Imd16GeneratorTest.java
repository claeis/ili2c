package ch.interlis.ili2c.generator.imd;

import org.junit.Assert;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.HashMap;

import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.config.GenerateOutputKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.Xtf24Reader;
import ch.interlis.iom_j.xtf.XtfReader;
import ch.interlis.iox.EndBasketEvent;
import ch.interlis.iox.EndTransferEvent;
import ch.interlis.iox.IoxEvent;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.ObjectEvent;
import ch.interlis.iox.StartBasketEvent;
import ch.interlis.iox.StartTransferEvent;
import ch.interlis.iox_j.PipelinePool;
import ch.interlis.iox_j.jts.Iox2jtsException;
import ch.interlis.iox_j.logging.LogEventFactory;
import ch.interlis.iox_j.validator.ValidationConfig;
import ch.interlis.iox_j.validator.Validator;

public class Imd16GeneratorTest {
    private static final String ILIS_META16_ILI = "standard/IlisMeta16.ili";
    private static final String UNITS23_ILI = "standard/Units.ili";
    private static final String LOCALUNIQUE23_ILI = "test/data/imdgenerator/LocalUnique23.ili";
    @Test
    public void ili23Test() throws Iox2jtsException, IoxException {
        final String OUT_FILE = "Simple23-out.imd";
        // generate imd file
        {
            // compile model
            TransferDescription td=null;
            Configuration ili2cConfig=new Configuration();
            FileEntry fileEntry=new FileEntry(ImdGeneratorTest.SIMPLE23_ILI, FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setOutputFile(OUT_FILE);
            ili2cConfig.setOutputKind(GenerateOutputKind.IMD16);
            td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
            Assert.assertNotNull(td);
            
        }
        
        // verify
        {
            // compile model
            TransferDescription td=null;
            Configuration ili2cConfig=new Configuration();
            FileEntry fileEntry=new FileEntry(ILIS_META16_ILI, FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
            Assert.assertNotNull(td);
            
            ValidationConfig modelConfig = new ValidationConfig();
            LogCollector logger = new LogCollector();
            LogEventFactory errFactory = new LogEventFactory();
            PipelinePool pipelinePool = new PipelinePool();
            Settings settings = new Settings();
            Validator validator = new Validator(td, modelConfig, logger, errFactory, pipelinePool, settings);
            
            Xtf24Reader reader=new Xtf24Reader(new File(OUT_FILE));
            reader.setModel(td);
            IoxEvent event=null;
            HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
             do{
                    event=reader.read();
                    //validator.validate(event); // requires ilivalidator#336
                    if(event instanceof StartTransferEvent){
                    }else if(event instanceof StartBasketEvent){
                    }else if(event instanceof ObjectEvent){
                        IomObject iomObj=((ObjectEvent)event).getIomObject();
                        if(iomObj.getobjectoid()!=null) {
                            objs.put(iomObj.getobjectoid(), iomObj);
                        }
                    }else if(event instanceof EndBasketEvent){
                    }else if(event instanceof EndTransferEvent){
                    }
             }while(!(event instanceof EndTransferEvent));
        }
    }
    @Test
    public void units23Test() throws Iox2jtsException, IoxException {
        final String OUT_FILE = "Units23-out.imd";
        // generate imd file
        {
            EhiLogger.getInstance().setTraceFilter(false);
            // compile model
            TransferDescription td=null;
            Configuration ili2cConfig=new Configuration();
            FileEntry fileEntry=new FileEntry(UNITS23_ILI, FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setOutputFile(OUT_FILE);
            ili2cConfig.setOutputKind(GenerateOutputKind.IMD16);
            td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
            Assert.assertNotNull(td);
            
        }
        
        // verify
        {
            // compile model
            TransferDescription td=null;
            Configuration ili2cConfig=new Configuration();
            FileEntry fileEntry=new FileEntry(ILIS_META16_ILI, FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
            Assert.assertNotNull(td);
            
            ValidationConfig modelConfig = new ValidationConfig();
            LogCollector logger = new LogCollector();
            LogEventFactory errFactory = new LogEventFactory();
            PipelinePool pipelinePool = new PipelinePool();
            Settings settings = new Settings();
            Validator validator = new Validator(td, modelConfig, logger, errFactory, pipelinePool, settings);
            
            Xtf24Reader reader=new Xtf24Reader(new File(OUT_FILE));
            reader.setModel(td);
            IoxEvent event=null;
            HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
             do{
                    event=reader.read();
                    //validator.validate(event); // requires ilivalidator#336
                    if(event instanceof StartTransferEvent){
                    }else if(event instanceof StartBasketEvent){
                    }else if(event instanceof ObjectEvent){
                        IomObject iomObj=((ObjectEvent)event).getIomObject();
                        if(iomObj.getobjectoid()!=null) {
                            objs.put(iomObj.getobjectoid(), iomObj);
                        }
                    }else if(event instanceof EndBasketEvent){
                    }else if(event instanceof EndTransferEvent){
                    }
             }while(!(event instanceof EndTransferEvent));
        }
    }

    @Test
    public void ili10Test() throws Iox2jtsException, IoxException {
        final String OUT_FILE = "Simple10-out.imd";
        // generate imd file
        {
            // compile model
            TransferDescription td=null;
            Configuration ili2cConfig=new Configuration();
            FileEntry fileEntry=new FileEntry(ImdGeneratorTest.SIMPLE10_ILI, FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setOutputFile(OUT_FILE);
            ili2cConfig.setOutputKind(GenerateOutputKind.IMD16);
            td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
            Assert.assertNotNull(td);
            
        }
        
        // verify
        {
            // compile model
            TransferDescription td=null;
            Configuration ili2cConfig=new Configuration();
            FileEntry fileEntry=new FileEntry(ILIS_META16_ILI, FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
            Assert.assertNotNull(td);
            
            ValidationConfig modelConfig = new ValidationConfig();
            LogCollector logger = new LogCollector();
            LogEventFactory errFactory = new LogEventFactory();
            PipelinePool pipelinePool = new PipelinePool();
            Settings settings = new Settings();
            Validator validator = new Validator(td, modelConfig, logger, errFactory, pipelinePool, settings);
            
            Xtf24Reader reader=new Xtf24Reader(new File(OUT_FILE));
            reader.setModel(td);
            IoxEvent event=null;
            HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
             do{
                    event=reader.read();
                    //validator.validate(event); // requires ilivalidator#336
                    if(event instanceof StartTransferEvent){
                    }else if(event instanceof StartBasketEvent){
                    }else if(event instanceof ObjectEvent){
                        IomObject iomObj=((ObjectEvent)event).getIomObject();
                        if(iomObj.getobjectoid()!=null) {
                            objs.put(iomObj.getobjectoid(), iomObj);
                        }
                    }else if(event instanceof EndBasketEvent){
                    }else if(event instanceof EndTransferEvent){
                    }
             }while(!(event instanceof EndTransferEvent));
        }
    }
    @Test
    public void localUnique23Test() throws Iox2jtsException, IoxException {
        final String OUT_FILE = "LocalUnique23-out.imd";
        // generate imd file
        {
            // compile model
            TransferDescription td=null;
            Configuration ili2cConfig=new Configuration();
            FileEntry fileEntry=new FileEntry(LOCALUNIQUE23_ILI, FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setOutputFile(OUT_FILE);
            ili2cConfig.setOutputKind(GenerateOutputKind.IMD16);
            td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
            assertNotNull(td);
            
        }
        
        // verify
        {
            // compile model
            TransferDescription td=null;
            Configuration ili2cConfig=new Configuration();
            FileEntry fileEntry=new FileEntry(ILIS_META16_ILI, FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
            assertNotNull(td);
            
            ValidationConfig modelConfig = new ValidationConfig();
            LogCollector logger = new LogCollector();
            LogEventFactory errFactory = new LogEventFactory();
            PipelinePool pipelinePool = new PipelinePool();
            Settings settings = new Settings();
            Validator validator = new Validator(td, modelConfig, logger, errFactory, pipelinePool, settings);
            
            Xtf24Reader reader=new Xtf24Reader(new File(OUT_FILE));
            reader.setModel(td);
            IoxEvent event=null;
            HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
             do{
                    event=reader.read();
                    if (ImdGeneratorTest.issue336fixed) {
                        validator.validate(event); // requires ilivalidator#336
                    }
                    if(event instanceof StartTransferEvent){
                    }else if(event instanceof StartBasketEvent){
                    }else if(event instanceof ObjectEvent){
                        IomObject iomObj=((ObjectEvent)event).getIomObject();
                        if(iomObj.getobjectoid()!=null) {
                            objs.put(iomObj.getobjectoid(), iomObj);
                        }
                    }else if(event instanceof EndBasketEvent){
                    }else if(event instanceof EndTransferEvent){
                    }
             }while(!(event instanceof EndTransferEvent));
             
             IomObject cnstr=objs.get("LocalUnique23.TestA.Class1.Constraint1");
             assertEquals(2,cnstr.getattrvaluecount(ch.interlis.models.IlisMeta16.ModelData.UniqueConstraint.tag_UniqueDef));
             {
                 IomObject pathOrInspFactor=cnstr.getattrobj(ch.interlis.models.IlisMeta16.ModelData.UniqueConstraint.tag_UniqueDef,0);
                 assertEquals(2,pathOrInspFactor.getattrvaluecount(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor.tag_PathEls));
                 {
                     IomObject pathEl=pathOrInspFactor.getattrobj(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor.tag_PathEls,0);
                     IomObject refObj=pathEl.getattrobj(ch.interlis.models.IlisMeta16.ModelData.PathEl.tag_Ref,0);
                     assertEquals("LocalUnique23.TestA.Class1.struct1",refObj.getobjectrefoid());
                 }
                 {
                     IomObject pathEl=pathOrInspFactor.getattrobj(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor.tag_PathEls,1);
                     IomObject refObj=pathEl.getattrobj(ch.interlis.models.IlisMeta16.ModelData.PathEl.tag_Ref,0);
                     assertEquals("LocalUnique23.TestA.Struct1.a1",refObj.getobjectrefoid());
                 }
             }
             {
                 IomObject pathOrInspFactor=cnstr.getattrobj(ch.interlis.models.IlisMeta16.ModelData.UniqueConstraint.tag_UniqueDef,1);
                 assertEquals(2,pathOrInspFactor.getattrvaluecount(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor.tag_PathEls));
                 {
                     IomObject pathEl=pathOrInspFactor.getattrobj(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor.tag_PathEls,0);
                     IomObject refObj=pathEl.getattrobj(ch.interlis.models.IlisMeta16.ModelData.PathEl.tag_Ref,0);
                     assertEquals("LocalUnique23.TestA.Class1.struct1",refObj.getobjectrefoid());
                 }
                 {
                     IomObject pathEl=pathOrInspFactor.getattrobj(ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor.tag_PathEls,1);
                     IomObject refObj=pathEl.getattrobj(ch.interlis.models.IlisMeta16.ModelData.PathEl.tag_Ref,0);
                     assertEquals("LocalUnique23.TestA.Struct1.a2",refObj.getobjectrefoid());
                 }
             }
             
        }
    }

}
