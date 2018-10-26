/* This file is part of the INTERLIS-Compiler.
 * For more information, please see <http://www.interlis.ch/>.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package ch.interlis.ili2c.generator;


import ch.interlis.ili2c.metamodel.*;
import java.io.Writer;
import java.util.*;
import ch.ehi.basics.io.IndentPrintWriter;
import ch.ehi.basics.settings.Settings;
import ch.ehi.basics.tools.TopoSort;

/** generates INTERLIS 1 (ili and fmt)
 *
 * @author ce
 */
public final class Interlis1Generator
{
  private IndentPrintWriter   ipw;
  private TransferDescription td;
  /** flag to distinuish between ili and fmt output
   */
  private boolean             genFmt=false;
  /** index of current attribute.
   *  OID has index 1.
   */
  private int fmtAttrIdx;
  /** tag used for comment lines in fmt output
   */
  private static String FMT_CMT="!!!!";
  private Type                typeOf_ILI1_DATE;
  private Type                typeOf_URI;
  private Type                typeOf_NAME;
  private Type                typeOf_BOOLEAN;
  private Type                typeOf_HALIGNMENT;
  private Type                typeOf_VALIGNMENT;
  private int                 numErrors = 0;

  private TransformationParameter params = null;

  static private ResourceBundle rsrc = ResourceBundle.getBundle(
    Interlis1Generator.class.getName(),
    Locale.getDefault());



  private Interlis1Generator (Writer out, TransferDescription td)
  {
    ipw = new IndentPrintWriter (out);
    this.td = td;

    typeOf_ILI1_DATE = td.INTERLIS.INTERLIS_1_DATE.getType ();
    typeOf_URI = td.INTERLIS.URI.getType ();
    typeOf_NAME = td.INTERLIS.NAME.getType ();
    typeOf_BOOLEAN = td.INTERLIS.BOOLEAN.getType ();
    typeOf_HALIGNMENT = td.INTERLIS.HALIGNMENT.getType ();
    typeOf_VALIGNMENT = td.INTERLIS.VALIGNMENT.getType ();
  }


  private void finish()
  {
    ipw.close();
  }


  private void printError ()
  {
    numErrors = numErrors + 1;
    ipw.print (Element.makeErrorName (null));
  }

  private final boolean printErrorOrToString (Object obj, double factor, double diff)
  {
    if (obj == null)
    {
      printError ();
      return true;
    }
    
    PrecisionDecimal parameterValue = (PrecisionDecimal)obj;
    
	int value = (int) ((int) (factor * parameterValue.doubleValue()) + diff);
	String newValue = String.valueOf(value);
	for (int j = 0; j < parameterValue.getAccuracy(); j++) {
		if (j == 0) {
			newValue = newValue + ".0"; 
		} else {
			newValue += "0";
		}
	}
	ipw.print(newValue);
//    if(obj instanceof PrecisionDecimal){
//	    ipw.print(((PrecisionDecimal)obj).toIli1String());
//    }else{
//    	ipw.print (obj.toString ());
//    }
    return false;
  }

  private final boolean printErrorOrToString (Object obj)
  {
    if (obj == null)
    {
      printError ();
      return true;
    }

    if(obj instanceof PrecisionDecimal){
	    ipw.print(((PrecisionDecimal)obj).toIli1String());
    }else{
    	ipw.print (obj.toString ());
    }
    return false;
  }


  private void printExplanation (String explanation)
  {
    ipw.print ("//");
    if (explanation != null)
      ipw.print (explanation);
    ipw.print ("//");
  }


  public static int generate (
    Writer out, TransferDescription td, TransformationParameter params)
  {
    Interlis1Generator i = new Interlis1Generator(out, td);
    i.setParams(params);
    i.printTransferDescription (td);
    i.finish();
    return i.numErrors;
  }
  
  public static int generateFmt(Writer out, TransferDescription td)
  {
    Interlis1Generator i = new Interlis1Generator(out, td);
    i.genFmt=true;
    i.printTransferDescription (td);
    i.finish();
    return i.numErrors;
  }

  private void printTransferDescriptionHeader (TransferDescription td)
  {
    if(genFmt){
      ipw.println("SCNT");
      ipw.println("Beschreibung der Transferdatei");
      ipw.println("////");
      ipw.println("MTID model.ili"); // ModelDef.getFilename()
    }else{
      ipw.println("TRANSFER TransferName;");
      ipw.println();
    }
  }


  private void printTransferDescriptionTrailer (TransferDescription td)
  {
    if(genFmt){
      ipw.println("ENDE");
    }else{
      ipw.println("FORMAT");
      ipw.indent ();
      ipw.println ("FREE;");
      ipw.unindent ();
      ipw.println();


      ipw.println("CODE");
      ipw.indent ();
      ipw.println ("BLANK = DEFAULT, UNDEFINED = DEFAULT, CONTINUE = DEFAULT;");
      ipw.println ("TID = ANY;");
      ipw.unindent ();
      ipw.println ("END.");
    }
  }
  
  private void printTransferDescription (
		    TransferDescription   td)
		  {
		    printTransferDescriptionHeader (td);
		    int numEmittedModels = 0;
		    Iterator iter = td.iterator ();
		    while (iter.hasNext ())
		    {
		      Object obj = iter.next ();
		      if ((obj instanceof Model) && needToPrintModel ((Model) obj))
		      {
		        if (numEmittedModels > 0)
		        {
		          printTransferDescriptionTrailer (td);
		          if(genFmt){
		          }else{
		            ipw.println ();
		            ipw.println ();
		            ipw.println ("!! " + rsrc.getString ("err_multipleModels_line_1"));
		            ipw.println ("!! " + rsrc.getString ("err_multipleModels_line_2"));
		            ipw.println ("!! " + rsrc.getString ("err_multipleModels_line_3"));
		            ipw.println ();
		          }
		          printTransferDescriptionHeader (td);
		        }
		        numEmittedModels = numEmittedModels + 1;
		        printModel ((Model) obj);
		        if(!genFmt){
		          ipw.println ();
		        }
		      }
		    }
		    printTransferDescriptionTrailer (td);
		  }

