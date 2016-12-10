package ch.interlis.ili2c.metamodel;


public class EnumValType extends Type
{
	private boolean onlyLeafs=false;
	public EnumValType(boolean onlyLeafs)
	{
		this.onlyLeafs=onlyLeafs;
	}
		public boolean isOnlyLeafs() {
			return onlyLeafs;
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


    public EnumValType clone() {
        return (EnumValType) super.clone();
    }

}


