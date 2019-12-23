package ch.interlis.ili2c.Interlis22;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;
import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.CompilerLogEvent;
import ch.interlis.ili2c.Ili2cFailure;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class Associations22Test {
	
	private static final String TEST_OUT="test/data/ili22/associations/";
	
	// This test checks if the compiler accepts a basic association definition.
	@Test
	public void acceptBasicAssocDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"acceptBasicAssocDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects an illegal role definition (starting with a ':').
	@Ignore
	@Test
	public void detectIllegalRoleDef_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectIllegalRoleDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNull(td);
		assertEquals(3,errs.getErrs().size());
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(14, compilerLogEvent.getLine());
			assertEquals("unexpected token: --", compilerLogEvent.getRawEventMsg());
		}
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(1);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(15, compilerLogEvent.getLine());
			assertEquals("expecting ':', found '--'", compilerLogEvent.getRawEventMsg());
		}
		{
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(2);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(15, compilerLogEvent.getLine());
			assertEquals("expecting ':', found ';'", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler detects an illegal cardinality definition (numbers seperated by ',').
	@Test
	public void detectIllegalCardinalityDef_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectIllegalCardinalityDef.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(14, compilerLogEvent.getLine());
		assertEquals("unexpected token: ,", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler detects an additional role in a extendeted association definition.
	@Test
	public void detectAdditionalRoleInExtAssoc_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectAdditionalRoleInExtAssoc.ili", FileEntryKind.ILIMODELFILE);
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
		assertEquals(22, compilerLogEvent.getLine());
		assertEquals("There can not be an additional role \"c\" in an extended association.", compilerLogEvent.getRawEventMsg());
	}
	
	// This test checks if the compiler accepts an extended role.
	@Ignore
	@Test
	public void acceptExtRole() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"acceptExtRole.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects an extended role with a lesser aggregation kind than its base.
	@Test
	public void detectExtRoleWithLesserAggregationThanBase_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectExtRoleWithLesserAggregationThanBase.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(21, compilerLogEvent.getLine());
			assertEquals("A role with a weaker aggregation kind can not extend a stronger one.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler detects an extended role with a wider cardinality than its base.
	@Test
	public void detectExtRoleCardWiderThanBase_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectExtRoleCardWiderThanBase.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(21, compilerLogEvent.getLine());
			assertEquals("A role with cardinality {1..20} can not extend one whose cardinality is {1..10}. The cardinality must be more restrictive.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler detects multiple aggregation roles.
	@Test
	public void detectMultipleAggregationRoles_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectMultipleAggregationRoles.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(16, compilerLogEvent.getLine());
			assertEquals("There can only be one role with kind aggregation or kind composition in a association.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler detects abstract role in a concrete association.
	@Ignore
	@Test
	public void detectAbstractRoleInConcreteAssoc_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectAbstractRoleInConcreteAssoc.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(17, compilerLogEvent.getLine());
			assertEquals("ASSOCIATION model.topic.a2b is not ABSTRACT; therefore, it can not have abstract roles.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler accepts the property EXTENDED in a AssociationDef.
	@Test
	public void acceptPropertyEXTENDEDInAssoc() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"acceptPropertyEXTENDEDInAssoc.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects a duplicate role name.
	@Ignore
	@Test
	public void detectDublicateRoleName_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectDublicateRoleName.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(15, compilerLogEvent.getLine());
			assertEquals("There can not be an RoleDef \"a\" in ASSOCIATION model.topic.a because there is already one with the same name. Use a different name.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler detects a role name that is in conflict with an attribute name.
	@Test
	public void detectRolenameConflictWithAttrname_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectRolenameConflictWithAttrname.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(16, compilerLogEvent.getLine());
			assertEquals("There can not be an attribute \"a\" in ASSOCIATION model.topic.ab because there is already an attribute or a role with the same name. Use a different name.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler detects a role name that is in conflict with an attribute name in the source class.
	@Test
	public void detectNameConflictsInClass_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectNameConflictsInClass.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(14, compilerLogEvent.getLine());
			assertEquals("There can not be a role \"b\", because there is already an attribute or role with the same name in role a's target (ClassA).", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler detects a role name that is, as part of the namespace formed by the source class, in conflict with an other role name.
	@Test
	public void detectRolenameIsPartOfNamespace_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectRolenameIsPartOfNamespace.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(21, compilerLogEvent.getLine());
			assertEquals("There can not be a role \"other\", because there is already an attribute or role with the same name in role a's target (ClassA).", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler detects a attribute name in an extended class, that is in conflict with the name of a target role in the base class.
	@Test
	public void detectExtClassAttrAndTargetRoleNameConflicts() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectExtClassAttrAndTargetRoleNameConflicts.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(19, compilerLogEvent.getLine());
			assertEquals("CLASS model.topic.APlus has already a target role with name b.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler accepts a derived association definition.
	@Ignore
	@Test
	public void acceptDerivedAssocDef() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"acceptDerivedAssocDef.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
		}
		assertNotNull(td);
		assertEquals(0,errs.getErrs().size());
	}
	
	// This test checks if the compiler detects a cardinality >1 in a composition role.
	@Test
	public void detectCardInCompositionRole_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectCardInCompositionRole.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(14, compilerLogEvent.getLine());
			assertEquals("The maximum cardinality of role a should not exceed 1, because its a composition.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler detects a cross topic role.
	@Test
	public void detectCrossTopicRole_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectCrossTopicRole.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(44, compilerLogEvent.getLine());
			assertEquals("A cross topic role requires property EXTERNAL.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler accepts the same role name in different extended topics.
	@Test
	public void acceptSameRolenameInDiffExtTopics_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"acceptSameRolenameInDiffExtTopics.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(16, compilerLogEvent.getLine());
			assertEquals("There can only be one role with kind aggregation or kind composition in a association.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler accepts a role that references ANYCLASS.
	@Test
	public void acceptRoleRefToANYCLASS() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"acceptRoleRefToANYCLASS.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNotNull(td);
			assertEquals(0,errs.getErrs().size());
		}
	}
	
	// This test checks if the compiler detects a role that points to a structure.
	@Test
	public void detectRoleWhichPointsToStruct_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectRoleWhichPointsToStruct.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(13, compilerLogEvent.getLine());
			assertEquals("A role (b) is only allowed to reference a class.", compilerLogEvent.getRawEventMsg());
		}
	}
	
	// This test checks if the compiler accepts an association with an association as target end.
	@Test
	public void acceptAssocWithAssocTargetEnd() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"acceptAssocWithAssocTargetEnd.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNotNull(td);
			assertEquals(0,errs.getErrs().size());
		}
	}
	
	// This test checks if the compiler accepts a role that references ANYCLASS with restriction.
	@Test
	public void acceptRoleRefANYCLASSWithRestriction() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"acceptRoleRefANYCLASSWithRestriction.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNotNull(td);
			assertEquals(0,errs.getErrs().size());
		}
	}
	
	// This test checks if the compiler checks that a role doesn't conflict with a attribute of an extended class.
	@Test
	public void checkRoleConflictWithAttrOfExtClass() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"checkRoleConflictWithAttrOfExtClass.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNotNull(td);
			assertEquals(0,errs.getErrs().size());
		}
	}
	
	// This test checks if the compiler detects an association without roles.
	@Test
	public void detectAssocWithoutRoles_Fail() {
		LogCollector errs=new LogCollector();
		EhiLogger.getInstance().addListener(errs);
		Configuration ili2cConfig=new Configuration();
		FileEntry fileEntry=new FileEntry(TEST_OUT+"detectAssocWithoutRoles.ili", FileEntryKind.ILIMODELFILE);
		ili2cConfig.addFileEntry(fileEntry);
		TransferDescription td=null;
		try{
			td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
		}catch(Ili2cFailure ex){
			assertNull(td);
			assertEquals(1,errs.getErrs().size());
			CompilerLogEvent logEvent= (CompilerLogEvent) errs.getErrs().get(0);
			CompilerLogEvent compilerLogEvent=(CompilerLogEvent) logEvent;
			assertEquals(8, compilerLogEvent.getLine());
			assertEquals("An association requires at least two role definitions.", compilerLogEvent.getRawEventMsg());
		}
	}
}