  private boolean needToPrintModel (Model model)
  {
    if (model == null)
      return false;

    if (model == td.INTERLIS)
      return false;

    if (!(model instanceof DataModel))
      return false;

    Iterator iter = model.iterator ();
    while (iter.hasNext ())
    {
      Object obj = iter.next ();
      if ((obj instanceof Topic) && needToPrintTopic ((Topic) obj))
        return true;
    }
    return false;
  }


  private boolean needToPrintTopic (Topic topic)
  {
    if (topic == null)
      return false;

    if (topic.isAbstract ())
      return true;

    Iterator iter = topic.getViewables ().iterator();
    while (iter.hasNext ())
    {
      Object obj = iter.next ();
      if ((obj instanceof Table) && needToPrintTable ((Table) obj))
        return true;
    }
    return false;
  }


  /** @return If <code>table</code> is a concrete table.
              Abstract tables and structures do not have
              to be printed.
  */
  private boolean needToPrintTable (AbstractClassDef table)
  {
    if (table == null)
      return false;

    /* Do not print out tables inserted by the compiler,
       such as line attribute tables.
    */
    if ((table instanceof Table) && ((Table)table).isImplicit ())
      return false;

    if(table.isAbstract()){
      return false;
    }
    if(table instanceof Table ){ //&& ((Table)table).isIdentifiable()){
      return true;
    }
    if((table instanceof AssociationDef) && !((AssociationDef)table).isLightweight()){
      return true;
    }
    return false;
  }

  public void printDocumentation(String doc)
	{
	if(doc==null)return;
	if(doc.length()==0)return;
	String beg="!!* ";
	// for each line
	int last=0;
	int next=doc.indexOf("\n",last);
	while(next>-1){
	  String line=doc.substring(last,next);
	  ipw.println(beg+line);
	  last=next+1;
	  next=doc.indexOf("\n",last);
	}
	  String line=doc.substring(last);
	  ipw.println(beg+line);
	}

  private void printModel (Model model)
  {
    Class lastPrinted = null;

    if(genFmt){
      ipw.println("MODL "+model.getName ());
    }else{
		printDocumentation(model.getDocumentation());
		printMetaValues(model.getMetaValues());
    	if(model.getIssuer()!=null){
			ipw.println ("!!* @Issuer "+model.getIssuer());
    	}
		if(model.getModelVersion()!=null){
			ipw.println ("!!* @Version "+model.getModelVersion());
		}
      ipw.print ("MODEL "); 
      if (params != null) {
    	  ipw.println (params.getNewModelName());  
      } else {
    	  ipw.println (model.getName ());
      }

      ipw.indent ();
    }

	Iterator iter=null;
	if(genFmt){

	}else{
		iter = model.iterator();
		boolean isFirst=true;
		while (iter.hasNext ())
		{
		  Object obj = iter.next ();
		  if (obj instanceof Domain)
		  {
			Domain domain = (Domain) obj;
			if(isFirst){
				ipw.println("DOMAIN");
				isFirst=false;
				ipw.indent();
			}
			printDomain(domain);
		  }
		}
		if(!isFirst){
			ipw.unindent();
			ipw.println("");
		}
	}

    iter = model.iterator ();
    while (iter.hasNext ())
    {
      Object obj = iter.next ();
      if (obj instanceof Topic)
      {
        Topic topic = (Topic) obj;
        if (needToPrintTopic (topic))
        {
          if (lastPrinted != null){
            if(genFmt){
            }else{
              ipw.println ();
            }
          }
          printTopic (topic);
          lastPrinted = Topic.class;
        }
      }
	    if(obj instanceof Table && ((Table)obj).isIdentifiable()){
    	  if(!genFmt){
    		  printTable((AbstractClassDef)obj);
              lastPrinted = Table.class;
    	  }
      }
    }

    if(genFmt){
      ipw.println("EMOD");
    }else{
      ipw.unindent ();
      ipw.print ("END ");
      if (params != null) {
    	  ipw.print(params.getNewModelName());  
      } else {
    	  ipw.print(model.getName ());
      }
      ipw.println ('.');
    }
  }

  private void printTopic (Topic topic)
  {
    if(genFmt){
      ipw.println(FMT_CMT); // additional line to improve readability
      ipw.println("TOPI "+topic.getName());
    }else{
	  printDocumentation(topic.getDocumentation());
	  printMetaValues(topic.getMetaValues());
      ipw.print ("TOPIC ");
      ipw.print (topic.getName ());
      ipw.println (" =");
      ipw.indent ();
    }


	Iterator defi = null;
	if(genFmt){

	}else{
		defi=topic.iterator();
		boolean isFirst=true;
		while (defi.hasNext ())
		{
		  Object obj = defi.next ();
		  if (obj instanceof Domain)
		  {
			Domain domain = (Domain) obj;
			if(isFirst){
				ipw.println("DOMAIN");
				isFirst=false;
				ipw.indent();
			}
		  	printDomain(domain);
		  }
		}
		if(!isFirst){
			ipw.unindent();
			ipw.println("");
		}
	}
    Class lastPrinted = null;


	List tables=topic.getViewables();
	// INTERLIS 2 model?
	if(!((Model)topic.getContainer()).getIliVersion().equals(Model.ILI1)){
		TopoSort sortDefs=new TopoSort();
		defi = topic.getViewables().iterator();
		while (defi.hasNext ())
		{
		  Object obj = defi.next ();
		  if (obj instanceof AbstractClassDef)
		  {
			AbstractClassDef table = (AbstractClassDef) obj;
			if (needToPrintTable (table))
			{
				sortDefs.add(table);

				Iterator attri = table.getAttributesAndRoles2();
				while (attri.hasNext ()){
					ViewableTransferElement roleo = (ViewableTransferElement)attri.next();
					if (roleo.obj instanceof AttributeDef){
					}else if(roleo.obj instanceof RoleDef){
						RoleDef role = (RoleDef) roleo.obj;
						if(role.getContainer()==table){
							sortDefs.addcond(role.getDestination(),table);
						}else if(role.getContainer()!=table){
						  sortDefs.addcond(role.getDestination(),table);
						}
					}
				}
			}
		  }
		}
		if(!sortDefs.sort()){
			throw new IllegalStateException("Cycle in table definitions");
		}
		tables=sortDefs.getResult();
	}
    defi = tables.iterator();
    while (defi.hasNext ())
    {
      Object obj = defi.next ();
      if (obj instanceof AbstractClassDef)
      {
        AbstractClassDef table = (AbstractClassDef) obj;

        if (needToPrintTable (table))
        {
          if (lastPrinted != null){
            if(!genFmt){
              ipw.println ();
            }
          }

          printTable (table);
          lastPrinted = Table.class;
        }
      }
    }

    if(genFmt){
      ipw.println("ETOP");
    }else{
      ipw.unindent ();
      ipw.print ("END ");
      ipw.print (topic.getName ());
      ipw.println ('.');
    }
  }

