package ch.interlis.ili2c.metamodel;

/** A class that designates the set of base objects built
 * by the AGGREGATES keyword as part of an object path.
 */

public class AggregationRef extends AbstractAttributeRef
{
	private AggregationView base;
        private CompositionType domain=null;
	public AggregationRef(AggregationView base){
		this.base=base;
	}
        public String getName ()
        {
          return "AGGREGATES";
        }
    public Type getDomain(){
      if(domain==null){
        try {
          domain=new CompositionType();
          domain.setOrdered(false);
          domain.setCardinality(new Cardinality(0,Cardinality.UNBOUND));
          domain.setComponentType(base.getAggregates());
        } catch (Exception ex) {
          throw new java.lang.IllegalStateException(ex.getLocalizedMessage());
        }
      }
      return domain;
    }
	public AggregationView getBase() {
		return base;
	}
    

}


