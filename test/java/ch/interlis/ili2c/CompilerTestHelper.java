package ch.interlis.ili2c;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.Container;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.TransferDescription;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CompilerTestHelper {
    public static TransferDescription getTransferDescription(String filePath){
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(filePath, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        try {
            return Ili2c.runCompiler(ili2cConfig);
        } catch (Ili2cFailure e) {
            Assert.fail(String.format("Compilation of %s failed", filePath));
            return null;
        }
    }

    public static LogCollector getCompileErrors(String filePath) {
        LogCollector logger = new LogCollector();
        EhiLogger.getInstance().addListener(logger);
        Configuration ili2cConfig = new Configuration();
        FileEntry fileEntry = new FileEntry(filePath, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        try {
            Ili2c.runCompiler(ili2cConfig);
            Assert.fail(String.format("Compilation of %s succeeded, but was expected to fail", filePath));
            return logger;
        } catch (Ili2cFailure e) {
            return logger;
        }
    }

    public static <T extends Element, E extends Element> List<T> getInstancesOfType(TransferDescription td, String scopedObjectName, Class<T> type) {
        ArrayList<T> result = new ArrayList<T>();
        for (Iterator<E> it = ((Container<E>)td.getElement(scopedObjectName)).iterator(); it.hasNext(); ) {
            Element element = it.next();
            if (type.isAssignableFrom(element.getClass())){
                result.add((T) element);
            }
        }
        return result;
    }
}