	/** collects the UniquenessConstraint from the given table and all its base tables.
	 */
  private Iterator determineUniquenessConstraints (AbstractClassDef forTable)
  {
    List result = new LinkedList ();

    for (AbstractClassDef tab = forTable; tab != null; tab = (AbstractClassDef) tab.getExtending())
    {
      Iterator iter = tab.iterator ();
      while (iter.hasNext ())
      {
        Object obj = iter.next ();
        if (!(obj instanceof UniquenessConstraint))
          continue;

        result.add (obj);
      }
    }
    return result.iterator ();
  }

  private void printTable (AbstractClassDef table)
  {
    Iterator iter;
    if(genFmt){
      // print area tables
      iter = table.getAttributes ();
      while (iter.hasNext ()){
        Object obj = iter.next();
        if (obj instanceof AttributeDef)
        {
          AttributeDef attr = (AttributeDef) obj;
          Type type = Type.findReal (attr.getDomain());
          if(type instanceof AreaType){
            printSurfaceOrAreaTypeFmt(table,attr,(AreaType)type);
          }
        }
      }
      // start main table
      ipw.println(FMT_CMT); // additional line to improve readability
      ipw.println("TABL "+table.getName ());
      ipw.print("OBJE "+genFmtField(1,1));
      fmtAttrIdx=2;
    }else{
	  printDocumentation(table.getDocumentation());
	  printMetaValues(table.getMetaValues());
	  ipw.print ("TABLE ");
      ipw.print (table.getName ());
      ipw.println (" =");
      ipw.indent ();
    }

    // build list of attributes and keep Ili1 source ordering
    java.util.List attrlist=new java.util.ArrayList();
    iter = table.getAttributesAndRoles ();
    while (iter.hasNext ()){
        Object obj = iter.next();
        if (obj instanceof AttributeDef){
          attrlist.add(obj);
        }else if(obj instanceof RoleDef && !((AssociationDef)table).isLightweight()){
          attrlist.add(obj);
        }
    }
      java.util.ArrayList rolesSorted=new ArrayList(table.getLightweightAssociations());
      java.util.Collections.sort(rolesSorted,new java.util.Comparator(){
        public int compare(Object o1,Object o2){
          int idx1=((RoleDef)o1).getIli1AttrIdx();
          int idx2=((RoleDef)o2).getIli1AttrIdx();
          if(idx1==idx2)return 0;
          if(idx1==-1)return 1;
          if(idx2==-1)return -1;
          if(idx1<idx2)return -1;
          return 1;
        }
      });
      iter=rolesSorted.iterator();
      while (iter.hasNext()){
	RoleDef role = (RoleDef) iter.next();
        RoleDef oppend = getOppEnd(role);
        if(role.getIli1AttrIdx()==-1){
          attrlist.add(role);
        }else{
          attrlist.add(role.getIli1AttrIdx(),role);
        }
      }

    iter = attrlist.iterator();
    while (iter.hasNext ()){
        Object obj = iter.next();
        if (obj instanceof AttributeDef){
          AttributeDef attr = (AttributeDef) obj;
          printAttribute (table, attr);
        }else if(obj instanceof RoleDef){
            RoleDef role = (RoleDef) obj;
            if(role.getContainer()==table
                && (table instanceof AssociationDef)
                && !((AssociationDef)table).isLightweight()){
              if(genFmt){
                ipw.print(" "+genFmtField(fmtAttrIdx,1));
                fmtAttrIdx++;
              }else{
                ipw.println(role.getName()+" : ->"+role.getDestination().getName()+"; !! {1}");
              }
            }else if(role.getContainer()!=table){
              RoleDef oppend = getOppEnd(role);
              //ipw.println (oppend.getName());
              if(genFmt){
                ipw.print(" "+genFmtField(fmtAttrIdx,1));
                fmtAttrIdx++;
              }else{
                ipw.println(oppend.getName()+" : ->"+oppend.getDestination().getName()+"; !! "+role.getCardinality().toString());
              }
            }
        }
    }

    if(genFmt){
      // force newline
      ipw.println("");
      // print attribute explanations
      ipw.println(FMT_CMT); // additional line to improve readability
      ipw.println(FMT_CMT+" 1: Objektidentifikation");
      iter = attrlist.iterator();
      int idx=2;
      while (iter.hasNext ()){
        Object obj = iter.next();
        if (obj instanceof AttributeDef){
          AttributeDef attr = (AttributeDef) obj;
          Type type = Type.findReal (attr.getDomain());
          if((type instanceof LineType)
              && !(type instanceof AreaType)){
              // LineType's but not AreaType
              ; // not part of main record
          }else{
            // AreaType: centroid is part of main record
            // other non LineType's
            ipw.println(FMT_CMT+" "+getIdxCode(idx)+": "+attr.getName());
          }
        }else if(obj instanceof RoleDef){
            RoleDef role = (RoleDef) obj;
            if(role.getContainer()==table
                && (table instanceof AssociationDef)
                && !((AssociationDef)table).isLightweight()){
              ipw.println(FMT_CMT+" "+getIdxCode(idx)+": "+role.getName()+" ->"+role.getDestination().getName());
            }else if(role.getContainer()!=table){
              RoleDef oppend = getOppEnd(role);
              ipw.println(FMT_CMT+" "+getIdxCode(idx)+": "+oppend.getName()+" ->"+oppend.getDestination().getName());
            }
        }
        idx++;
      }
      ipw.println(FMT_CMT); // additional line to improve readability
      // print polyline attributes
      iter = table.getAttributes ();
      while (iter.hasNext ()){
        AttributeDef attr=(AttributeDef) iter.next ();
        Type type = Type.findReal (attr.getDomain());
        if(type instanceof PolylineType){
          ipw.println(FMT_CMT+" "+attr.getName());
          printLineTypeFmt((PolylineType)type);
        }
      }

     }else{
      ipw.unindent ();

      Iterator idents = determineUniquenessConstraints (table);
      if (idents.hasNext ())
      {
        ipw.println ("IDENT");
        ipw.indent ();
        do
        {
          UniquenessConstraint uc = (UniquenessConstraint) idents.next ();
          UniqueEl attribs = uc.getElements();
          if (attribs == null)
            printError ();
          else
          {
          	Iterator attri=attribs.iteratorAttribute();
          	String sep="";
            for (; attri.hasNext();)
            {
           		ObjectPath path=(ObjectPath)attri.next();
              if (path == null){
                printError ();
              }else{
				ipw.print(sep+path.getPathElements()[0].getName());
              }
			  sep=", ";
            }
          }
          ipw.println (';');
        }
        while (idents.hasNext ());
        ipw.unindent ();
      }else{
        ipw.println ("NO IDENT");
      }
    }

    if(genFmt){
      ipw.println("ETAB");
      // print surface tables
      iter = table.getAttributes ();
      while (iter.hasNext ()){
        AttributeDef attr=(AttributeDef) iter.next ();
        Type type = Type.findReal (attr.getDomain());
        if(type instanceof SurfaceType){
          printSurfaceOrAreaTypeFmt(table,attr,(SurfaceType)type);
        }
      }
    }else{
      ipw.print ("END ");
      ipw.print (table.getName ());
      ipw.println (';');
    }
  }

