package ch.interlis.ili2c.config;
public class FileEntry implements Cloneable
{

public FileEntry(String filename, int kind)
    {
    this.kind=kind;
    this.filename=filename;
    return;
    }
	@Override
	public Object clone() throws CloneNotSupportedException {
		return (FileEntry)super.clone();
	}

  private String filename;

  /** get current value of filename
   *  @see #setFilename
   */
  public  String getFilename()
  {
    return filename;
  }

  /** set current value of filename
   *  @see #getFilename
   */
  public  void setFilename(String value1)
  {
    if(filename != value1){
      filename = value1;
      
    }
  }

  private int kind;

  /** get current value of kind
   *  @see #setKind
   */
  public  int getKind()
  {
    return kind;
  }

  /** set current value of kind
   *  @see #getKind
   */
  public  void setKind(int value1)
  {
    if(kind != value1){
      kind = value1;
      
    }
  }

}

