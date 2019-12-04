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

public class View23Test {
	
	private static final String TEST_OUT="test/data/ili23/view/";
	
	// This test checks if the compiler accepts a view basename in the EQUAL part of an Aggregation View.
	@Ignore
	@Test
	public void view_ViewBasenameEQUALAggregationView() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_ViewBasenameEQUALAggregationView.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}

	// This test checks if the compiler accepts a PARENT path element.
	@Test
	public void view_AcceptPARENTPathElement() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_AcceptPARENTPathElement.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a simple base extention in a view definition.
	@Test
	public void view_AcceptSimpleBaseExtentionInView() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_AcceptSimpleBaseExtentionInView.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects a attribute name that conflicts with a base view name.
	@Test
	public void view_DetectAttrnameConflictWithBaseViewname_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_DetectAttrnameConflictWithBaseViewname.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(21, compilerLogEvent.getLine());
		assertEquals("VIEW Test.View.VB can not declare several attributes bearing the same name. There is already an attribute \"a\".", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler detects a duplicate attribute name in a view.
	@Test
	public void view_DetectDublicateAttrnameInView_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_DetectDublicateAttrnameInView.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(27, compilerLogEvent.getLine());
		assertEquals("VIEW Test.Join.BC can not declare several attributes bearing the same name. There is already an attribute \"Attr1\".", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts a basename of an renamed inspection definition.
	@Test
	public void view_AcceptBasenameOfRenamedInspectionDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_AcceptBasenameOfRenamedInspectionDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a basename of an unrenamed inspection definition.
	@Test
	public void view_AcceptBasenameOfUnrenamedInspectionDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_AcceptBasenameOfUnrenamedInspectionDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects an illegal base extension in a view definition.
	@Test
	public void view_DetectAnIllegalBaseExtensionInView_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_DetectAnIllegalBaseExtensionInView.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(26, compilerLogEvent.getLine());
		assertEquals("C must be an extension of B.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts a join definition without the use of base-names.
	@Test
	public void view_AcceptJoinDefWithoutBasenames() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_AcceptJoinDefWithoutBasenames.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a basic aggregation definition.
	@Ignore
	@Test
	public void view_AcceptBasicAggregationDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_AcceptBasicAggregationDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a basic view projection definition.
	@Test
	public void view_AcceptBasicViewProjectionDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_AcceptBasicViewProjectionDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a basic inspection definition of a geometry type.
	@Ignore
	@Test
	public void view_AcceptBasicInspectionDefOfGeoType() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_AcceptBasicInspectionDefOfGeoType.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a basic inspection definition.
	@Test
	public void view_AcceptBasicInspectionDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_AcceptBasicInspectionDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a basic union definition.
	@Test
	public void view_AcceptBasicUnionDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_AcceptBasicUnionDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a basic join definition.
	@Test
	public void view_AcceptBasicJoinDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_AcceptBasicJoinDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts a basic selection view definition.
	@Test
	public void view_AcceptBasicSelectionViewDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"view_AcceptBasicSelectionViewDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects an abstract attribute in an concret view definition.
	@Ignore
	@Test
	public void abstractView_DetectAbstractAllOfAttrInConcreteViewDef_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"abstractView_DetectAbstractAllOfAttrInConcreteViewDef.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(26, compilerLogEvent.getLine());
		assertEquals("VIEW Test.Join.BC must be declared as ABSTRACT. One of its attributes (Attr1) is abstract.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler detects an abstract attribute in an concret view definition.
	@Ignore
	@Test
	public void abstractView_DetectAbstractAttrInConcretViewDef_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"abstractView_DetectAbstractAttrInConcretViewDef.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(26, compilerLogEvent.getLine());
		assertEquals("VIEW Test.Join.BC must be declared as ABSTRACT. One of its attributes (Attr1) is abstract.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts an abstract base in an concret view definition.
	@Test
	public void abstractView_AbstractBaseInConcretViewDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"abstractView_AbstractBaseInConcretViewDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler accepts an abstract base in an abstract join definition.
	@Test
	public void abstractView_AcceptAbstractBaseInAbstractJoinDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"abstractView_AcceptAbstractBaseInAbstractJoinDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
}