  private void printMetaValues(Settings values) {
	 if (values != null) {
		 for (Iterator valuei = values.getValues().iterator(); valuei.hasNext();) {
			 String name = (String) valuei.next();
			 String value = "";
			 if (name.equals("CRS")) {
				 value  = String.valueOf(params.getEpsgCode());
			 } else {
				 value = values.getValue(name);
			 }
			 ipw.print("!!@ ");
			 ipw.print(name);
			 ipw.print("=");
			 if (value.indexOf(' ') != -1 || value.indexOf('=') != -1 || value.indexOf(';') != -1 
					 || value.indexOf(',') != -1 || value.indexOf('"') != -1 || value.indexOf('\\') != -1) {
				 ipw.println("\""+value+"\"");
			 } else {
				 ipw.println(value);
			 }
		 }
	 }
  }


private void printDomain(Domain domain)
  {
	if(genFmt){
	}else{
	  printDocumentation(domain.getDocumentation());
	  printMetaValues(domain.getMetaValues());
	  ipw.print (domain.getName ());
	  ipw.print (" = ");

	  String comment = printType (null, domain.getType (),true);
	  if (comment != null){
		ipw.print (";  !! ");
		ipw.println (comment);
	  }else{
		ipw.println (';');
	  }
	}
  }
  
  private void printAttribute (AbstractClassDef forTable, AttributeDef attr)
  {
    /* Do not print abstract attributes. */
    if (attr.isAbstract ())
      return;

    if(genFmt){
      String comment = printType (forTable, attr.getDomain (),false);
      fmtAttrIdx++;
    }else{
		printDocumentation(attr.getDocumentation());
	  printMetaValues(attr.getMetaValues());
      ipw.print (attr.getName ());
      ipw.print (" : ");

      String comment = printType (forTable, attr.getDomain (),false);
      if(!genFmt && attr.getExplanation()!=null){
		ipw.print (" ");
      	printExplanation(attr.getExplanation());
      }
      if (comment != null){
        ipw.print (";  !! ");
        ipw.println (comment);
      }else{
        ipw.println (';');
      }
    }
  }

