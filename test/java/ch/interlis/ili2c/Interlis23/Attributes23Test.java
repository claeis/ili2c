package ch.interlis.ili2c.Interlis23;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerLogEvent;
import ch.interlis.ili2c.Ili2c;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class Attributes23Test {
	
	private static final String TEST_OUT="test/data/ili23/attribute/";
	
	// This test checks if the compiler detects a duplicate attribute name.
	@Test
	public void DublicateAttrNames_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"DublicateAttrNames_Fail.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(2,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(7, compilerLogEvent.getLine());
		assertEquals("CLASS model.topic.class can not declare several attributes bearing the same name. There is already an attribute \"attr\".", compilerLogEvent.getRawEventMsg());
	
		CompilerLogEvent logEvent2= (CompilerLogEvent) errs.getErrs().get(1);
		CompilerLogEvent compilerLogEvent2=(CompilerLogEvent) logEvent2;
		assertEquals(7, compilerLogEvent2.getLine());
		assertEquals("Text types can only extend other text types.", compilerLogEvent2.getRawEventMsg());
	
	}
	
	// This test checks if the compiler accepts a basic attribute definition.
	@Test
	public void BasicAttributeDefinition() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"BasicAttributeDefinition.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects a TRANSIENT attribute without a factor.
	@Test
	public void TransientAttributeWithoutAFactor_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"TransientAttributeWithoutAFactor_Fail.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(6, compilerLogEvent.getLine());
		assertEquals("TRANSIENT attribute \"attr\" requires an assignment of a factor.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts an abstract TRANSIENT attribute without a factor.
	@Test
	public void TransientAttributeWithoutAFactor_Ok() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"TransientAttributeWithoutAFactor_Ok.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects an extended attribute with a different TRANSIENT mode.
	@Test
	public void ExtendedAttrWithDiffTransientMode_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"ExtendedAttrWithDiffTransientMode_Fail.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(9, compilerLogEvent.getLine());
		assertEquals("TRANSIENT mode of attribute \"attr\" requires to be the same as of base attribute.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts a refattr in a struct at model level.
	@Test
	public void RefAttrInStructModelLevel() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"RefAttrInStructModelLevel.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects a reference attribute to a structure.
	@Test
	public void RefAttrToStruct_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"RefAttrToStruct_Fail.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(8, compilerLogEvent.getLine());
		assertEquals("A reference attribute (attr) is only allowed to reference a class.", compilerLogEvent.getRawEventMsg());
	}
    @Test
    public void RefAttrInClass_Fail() {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT+"RefAttrInClass_Fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
        }
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
        assertEquals(8, compilerLogEvent.getLine());
        assertEquals("A reference attribute (attr) is only allowed in a STRUCTURE.", compilerLogEvent.getRawEventMsg());
    }
	
	// This test checks if the compiler accepts a reference attribute to ANYCLASS.
	@Test
	public void RefAttrToAnyClass() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"RefAttrToAnyClass.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects a cross topic reference attribute.
	@Test
	public void RefAttrCrossTopic_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"RefAttrCrossTopic_Fail.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(33, compilerLogEvent.getLine());
		assertEquals("A cross topic reference requires property EXTERNAL.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts a basic reference attribute definition.
	@Test
	public void BasicRefAttrDefinition() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"BasicRefAttrDefinition.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts an AttriutePathType with a ClassType restriction
	@Test
	public void attributePathType_ClassTypeRestriction() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"attributePathType_ClassTypeRestriction.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects an AttriutePathType with a non-ClassType AttributePath restriction
	@Test
	public void attributePathType_NonClassTypeAttrPathRestriction_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"attributePathType_NonClassTypeAttrPathRestriction.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals(17, compilerLogEvent.getLine());
		assertEquals("\"objClass\" is not a path to a ClassType attribute.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts an AttriutePathType with a Argument restriction
	@Test
	public void attributePathType_ArgumentRestriction() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"attributePathType_ArgumentRestriction.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects an extended attribute with a different type.
	@Test
	public void ExtendedAttrWithDiffType_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"ExtendedAttrWithDiffType_Fail.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(1,errs.getErrs().size());
		CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
		CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
		assertEquals("model.Coord is not extending model.Hoehe", compilerLogEvent.getRawEventMsg());
	}
    @Test
    public void ExtendedAttrStructWithDiffCardinality_Fail() {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT+"ExtendedAttrStructWithDiffCardinality_Fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
        }
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
        assertEquals("An attribute with cardinality {1..*} can not extend one whose cardinality is {0..1}. The cardinality must be more restrictive.", compilerLogEvent.getRawEventMsg());
    }
    @Test
    public void ExtendedAttrRefWithDiffCardinality_Fail() {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT+"ExtendedAttrRefWithDiffCardinality_Fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
        }
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
        assertEquals("An attribute with cardinality {0..1} can not extend one whose cardinality is {1}. The cardinality must be more restrictive.", compilerLogEvent.getRawEventMsg());
    }
    @Test
    public void ExtendedAttrSimpleWithDiffCardinality_Fail() {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(TEST_OUT+"ExtendedAttrSimpleWithDiffCardinality_Fail.ili", FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=null;
        try{
            td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        }catch(Ili2cFailure ex){
        }
        assertNull(td);
        assertEquals(1,errs.getErrs().size());
        CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
        CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
        assertEquals("An attribute with cardinality {0..1} can not extend one whose cardinality is {1}. The cardinality must be more restrictive.", compilerLogEvent.getRawEventMsg());
    }
	
	// This test checks if the compiler accepts an extended attribute in an association.
	@Test
	public void ExtendedAttrInAnAssociation() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"ExtendedAttrInAnAssociation.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts an extended attribute with the same enumeration domain.
	@Test
	public void ExtendedAttrWithSameEnumDomain() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"ExtendedAttrWithSameEnumDomain.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
	}
}