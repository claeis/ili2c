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
}