  private String printType (Container scope, Type typ, boolean withoutOptional)
  {
    String comment = null;

    if (typ == null)
    {
      printError ();
      return rsrc.getString ("err_inSource");
    }

    Type t = typ;
    boolean mand = false;
    do {
      mand |= t.isMandatory ();
      if (t instanceof TypeAlias)
      {
        Domain aliased = ((TypeAlias) t).getAliasing();
        t = aliased.getType ();
      }
    } while (t instanceof TypeAlias);
    if (!mand){
      if(genFmt){
      }else{
        if(!withoutOptional){
          ipw.print ("OPTIONAL ");
        }
      }
    }
    if(typ instanceof TypeAlias){
        Domain aliased = ((TypeAlias) typ).getAliasing();
        if(aliased==td.INTERLIS.INTERLIS_1_DATE){
            if(genFmt){
                ipw.print(" "+genFmtField(fmtAttrIdx,8));
              }else{
                ipw.print ("DATE");
              }
    		return null;
        }else if(aliased==td.INTERLIS.BOOLEAN){
            if(genFmt){
                ipw.print(" "+genFmtField(fmtAttrIdx,1));
              }else{
                ipw.print ("BOOLEAN");
              }
    		return null;
        }else if(aliased==td.INTERLIS.HALIGNMENT){
            if(genFmt){
                ipw.print(" "+genFmtField(fmtAttrIdx,1));
              }else{
                ipw.print ("HALIGNMENT");
              }
    		return null;
        }else if(aliased==td.INTERLIS.VALIGNMENT){
            if(genFmt){
                ipw.print(" "+genFmtField(fmtAttrIdx,1));
              }else{
                ipw.print ("VALIGNMENT");
              }
    		return null;
        }else{
        	if(!genFmt){
    			ipw.print(aliased.getName());
    			return null;
        	}
        }
    }
    t = Type.findReal (typ);
    if (t instanceof TextType)
    {
    	int maxLength=((TextType) t).getMaxLength ();
    	if(maxLength==-1){
    		maxLength=2400;
    	}
        if(genFmt){
            ipw.print(" "+genFmtField(fmtAttrIdx,maxLength));
          }else{
            ipw.print ("TEXT*");
            ipw.print (maxLength);
          }
          if (t == typeOf_URI)
            comment = rsrc.getString ("comment_uri");
          else if (t == typeOf_NAME)
            comment = rsrc.getString ("comment_name");
          return comment;
    }
    else if (t instanceof NumericType)
      return printNumericType ((NumericType) t);
    else if (t instanceof CoordType)
      return printCoordType ((CoordType) t);
    else if (t instanceof EnumerationType)
    {
        printEnumeration (((EnumerationType) t).getEnumeration ());
    }
    else if (t instanceof LineType){
      if(genFmt){
        LineType lineType = (LineType)Type.findReal (t);
        if(lineType instanceof AreaType){
          // centroid is part of main record
          Domain controlPointDomain = lineType.getControlPointDomain();
          if (controlPointDomain == null)
            printError ();
          else
            return printCoordType ((CoordType) Type.findReal (controlPointDomain.getType()));
        }
        return null;
      }
      return printLineType ((LineType) t);
    }else if (t instanceof ReferenceType){
      return printReferenceType (scope, (ReferenceType) t);
    }else if(t instanceof CompositionType){
    	CompositionType ct=(CompositionType)t;
        String referredName = ct.getComponentType().getName ();
        ipw.print ("-> ");
        if(ct.getCardinality().getMaximum()>1){
            ipw.print (ct.getCardinality().toString()+" ");
            comment=rsrc.getString ("err_notExpressible");
        }else{
        	comment=null;
        }
        ipw.print (referredName);
        return comment;
    }else if (t instanceof ClassType){
        int classTypeLen=767; // NAME.NAME.NAME
        if(genFmt){
          ipw.print(" "+genFmtField(fmtAttrIdx,classTypeLen));
        }else{
          ipw.print ("TEXT*");
          ipw.print (classTypeLen);
        }
        return null;
    }else if (t instanceof StructuredUnitType){
        StructuredUnitType st=(StructuredUnitType)t;
        int textLen=Math.min(st.getMaximum().toString().length(),st.getMinimum().toString().length());
        if(genFmt){
          ipw.print(" "+genFmtField(fmtAttrIdx,textLen));
        }else{
          ipw.print ("TEXT*");
          ipw.print (textLen);
        }
        return null;
    }else if (t instanceof OIDType){
      OIDType ot=(OIDType)t;
      return printType(scope,ot.getOIDType(),true);
    //}else if (t instanceof BasketType){
    }else{
      printError ();
      return rsrc.getString ("err_notExpressible");
    }

    return comment;
  }



  private String printReferenceType (Container scope, ReferenceType type)
  {
    if(genFmt){
    }else{
      Viewable referred;
      String referredName;

      if ((type == null) || ((referred = type.getReferred ()) == null))
      {
        printError ();
        return rsrc.getString ("err_inSource");
      }

      /* References to abstract classes can not be expressed in INTERLIS-1. */
      if (referred.isAbstract())
      {
        printError ();
        ipw.println ();

        ipw.print ("!! ");
        ipw.println
        (
          (new java.text.MessageFormat (rsrc.getString ("err_relationToAbstractClass_line_1")))
          .format
          (
            new Object[]
            {
              referred.toString ()
            }
          )
        );

        ipw.print ("!! ");
        ipw.println (rsrc.getString ("err_relationToAbstractClass_line_2"));


        return null;
      }

      /* References across topics can not be expressed in INTERLIS-1. */
      Topic scopeTopic = (Topic) scope.getContainerOrSame (Topic.class);
      Topic referredTopic = (Topic) referred.getContainer (Topic.class);

      if (scopeTopic != referredTopic)
      {
        printError ();
        ipw.println ();

        ipw.print ("!! ");
        ipw.println
        (
          (new java.text.MessageFormat (rsrc.getString ("err_crossTopicRelation_line_1")))
          .format
          (
            new Object[]
            {
              referred.toString (),
              referredTopic.toString (),
              scopeTopic.toString ()
            }
          )
        );

        ipw.print ("!! ");
        ipw.println (rsrc.getString ("err_crossTopicRelation_line_2"));


        return null;
      }

      referredName = referred.getName ();
      ipw.print ("-> ");
      if ((referredName == null) || (referredName.length () == 0))
      {
        printError ();
        return rsrc.getString ("err_inSource");
      }

      ipw.print (referredName);
    }
    return null;
  }


