package ch.interlis.ili2c.generator.nls;

import org.junit.Assert;
import org.junit.Test;

public class NlsModelElementTest {
    @Test
    public void testLangNull() {
        NlsModelElement nlsEle=new NlsModelElement();
        nlsEle.setName(null,"Name");
        nlsEle.setDocumentation("de","Documentation_de");
        java.util.Set<String> langs=new java.util.HashSet<String>(nlsEle.getLanguages());
        Assert.assertEquals(2,langs.size());
        Assert.assertTrue(langs.contains(null));
        Assert.assertTrue(langs.contains("de"));
        Assert.assertEquals("Name",nlsEle.getName(null));
        Assert.assertEquals("Documentation_de",nlsEle.getDocumentation("de"));
    }

}
