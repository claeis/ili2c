package ch.interlis.ili2c.metamodel;


public class BasketType extends Type {
	/** Kind of this basket.
	* DATA, VIEW, BASE or GRAPHIC.
	* May be 0 (=not specified).
	*/
	private int kind;
	/** Description of this basket. May be null (=not specified).
	*/
	private Topic topic;
	public int getKind()
	{
          return kind;
        }
	public void setKind(int kind)
	{
		// acceptable values
		// ch.interlis.Properties.eDATA
		// ch.interlis.Properties.eVIEW
		// ch.interlis.Properties.eBASE
		// ch.interlis.Properties.eGRAPHIC
		if(kind!=ch.interlis.ili2c.metamodel.Properties.eUNDEFINED
                        && kind!=ch.interlis.ili2c.metamodel.Properties.eDATA
			&& kind!=ch.interlis.ili2c.metamodel.Properties.eVIEW
			&& kind!=ch.interlis.ili2c.metamodel.Properties.eBASE
			&& kind!=ch.interlis.ili2c.metamodel.Properties.eGRAPHIC
			){
			throw new IllegalArgumentException (
				formatMessage ("err_basketType_kind")
				);

		}
		this.kind=kind;
	}
        public Topic getTopic()
        {
          return topic;
        }
	public void setTopic(Topic topic)
	{
		this.topic=topic;
	}
	  void checkTypeExtension (Type wantToExtend)
	  {
	    if ((wantToExtend == null)
	      || ((wantToExtend = wantToExtend.resolveAliases()) == null))
	      return;
	    if (!(wantToExtend.getClass().equals(this.getClass()))){
	        throw new Ili2cSemanticException (rsrc.getString (
	        "err_type_ExtOther"));
	    }
	    checkCardinalityExtension(wantToExtend);
	  }


    public BasketType clone() {
        return (BasketType) super.clone();
    }

}


