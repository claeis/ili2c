package ch.interlis.ili2c.generator.imd;

import org.junit.Assert;

import java.io.File;
import java.util.HashMap;

import org.junit.Test;

import ch.ehi.basics.settings.Settings;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.config.GenerateOutputKind;
import ch.interlis.ili2c.metamodel.TransferDescription;
import ch.interlis.iom.IomObject;
import ch.interlis.iom_j.xtf.Xtf24Reader;
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
    public static final String SIMPLE24_ILI = "test/data/imdgenerator/Simple24.ili";
    private static final String ILIS_META16_ILI = "standard/IlisMeta16.ili";
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
    public void ili24Test() throws Iox2jtsException, IoxException {
        final String OUT_FILE = "Simple24-out.imd";
        // generate imd file
        {
            // compile model
            TransferDescription td=null;
            Configuration ili2cConfig=new Configuration();
            FileEntry fileEntry=new FileEntry(SIMPLE24_ILI, FileEntryKind.ILIMODELFILE);
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

}
