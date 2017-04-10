package ch.interlis.ili2c.config;
public class BoidEntry implements Cloneable
{
  public BoidEntry(String metaDataUseDef, String boid)
    {
    this.boid=boid;
    this.metaDataUseDef=metaDataUseDef;
    return;
    }
	@Override
	public Object clone() throws CloneNotSupportedException {
		return (BoidEntry)super.clone();
	}

 private String metaDataUseDef;

  /** get current value of metaDataUseDef
   *  @see #setMetaDataUseDef
   */
  public  String getMetaDataUseDef()
  {
    return metaDataUseDef;
  }

  /** set current value of metaDataUseDef
   *  @see #getMetaDataUseDef
   */
  public  void setMetaDataUseDef(String value1)
  {
    if(metaDataUseDef != value1){
      metaDataUseDef = value1;

    }
  }

  private String boid;

  /** get current value of boid
   *  @see #setBoid
   */
  public  String getBoid()
  {
    return boid;
  }

  /** set current value of boid
   *  @see #getBoid
   */
  public  void setBoid(String value1)
  {
    if(boid != value1){
      boid = value1;

    }
  }

}