  private String printNumericType (NumericType type)
  {
    if(genFmt){
      String fld=genFmtField(fmtAttrIdx,type.getMinimum(),type.getMaximum());
      ipw.print(" "+fld);
      return null;
    }else{
      Unit             unit = type.getUnit ();
      String           before;
      String           between = " ";
      String           after = "";
      String           comment = null;
      boolean          error = false;


      if (unit != null)
      {
        String docName = unit.getDocName ();
        String shortName = unit.getName ();
        if (shortName != null)
        {
          if (shortName.equals (docName))
            comment = "[" + shortName + "]";
          else
            comment = docName + " [" + shortName + "]";
        }
      }

      if (isDIM1 (type))
        before = "DIM1 ";
      else if (isDIM2 (type))
        before = "DIM2 ";
      else if (isRADIANS (type))
      {
        before = "RADIANS ";
        comment = null;
      }
      else if (isDEGREES (type))
      {
        before = "DEGREES ";
        comment = null;
      }
      else if (isGRADS (type))
      {
        before = "GRADS ";
        comment = null;
      }
      else
      {
        before = "[";
        between = " .. ";
        after = "]";
      }

      ipw.print (before);
      error |= printErrorOrToString (type.getMinimum());
      ipw.print (between);
      error |= printErrorOrToString (type.getMaximum());
      ipw.print (after);


      if (error)
        return rsrc.getString ("err_inSource");
      else
        return comment;
    }
  }




  private boolean isDIM1 (NumericType type)
  {
    if (type == null)
      return false;

    Unit unit = type.getUnit ();
    if (unit == null)
      return false;
	if (!((unit instanceof NumericallyDerivedUnit)
			|| (unit instanceof BaseUnit))){
	  return false;
	}

    return unit.isExtendingIndirectly (td.INTERLIS.LENGTH);
  }



  private boolean isDIM2 (NumericType type)
  {
    Unit unit, baseUnit, otherUnit;
    ComposedUnit.Composed[] parts;


    if (type == null)
      return false;

    unit = type.getUnit ();
    if (!(unit instanceof ComposedUnit))
      return false;

    parts = ((ComposedUnit) unit).getComposedUnits();
    if ((parts == null) || (parts.length != 2))
      return false;

    if (!parts[0].getUnit ().isExtendingIndirectly (td.INTERLIS.LENGTH))
      return false;
    if (parts[0].getCompositionOperator() != '*')
      return false;
	if (!parts[1].getUnit ().isExtendingIndirectly (td.INTERLIS.LENGTH))
	  return false;
	if (parts[1].getCompositionOperator() != '*')
	  return false;

    return true;
  }



  private boolean isRADIANS (NumericType type)
  {
    if (type == null)
      return false;

    return type.getUnit () == td.INTERLIS.RADIAN;
  }



  private boolean isDEGREES (NumericType type)
  {
    Unit unit, baseUnit;
    NumericallyDerivedUnit.Factor[] factors;


    if (type == null)
      return false;

    unit = type.getUnit ();
    if (!(unit instanceof NumericallyDerivedUnit))
      return false;

    baseUnit = (Unit)((NumericallyDerivedUnit) unit).getExtending ();
    if (baseUnit != td.INTERLIS.RADIAN)
      return false;

    factors = ((NumericallyDerivedUnit) unit).getConversionFactors ();
    if ((factors == null) || (factors.length != 2)
        || (factors[0] == null) || (factors[1] == null))
      return false;

    if (factors[0].getConversionFactor () != PrecisionDecimal.PI)
      return false;


    if (factors[0].getConversionOperator () != '*')
      return false;


    if (factors[1].getConversionFactor ().doubleValue() != 180.0)
      return false;


    if (factors[1].getConversionOperator () != '/')
      return false;

    return true;
  }



  private boolean isGRADS (NumericType type)
  {
    Unit unit, baseUnit;
    NumericallyDerivedUnit.Factor[] factors;


    if (type == null)
      return false;

    unit = type.getUnit ();
    if (!(unit instanceof NumericallyDerivedUnit))
      return false;


    baseUnit = (Unit)((NumericallyDerivedUnit) unit).getExtending ();
    if (baseUnit != td.INTERLIS.RADIAN)
      return false;


    factors = ((NumericallyDerivedUnit) unit).getConversionFactors ();
    if ((factors == null) || (factors.length != 2)
        || (factors[0] == null) || (factors[1] == null))
      return false;


    if (factors[0].getConversionFactor () != PrecisionDecimal.PI)
      return false;


    if (factors[0].getConversionOperator () != '*')
      return false;


    if (factors[1].getConversionFactor().doubleValue() != 200.0)
      return false;


    if (factors[1].getConversionOperator () != '/')
      return false;

    return true;
  }

  public static int countEnumLeafs(ch.interlis.ili2c.metamodel.Enumeration enumer){
      int ret=0;
      Iterator iter = enumer.getElements();
      while (iter.hasNext()) {
        ch.interlis.ili2c.metamodel.Enumeration.Element ee=(ch.interlis.ili2c.metamodel.Enumeration.Element) iter.next();
        ch.interlis.ili2c.metamodel.Enumeration subEnum = ee.getSubEnumeration();
        if (subEnum != null)
        {
          ret+=countEnumLeafs(subEnum);
        }else{
          // ee is a leaf
          ret++;
        }
      }
      return ret;
  }
  private void printEnumeration (ch.interlis.ili2c.metamodel.Enumeration enumer)
  {
    if(genFmt){
      // count leafs
      int leafs=countEnumLeafs(enumer);
      int size=Integer.toString(leafs).length();
      ipw.print(" "+genFmtField(fmtAttrIdx,size));
    }else{
      ipw.println ('(');
      ipw.indent ();

      if (enumer == null)
        printError ();
      else
      {
        Iterator iter = enumer.getElements();
        while (iter.hasNext()) {
          printEnumerationElement((ch.interlis.ili2c.metamodel.Enumeration.Element) iter.next());
          if (iter.hasNext())
            ipw.println (',');
        }
      }


      ipw.unindent ();
      ipw.print (')');
    }
  }


  private void printEnumerationElement (ch.interlis.ili2c.metamodel.Enumeration.Element ee)
  {
    ipw.print(ee.getName());


    ch.interlis.ili2c.metamodel.Enumeration subEnum = ee.getSubEnumeration();
    if (subEnum != null)
    {
      ipw.print(' ');
      printEnumeration (subEnum);
    }
  }

