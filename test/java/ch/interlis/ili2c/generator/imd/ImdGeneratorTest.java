package ch.interlis.ili2c.generator.imd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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

public class ImdGeneratorTest {
    @Test
    public void ili23Test() throws Iox2jtsException, IoxException {
        final String OUT_FILE = "Simple23-out.imd";
        // generate imd file
        {
            // compile model
            TransferDescription td=null;
            Configuration ili2cConfig=new Configuration();
            FileEntry fileEntry=new FileEntry("test/data/imdgenerator/Simple23.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setOutputFile(OUT_FILE);
            ili2cConfig.setOutputKind(GenerateOutputKind.IMD);
            td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
            assertNotNull(td);
            
        }
        
        // verify
        {
            // compile model
            TransferDescription td=null;
            Configuration ili2cConfig=new Configuration();
            FileEntry fileEntry=new FileEntry("standard/IlisMeta07.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
            assertNotNull(td);
            
            ValidationConfig modelConfig = new ValidationConfig();
            LogCollector logger = new LogCollector();
            LogEventFactory errFactory = new LogEventFactory();
            PipelinePool pipelinePool = new PipelinePool();
            Settings settings = new Settings();
            Validator validator = new Validator(td, modelConfig, logger, errFactory, pipelinePool, settings);
            
            XtfReader reader=new XtfReader(new File(OUT_FILE));
            reader.setModel(td);
            IoxEvent event=null;
            HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
             do{
                    event=reader.read();
                    //validator.validate(event);
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
            FileEntry fileEntry=new FileEntry("test/data/imdgenerator/Simple10.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            ili2cConfig.setOutputFile(OUT_FILE);
            ili2cConfig.setOutputKind(GenerateOutputKind.IMD);
            td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
            assertNotNull(td);
            
        }
        
        // verify
        {
            // compile model
            TransferDescription td=null;
            Configuration ili2cConfig=new Configuration();
            FileEntry fileEntry=new FileEntry("standard/IlisMeta07.ili", FileEntryKind.ILIMODELFILE);
            ili2cConfig.addFileEntry(fileEntry);
            td=ch.interlis.ili2c.Main.runCompiler(ili2cConfig);
            assertNotNull(td);
            
            ValidationConfig modelConfig = new ValidationConfig();
            LogCollector logger = new LogCollector();
            LogEventFactory errFactory = new LogEventFactory();
            PipelinePool pipelinePool = new PipelinePool();
            Settings settings = new Settings();
            Validator validator = new Validator(td, modelConfig, logger, errFactory, pipelinePool, settings);
            
            XtfReader reader=new XtfReader(new File(OUT_FILE));
            reader.setModel(td);
            IoxEvent event=null;
            HashMap<String,IomObject> objs=new HashMap<String,IomObject>();
             do{
                    event=reader.read();
                    //validator.validate(event);
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
