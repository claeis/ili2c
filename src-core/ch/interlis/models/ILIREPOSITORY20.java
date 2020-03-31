package ch.interlis.models;
public class ILIREPOSITORY20{
  private ILIREPOSITORY20() {}
  public final static String MODEL= "IliRepository20";
  public final static String RepositoryIndex= "IliRepository20.RepositoryIndex";
  public static ch.interlis.iom_j.xtf.XtfModel getXtfModel(){ return new ch.interlis.iom_j.xtf.XtfModel("IliRepository20","http://models.interlis.ch/core","2020-01-15"); }
  static public ch.interlis.iox.IoxFactory getIoxFactory()
  {
    return new ch.interlis.iox.IoxFactory(){
      public ch.interlis.iom.IomObject createIomObject(String type,String oid) throws ch.interlis.iox.IoxException {
      if(type.equals("IliRepository20.ModelName_"))return new ch.interlis.models.IliRepository20.ModelName_();
      if(type.equals("IliRepository20.WebService_"))return new ch.interlis.models.IliRepository20.WebService_();
      if(type.equals("IliRepository20.WebSite_"))return new ch.interlis.models.IliRepository20.WebSite_();
      if(type.equals("IliRepository20.RepositoryIndex.ModelMetadata"))return new ch.interlis.models.IliRepository20.RepositoryIndex.ModelMetadata(oid);
      return null;
      }
    };
  }
  static public ch.interlis.iom_j.ViewableProperties getIoxMapping()
  {
    ch.interlis.iom_j.ViewableProperties mapping=new ch.interlis.iom_j.ViewableProperties();
    java.util.HashMap<String,String> nameMap=new java.util.HashMap<String,String>();
    nameMap.put("IliRepository20.RepositoryIndex","RepositoryIndex");
    nameMap.put("IliRepository20.ModelName_", "ModelName_");
    mapping.defineClass("IliRepository20.ModelName_", new String[]{   "value"
      });
    nameMap.put("IliRepository20.WebService_", "WebService_");
    mapping.defineClass("IliRepository20.WebService_", new String[]{   "value"
      });
    nameMap.put("IliRepository20.WebSite_", "WebSite_");
    mapping.defineClass("IliRepository20.WebSite_", new String[]{   "value"
      });
    nameMap.put("IliRepository20.RepositoryIndex.ModelMetadata", "ModelMetadata");
    mapping.defineClass("IliRepository20.RepositoryIndex.ModelMetadata", new String[]{   "Name"
      ,"SchemaLanguage"
      ,"File"
      ,"Version"
      ,"VersionComment"
      ,"publishingDate"
      ,"Original"
      ,"dependsOnModel"
      ,"precursorVersion"
      ,"followupModel"
      ,"derivedModel"
      ,"Title"
      ,"shortDescription"
      ,"Tags"
      ,"Issuer"
      ,"technicalContact"
      ,"furtherInformation"
      ,"furtherMetadata"
      ,"knownWMS"
      ,"knownWFS"
      ,"knownPortal"
      ,"browseOnly"
      ,"md5"
      });
    mapping.setXtf24nameMapping(nameMap);
    return mapping;
  }
}
