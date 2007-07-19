package ch.interlis.ili2c.metamodel;

public class FormattedTypeBaseAttrRef {
	private String postfix=null;
	private AttributeDef attr=null;
	private int intPos=0;
	private Domain formatted=null;

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}

	public AttributeDef getAttr() {
		return attr;
	}

	public void setAttr(AttributeDef attr) {
		this.attr = attr;
	}

	public Domain getFormatted() {
		return formatted;
	}

	public void setFormatted(Domain formatted) {
		this.formatted = formatted;
	}

	public int getIntPos() {
		return intPos;
	}

	public void setIntPos(int intPos) {
		this.intPos = intPos;
	}
}
