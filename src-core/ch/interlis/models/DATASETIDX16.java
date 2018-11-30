package ch.interlis.models;
public class DATASETIDX16{
  private DATASETIDX16() {}
  public final static String MODEL= "DatasetIdx16";
  public final static String DataIndex= "DatasetIdx16.DataIndex";
  public static ch.interlis.iom_j.xtf.XtfModel getXtfModel(){ return new ch.interlis.iom_j.xtf.XtfModel("DatasetIdx16","mailto:ce@eisenhutinformatik.ch","2018-11-21"); }
  static public ch.interlis.iox.IoxFactory getIoxFactory()
  {
    return new ch.interlis.iox.IoxFactory(){
      public ch.interlis.iom.IomObject createIomObject(String type,String oid) throws ch.interlis.iox.IoxException {
      if(type.equals("DatasetIdx16.DataFile"))return new ch.interlis.models.DatasetIdx16.DataFile();
      if(type.equals("DatasetIdx16.Metadata"))return new ch.interlis.models.DatasetIdx16.Metadata();
      if(type.equals("DatasetIdx16.DataIndex.Metadata"))return new ch.interlis.models.DatasetIdx16.DataIndex.Metadata(oid);
      if(type.equals("DatasetIdx16.DataLink"))return new ch.interlis.models.DatasetIdx16.DataLink();
      if(type.equals("DatasetIdx16.MultilingualText"))return new ch.interlis.models.DatasetIdx16.MultilingualText();
      if(type.equals("DatasetIdx16.Code_"))return new ch.interlis.models.DatasetIdx16.Code_();
      if(type.equals("DatasetIdx16.WebService_"))return new ch.interlis.models.DatasetIdx16.WebService_();
      if(type.equals("DatasetIdx16.LocalisedMText"))return new ch.interlis.models.DatasetIdx16.LocalisedMText();
      if(type.equals("DatasetIdx16.MultilingualMText"))return new ch.interlis.models.DatasetIdx16.MultilingualMText();
      if(type.equals("DatasetIdx16.BoundingBox"))return new ch.interlis.models.DatasetIdx16.BoundingBox();
      if(type.equals("DatasetIdx16.File"))return new ch.interlis.models.DatasetIdx16.File();
      if(type.equals("DatasetIdx16.DataIndex.BasketMetadata"))return new ch.interlis.models.DatasetIdx16.DataIndex.BasketMetadata();
      if(type.equals("DatasetIdx16.DataIndex.DatasetMetadata"))return new ch.interlis.models.DatasetIdx16.DataIndex.DatasetMetadata(oid);
      if(type.equals("DatasetIdx16.ModelLink"))return new ch.interlis.models.DatasetIdx16.ModelLink();
      if(type.equals("DatasetIdx16.QualityResult"))return new ch.interlis.models.DatasetIdx16.QualityResult();
      if(type.equals("DatasetIdx16.WebSite_"))return new ch.interlis.models.DatasetIdx16.WebSite_();
      if(type.equals("DatasetIdx16.LocalisedText"))return new ch.interlis.models.DatasetIdx16.LocalisedText();
      return null;
      }
    };
  }
  static public ch.interlis.iom_j.ViewableProperties getIoxMapping()
  {
    ch.interlis.iom_j.ViewableProperties mapping=new ch.interlis.iom_j.ViewableProperties();
    java.util.HashMap<String,String> nameMap=new java.util.HashMap<String,String>();
    nameMap.put("DatasetIdx16.DataIndex","DataIndex");
    nameMap.put("DatasetIdx16.DataFile", "DataFile");
    mapping.defineClass("DatasetIdx16.DataFile", new String[]{   "fileFormat"
      ,"file"
      });
    nameMap.put("DatasetIdx16.Metadata", "Metadata");
    mapping.defineClass("DatasetIdx16.Metadata", new String[]{   "id"
      ,"originalId"
      ,"version"
      ,"versionComment"
      ,"precursorVersion"
      ,"followupData"
      ,"derivedData"
      ,"sourceData"
      ,"model"
      ,"epsgCode"
      ,"geoScope"
      ,"resolutionScope"
      ,"publishingDate"
      ,"lastEditingDate"
      ,"original"
      ,"restrictions"
      ,"qualityResults"
      ,"owner"
      ,"boundary"
      });
    nameMap.put("DatasetIdx16.DataIndex.Metadata", "DataIndex.Metadata");
    mapping.defineClass("DatasetIdx16.DataIndex.Metadata", new String[]{   "id"
      ,"originalId"
      ,"version"
      ,"versionComment"
      ,"precursorVersion"
      ,"followupData"
      ,"derivedData"
      ,"sourceData"
      ,"model"
      ,"epsgCode"
      ,"geoScope"
      ,"resolutionScope"
      ,"publishingDate"
      ,"lastEditingDate"
      ,"original"
      ,"restrictions"
      ,"qualityResults"
      ,"owner"
      ,"boundary"
      ,"technicalContact"
      });
    nameMap.put("DatasetIdx16.DataLink", "DataLink");
    mapping.defineClass("DatasetIdx16.DataLink", new String[]{   "basketId"
      ,"datasetId"
      ,"localBasketId"
      });
    nameMap.put("DatasetIdx16.MultilingualText", "MultilingualText");
    mapping.defineClass("DatasetIdx16.MultilingualText", new String[]{   "LocalisedText"
      });
    nameMap.put("DatasetIdx16.Code_", "Code_");
    mapping.defineClass("DatasetIdx16.Code_", new String[]{   "value"
      });
    nameMap.put("DatasetIdx16.WebService_", "WebService_");
    mapping.defineClass("DatasetIdx16.WebService_", new String[]{   "value"
      });
    nameMap.put("DatasetIdx16.LocalisedMText", "LocalisedMText");
    mapping.defineClass("DatasetIdx16.LocalisedMText", new String[]{   "Language"
      ,"Text"
      });
    nameMap.put("DatasetIdx16.MultilingualMText", "MultilingualMText");
    mapping.defineClass("DatasetIdx16.MultilingualMText", new String[]{   "LocalisedText"
      });
    nameMap.put("DatasetIdx16.BoundingBox", "BoundingBox");
    mapping.defineClass("DatasetIdx16.BoundingBox", new String[]{   "westlimit"
      ,"southlimit"
      ,"eastlimit"
      ,"northlimit"
      });
    nameMap.put("DatasetIdx16.File", "File");
    mapping.defineClass("DatasetIdx16.File", new String[]{   "path"
      ,"md5"
      });
    nameMap.put("DatasetIdx16.DataIndex.BasketMetadata", "BasketMetadata");
    mapping.defineClass("DatasetIdx16.DataIndex.BasketMetadata", new String[]{   "id"
      ,"originalId"
      ,"version"
      ,"versionComment"
      ,"precursorVersion"
      ,"followupData"
      ,"derivedData"
      ,"sourceData"
      ,"model"
      ,"epsgCode"
      ,"geoScope"
      ,"resolutionScope"
      ,"publishingDate"
      ,"lastEditingDate"
      ,"original"
      ,"restrictions"
      ,"qualityResults"
      ,"owner"
      ,"boundary"
      ,"localId"
      });
    nameMap.put("DatasetIdx16.DataIndex.DatasetMetadata", "DatasetMetadata");
    mapping.defineClass("DatasetIdx16.DataIndex.DatasetMetadata", new String[]{   "id"
      ,"originalId"
      ,"version"
      ,"versionComment"
      ,"precursorVersion"
      ,"followupData"
      ,"derivedData"
      ,"sourceData"
      ,"model"
      ,"epsgCode"
      ,"geoScope"
      ,"resolutionScope"
      ,"publishingDate"
      ,"lastEditingDate"
      ,"original"
      ,"restrictions"
      ,"qualityResults"
      ,"owner"
      ,"boundary"
      ,"title"
      ,"shortDescription"
      ,"keywords"
      ,"categories"
      ,"servicer"
      ,"technicalContact"
      ,"furtherInformation"
      ,"furtherMetadata"
      ,"knownWMS"
      ,"knownWFS"
      ,"atomWS"
      ,"furtherWS"
      ,"knownPortal"
      ,"files"
      ,"baskets"
      });
    nameMap.put("DatasetIdx16.ModelLink", "ModelLink");
    mapping.defineClass("DatasetIdx16.ModelLink", new String[]{   "name"
      ,"locationHint"
      ,"versionHint"
      });
    nameMap.put("DatasetIdx16.QualityResult", "QualityResult");
    mapping.defineClass("DatasetIdx16.QualityResult", new String[]{   "summary"
      ,"code"
      ,"information"
      ,"data"
      });
    nameMap.put("DatasetIdx16.WebSite_", "WebSite_");
    mapping.defineClass("DatasetIdx16.WebSite_", new String[]{   "value"
      });
    nameMap.put("DatasetIdx16.LocalisedText", "LocalisedText");
    mapping.defineClass("DatasetIdx16.LocalisedText", new String[]{   "Language"
      ,"Text"
      });
    mapping.setXtf24nameMapping(nameMap);
    return mapping;
  }
}
