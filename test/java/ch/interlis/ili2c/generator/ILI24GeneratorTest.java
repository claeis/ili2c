package ch.interlis.ili2c.generator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;

import ch.interlis.ili2c.CompilerTestHelper;
import ch.interlis.ili2c.metamodel.Constraint;
import ch.interlis.ili2c.metamodel.ContextDef;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.Model;
import org.junit.Assert;
import org.junit.Test;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.LogCollector;
import ch.interlis.ili2c.Interlis24.Attribute24Test;
import ch.interlis.ili2c.config.Configuration;
import ch.interlis.ili2c.config.FileEntry;
import ch.interlis.ili2c.config.FileEntryKind;
import ch.interlis.ili2c.generator.nls.Ili2TranslationXmlTest;
import ch.interlis.ili2c.metamodel.DataModel;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.TransferDescription;

public class ILI24GeneratorTest {
    private static final String ILI_FILE = "test/data/interlis2generator/EnumOk24.ili";
    private static final String CONTEXT_ILI_FILE = "test/data/interlis2generator/Context.ili";
    private static final String DOMAIN_ILI_FILE = "test/data/interlis2generator/Domain.ili";
    private static final String STRING_ILI_FILE = "test/data/interlis2generator/String.ili";
    private static final String OUTPUT_ILI_FILE = "out.ili";
    @Test
    public void model() throws Exception {
        // ili lesen
        TransferDescription td = Ili2TranslationXmlTest.compileIliModel(new File(ILI_FILE));
        // neues ili schreiben
        java.io.Writer out = new java.io.OutputStreamWriter(new FileOutputStream(OUTPUT_ILI_FILE),"UTF-8");
        new Interlis2Generator().generate(out, td, false);
        out.close();
        // neues ili lesen
        TransferDescription newTd = Ili2TranslationXmlTest.compileIliModel(new File(OUTPUT_ILI_FILE), null);
        assertNotNull(newTd);
        Element modelEle = newTd.getElement("EnumOk24A");
        assertNotNull(modelEle);
        assertEquals(DataModel.class, modelEle.getClass());
        assertEquals(DataModel.ILI2_4,((DataModel)modelEle).getIliVersion());
    }
    @Test
    public void dateTime() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(Attribute24Test.ILI_DATE_TIME, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        Assert.assertNotNull(td);
        Assert.assertEquals(0,errs.getErrs().size());
        java.io.StringWriter syntaxBuffer=new java.io.StringWriter();
        Interlis2Generator makeSyntax=Interlis2Generator.generateElements24(syntaxBuffer,td);
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.DomainDate"), null);
            Assert.assertEquals("DOMAIN DomainDate = DATE;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.TopicA.ClassA.attrDate"), null);
            Assert.assertEquals("attrDate : DATE;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
    }
    @Test
    public void multiCoord() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(Attribute24Test.ILI_MULTICOORD, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        Assert.assertNotNull(td);
        Assert.assertEquals(0,errs.getErrs().size());
        java.io.StringWriter syntaxBuffer=new java.io.StringWriter();
        Interlis2Generator makeSyntax=Interlis2Generator.generateElements24(syntaxBuffer,td);
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.DomainMultiCoord"), null);
            Assert.assertEquals("DOMAIN DomainMultiCoord = MULTICOORD 2460000.000 .. 2870000.000, 1045000.000 .. 1310000.000;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.TopicA.ClassA.attrMultiCoord"), null);
            Assert.assertEquals("attrMultiCoord : MULTICOORD 2460000.000 .. 2870000.000, 1045000.000 .. 1310000.000;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
    }
    @Test
    public void multiPolyline() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(Attribute24Test.ILI_MULTIPOLYLINE, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        Assert.assertNotNull(td);
        Assert.assertEquals(0,errs.getErrs().size());
        java.io.StringWriter syntaxBuffer=new java.io.StringWriter();
        Interlis2Generator makeSyntax=Interlis2Generator.generateElements24(syntaxBuffer,td);
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.DomainMultiPolyline"), null);
            Assert.assertEquals("DOMAIN DomainMultiPolyline = MULTIPOLYLINE WITH (STRAIGHTS, ARCS) VERTEX ModelA.Coord;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.TopicA.ClassA.attrMultiPolyline"), null);
            Assert.assertEquals("attrMultiPolyline : MULTIPOLYLINE WITH (STRAIGHTS, ARCS) VERTEX ModelA.Coord;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
    }
    @Test
    public void multiSurface() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(Attribute24Test.ILI_MULTISURFACE, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        Assert.assertNotNull(td);
        Assert.assertEquals(0,errs.getErrs().size());
        java.io.StringWriter syntaxBuffer=new java.io.StringWriter();
        Interlis2Generator makeSyntax=Interlis2Generator.generateElements24(syntaxBuffer,td);
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.DomainMultiSurface"), null);
            Assert.assertEquals("DOMAIN DomainMultiSurface = MULTISURFACE WITH (STRAIGHTS, ARCS) VERTEX ModelA.Coord WITHOUT OVERLAPS > 0.001;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.TopicA.ClassA.attrMultiSurface"), null);
            Assert.assertEquals("attrMultiSurface : MULTISURFACE WITH (STRAIGHTS, ARCS) VERTEX ModelA.Coord WITHOUT OVERLAPS > 0.001;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
    }
    @Test
    public void multiArea() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(Attribute24Test.ILI_MULTIAREA, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        Assert.assertNotNull(td);
        Assert.assertEquals(0,errs.getErrs().size());
        java.io.StringWriter syntaxBuffer=new java.io.StringWriter();
        Interlis2Generator makeSyntax=Interlis2Generator.generateElements24(syntaxBuffer,td);
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.DomainMultiArea"), null);
            Assert.assertEquals("DOMAIN DomainMultiArea = MULTIAREA WITH (STRAIGHTS, ARCS) VERTEX ModelA.Coord WITHOUT OVERLAPS > 0.001;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.TopicA.ClassA.attrMultiArea"), null);
            Assert.assertEquals("attrMultiArea : MULTIAREA WITH (STRAIGHTS, ARCS) VERTEX ModelA.Coord WITHOUT OVERLAPS > 0.001;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
    }
    @Test
    public void text_list() throws Exception {
        LogCollector errs=new LogCollector();
        EhiLogger.getInstance().addListener(errs);
        Configuration ili2cConfig=new Configuration();
        FileEntry fileEntry=new FileEntry(Attribute24Test.ILI_TEXT_LIST, FileEntryKind.ILIMODELFILE);
        ili2cConfig.addFileEntry(fileEntry);
        TransferDescription td=ch.interlis.ili2c.Ili2c.runCompiler(ili2cConfig);
        Assert.assertNotNull(td);
        Assert.assertEquals(0,errs.getErrs().size());
        java.io.StringWriter syntaxBuffer=new java.io.StringWriter();
        Interlis2Generator makeSyntax=Interlis2Generator.generateElements24(syntaxBuffer,td);
        {
            syntaxBuffer.getBuffer().setLength(0);
            makeSyntax.printElement(null, null, td.getElement("ModelA.TopicA.ClassA.attrA"), null);
            Assert.assertEquals("attrA : LIST {0..*} OF TEXT*20;",syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
        }
    }

    @Test
    public void genericCoord() {
        TransferDescription td = CompilerTestHelper.getTransferDescription(CONTEXT_ILI_FILE);
        assertNotNull(td);
        Domain domain = (Domain) td.getElement("ModelA.Coord2");

        java.io.StringWriter syntaxBuffer = new java.io.StringWriter();
        Interlis2Generator makeSyntax = Interlis2Generator.generateElements24(syntaxBuffer, td);
        makeSyntax.printDomainDef(domain.getContainer(), domain, null);

        Assert.assertEquals("Coord2 (GENERIC) = COORD NUMERIC, NUMERIC;", syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
    }

    @Test
    public void context() {
        TransferDescription td = CompilerTestHelper.getTransferDescription(CONTEXT_ILI_FILE);
        assertNotNull(td);
        ContextDef contextDef = (ContextDef) td.getElement("ModelA.default");

        java.io.StringWriter syntaxBuffer = new java.io.StringWriter();
        Interlis2Generator makeSyntax = Interlis2Generator.generateElements24(syntaxBuffer,td);
        makeSyntax.printContext(contextDef.getContainer(), contextDef);

        Assert.assertEquals("default = ModelA.Coord2 = ModelA.Coord2_CHLV03 OR ModelA.Coord2_CHLV95;", syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
    }

    @Test
    public void contextSyntax() {
        TransferDescription td = CompilerTestHelper.getTransferDescription(CONTEXT_ILI_FILE);
        assertNotNull(td);
        ContextDef contextDef = (ContextDef) td.getElement("ModelA.default");

        java.io.StringWriter syntaxBuffer = new java.io.StringWriter();
        Interlis2Generator makeSyntax = Interlis2Generator.generateElements24(syntaxBuffer,td);
        makeSyntax.printContextSyntax(contextDef.getContainer(), contextDef);

        Assert.assertEquals("ModelA.Coord2 = ModelA.Coord2_CHLV03 OR ModelA.Coord2_CHLV95;", syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
    }

    @Test
    public void domainSingleConstraint() {
        TransferDescription td = CompilerTestHelper.getTransferDescription(DOMAIN_ILI_FILE);
        assertNotNull(td);
        Domain domain = (Domain) td.getElement("ModelA.SingleConstraint");

        java.io.StringWriter syntaxBuffer = new java.io.StringWriter();
        Interlis2Generator makeSyntax = Interlis2Generator.generateElements24(syntaxBuffer,td);
        makeSyntax.printDomainDef(domain.getContainer(), domain, null);

        Assert.assertEquals("SingleConstraint = TEXT*30 CONSTRAINTS Values: THIS == \"SomeConstant\" OR THIS == \"OtherConstant\";", syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
    }

    @Test
    public void domainMultipleConstraints() {
        TransferDescription td = CompilerTestHelper.getTransferDescription(DOMAIN_ILI_FILE);
        assertNotNull(td);
        Domain domain = (Domain) td.getElement("ModelA.MultipleConstraints");

        java.io.StringWriter syntaxBuffer = new java.io.StringWriter();
        Interlis2Generator makeSyntax = Interlis2Generator.generateElements24(syntaxBuffer,td);
        makeSyntax.printDomainDef(domain.getContainer(), domain, null);

        Assert.assertEquals("MultipleConstraints = TEXT*30 CONSTRAINTS Values: THIS == \"SomeConstant\" OR THIS == \"OtherConstant\", Length: INTERLIS.len(THIS) > 5;", syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
    }

    @Test
    public void stringEscapeSequencesModelVersion() {
        TransferDescription td = CompilerTestHelper.getTransferDescription(STRING_ILI_FILE);
        assertNotNull(td);

        Model model = (Model) td.getElement("ModelA");
        assertEquals("1.2", model.getModelVersion());
    }

    @Test
    public void stringEscapeSequencesQuoteBackslash() {
        TransferDescription td = CompilerTestHelper.getTransferDescription(STRING_ILI_FILE);
        assertNotNull(td);

        Constraint constraint = (Constraint) td.getElement("ModelA.TopicA.ClassA.quoteBackslash");

        java.io.StringWriter syntaxBuffer = new java.io.StringWriter();
        Interlis2Generator makeSyntax = Interlis2Generator.generateElements24(syntaxBuffer,td);
        makeSyntax.printConstraint(constraint);

        assertEquals("MANDATORY CONSTRAINT quoteBackslash: attrA <> \"\\\"\" AND attrA <> \"\\\\\";", syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
    }

    @Test
    public void stringEscapeSequencesUnicode() {
        TransferDescription td = CompilerTestHelper.getTransferDescription(STRING_ILI_FILE);
        assertNotNull(td);

        Constraint constraint = (Constraint) td.getElement("ModelA.TopicA.ClassA.unicode");

        java.io.StringWriter syntaxBuffer = new java.io.StringWriter();
        Interlis2Generator makeSyntax = Interlis2Generator.generateElements24(syntaxBuffer,td);
        makeSyntax.printConstraint(constraint);

        assertEquals("MANDATORY CONSTRAINT unicode: attrA <> \"\\u000a\" AND attrA <> \"\\u000d\";", syntaxBuffer.toString().replaceAll("\\s+", " ").trim());
    }
}