  private String printCoordType (CoordType coord)
  {
    /* Fix for BUG-1999-00003.
       Sascha Brawer <brawer@acm.org>, 1999-10-07
    */
    if (coord == null)
    {
      printError ();
      return rsrc.getString ("err_inSource");
    }
    /* End of fix */

    NumericalType[] dimensions = coord.getDimensions ();
    boolean         error = false;

    if (dimensions == null)
    {
      printError ();
      return rsrc.getString ("err_inSource");
    }

    if (dimensions.length == 2){
      if(!genFmt){
        ipw.print ("COORD2");
      }
    }else if (dimensions.length == 3){
      if(!genFmt){
        ipw.print ("COORD3");
      }
    }else{
      printError ();
      return rsrc.getString ("err_notExpressible");
    }


    /* Structured numeric types can not be expressed in INTERLIS-1
       as coordinate dimensions
    */
    if ((dimensions[0] instanceof StructuredUnitType)
        || (dimensions[1] instanceof StructuredUnitType)
        || ((dimensions.length > 2) && (dimensions[2] instanceof StructuredUnitType)))
    {
      printError ();
      return rsrc.getString ("err_notExpressible");
    }

    if(!genFmt){
      ipw.println ();
      ipw.indent ();
    }

    PrecisionDecimal minE = null, minN = null, minH = null;
    PrecisionDecimal maxE = null, maxN = null, maxH = null;


    NumericType eastingDimension, northingDimension, heightDimension;

    if (dimensions[0] instanceof NumericType)
      eastingDimension = (NumericType) dimensions[0];
    else
      eastingDimension = null;


    if (dimensions[1] instanceof NumericType)
      northingDimension = (NumericType) dimensions[1];
    else
      northingDimension = null;


    if ((dimensions.length == 3) && (dimensions[2] instanceof NumericType))
      heightDimension = (NumericType) dimensions[2];
    else
      heightDimension = null;

    if (eastingDimension != null)
    {
      minE = eastingDimension.getMinimum();
      maxE = eastingDimension.getMaximum();
    }

    if (northingDimension != null)
    {
      minN = northingDimension.getMinimum();
      maxN = northingDimension.getMaximum();
    }


    if (heightDimension != null)
    {
      minH = heightDimension.getMinimum();
      maxH = heightDimension.getMaximum();
    }

    if(genFmt){
      ipw.print(" "+genFmtField(fmtAttrIdx,minE,maxE));
      ipw.print(" "+genFmtField(fmtAttrIdx,minN,maxN));
      if (dimensions.length == 3){
        ipw.print(" "+genFmtField(fmtAttrIdx,minH,maxH));
      }
    }else{
    	if (params != null) {
    		error |= printErrorOrToString (minE, params.getFactor_x(), params.getDiff_x());
    	} else {
    		error |= printErrorOrToString (minE);
    	}
      ipw.print (' ');
      	if (params != null) {
      		error |= printErrorOrToString (minN, params.getFactor_x(), params.getDiff_x());
      	} else {
      		error |= printErrorOrToString (minN);
      	}
      
      if (dimensions.length == 3){
        ipw.print (' ');
        error |= printErrorOrToString (minH);
      }
      ipw.println ();
      	if (params != null) {
      		error |= printErrorOrToString (maxE, params.getFactor_y(), params.getDiff_y());
      	} else {
      		error |= printErrorOrToString (maxE);
      	}
      ipw.print (' ');
      	if (params != null) {
      		error |= printErrorOrToString (maxN, params.getFactor_y(), params.getDiff_y());
      	} else {
      		error |= printErrorOrToString (maxN);
      	}
      
      if (dimensions.length == 3){
        ipw.print (' ');
        error |= printErrorOrToString (maxH);
      }
      ipw.unindent ();
    }
    if (error)
      return rsrc.getString ("err_inSource");
    else
      return null;
  }
  
