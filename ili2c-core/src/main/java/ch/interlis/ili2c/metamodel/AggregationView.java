package ch.interlis.ili2c.metamodel;

import java.util.List;

public class AggregationView extends UnextendableView
{
	private ViewableAlias base=null;
	// columns may be null == ALL OF
	private UniqueEl columns=null;
    private Table aggregates=null;
	public AggregationView(ViewableAlias base)
	{
		this.base=base;
	}
        public void setEqual(UniqueEl cols)
        {
          columns=cols;
        }
        public UniqueEl getEqual()
        {
          return columns;
        }
	public ViewableAlias getBase()
	{
		return base;
	}
    public Table getAggregates(){
      if(aggregates==null){
        try {
          aggregates=new Table();
          aggregates.setIdentifiable(false);
          aggregates.setName("_AGGREGATES_"+base.getName());
          // copy attributes from base
          java.util.Iterator attrs = base.getAliasing().getAttributes ();
          while (attrs.hasNext ())
          {
            AttributeDef attr = (AttributeDef) attrs.next();
            LocalAttribute pa = new LocalAttribute ();
              pa.setName (attr.getName ());
              pa.setDomain (attr.getDomain ());
              aggregates.add (pa);
          }
        } catch (Exception ex) {
          throw new java.lang.IllegalStateException(ex.getLocalizedMessage());
        }

      }
      return aggregates;
    }
    @Override
    public void checkTranslationOf(List<Ili2cSemanticException> errs,String name,String baseName)
      throws java.lang.IllegalStateException
    {
        super.checkTranslationOf(errs,name,baseName);
        AggregationView baseElement=(AggregationView)getTranslationOf();
        if(baseElement==null) {
            return;
        }
        
        Ili2cSemanticException err=null;
        err=checkElementRef(getBase().getAliasing(),baseElement.getBase().getAliasing(),getSourceLine(),"err_diff_baseViewMismatch");
        if(err!=null) {
            errs.add(err);
        }
        ObjectPath[] elev=getEqual().getAttributes();
        ObjectPath[] otherElev=baseElement.getEqual().getAttributes();
        if(elev.length!=otherElev.length) {
            err=new Ili2cSemanticException(getSourceLine(), Element.formatMessage("err_diff_attributeListMismatch"));
            errs.add(err);
        }else {
            for(int ri=0;ri<elev.length;ri++) {
                err=Evaluable.checkTranslation(elev[ri], otherElev[ri], getSourceLine(), "err_diff_attributeListMismatch");
                if(err!=null) {
                    errs.add(err);
                }
            }
        }
    }
}
