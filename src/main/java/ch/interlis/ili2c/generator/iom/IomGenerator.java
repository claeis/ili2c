package ch.interlis.ili2c.generator.iom;

import ch.interlis.ili2c.metamodel.*;
import java.io.Writer;
import java.io.IOException;
import java.util.*;
import ch.ehi.basics.logging.EhiLogger;

/**
 * @author ce
 */
public class IomGenerator
	implements VisitorCallback, WriterCallback
{
	public static String MODEL="iom04";
	public static String TOPIC="metamodel";
	Writer   out;
	int                 numErrors = 0;

	private IomGenerator (Writer out)
	{
	  this.out = out;
	}



	private void finish()
	{
	}
	public static int generate (Writer out, TransferDescription td)
	{
	  IomGenerator d = new IomGenerator (out);
	  d.initMapping();
	  try{
		d.encode (td);
	  }
	  catch(IOException ex){
	  	EhiLogger.logError(ex);
	  }
	  d.finish();
	  return d.numErrors;
	}

	private void initMapping()
	{
		//
		// ModelDef
		//
		addMapping(ch.interlis.ili2c.metamodel.TransferDescription.class,new VisitTransferDescription());
		addMapping(ch.interlis.ili2c.metamodel.Ili1Format.class,new VisitIli1Format());
		addMapping(VisitTransferDescription.MetadataMapping.class,new VisitMetadataMapping());
		// TODO DataContainer, ImportedByImports, Contracts, GraphicParameterDef
		addMapping(ch.interlis.ili2c.metamodel.DataModel.class,new VisitModel());
		addMapping(ch.interlis.ili2c.metamodel.PredefinedModel.class,new VisitModel());
		addMapping(ch.interlis.ili2c.metamodel.RefSystemModel.class,new VisitModel());
		addMapping(ch.interlis.ili2c.metamodel.SymbologyModel.class,new VisitModel());
		addMapping(ch.interlis.ili2c.metamodel.TypeModel.class,new VisitModel());

		//
		// TopicDef
		//
		addMapping(ch.interlis.ili2c.metamodel.Topic.class,new VisitTopic());

		//
		// ClassDef
		//
		addMapping(ch.interlis.ili2c.metamodel.Table.class,new VisitTable());
		addMapping(ch.interlis.ili2c.metamodel.AssociationDef.class,new VisitAssociationDef());
		addMapping(ch.interlis.ili2c.metamodel.RoleDef.class,new VisitRoleDef());
		addMapping(ch.interlis.ili2c.metamodel.LocalAttribute.class,new VisitLocalAttribute());
		addMapping(ch.interlis.ili2c.metamodel.Cardinality.class,new VisitCardinality());
		addMapping(ViewableAttributesAndRoles.class,new VisitViewableAttributesAndRoles());
		addMapping(TargetLightweightAssociation.class,new VisitTargetLightweightAssociation());

		//
		// DomainDef
		//
		addMapping(ch.interlis.ili2c.metamodel.LineForm.class,new VisitLineForm());
		addMapping(ch.interlis.ili2c.metamodel.Domain.class,new VisitDomain());
		addMapping(ch.interlis.ili2c.metamodel.TypeAlias.class,new VisitTypeAlias());
		addMapping(ch.interlis.ili2c.metamodel.EnumerationType.class,new VisitEnumerationType());
		addMapping(ch.interlis.ili2c.metamodel.Enumeration.class,new VisitEnumeration());
		addMapping(EnumerationElementWrapper.class,new VisitEnumerationElementWrapper());
		addMapping(ch.interlis.ili2c.metamodel.CoordType.class,new VisitCoordType());
		addMapping(CoordTypeDimensionWrapper.class,new VisitNumericalType());
		addMapping(ch.interlis.ili2c.metamodel.NumericType.class,new VisitNumericalType());
		addMapping(ch.interlis.ili2c.metamodel.StructuredUnitType.class,new VisitNumericalType());
		addMapping(ch.interlis.ili2c.metamodel.TextType.class,new VisitTextType());
		addMapping(ch.interlis.ili2c.metamodel.BasketType.class,new VisitBasketType());
		addMapping(ch.interlis.ili2c.metamodel.ClassType.class,new VisitClassType());
		addMapping(ClassTypeRestrictedTo.class,new VisitClassTypeRestrictedTo());
		addMapping(ch.interlis.ili2c.metamodel.CompositionType.class,new VisitCompositionType());
		addMapping(RestrictionOfCompTypeRestrictedTo.class,new VisitRestrictionOfCompTypeRestrictedTo());
		addMapping(ch.interlis.ili2c.metamodel.PolylineType.class,new VisitLineType());
		addMapping(ch.interlis.ili2c.metamodel.SurfaceType.class,new VisitLineType());
		addMapping(ch.interlis.ili2c.metamodel.AreaType.class,new VisitLineType());
		addMapping(LineTypeLineForm.class,new VisitLineTypeLineForm());
		addMapping(ch.interlis.ili2c.metamodel.MetaobjectType.class,new VisitMetaobjectType());
		addMapping(ch.interlis.ili2c.metamodel.ObjectType.class,new VisitObjectType());
		addMapping(ch.interlis.ili2c.metamodel.ReferenceType.class,new VisitReferenceType());
		addMapping(RestrictionOfRefTypeRestrictedTo.class,new VisitRestrictionOfRefTypeRestrictedTo());
		addMapping(ch.interlis.ili2c.metamodel.AnyOIDType.class,new VisitOIDType());
		addMapping(ch.interlis.ili2c.metamodel.TextOIDType.class,new VisitOIDType());
		addMapping(ch.interlis.ili2c.metamodel.NumericOIDType.class,new VisitOIDType());
		addMapping(ch.interlis.ili2c.metamodel.RefSystemRef.CoordDomainAxis.class,new VisitRefSystemRef());
		addMapping(ch.interlis.ili2c.metamodel.RefSystemRef.CoordDomain.class,new VisitRefSystemRef());
		addMapping(ch.interlis.ili2c.metamodel.RefSystemRef.CoordSystem.class,new VisitRefSystemRef());
		addMapping(ch.interlis.ili2c.metamodel.RefSystemRef.CoordSystemAxis.class,new VisitRefSystemRef());

		// MetaObject
		addMapping(ch.interlis.ili2c.metamodel.MetaObject.class,new VisitMetaObject());

		// Constraints
		addMapping(ch.interlis.ili2c.metamodel.UniquenessConstraint.class,new VisitUniquenessConstraint());
		addMapping(ch.interlis.ili2c.metamodel.ExistenceConstraint.class,new VisitExistenceConstraint());
		addMapping(ch.interlis.ili2c.metamodel.MandatoryConstraint.class,new VisitMandatoryConstraint());
		addMapping(ch.interlis.ili2c.metamodel.PlausibilityConstraint.class,new VisitMandatoryConstraint());

		// ObjectPath
		addMapping(ObjectPathWrapper.class,new VisitObjectPathWrapper());
		addMapping(PathElWrapper.class,new VisitPathElWrapper());

		// Constant
		addMapping(ConstantWrapper.class,new VisitConstantWrapper());

		// RuntimeParameterDef
		addMapping(ch.interlis.ili2c.metamodel.GraphicParameterDef.class,new VisitRuntimeParameterDef());

		// RuntimeParameterValue
		addMapping(RuntimeParameterValueWrapper.class,new VisitRuntimeParameterValueWrapper());

		// FunctionDef
		addMapping(ch.interlis.ili2c.metamodel.Function.class,new VisitFunctionDef());
		addMapping(FormalArgumentWrapper.class,new VisitFormalArgumentWrapper());

		// FunctionCall
		addMapping(FunctionCallWrapper.class,new VisitFunctionCallWrapper());

		// Expression
		addMapping(ExpressionWrapper.class,new VisitExpressionWrapper());

		// Unit
		addMapping(ch.interlis.ili2c.metamodel.BaseUnit.class,new VisitBaseUnit());
		addMapping(ch.interlis.ili2c.metamodel.ComposedUnit.class,new VisitComposedUnit());
		addMapping(ComposedUnit_ComposedWrapper.class,new VisitComposedUnit_ComposedWrapper());
		addMapping(ch.interlis.ili2c.metamodel.DerivedUnit.class,new VisitDerivedUnit());
		addMapping(ch.interlis.ili2c.metamodel.NumericallyDerivedUnit.class,new VisitDerivedUnit());
		addMapping(ch.interlis.ili2c.metamodel.FunctionallyDerivedUnit.class,new VisitDerivedUnit());
		addMapping(NumericallyDerivedUnit_FactorWrapper.class,new VisitNumericallyDerivedUnit_FactorWrapper());
		addMapping(ch.interlis.ili2c.metamodel.StructuredUnit.class,new VisitStructuredUnit());
		addMapping(StructuredUnit_PartWrapper.class,new VisitStructuredUnit_PartWrapper());
	}
	private void addMapping(Class aclass,Visitor visit)
	{
		visitors.put(aclass,visit);
		writers.put(aclass,visit);
	}

	private ArrayList createfuncv=new ArrayList();

	public void encode(Object rootObj)
	throws IOException
	{
		String basketName=null;
			out.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>"+newline());
			out.write("<TRANSFER>"+newline());
			out.write("<HEADERSECTION VERSION=\"2.2\" SENDER=\"ili2c-"+TransferDescription.getVersion()+"\">"+newline());

			out.write("<ALIAS>"+newline());
			writeAliasTable();
			out.write("</ALIAS>"+newline());

			out.write("</HEADERSECTION>"+newline());
			out.write("<DATASECTION>"+newline());
			basketName=MODEL+"."+TOPIC;
			out.write("<"+basketName+" BID=\"xb1\">"+newline());
		  addPendingObject(rootObj);
		  while(pendingObjects.size()>0){
			  Object next=pendingObjects.iterator().next();
			  visitObject(next);
			  if(next!=rootObj){
				  writeObject(next);
			  }
			  pendingObjects.remove(next);
		  }
		  // rootObj should be the last written object
		  writeObject(rootObj);
			out.write("</"+basketName+">"+newline());
			out.write("</DATASECTION>"+newline());
			out.write("</TRANSFER>"+newline());
	}

	private int objid=0;

	private Map object2Id = new HashMap(); // map<Object obj,int id>
	private Set pendingObjects=new HashSet(); // set<Object obj>
	private Map visitors = new HashMap(); // map<Class class,Visitor visitor>
	private Map writers = new HashMap(); // map<Class class,ObjWriter writer>

	public String getobjid(Object obj)
	{
		int objid=((Integer)object2Id.get(obj)).intValue();
		return Integer.toString(objid);
	}
	java.util.Set unknownClasses=new java.util.HashSet();
	private void visitObject(Object obj)
	{
		// call visitor(obj)
     		// callback addPendingObject(subobj);

		if(visitors.containsKey(obj.getClass())){
			Visitor visitor=(Visitor)visitors.get(obj.getClass());
			visitor.visitObject(obj,this);
		}else{
			if(!unknownClasses.contains(obj.getClass())){
				unknownClasses.add(obj.getClass());
				//System.err.println("unknown class: "+obj.getClass().getName());
			}
		}

	}
	  private void writeObject(Object obj)
		throws IOException
	  {

		// call writer(obj)
			// callback getobjid(obj)
			// callback getobjid(subobj)
			// callback encodeString(string)
		if(writers.containsKey(obj.getClass())){
			ObjWriter writer=(ObjWriter)writers.get(obj.getClass());
			out.write(newline());
			writer.writeObject(out,obj,this);
		}else{
			if(!unknownClasses.contains(obj.getClass())){
				unknownClasses.add(obj.getClass());
				System.err.println("unknown class: "+obj.getClass().getName());
			}
		}

  }


	  public void addPendingObject(Object obj)
	  {

		  // object already seen?
		  if(object2Id.containsKey(obj)){
		  	return;
		  }
		  if(pendingObjects.contains(obj)){
			  return;
		  }
		  // object not yet seen
		  // give object an id
		  if(obj.getClass()==java.lang.String.class){
			return;
		  }
		  int thisobjid=objid++;
		  object2Id.put(obj,new Integer(thisobjid));
		  pendingObjects.add(obj);

	  }


	public String encodeOid(String s)
	{
		return "x"+s;
	}
	public String encodeString(String s)
	{
		  StringBuilder str = new StringBuilder();

		  int len = (s != null) ? s.length() : 0;
		  for (int i = 0; i < len; i++) {
			  char ch = s.charAt(i);
			  if(ch=='<'){
					  str.append("&lt;");
			  }else if(ch=='>'){
					  str.append("&gt;");
			  }else if(ch=='&'){
					  str.append("&amp;");
			  }else if(ch=='\''){
					  str.append("&apos;");
			  }else if(ch=='"'){
					  str.append("&quot;");
			  }else if(ch>=0x80
				  || ch=='\r'
				  || ch=='\n'
				  ){
					  str.append("&#");
					  str.append(Integer.toString(ch));
					  str.append(';');
			  }else{
					  str.append(ch);
			  }
		  }

		  return str.toString();

	}
	public String encodeBoolean(boolean value)
	{
		return value ? "true" : "false";
	}
	public String encodeInteger(int value)
	{
		return Integer.toString(value);
	}
	public String encodeLong(long value)
	{
		return Long.toString(value);
	}
	public String encodePrecisionDecimal(PrecisionDecimal value)
	{
		return value.toString();
	}
	public String encodeDouble(double value)
	{
		return Double.toString(value);
	}
	public String encodeStructDec(Constant.Structured value)
	{
		return value.toString();
	}


	/** current line seperator
	 *
	 */
	static private String nl=null;

	/** write a line seperator
	 *
	 */
	public String newline()
	{
	  if(nl==null)nl=System.getProperty("line.separator");
	  return nl;
	}
	private void writeAliasTable()
	{
		try{
			java.io.InputStream template = getClass().getResourceAsStream("/ch/interlis/ili2c/generator/iom/AliasTable.txt");
			byte[] bt = new byte[1024];
			int i;
			while((i=template.read(bt)) != -1)
				{
					out.write(new String(bt,0,i));
				}
		}catch(IOException ex){
			System.err.println(ex.getLocalizedMessage());
		}
	}
}
