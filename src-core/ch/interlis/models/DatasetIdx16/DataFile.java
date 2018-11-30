package ch.interlis.models.DatasetIdx16;
public class DataFile extends ch.interlis.iom_j.Iom_jObject
{
  public final static String tag= "DatasetIdx16.DataFile";
  public DataFile() {
    super(tag,null);
  }
  public String getobjecttag() {
    return tag;
  }
  public final static String tag_fileFormat="fileFormat";
  /** Mimetype of file format
   */
  public String getfileFormat() {
    String value=getattrvalue("fileFormat");
    return value;
  }
  /** Mimetype of file format
   */
  public void setfileFormat(String value) {
    setattrvalue("fileFormat", value);
  }
  public final static String tag_file="file";
  /** path of data-file, as it is in the repository e.g. "BFE/91.1_TypeOfReactorCatalogue_12-09-10.xml".
   * More than one entry if it is split into multiple parts.
   */
  public int sizefile() {return getattrvaluecount("file");}
  public ch.interlis.models.DatasetIdx16.File[] getfile() {
    int size=getattrvaluecount("file");
    ch.interlis.models.DatasetIdx16.File value[]=new ch.interlis.models.DatasetIdx16.File[size];
    for(int i=0;i<size;i++){
      value[i]=(ch.interlis.models.DatasetIdx16.File)getattrobj("file",i);
    }
    return value;
  }
  /** path of data-file, as it is in the repository e.g. "BFE/91.1_TypeOfReactorCatalogue_12-09-10.xml".
   * More than one entry if it is split into multiple parts.
   */
  public void addfile(ch.interlis.models.DatasetIdx16.File value) {
    addattrobj("file", value);
  }
}