  private String printLineType (LineType lineType)
  {
    if(genFmt){
      return null;
    }
    String comment = null;

    if (lineType instanceof PolylineType)
      ipw.print ("POLYLINE ");
    else if (lineType instanceof SurfaceType)
      ipw.print ("SURFACE ");
    else if (lineType instanceof AreaType)
      ipw.print ("AREA ");

    LineForm[] lineForms = lineType.getLineForms ();
    ipw.print ("WITH (");
    for (int i = 0; i < lineForms.length; i++)
    {
      if (i > 0)
        ipw.print (", ");

      if (lineForms[i] == null)
        printError ();
      else if (lineForms[i] == td.INTERLIS.STRAIGHTS)
        ipw.print ("STRAIGHTS");
      else if (lineForms[i] == td.INTERLIS.ARCS)
        ipw.print ("ARCS");
      else
        printExplanation (lineForms[i].getExplanation());
    }
    ipw.println (")");

    ipw.indent ();
    ipw.print ("VERTEX ");
    Domain controlPointDomain = lineType.getControlPointDomain();
    if (controlPointDomain == null)
      printError ();
    else
      comment = printCoordType ((CoordType) Type.findReal (controlPointDomain.getType()));

    PrecisionDecimal maxOverlap = lineType.getMaxOverlap ();
    if (maxOverlap != null)
    {
      ipw.println ();
      ipw.print ("WITHOUT OVERLAPS > ");
      ipw.print (maxOverlap.toString ());
    }
    printLineAttributes (lineType);
    ipw.unindent ();

    return comment;
  }
  private void printSurfaceOrAreaTypeFmt(AbstractClassDef table,AttributeDef thisAttr,SurfaceOrAreaType lineType)
  {
    if(!genFmt){
      return;
    }
      ipw.println(FMT_CMT); // additional line to improve readability
      ipw.println("TABL "+table.getName()+"_"+thisAttr.getName());
      ipw.print("OBJE "+genFmtField(1,1));
      fmtAttrIdx=2;
      if(lineType instanceof SurfaceType){
        ipw.print(" "+genFmtField(fmtAttrIdx,1));
        fmtAttrIdx++;
      }
      // print line attrs
      Iterator iter=null;
      Table attributeTable = lineType.getLineAttributeStructure ();
      if(attributeTable!=null){
        iter = attributeTable.getAttributes ();
        while (iter.hasNext ()){
          printAttribute (attributeTable, (AttributeDef) iter.next ());
        }
      }

      // force newline
      ipw.println();
      // print attribute explanations
      ipw.println(FMT_CMT); // additional line to improve readability
      ipw.println(FMT_CMT+" 1: Objektidentifikation");
      int idx=2;
      if(lineType instanceof SurfaceType){
        ipw.println(FMT_CMT+" 2: ->"+table.getName());
        idx++;
      }
      if(attributeTable!=null){
        iter = attributeTable.getAttributes ();
        while (iter.hasNext ()){
          AttributeDef attr=(AttributeDef) iter.next ();
          Type type = Type.findReal (attr.getDomain());
          if((type instanceof LineType)
              && !(type instanceof AreaType)){
              // LineType's but not AreaType
              ; // not part of main record
          }else{
            // AreaType: centroid is part of main record
            // other non LineType's
            ipw.println(FMT_CMT+" "+getIdxCode(idx)+": "+attr.getName());
          }
          idx++;
        }
      }
      ipw.println(FMT_CMT); // additional line to improve readability

      printLineTypeFmt(lineType);
      ipw.println("ETAB");
  }
  private void printLineTypeFmt(LineType lineType)
  {
    if(!genFmt){
      return;
    }

    if (lineType instanceof PolylineType){
    }else if (lineType instanceof SurfaceType){
      //ipw.println("OBJE 1");
    }else if (lineType instanceof AreaType){
      //ipw.print ("AREA ");
    }
    CoordType ct=null;
    Domain controlPointDomain = lineType.getControlPointDomain();
    if (controlPointDomain == null){
      printError ();
    }else{
      ct= (CoordType) Type.findReal (controlPointDomain.getType());
    }
    fmtAttrIdx=1;
    ipw.print ("STPT ");
    printCoordType (ct);
    ipw.println();
    ipw.print ("LIPT ");
    printCoordType (ct);
    ipw.println();
    LineForm[] lineForms = lineType.getLineForms ();
    for (int i = 0; i < lineForms.length; i++)
    {
      if (lineForms[i] == null)
        printError ();
      if (lineForms[i] == td.INTERLIS.ARCS){
        ipw.print ("ARCP ");
        printCoordType(ct);
        ipw.println();
      }
    }
    ipw.println ("ELIN");

   // printLineAttributes (lineType);

    return;
  }


  private void printLineAttributes (LineType lineType)
  {
    if (lineType == null)
      return;

    Table attributeTable = null;

    if (lineType instanceof SurfaceOrAreaType)
      attributeTable = ((SurfaceOrAreaType) lineType).getLineAttributeStructure ();

    if (attributeTable == null)
      return;

    /* This is not true anymore, since the entire line attribute
       handling has changed a lot. -- Sascha Brawer, 2000-02-08.

       See BUG-1999-00002. needToPrintTable will always return false for
       implicit tables. All line attribute tables have been created
       implicitly by the compiler. Indeed, they should not be printed
       out as part of the topic they belong to.

       if (!needToPrintTable (attributeTable))
         return;
    */


    boolean hasOutput = false;
    Iterator attrs = attributeTable.getAttributes ();
    while (attrs.hasNext ())
    {
      AttributeDef attr = (AttributeDef) attrs.next ();
      if (!hasOutput)
      {
        ipw.println ();
        ipw.println ("LINEATTR =");
        ipw.indent ();
        hasOutput = true;
      }

      printAttribute (attributeTable, attr);
    }

    if (hasOutput)
    {
      ipw.unindent ();
      ipw.print ("END");
    }
  }

  private static char getIdxCode(int idx){
    return Character.toUpperCase(Character.forDigit(idx,Character.MAX_RADIX));
  }
  private String genFmtField(int idx,int size){
    StringBuilder ret=new StringBuilder(size);
    char c=getIdxCode(idx);
    for(int i=0;i<size;i++){
      ret.append(c);
    }
    return ret.toString();
  }
  public static String genFmtField(int idx,PrecisionDecimal value){
    StringBuilder ret=new StringBuilder(10);
    char c=getIdxCode(idx);
    int size=value.getUnscaledValue().toString().length();
    for(int i=0;i<size;i++){
      ret.append(c);
    }
    size=value.getAccuracy();
    if(size>0){
      ret.append('.');
      for(int i=0;i<size;i++){
        ret.append(c);
      }
    }
    return ret.toString();
  }
  public static String genFmtField(int idx,PrecisionDecimal min,PrecisionDecimal max){
    String minFld=genFmtField(idx,min);
    String maxFld=genFmtField(idx,max);
    if(minFld.length()>maxFld.length()){
      return minFld;
    }
    return maxFld;
  }
  private RoleDef getOppEnd(RoleDef oneRole){
    RoleDef role1=null;
    RoleDef role2=null;
    Iterator rolei = ((AssociationDef)oneRole.getContainer()).getAttributesAndRoles();
    while (rolei.hasNext())
    {
      Object obj = rolei.next();
      if (obj instanceof RoleDef)
      {
        if(role1==null){
	  role1 = (RoleDef) obj;
        }else if(role2==null){
	  role2 = (RoleDef) obj;
        }
      }
    }
    if(role1==oneRole){
      return role2;
    }
    return role1;
  }


public TransformationParameter getParams() {
	return params;
}


public void setParams(TransformationParameter params) {
	this.params = params;
}

}
