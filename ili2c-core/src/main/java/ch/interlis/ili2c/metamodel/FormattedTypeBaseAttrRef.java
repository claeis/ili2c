package ch.interlis.ili2c.metamodel;

public class FormattedTypeBaseAttrRef {
	private String postfix=null;
	private AttributeDef attr=null;
	private int intPos=0;
	private Domain formatted=null;
	private FormattedType parent=null;
	public FormattedTypeBaseAttrRef(FormattedType parent,AttributeDef attr,int intPos)
	{
		this.parent = parent;
		this.attr = attr;
		this.intPos = intPos;
	}
	public FormattedTypeBaseAttrRef(FormattedType parent,AttributeDef attr,Domain formatted)
	{
		this.parent = parent;
		this.attr = attr;
		this.formatted = formatted;
	}

	public AttributeDef getAttr() {
		return attr;
	}
	public Domain getFormatted() {
		return formatted;
	}
	public int getIntPos() {
		return intPos;
	}

	public String getPostfix() {
		return postfix;
	}

	public void setPostfix(String postfix) {
		this.postfix = postfix;
	}
	public FormattedType getParent()
	{
		return parent;
	}

    FormattedTypeBaseAttrRef newParent(FormattedType parent) {
        FormattedTypeBaseAttrRef ref = this;

        if (parent != this.parent) {
            ref = new FormattedTypeBaseAttrRef(parent, attr, formatted);
            ref.intPos = intPos;
            ref.postfix = postfix;
        }
        return ref;
    }
}
