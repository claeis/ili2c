
package ch.interlis.ili2c.generator;

import java.util.ArrayList;
import java.util.Iterator;

import ch.ehi.basics.types.OutParam;

import ch.ehi.basics.logging.EhiLogger;
import ch.interlis.ili2c.metamodel.AttributeDef;
import ch.interlis.ili2c.metamodel.Container;
import ch.interlis.ili2c.metamodel.Domain;
import ch.interlis.ili2c.metamodel.DomainConstraint;
import ch.interlis.ili2c.metamodel.Element;
import ch.interlis.ili2c.metamodel.EnumerationType;
import ch.interlis.ili2c.metamodel.ObjectPath;
import ch.interlis.ili2c.metamodel.ValueRefThis;
import ch.interlis.iom_j.ViewableProperties;
import ch.interlis.iom_j.xtf.XtfWriterBase;
import ch.interlis.iom_j.xtf.XtfModel;
import ch.interlis.iox.IoxException;
import ch.interlis.iox.IoxWriter;
import ch.interlis.iox_j.EndBasketEvent;
import ch.interlis.iox_j.EndTransferEvent;
import ch.interlis.iox_j.ObjectEvent;
import ch.interlis.iox_j.StartBasketEvent;
import ch.interlis.iox_j.StartTransferEvent;
import ch.interlis.models.IlisMeta16.ModelData.*;
import ch.interlis.models.*;

public class Imd16Generator {

	IoxWriter out=null;
	private static String AGGREGATES="AGGREGATES";
	private static String ALIAS="ALIAS";
    private static String ENUM_TOP="TOP";
	
	ch.interlis.ili2c.metamodel.TransferDescription td=null;
	public Imd16Generator( IoxWriter out ) {
		this.out = out;
	}

	public static void generate( java.io.File out, ch.interlis.ili2c.metamodel.TransferDescription td,String sender ) 
	{
		IoxWriter ioxWriter = null;
		Imd16Generator d = null;
		try {
			try{
				ioxWriter = new XtfWriterBase( out,  getIoxMapping(),"2.4");
				((XtfWriterBase)ioxWriter).setModels(new XtfModel[]{ILISMETA16.getXtfModel()});
				d = new Imd16Generator( ioxWriter );

				d.encode( td,sender );
			}finally{
				if(ioxWriter!=null){
					ioxWriter.flush();
					ioxWriter.close();
					ioxWriter=null;
				}
			}
		} catch ( Throwable e ) {
			throw new IllegalStateException( e );
		}
		return;
	}
	
	public void encode( ch.interlis.ili2c.metamodel.TransferDescription rootObj,String sender ) throws IoxException {

		this.td=rootObj;
		
		// setup language mapping
        for ( Iterator it = rootObj.iterator(); it.hasNext(); ) {
            Object o = it.next();
            if ( o instanceof ch.interlis.ili2c.metamodel.Model ) {
                ch.interlis.ili2c.metamodel.Model ele=(ch.interlis.ili2c.metamodel.Model)o;
                setupLanguageMappingElement(ele);
                setupLanguageMapping(ele);
            }
        }

		StartTransferEvent startTransferEvent = new StartTransferEvent();
//		itfStartTransferEvent.setModelDefinition( "" );
//		itfStartTransferEvent.setModelId( "" ); {
		
		startTransferEvent.setSender( sender );
		out.write( startTransferEvent );

		for ( Iterator it = rootObj.iterator(); it.hasNext(); ) {
			Object o = it.next();
			if ( o instanceof ch.interlis.ili2c.metamodel.Model ) {
			    ch.interlis.ili2c.metamodel.Model model=(ch.interlis.ili2c.metamodel.Model)o;
			    if(model.getTranslationOf()==null) {
	                visitModel(model);
			    }else {
                    visitTranslatedModel(model);
			    }
			}
		}
		
		
		
		out.write( new EndTransferEvent() );
		
		out.flush();
	}

    private void visitTranslatedModel( ch.interlis.ili2c.metamodel.Model model ) 
    throws IoxException
    {

        StartBasketEvent startBasketEvent = new StartBasketEvent( ILISMETA16.ModelTranslation, "MODEL."+model.getScopedName(null) );
        out.write( startBasketEvent );
        ch.interlis.models.IlisMeta16.ModelTranslation.Translation iomModel = new ch.interlis.models.IlisMeta16.ModelTranslation.Translation( "TRANSLATION."+model.getScopedName(null) );
        iomModel.setLanguage(model.getLanguage());
        visitTranslatedElements(iomModel,model);
        out.write(new ObjectEvent(iomModel));
        EndBasketEvent endBasketEvent = new EndBasketEvent();
        out.write( endBasketEvent );
    }
	private void visitModel( ch.interlis.ili2c.metamodel.Model model ) 
	throws IoxException
	{

		StartBasketEvent startBasketEvent = new StartBasketEvent( ILISMETA16.ModelData, "MODEL."+model.getScopedName(null) );
		out.write( startBasketEvent );
		
		
		ch.interlis.models.IlisMeta16.ModelData.Model iomModel = new ch.interlis.models.IlisMeta16.ModelData.Model( model.getScopedName(null) );
				
		iomModel.setName( model.getName() );
		
		if ( model.getDocumentation() != null ) {
			DocText doc = new DocText();
			doc.setText( model.getDocumentation() );
			iomModel.addDocumentation( doc );
		}
		
		// Model
		iomModel.setContracted( model.isContracted() );
		iomModel.setiliVersion( model.getIliVersion() );
		if(model.getLanguage()!=null) {
	        iomModel.setLanguage( model.getLanguage() );
		}
		if(model.getCharSetIANAName()!=null) {
	        iomModel.setCharSetIANAName(model.getCharSetIANAName());
		}
		if(model.getXmlns()!=null) {
	        iomModel.setxmlns(model.getXmlns());
		}
		if(model.getNoIncrementalTransfer()!=null){
			iomModel.setNoIncrementalTransfer(model.getNoIncrementalTransfer());
		}
		if(model.getIliVersion().equals(ch.interlis.ili2c.metamodel.Model.ILI2_3)){
			iomModel.setAt( model.getIssuer() );
			iomModel.setVersion( model.getModelVersion() );
		}else if(model.getIliVersion().equals(ch.interlis.ili2c.metamodel.Model.ILI1)){
			iomModel.setili1Transfername(td.getName());
			// set ili1 format
			ch.interlis.ili2c.metamodel.Ili1Format ili1fmt=td.getIli1Format();
			Ili1Format iomIli1Format=new Ili1Format();
			iomIli1Format.setisFree(ili1fmt.isFree);
			if(!ili1fmt.isFree){
				iomIli1Format.setLineSize(ili1fmt.lineSize);
				iomIli1Format.settidSize(ili1fmt.tidSize);
			}
			iomIli1Format.setblankCode(ili1fmt.blankCode);
			iomIli1Format.setundefinedCode(ili1fmt.undefinedCode);
			iomIli1Format.setcontinueCode(ili1fmt.continueCode);
			iomIli1Format.setFont(ili1fmt.font);
			if(ili1fmt.tidKind==ch.interlis.ili2c.metamodel.Ili1Format.TID_I16){
				iomIli1Format.settidKind(Ili1Format_tidKind.TID_I16);
			}else if(ili1fmt.tidKind==ch.interlis.ili2c.metamodel.Ili1Format.TID_I32){
				iomIli1Format.settidKind(Ili1Format_tidKind.TID_I32);
			}else if(ili1fmt.tidKind==ch.interlis.ili2c.metamodel.Ili1Format.TID_ANY){
				iomIli1Format.settidKind(Ili1Format_tidKind.TID_ANY);
			}else if(ili1fmt.tidKind==ch.interlis.ili2c.metamodel.Ili1Format.TID_EXPLANATION){
				iomIli1Format.settidKind(Ili1Format_tidKind.TID_EXPLANATION);
				iomIli1Format.settidExplanation(ili1fmt.tidExplanation);
			}else{
				throw new IllegalArgumentException("unexpexcted tidKind "+ili1fmt.tidKind);
			}
			iomModel.setili1Format(iomIli1Format);
		}

		if ( model instanceof ch.interlis.ili2c.metamodel.DataModel ) {
			iomModel.setKind( Model_Kind.NormalM );
		} else if ( model instanceof ch.interlis.ili2c.metamodel.RefSystemModel ) {
			iomModel.setKind( Model_Kind.RefSystemM );
		} else if ( model instanceof ch.interlis.ili2c.metamodel.SymbologyModel ) {
			iomModel.setKind( Model_Kind.SymbologyM );
		} else if ( model instanceof ch.interlis.ili2c.metamodel.TypeModel ) {
			iomModel.setKind( Model_Kind.TypeM );
		} else {
			throw new IllegalArgumentException( "Unknown subclass of Model: " + model.getClass() );
		}

		out.write(new ObjectEvent(iomModel));
		visitMetaValues(getMetaValues(model),iomModel.getobjectoid());
		
		ch.interlis.ili2c.metamodel.Model[] importing = model.getImporting();
		String modelOid = iomModel.getobjectoid();
		
		for ( int i = 0; i < importing.length; i++ ) {
			ch.interlis.ili2c.metamodel.Model importable = importing[i];
			Import iomImport = new Import();
			iomImport.setImportingP( modelOid );
			iomImport.setImportedP( getRef(importable) );
			
			out.write(new ObjectEvent(iomImport));
		}
		visitElements(model);
		
		out.write( new EndBasketEvent() );

	}
	private final String METAOBJECT="METAOBJECT";
	private void visitMetaValues(ch.ehi.basics.settings.Settings values,String modelElementId)
	throws IoxException
	{
		if(values!=null){
			for( MetaAttribute val: createMetaAttributes(values,modelElementId,null)){
				out.write(new ObjectEvent(val));
			}
		}
	}
	private ArrayList<MetaAttribute> createMetaAttributes(ch.ehi.basics.settings.Settings values,String modelElmentId,String namePrefix)
	throws IoxException
	{
		ArrayList<MetaAttribute> ret=new ArrayList<MetaAttribute>();
		if(values!=null){
			if(namePrefix==null){
				namePrefix="";
			}
			Iterator it=values.getValues().iterator();
			while(it.hasNext()){
				String name=(String)it.next();
				String value=values.getValue(name);
				MetaAttribute iomMetaAttr=new MetaAttribute(modelElmentId+"."+METAOBJECT+"."+namePrefix+name);
				iomMetaAttr.setMetaElement(modelElmentId);
				iomMetaAttr.setName(namePrefix+name);
				iomMetaAttr.setValue(value);
				ret.add(iomMetaAttr);
			}
		}
		return ret;
	}

	private final String UNIT_META_NAME="BASKET";
	private void visitTopic( ch.interlis.ili2c.metamodel.Topic topic ) 
	throws IoxException
	{
		
		SubModel subModel = new SubModel( topic.getScopedName(null) );
		subModel.setElementInPackage( getRef(topic.getContainer()) );

		subModel.setName( topic.getName() );
		
		if(topic.getDocumentation()!=null){
			DocText doc = new DocText();
			doc.setText( topic.getDocumentation() );
			subModel.addDocumentation( doc );					
		}

		out.write(new ObjectEvent(subModel));
		visitMetaValues(getMetaValues(topic),subModel.getobjectoid());

		String dataUnitOid = topic.getScopedName(null)+"."+UNIT_META_NAME;
		DataUnit dataUnit = new DataUnit(dataUnitOid);
		dataUnit.setElementInPackage( subModel.getobjectoid() );
		// DataUnit 		
		dataUnit.setViewUnit( topic.isViewTopic() );
		dataUnit.setDataUnitName( getRef(topic) ); // Basket-Name
		dataUnit.setName( UNIT_META_NAME );
		if(topic.getBasketOid()!=null){
			dataUnit.setOid(getRef(topic.getBasketOid()));
		}

		// ExtendableME 
		dataUnit.setAbstract( topic.isAbstract() );
		if ( topic.getExtending() != null ) {
			dataUnit.setSuper( getRef(topic.getExtending()) +"."+UNIT_META_NAME);
		}
		dataUnit.setFinal( topic.isFinal() );
		dataUnit.setGeneric(false);
		
		Iterator depi=topic.getDependentOn();
		while(depi.hasNext()){
			ch.interlis.ili2c.metamodel.Topic dep=(ch.interlis.ili2c.metamodel.Topic)depi.next();
			Dependency dependency=new Dependency();
			dependency.setDependent(getRef(topic)+"."+UNIT_META_NAME);
			dependency.setUsing(getRef(dep)+"."+UNIT_META_NAME);
			out.write(new ObjectEvent(dependency));
		}
		
		out.write(new ObjectEvent(dataUnit));
		
		Iterator tei=topic.getTransferViewables().iterator();
		while(tei.hasNext()){
			ch.interlis.ili2c.metamodel.Viewable v=(ch.interlis.ili2c.metamodel.Viewable)tei.next();
			AllowedInBasket te=new AllowedInBasket();
			te.setClassInBasket(getRef(v));
			te.setOfDataUnit(dataUnit.getobjectoid());
			out.write(new ObjectEvent(te));
		}

		ch.interlis.ili2c.metamodel.Model model = (ch.interlis.ili2c.metamodel.Model) topic.getContainer(ch.interlis.ili2c.metamodel.Model.class);
		for (ch.interlis.ili2c.metamodel.Domain deferredGeneric : topic.getDefferedGenerics()) {
			String genericDefOid = dataUnitOid + "." + deferredGeneric.getName();
			GenericDef genericDef = new GenericDef(genericDefOid);
			genericDef.setContext(dataUnitOid);
			genericDef.setGenericDomain(deferredGeneric.getScopedName());
			out.write(new ObjectEvent(genericDef));

			ch.interlis.ili2c.metamodel.Domain[] allowedDomains = model.resolveGenericDomain(deferredGeneric);
			for (ch.interlis.ili2c.metamodel.Domain allowed : allowedDomains) {
				ConcreteForGeneric concreteForGeneric = new ConcreteForGeneric();
				concreteForGeneric.setGenericDef(genericDefOid);
				concreteForGeneric.setConcreteDomain(getRef(allowed));
				out.write(new ObjectEvent(concreteForGeneric));
			}
		}
		
		visitElements(topic);
		
		return;		
	}
	private String getRef(Element ele) {
        return ele.getElementInRootLanguage().getScopedName();
    }

    private void visitViewable(ch.interlis.ili2c.metamodel.Viewable classDef ) 
	throws IoxException
	{
		ch.interlis.models.IlisMeta16.ModelData.Class iomClass = null;
		if(classDef instanceof ch.interlis.ili2c.metamodel.View){
			iomClass=new ch.interlis.models.IlisMeta16.ModelData.View(classDef.getScopedName(null) );
		}else if(classDef instanceof ch.interlis.ili2c.metamodel.AbstractClassDef){
			iomClass=new ch.interlis.models.IlisMeta16.ModelData.Class(classDef.getScopedName(null) );
		}else{
			throw new IllegalArgumentException( "Unknown subclass of Viewable: " + classDef.getClass() );
		}
		iomClass.setElementInPackage( getRef(classDef.getContainer()) );

		iomClass.setName( classDef.getName() );
		
		if ( classDef.getDocumentation() != null ) {
			DocText doc = new DocText();
			doc.setText( classDef.getDocumentation() );
			iomClass.addDocumentation( doc );
		}
		
		// ExtendableME 
		iomClass.setAbstract( classDef.isAbstract() );
		if ( classDef.getExtending() != null ) {
			iomClass.setSuper( getRef(classDef.getExtending()));
		}
		iomClass.setFinal( classDef.isFinal() );
		iomClass.setGeneric(false);
		iomClass.setEmbeddedRoleTransfer(false );
		
		int attrOrderPos=1;
		if(classDef instanceof ch.interlis.ili2c.metamodel.Table){
			ch.interlis.ili2c.metamodel.Table table=(ch.interlis.ili2c.metamodel.Table)classDef;
			if ( table.isIdentifiable() ) {
				iomClass.setKind( Class_Kind._class );
				ch.interlis.ili2c.metamodel.Domain oidDomain=table.getDefinedOid();
				if(oidDomain==null){
					ch.interlis.ili2c.metamodel.Topic topic=(ch.interlis.ili2c.metamodel.Topic)table.getContainer(ch.interlis.ili2c.metamodel.Topic.class);
					if(topic!=null){
						oidDomain=topic.getOid();
					}
				}
				if(oidDomain!=null && !(oidDomain instanceof ch.interlis.ili2c.metamodel.NoOid)){
					iomClass.setOid(getRef(oidDomain));
				}
			} else {
				iomClass.setKind( Class_Kind.Structure );
			}
			if(td.getLastModel().getIliVersion().equals(ch.interlis.ili2c.metamodel.Model.ILI1)){
				iomClass.setili1OptionalTable(table.isIli1Optional());
			}
		}else if(classDef instanceof ch.interlis.ili2c.metamodel.View){
			ch.interlis.ili2c.metamodel.View view=(ch.interlis.ili2c.metamodel.View)classDef;
			View iomView=(View)iomClass;
			iomClass.setKind( Class_Kind.View );
			if(view instanceof ch.interlis.ili2c.metamodel.AggregationView){
				ch.interlis.ili2c.metamodel.AggregationView agg=(ch.interlis.ili2c.metamodel.AggregationView)view;
				if(agg.getEqual()==null){
					iomView.setFormationKind(View_FormationKind.Aggregation_All);
				}else{
					iomView.setFormationKind(View_FormationKind.Aggregation_Equal);
					ch.interlis.ili2c.metamodel.UniqueEl uniqueEl=agg.getEqual();
					Iterator attri=uniqueEl.iteratorAttribute();
					while(attri.hasNext()){
						ch.interlis.ili2c.metamodel.ObjectPath attr=(ch.interlis.ili2c.metamodel.ObjectPath)attri.next();
						iomView.addFormationParameter(visitExpression(attr));
					}
				}
				ch.interlis.ili2c.metamodel.ViewableAlias viewAlias=agg.getBase();
				RenamedBaseView iomBase=new RenamedBaseView(iomClass.getobjectoid()+"."+ALIAS+"."+viewAlias.getName());
				iomBase.setView(getRef(view),1);
				iomBase.setName(viewAlias.getName());
				iomBase.setBaseView(getRef(viewAlias.getAliasing()));
				out.write(new ObjectEvent(iomBase));
				// AGGREGATES
				String attrTid=getRef(view)+"."+AGGREGATES;
				AttrOrParam iomAttr = new AttrOrParam( attrTid );
				iomAttr.setAttrParent(iomView.getobjectoid(),attrOrderPos++);
				iomAttr.setName( AGGREGATES );
				out.write(new ObjectEvent(iomAttr));
				MultiValue iomMultiValue=new MultiValue(attrTid+"."+LOCAL_TYPE_NAME);
				iomMultiValue.setName(LOCAL_TYPE_NAME);
				iomMultiValue.setOrdered(false);
				Multiplicity iomMultiplicity = visitCardinality(new ch.interlis.ili2c.metamodel.Cardinality(0,ch.interlis.ili2c.metamodel.Cardinality.UNBOUND));
				iomMultiValue.setMultiplicity(iomMultiplicity);
				iomMultiValue.setBaseType(iomBase.getBaseView());
				out.write(new ObjectEvent(iomMultiValue));
			}else if(view instanceof ch.interlis.ili2c.metamodel.DecompositionView){
				ch.interlis.ili2c.metamodel.DecompositionView insp=(ch.interlis.ili2c.metamodel.DecompositionView)view;
				iomView.setFormationKind(insp.isAreaDecomposition()?View_FormationKind.Inspection_Area:View_FormationKind.Inspection_Normal);
				PathOrInspFactor iomPath=new PathOrInspFactor();
				iomView.addFormationParameter(visitObjectPathEls(iomPath,insp.getDecomposedAttribute()));
				ch.interlis.ili2c.metamodel.ViewableAlias viewAlias=insp.getRenamedViewable();
				RenamedBaseView iomBase=new RenamedBaseView(iomClass.getobjectoid()+"."+ALIAS+"."+viewAlias.getName());
				iomBase.setView(getRef(view),1);
				iomBase.setName(viewAlias.getName());
				iomBase.setBaseView(getRef(viewAlias.getAliasing()));
				out.write(new ObjectEvent(iomBase));
			}else if(view instanceof ch.interlis.ili2c.metamodel.ExtendedView){
				iomView.setFormationKind(View_FormationKind.Projection);
			}else if(view instanceof ch.interlis.ili2c.metamodel.JoinView){
				ch.interlis.ili2c.metamodel.JoinView join=(ch.interlis.ili2c.metamodel.JoinView)view;
				iomView.setFormationKind(View_FormationKind.Join);
				ch.interlis.ili2c.metamodel.ViewableAlias viewAlias[]=join.getJoining();
				for(int aliasi=0;aliasi<viewAlias.length;aliasi++){
					RenamedBaseView iomBase=new RenamedBaseView(iomClass.getobjectoid()+"."+ALIAS+"."+viewAlias[aliasi].getName());
					iomBase.setName(viewAlias[aliasi].getName());
					iomBase.setOrNull(viewAlias[aliasi].isIncludeNull());
					iomBase.setView(getRef(view),aliasi+1);
					iomBase.setBaseView(getRef(viewAlias[aliasi].getAliasing()));
					out.write(new ObjectEvent(iomBase));
				}
			}else if(view instanceof ch.interlis.ili2c.metamodel.Projection){
				ch.interlis.ili2c.metamodel.Projection proj=(ch.interlis.ili2c.metamodel.Projection)view;
				iomView.setFormationKind(View_FormationKind.Projection);
				ch.interlis.ili2c.metamodel.ViewableAlias viewAlias=proj.getSelected();
				RenamedBaseView iomBase=new RenamedBaseView(iomClass.getobjectoid()+"."+ALIAS+"."+viewAlias.getName());
				iomBase.setView(getRef(view),1);
				iomBase.setName(viewAlias.getName());
				iomBase.setBaseView(getRef(viewAlias.getAliasing()));
				out.write(new ObjectEvent(iomBase));
			}else if(view instanceof ch.interlis.ili2c.metamodel.UnionView){
				ch.interlis.ili2c.metamodel.UnionView uv=(ch.interlis.ili2c.metamodel.UnionView)view;
				iomView.setFormationKind(View_FormationKind.Union);
				ch.interlis.ili2c.metamodel.ViewableAlias unitedv[]=uv.getUnited();
				for(int i=0;i<unitedv.length;i++){
					ch.interlis.ili2c.metamodel.ViewableAlias viewAlias=unitedv[i];
					RenamedBaseView iomBase=new RenamedBaseView(iomClass.getobjectoid()+"."+ALIAS+"."+viewAlias.getName());
					iomBase.setView(getRef(view),i+1);
					iomBase.setName(viewAlias.getName());
					iomBase.setBaseView(getRef(viewAlias.getAliasing()));
					out.write(new ObjectEvent(iomBase));
				}
			}else{	
				throw new IllegalArgumentException( "Unknown subclass of View: " + classDef.getClass() );
			}	
			
		}else{ // if(classDef instanceof ch.interlis.ili2c.metamodel.AssociationDef){
			ch.interlis.ili2c.metamodel.AssociationDef assoc=(ch.interlis.ili2c.metamodel.AssociationDef)classDef;
			iomClass.setKind(Class_Kind.Association);
			iomClass.setEmbeddedRoleTransfer(assoc.isLightweight());
			ch.interlis.ili2c.metamodel.Domain oidDomain=assoc.getDefinedOid();
			if(oidDomain==null && assoc.isIdentifiable()){
				ch.interlis.ili2c.metamodel.Topic topic=(ch.interlis.ili2c.metamodel.Topic)assoc.getContainer(ch.interlis.ili2c.metamodel.Topic.class);
				if(topic!=null){
					oidDomain=topic.getOid();
				}
			}
			if(oidDomain!=null && !(oidDomain instanceof ch.interlis.ili2c.metamodel.NoOid)){
				iomClass.setOid(getRef(oidDomain));
			}

			if(assoc.containsCardinality()){
				iomClass.setMultiplicity(visitCardinality(assoc.getDefinedCardinality()));
			}
			if(assoc.getDerivedFrom()!=null){
				iomClass.setView(getRef(assoc.getDerivedFrom()));
			}
		}
		
		ArrayList<MetaAttribute> cnstrMetaAttrs=new ArrayList<MetaAttribute>(); 
		int paramOrderPos=1;
		int roleOrderPos=1;
		for( Iterator it =  classDef.iterator(); it.hasNext(); ) {
			ch.interlis.ili2c.metamodel.Element element = (ch.interlis.ili2c.metamodel.Element)it.next();
			if ( element instanceof ch.interlis.ili2c.metamodel.Constraint ) {
				Constraint iomConstraint=visitConstraint((ch.interlis.ili2c.metamodel.Constraint)element,cnstrMetaAttrs,iomClass.getobjectoid());
				iomConstraint.setToClass(iomClass.getobjectoid());
				out.write(new ObjectEvent(iomConstraint));
			} else if ( element instanceof ch.interlis.ili2c.metamodel.Parameter ) {
				visitParameter((ch.interlis.ili2c.metamodel.Parameter)element,paramOrderPos);
				paramOrderPos++;
			} else if ( element instanceof ch.interlis.ili2c.metamodel.RoleDef ) {
				visitRoleDef((ch.interlis.ili2c.metamodel.RoleDef)element,roleOrderPos);
				roleOrderPos++;
			} else if ( element instanceof ch.interlis.ili2c.metamodel.ExpressionSelection) {
				if(classDef instanceof ch.interlis.ili2c.metamodel.View){
					ch.interlis.ili2c.metamodel.ExpressionSelection sel=(ch.interlis.ili2c.metamodel.ExpressionSelection)element;
					View iomView=(View)iomClass;
					iomView.setWhere(visitExpression(sel.getCondition()));
				}else{
					throw new IllegalArgumentException( "Illegal subclass of Viewable with Selection: " + classDef.getClass() );
				}
			} else {
				// skip it
			}
		}
		
		out.write(new ObjectEvent(iomClass));
		visitMetaValues(getMetaValues(classDef),iomClass.getobjectoid());
		for(MetaAttribute val:cnstrMetaAttrs){
			out.write(new ObjectEvent(val));
		}
		
		Iterator iter = classDef.getAttributesAndRoles2();
		while (iter.hasNext()) {
			ch.interlis.ili2c.metamodel.ViewableTransferElement obj = (ch.interlis.ili2c.metamodel.ViewableTransferElement)iter.next();
			if (obj.obj instanceof ch.interlis.ili2c.metamodel.AttributeDef) {
				ch.interlis.ili2c.metamodel.AttributeDef attr = (ch.interlis.ili2c.metamodel.AttributeDef) obj.obj;
				// not an inherited attr
				if(attr.getContainer()==classDef){
					visitAttribute((ch.interlis.ili2c.metamodel.LocalAttribute)attr,attrOrderPos);
				}
				TransferElement iomTe=new TransferElement();
				iomTe.setTransferClass(iomClass.getobjectoid());
				iomTe.setTransferElement(getAttrTid(attr), attrOrderPos++);
				out.write(new ObjectEvent(iomTe));
			}
			if(obj.obj instanceof ch.interlis.ili2c.metamodel.RoleDef){
				ch.interlis.ili2c.metamodel.RoleDef role = (ch.interlis.ili2c.metamodel.RoleDef) obj.obj;

				// not an embedded role and roledef not defined in a lightweight association?
				if (!obj.embedded){
					if(!((ch.interlis.ili2c.metamodel.AssociationDef)classDef).isLightweight()){
						TransferElement iomTe=new TransferElement();
						iomTe.setTransferClass(iomClass.getobjectoid());
						iomTe.setTransferElement(getRoleTid((ch.interlis.ili2c.metamodel.RoleDef)role), attrOrderPos++);
						out.write(new ObjectEvent(iomTe));
					}
				}else {
					// a role of an embedded association
					ch.interlis.ili2c.metamodel.AssociationDef roleOwner = (ch.interlis.ili2c.metamodel.AssociationDef) role.getContainer();
					if(roleOwner.getDerivedFrom()==null){
						TransferElement iomTe=new TransferElement();
						iomTe.setTransferClass(iomClass.getobjectoid());
						iomTe.setTransferElement(getRoleTid((ch.interlis.ili2c.metamodel.RoleDef)role), attrOrderPos++);
						out.write(new ObjectEvent(iomTe));
					}
				}
			}
			
		}		
		// Ili1TransferElement
		if(classDef instanceof ch.interlis.ili2c.metamodel.AbstractClassDef){
			int itfattrOrderPos=1;
			ArrayList<ch.interlis.ili2c.metamodel.ViewableTransferElement> ili1attrs=ch.interlis.iom_j.itf.ModelUtilities.getIli1AttrList((ch.interlis.ili2c.metamodel.AbstractClassDef) classDef);
			for(ch.interlis.ili2c.metamodel.ViewableTransferElement obj:ili1attrs){
				if (obj.obj instanceof ch.interlis.ili2c.metamodel.AttributeDef) {
					ch.interlis.ili2c.metamodel.AttributeDef attr = (ch.interlis.ili2c.metamodel.AttributeDef) obj.obj;
					Ili1TransferElement iomTe=new Ili1TransferElement();
					iomTe.setIli1TransferClass(iomClass.getobjectoid());
					iomTe.setIli1RefAttr(getAttrTid(attr), itfattrOrderPos++);
					out.write(new ObjectEvent(iomTe));
				}
				if(obj.obj instanceof ch.interlis.ili2c.metamodel.RoleDef){
					ch.interlis.ili2c.metamodel.RoleDef role = (ch.interlis.ili2c.metamodel.RoleDef) obj.obj;

					// not an embedded role and roledef not defined in a lightweight association?
					if (!obj.embedded){
						if(!((ch.interlis.ili2c.metamodel.AssociationDef)classDef).isLightweight()){
							Ili1TransferElement iomTe=new Ili1TransferElement();
							iomTe.setIli1TransferClass(iomClass.getobjectoid());
							iomTe.setIli1RefAttr(getRoleTid((ch.interlis.ili2c.metamodel.RoleDef)role), itfattrOrderPos++);
							out.write(new ObjectEvent(iomTe));
						}
					}else {
						// a role of an embedded association
						ch.interlis.ili2c.metamodel.AssociationDef roleOwner = (ch.interlis.ili2c.metamodel.AssociationDef) role.getContainer();
						if(roleOwner.getDerivedFrom()==null){
							Ili1TransferElement iomTe=new Ili1TransferElement();
							iomTe.setIli1TransferClass(iomClass.getobjectoid());
							iomTe.setIli1RefAttr(getRoleTid((ch.interlis.ili2c.metamodel.RoleDef)role), itfattrOrderPos++);
							out.write(new ObjectEvent(iomTe));
						}
					}
				}
				
			}
		}

	}
	private void visitGraphic(ch.interlis.ili2c.metamodel.Graphic graphic ) 
	throws IoxException
	{
		ch.interlis.models.IlisMeta16.ModelData.Graphic iomGraphic = new ch.interlis.models.IlisMeta16.ModelData.Graphic(graphic.getScopedName(null) );
		iomGraphic.setElementInPackage( getRef(graphic.getContainer()) );

		iomGraphic.setName( graphic.getName() );
		
		if ( graphic.getDocumentation() != null ) {
			DocText doc = new DocText();
			doc.setText( graphic.getDocumentation() );
			iomGraphic.addDocumentation( doc );
		}
		
		// ExtendableME 
		iomGraphic.setAbstract( graphic.isAbstract() );
		if ( graphic.getExtending() != null ) {
			iomGraphic.setSuper( getRef(graphic.getExtending()));
		}
		iomGraphic.setFinal( graphic.isFinal() );
		iomGraphic.setGeneric(false);
		
		iomGraphic.setBase(getRef(graphic.getBasedOn()));
		
		for( Iterator it =  graphic.iterator(); it.hasNext(); ) {
			ch.interlis.ili2c.metamodel.Element element = (ch.interlis.ili2c.metamodel.Element)it.next();
			if ( element instanceof ch.interlis.ili2c.metamodel.SignAttribute ) {
				visitSignAttribute((ch.interlis.ili2c.metamodel.SignAttribute)element);
			} else if ( element instanceof ch.interlis.ili2c.metamodel.ExpressionSelection) {
					ch.interlis.ili2c.metamodel.ExpressionSelection sel=(ch.interlis.ili2c.metamodel.ExpressionSelection)element;
					iomGraphic.setWhere(visitExpression(sel.getCondition()));
			} else {
				// skip it
			}
		}
		
		out.write(new ObjectEvent(iomGraphic));
		visitMetaValues(getMetaValues(graphic),iomGraphic.getobjectoid());
	}
	private void visitSignAttribute(ch.interlis.ili2c.metamodel.SignAttribute graphic ) 
	throws IoxException
	{
		ch.interlis.models.IlisMeta16.ModelData.DrawingRule iomGraphic = new ch.interlis.models.IlisMeta16.ModelData.DrawingRule(graphic.getScopedName(null) );
		iomGraphic.setGraphic(getRef(graphic.getContainer()) );

		iomGraphic.setName( graphic.getName() );
		
		if ( graphic.getDocumentation() != null ) {
			DocText doc = new DocText();
			doc.setText( graphic.getDocumentation() );
			iomGraphic.addDocumentation( doc );
		}
		
		// ExtendableME 
		iomGraphic.setAbstract( graphic.isAbstract() );
		if ( graphic.getExtending() != null ) {
			iomGraphic.setSuper( getRef(graphic.getExtending()));
		}
		iomGraphic.setFinal( graphic.isFinal() );
		iomGraphic.setGeneric(false);
		
		iomGraphic.set_class(getRef(graphic.getGenerating()));
		
		ch.interlis.ili2c.metamodel.SignInstruction instrv[]=graphic.getInstructions();
		for( int instri=0;instri<instrv.length;instri++ ) {
			ch.interlis.ili2c.metamodel.SignInstruction instr=instrv[instri];
			CondSignParamAssignment iomCAsgmt=new CondSignParamAssignment();
			iomGraphic.addRule(iomCAsgmt);
			iomCAsgmt.setWhere(visitExpression(instr.getRestrictor()));
			ch.interlis.ili2c.metamodel.ParameterAssignment asgmtv[]=instr.getAssignments();
			for(int asgmti=0;asgmti<asgmtv.length;asgmti++){
				SignParamAssignment iomAsgmt=new SignParamAssignment();
				iomCAsgmt.addAssignments(iomAsgmt);
				ch.interlis.ili2c.metamodel.Parameter param=asgmtv[asgmti].getAssigned();
				iomAsgmt.setParam(getRef(param));
				iomAsgmt.setAssignment(visitExpression(asgmtv[asgmti].getValue()));
			}
		}
		
		out.write(new ObjectEvent(iomGraphic));
	}
	private void visitDomain(ch.interlis.ili2c.metamodel.Domain domain)
	throws IoxException
	{
		visitDomainType(new OutParam<String>(),domain.getType(),domain);

		// visit domain constraints
		if (!domain.isEmpty()) {
			Iterator<DomainConstraint> it = domain.iterator();
			ArrayList<MetaAttribute> metaAttributes = new ArrayList<MetaAttribute>();
			while (it.hasNext()) {
				DomainConstraint constraint = it.next();
				Constraint iomConstraint = visitConstraint(constraint, metaAttributes, domain.getScopedName(null));
				iomConstraint.setToDomain(getRef(domain));
				out.write(new ObjectEvent(iomConstraint));
			}
			for(MetaAttribute val:metaAttributes){
				out.write(new ObjectEvent(val));
			}
		}
	}
	private void visitAttribute(ch.interlis.ili2c.metamodel.LocalAttribute attr,int attrPos)
	throws IoxException
	{
		try{
			AttrOrParam iomAttr = new AttrOrParam( getAttrTid(attr) );
			iomAttr.setAttrParent(getRef(attr.getContainer()),attrPos);
			iomAttr.setName( attr.getName() );
			
			if ( attr.getDocumentation() != null ) {
				DocText doc = new DocText();
				doc.setText( attr.getDocumentation() );
				iomAttr.addDocumentation( doc );
			}
			
			ch.interlis.ili2c.metamodel.AttributeDef extending=(ch.interlis.ili2c.metamodel.AttributeDef)attr.getExtending();
			if(extending!=null){
				iomAttr.setSuper( getAttrTid(extending) );
			}
			iomAttr.setAbstract( attr.isAbstract() );
			iomAttr.setFinal( attr.isFinal() );
			iomAttr.setGeneric(false);
			
			ch.interlis.ili2c.metamodel.Evaluable[] derv=((ch.interlis.ili2c.metamodel.LocalAttribute)attr).getBasePaths();
			if(derv!=null){
				for(int i=0;i<derv.length;i++){
					iomAttr.addDerivates( visitExpression(derv[i]) );
				}
			}
			
			if ( attr.isSubdivision() ) {
				if ( attr.isContinuous() ) {
					iomAttr.setSubdivisionKind( AttrOrParam_SubdivisionKind.ContSubDiv );
				} else {
					iomAttr.setSubdivisionKind( AttrOrParam_SubdivisionKind.SubDiv );
				}
			} else {
				iomAttr.setSubdivisionKind( AttrOrParam_SubdivisionKind.NoSubDiv );
			}

			iomAttr.setTransient( attr.isTransient() );
			
			ch.interlis.ili2c.metamodel.Type type=attr.getDomain();
			if(type==null){
				// only valid if container is a view
				if(attr.getContainer() instanceof ch.interlis.ili2c.metamodel.View){
					ch.interlis.ili2c.metamodel.Evaluable basev[]=attr.getBasePaths();
					if(basev.length==1 && basev[0] instanceof ch.interlis.ili2c.metamodel.ObjectPath){
						ch.interlis.ili2c.metamodel.ObjectPath base=(ch.interlis.ili2c.metamodel.ObjectPath)basev[0];
						ch.interlis.ili2c.metamodel.PathEl pathel[]=base.getPathElements();
						ch.interlis.ili2c.metamodel.PathEl last=pathel[pathel.length-1];
						if(last instanceof ch.interlis.ili2c.metamodel.AttributeRef){
							ch.interlis.ili2c.metamodel.AttributeDef baseAttr=((ch.interlis.ili2c.metamodel.AttributeRef)last).getAttr();
							iomAttr.setType(getAttrTypeTid(baseAttr));
						}else if(last instanceof ch.interlis.ili2c.metamodel.StructAttributeRef){
							ch.interlis.ili2c.metamodel.AttributeDef baseAttr=((ch.interlis.ili2c.metamodel.StructAttributeRef)last).getAttr();
							iomAttr.setType(getAttrTypeTid(baseAttr));
						}else{
							throw new IllegalArgumentException(base+" not an attribute");
						}
					}else{
						throw new IllegalStateException("attribute requires one ObjectOrAttributePath");
					}
				}else{
					throw new IllegalStateException("attribute requires a type");
				}
			}else if(type instanceof ch.interlis.ili2c.metamodel.TypeAlias){
				ch.interlis.ili2c.metamodel.TypeAlias alias=(ch.interlis.ili2c.metamodel.TypeAlias)type;
				ch.interlis.ili2c.metamodel.Domain domain=alias.getAliasing();
				if(domain==td.INTERLIS.URI || domain==td.INTERLIS.NAME  || domain==td.INTERLIS.BOOLEAN){
				    OutParam<String> typeTid=new OutParam<String>();
					visitAttrLocalType(typeTid,type,attr,null); 
					iomAttr.setType(typeTid.value);
				}else{
					if(alias.getCardinality().getMaximum()>1){						
						MultiValue iomMultiValue=new MultiValue(attr.getContainer().getScopedName(null)+"."+attr.getName()+"."+MVT_TYPE_NAME);
						iomMultiValue.setName(MVT_TYPE_NAME);
						iomMultiValue.setLTParent(getAttrTid(attr));
						iomMultiValue.setOrdered(type.isOrdered());
						ch.interlis.ili2c.metamodel.Cardinality card=type.getCardinality();
						Multiplicity iomMultiplicity = visitCardinality(card);
						iomMultiValue.setMultiplicity(iomMultiplicity);
						iomMultiValue.setBaseType(getRef(domain));
						out.write(new ObjectEvent(iomMultiValue));
						iomAttr.setType(iomMultiValue.getobjectoid());
					}else if(alias.isMandatory()){
                        OutParam<String> typeTid=new OutParam<String>();
                        visitAttrLocalType(typeTid,domain.getType(),attr,domain.getScopedName(null)); 
                        iomAttr.setType(getTypeTid(type,attr,null));
					}else{
						iomAttr.setType(getRef(domain));
					}
				}
			}else{
				OutParam<String> typeTid=new OutParam<String>();
				visitAttrLocalType(typeTid,type,attr,null);
				iomAttr.setType(typeTid.value);
			}
			out.write(new ObjectEvent(iomAttr));
			visitMetaValues(getMetaValues(attr),iomAttr.getobjectoid());
		}catch(Exception ex){
			EhiLogger.logError(attr.getScopedName(),ex);
		}
	}
	private void visitParameter(ch.interlis.ili2c.metamodel.Parameter param,int orderPos)
	throws IoxException
	{
		try{
			AttrOrParam iomParam = new AttrOrParam( param.getScopedName() );
			iomParam.setParamParent(getRef(param.getContainer()),orderPos);
			iomParam.setName( param.getName() );
			
			if ( param.getDocumentation() != null ) {
				DocText doc = new DocText();
				doc.setText( param.getDocumentation() );
				iomParam.addDocumentation( doc );
			}
			
			ch.interlis.ili2c.metamodel.Parameter extending=(ch.interlis.ili2c.metamodel.Parameter)param.getExtending();
			if(extending!=null){
				iomParam.setSuper( getRef(extending) );
			}
			iomParam.setAbstract( param.isAbstract() );
			iomParam.setFinal( param.isFinal() );
			iomParam.setGeneric(false);
			
			ch.interlis.ili2c.metamodel.Type type=param.getType();
			if(type instanceof ch.interlis.ili2c.metamodel.TypeAlias){
				ch.interlis.ili2c.metamodel.TypeAlias alias=(ch.interlis.ili2c.metamodel.TypeAlias)type;
				ch.interlis.ili2c.metamodel.Domain domain=alias.getAliasing();
				if(domain==td.INTERLIS.URI || domain==td.INTERLIS.NAME || domain==td.INTERLIS.BOOLEAN){
				    OutParam<String> typeTid=new OutParam<String>();
					visitParameterLocalType(typeTid,type,param);
					iomParam.setType(typeTid.value);
				}else{
					if(alias.isMandatory()){
					    OutParam<String> typeTid=new OutParam<String>();
						visitParameterLocalType(typeTid,domain.getType(),param);
						iomParam.setType(typeTid.value);
					}else{
						iomParam.setType(getRef(domain));
					}
				}
			}else{
			    OutParam<String> typeTid=new OutParam<String>();
				visitParameterLocalType(typeTid,type,param);
				iomParam.setType(typeTid.value);
			}
			out.write(new ObjectEvent(iomParam));
			visitMetaValues(getMetaValues(param),iomParam.getobjectoid());
		}catch(Exception ex){
			EhiLogger.logError(param.getContainer().getScopedName(null)+"."+param.getName(),ex);
		}
	}
	private void visitRoleDef(ch.interlis.ili2c.metamodel.RoleDef role,int orderPos)
	throws IoxException
	{
		Role iomRole = new Role( getRoleTid(role) );
		iomRole.setAssociation(getRef(role.getContainer()),orderPos);
		iomRole.setName( role.getName() );
		
		if ( role.getDocumentation() != null ) {
			DocText doc = new DocText();
			doc.setText( role.getDocumentation() );
			iomRole.addDocumentation( doc );
		}
		
		ch.interlis.ili2c.metamodel.RoleDef extending=(ch.interlis.ili2c.metamodel.RoleDef)role.getExtending();
		if(extending!=null){
			iomRole.setSuper( getRoleTid(extending) );
		}
		iomRole.setAbstract( role.isAbstract() );
		iomRole.setFinal( role.isFinal() );
		iomRole.setGeneric(false);
		
		// mandatory has no meaning for a Role, but set it to keep instance compliant with model
		iomRole.setMandatory(true);
		
		Iterator refi=role.iteratorReference();
		while(refi.hasNext()){
			ch.interlis.ili2c.metamodel.ReferenceType ref=(ch.interlis.ili2c.metamodel.ReferenceType)refi.next();
			ch.interlis.ili2c.metamodel.AbstractClassDef target=ref.getReferred();

			BaseClass iomBaseClass=new BaseClass();
			iomBaseClass.setCRT(iomRole.getobjectoid());
			iomBaseClass.setBaseClass(getRef(target));
			out.write(new ObjectEvent(iomBaseClass));
			
			Iterator targeti=ref.iteratorRestrictedTo();
			while(targeti.hasNext()){
				target=(ch.interlis.ili2c.metamodel.AbstractClassDef)targeti.next();
				ClassRestriction iomRestriction=new ClassRestriction();
				iomRestriction.setClassRestriction(getRef(target));
				iomRestriction.setCRTR(iomRole.getobjectoid());
				out.write(new ObjectEvent(iomRestriction));
			}
			
		}
		
		iomRole.setExternal(role.isExternal());
		int kind=role.getKind();
		if(kind==ch.interlis.ili2c.metamodel.RoleDef.Kind.eAGGREGATE){
			iomRole.setStrongness(Role_Strongness.Aggr);
		}else if(kind==ch.interlis.ili2c.metamodel.RoleDef.Kind.eCOMPOSITE){
			iomRole.setStrongness(Role_Strongness.Comp);
		}else{
			iomRole.setStrongness(Role_Strongness.Assoc);
		}
		iomRole.setOrdered(role.isOrdered());
		ch.interlis.ili2c.metamodel.Cardinality card=role.getCardinality();
		Multiplicity iomMultiplicity = visitCardinality(card);
		iomRole.setMultiplicity(iomMultiplicity);
		if(((ch.interlis.ili2c.metamodel.AssociationDef)role.getContainer()).isLightweight()){
			iomRole.setEmbeddedTransfer(role.getOppEnd().isAssociationEmbedded());
		}else{
			iomRole.setEmbeddedTransfer(false);
		}
		
		if(role.getDerivedFrom()!=null){
			iomRole.addDerivates( visitExpression(role.getDerivedFrom()) );
		}

		out.write(new ObjectEvent(iomRole));
		visitMetaValues(getMetaValues(role),iomRole.getobjectoid());
	}

	private Multiplicity visitCardinality(ch.interlis.ili2c.metamodel.Cardinality card) {
		Multiplicity iomMultiplicity=new Multiplicity();
		iomMultiplicity.setMin((int)card.getMinimum());
		if(card.getMaximum()!=ch.interlis.ili2c.metamodel.Cardinality.UNBOUND){
			iomMultiplicity.setMax((int)card.getMaximum());
		}
		return iomMultiplicity;
	}

	private void visitLineForm(ch.interlis.ili2c.metamodel.LineForm lf)
	throws IoxException
	{
		LineForm iomLf = new LineForm(lf.getScopedName(null) );
		iomLf.setElementInPackage( getRef(lf.getContainer()) );

		iomLf.setName( lf.getName() );
		
		if ( lf.getDocumentation() != null ) {
			DocText doc = new DocText();
			doc.setText( lf.getDocumentation() );
			iomLf.addDocumentation( doc );
		}
		if(lf==td.INTERLIS.STRAIGHTS){
			iomLf.setStructure(getRef(td.INTERLIS.STRAIGHT_SEGMENT));
		}else if(lf==td.INTERLIS.ARCS){
			iomLf.setStructure(getRef(td.INTERLIS.ARC_SEGMENT));
		}else{
			iomLf.setStructure(getRef(lf.getSegmentStructure()));
		}
		out.write(new ObjectEvent(iomLf));
		visitMetaValues(getMetaValues(lf),iomLf.getobjectoid());
	
	}
	private Constraint visitConstraint(ch.interlis.ili2c.metamodel.Constraint cnstrt,ArrayList<MetaAttribute> metaAttrs,String viewableId)
	throws IoxException
	{
		String cnstrdName=cnstrt.getName();
		String cnstrdTid= null;
		cnstrdTid= viewableId+"."+cnstrdName;
		Constraint iomCnstrt=null;
		if(cnstrt instanceof ch.interlis.ili2c.metamodel.ExistenceConstraint){
			ch.interlis.ili2c.metamodel.ExistenceConstraint existenceConstraint = (ch.interlis.ili2c.metamodel.ExistenceConstraint)cnstrt;
			ExistenceConstraint iomConstraint = new ExistenceConstraint(cnstrdTid);
			PathOrInspFactor iomRestrictedAttr=visitObjectPath(existenceConstraint.getRestrictedAttribute());
			iomConstraint.setAttr(iomRestrictedAttr);

			Iterator<ObjectPath> requiredInIt = existenceConstraint.iteratorRequiredIn();
			while (requiredInIt.hasNext()) {
				ObjectPath objPath = requiredInIt.next();
				ExistenceDef iomExistsIn = new ExistenceDef();
				iomExistsIn.setExistsIn(getRef(objPath.getRoot()));
				iomExistsIn.setAttr(visitObjectPath(objPath));
				iomConstraint.addRequiredIn(iomExistsIn);
			}
			iomCnstrt=iomConstraint;
		}else if(cnstrt instanceof ch.interlis.ili2c.metamodel.MandatoryConstraint
				|| cnstrt instanceof ch.interlis.ili2c.metamodel.DomainConstraint){
			SimpleConstraint iomConstraint = new SimpleConstraint(cnstrdTid);
			iomCnstrt=iomConstraint;
			
			ch.interlis.ili2c.metamodel.Evaluable condition = cnstrt.getCondition();
			Expression expr = visitExpression(condition);
			iomConstraint.setLogicalExpression( expr );
			
			iomConstraint.setKind( SimpleConstraint_Kind.MandC );
		}else if(cnstrt instanceof ch.interlis.ili2c.metamodel.PlausibilityConstraint){
			ch.interlis.ili2c.metamodel.PlausibilityConstraint plausibilityConstraint = (ch.interlis.ili2c.metamodel.PlausibilityConstraint)cnstrt;
			SimpleConstraint iomConstraint = new SimpleConstraint(cnstrdTid);
			iomCnstrt=iomConstraint;
			
			ch.interlis.ili2c.metamodel.Evaluable condition = plausibilityConstraint.getCondition();
			Expression expr = visitExpression(condition);
			iomConstraint.setLogicalExpression( expr );
						
			int direction = plausibilityConstraint.getDirection();
			if ( direction == ch.interlis.ili2c.metamodel.PlausibilityConstraint.DIRECTION_AT_LEAST ) {
				iomConstraint.setKind( SimpleConstraint_Kind.LowPercC );
			} else {
				iomConstraint.setKind( SimpleConstraint_Kind.HighPercC );
			}

			double percentage = plausibilityConstraint.getPercentage();
			iomConstraint.setPercentage( percentage );
		}else if(cnstrt instanceof ch.interlis.ili2c.metamodel.UniquenessConstraint){
			UniqueConstraint iomUniqueConstraint = new UniqueConstraint(cnstrdTid);
			iomCnstrt=iomUniqueConstraint;
			ch.interlis.ili2c.metamodel.UniquenessConstraint uc=(ch.interlis.ili2c.metamodel.UniquenessConstraint)cnstrt;
			if ( uc.getLocal() ) {
				iomUniqueConstraint.setKind( UniqueConstraint_Kind.LocalU );
				Iterator attri=uc.getElements().iteratorAttribute();
				while(attri.hasNext()){
			    	  ch.interlis.ili2c.metamodel.ObjectPath attr=(ch.interlis.ili2c.metamodel.ObjectPath)attri.next();
			    	  PathOrInspFactor iomExpr=new PathOrInspFactor();
	                  visitObjectPathEls(iomExpr,uc.getPrefix());
			    	  visitObjectPathEls(iomExpr,attr);
                      iomUniqueConstraint.addUniqueDef(iomExpr);
				}
			} else {
				if (uc.perBasket()) {
					iomUniqueConstraint.setKind(UniqueConstraint_Kind.BasketU);
				} else {
					iomUniqueConstraint.setKind(UniqueConstraint_Kind.GlobalU);
				}
				Iterator attri=uc.getElements().iteratorAttribute();
				while(attri.hasNext()){
			    	  ch.interlis.ili2c.metamodel.ObjectPath attr=(ch.interlis.ili2c.metamodel.ObjectPath)attri.next();
			    	  PathOrInspFactor iomExpr=new PathOrInspFactor();
			    	  iomUniqueConstraint.addUniqueDef(visitObjectPathEls(iomExpr,attr));
				}
			}
			if(uc.getPreCondition()!=null){
				iomUniqueConstraint.setWhere( visitExpression(uc.getPreCondition()) );
				
			}
		}else if(cnstrt instanceof ch.interlis.ili2c.metamodel.SetConstraint){
			ch.interlis.ili2c.metamodel.SetConstraint sc=(ch.interlis.ili2c.metamodel.SetConstraint)cnstrt;
			SetConstraint iomConstraint = new SetConstraint(cnstrdTid);
			iomCnstrt=iomConstraint;
			iomConstraint.setConstraint( visitExpression(sc.getCondition()) );
			if(sc.getPreCondition()!=null){
				iomConstraint.setWhere( visitExpression(sc.getPreCondition()) );
			}
			iomConstraint.setPerBasket(sc.perBasket());
		}else{	
			throw new IllegalArgumentException("unexpected constraint type "+cnstrt.getClass().getName());
		}
		metaAttrs.addAll(createMetaAttributes(getMetaValues(cnstrt), cnstrdTid,null));
		iomCnstrt.setName(cnstrdName);
		if ( cnstrt.getDocumentation() != null ) {
			DocText doc = new DocText();
			doc.setText( cnstrt.getDocumentation() );
			iomCnstrt.addDocumentation( doc );
		}
		return iomCnstrt;
	}
	  private void printAttributePath (StringBuffer out,ch.interlis.ili2c.metamodel.Container scope, ch.interlis.ili2c.metamodel.ObjectPath path)
	  {
	    if (path == null)
	    {
	      throw new IllegalArgumentException("path==null");
	    }
	    ch.interlis.ili2c.metamodel.PathEl[] elv=path.getPathElements();
	    String sep="";
	    for(int i=0;i<elv.length;i++){
		out.append(sep);sep="->";
		out.append(elv[i].getName());
	    }
	  }
	private Expression visitExpression(ch.interlis.ili2c.metamodel.Evaluable e)
	throws IoxException
	{
		Expression ret=null;
		if(e instanceof ch.interlis.ili2c.metamodel.Constant.Undefined){
		    Constant iomConst=new Constant();
		    ret=iomConst;
		    iomConst.setType(Constant_Type.Undefined);
		    iomConst.setValue("UNDEFINED");
		}else if(e instanceof ch.interlis.ili2c.metamodel.Constant.Numeric){
			ch.interlis.ili2c.metamodel.Constant.Numeric cnum = (ch.interlis.ili2c.metamodel.Constant.Numeric) e;
		    Constant iomConst=new Constant();
		    ret=iomConst;
		    iomConst.setType(Constant_Type.Numeric);
		    iomConst.setValue(cnum.getValue().toString());
		}else if(e instanceof ch.interlis.ili2c.metamodel.Constant.Text){
			ch.interlis.ili2c.metamodel.Constant.Text cnum = (ch.interlis.ili2c.metamodel.Constant.Text) e;
		    Constant iomConst=new Constant();
		    ret=iomConst;
		    iomConst.setType(Constant_Type.Text);
		    iomConst.setValue(cnum.getValue());
		}else if(e instanceof ch.interlis.ili2c.metamodel.Constant.Class){
			ch.interlis.ili2c.metamodel.Constant.Class cnum = (ch.interlis.ili2c.metamodel.Constant.Class) e;
		    Constant iomConst=new Constant();
		    ret=iomConst;
		    iomConst.setType(Constant_Type.Text);
		    iomConst.setValue(getRef(cnum.getValue())); // TODO adapt to new IlisMeta
		}else if(e instanceof ch.interlis.ili2c.metamodel.Constant.AttributePath){
			ch.interlis.ili2c.metamodel.Constant.AttributePath cnum = (ch.interlis.ili2c.metamodel.Constant.AttributePath)e;
		    Constant iomConst=new Constant();
		    ret=iomConst;
		    iomConst.setType(Constant_Type.Text);
		    iomConst.setValue(getRef(cnum.getValue())); // TODO adapt to new IlisMeta
		}else if(e instanceof ch.interlis.ili2c.metamodel.Constant.Enumeration){
		      StringBuffer value=new StringBuffer("#");
		      String[] val = ((ch.interlis.ili2c.metamodel.Constant.Enumeration) e).getValue ();
		      if (val == null){
		        throw new IllegalArgumentException("enumConst.val==null");
		      }
		        for (int i = 0; i < val.length; i++)
		        {
		          if (i > 0){
		            value.append('.');
		          }
		          value.append(val[i]);
		        }
			    Constant iomConst=new Constant();
			    ret=iomConst;
			    iomConst.setType(Constant_Type.Enumeration);
			    iomConst.setValue(value.toString());
		}else if(e instanceof ch.interlis.ili2c.metamodel.Constant.Structured){
		      String val = ((ch.interlis.ili2c.metamodel.Constant.Structured) e).toString ();
		      if (val == null){
			        throw new IllegalArgumentException("structNumConst.val==null");
		      }
			    Constant iomConst=new Constant();
			    ret=iomConst;
			    iomConst.setType(Constant_Type.Text);
			    iomConst.setValue(val);
		}else if(e instanceof ch.interlis.ili2c.metamodel.ConditionalExpression){
			ch.interlis.ili2c.metamodel.ConditionalExpression condExpr=(ch.interlis.ili2c.metamodel.ConditionalExpression)e;
			EnumMapping iomMapping=new EnumMapping();
			ret=iomMapping;
			iomMapping.setEnumValue(visitObjectPath(condExpr.getAttribute()));
			ch.interlis.ili2c.metamodel.ConditionalExpression.Condition condv[]=condExpr.getConditions();
			for(int condi=0;condi<condv.length;condi++){
				ch.interlis.ili2c.metamodel.ConditionalExpression.Condition cond=condv[condi];
				
				EnumAssignment iomAsgmt=new EnumAssignment();
				iomAsgmt.setValueToAssign(visitExpression(cond.getValue()));
				ch.interlis.ili2c.metamodel.Constant.EnumConstOrRange range=cond.getCondition();
				if(range instanceof ch.interlis.ili2c.metamodel.Constant.EnumerationRange){
					// TODO iomAsgmt.setMinEnumValue(node-oid); in ili2c add enum-type to Constant.EnumConstOrRange
					// todo iomAsgmt.setMaxEnumValue(node-oid);
				}else if(range instanceof ch.interlis.ili2c.metamodel.Constant.Enumeration){
					// todo iomAsgmt.setMinEnumValue(node-oid);
				}else{
					throw new IllegalArgumentException( "Unknown subclass: " + range.getClass() );
				}
				iomMapping.addCases(iomAsgmt);
			}
		}else if(e instanceof ch.interlis.ili2c.metamodel.Constant.ReferenceToMetaObject){
			ch.interlis.ili2c.metamodel.Constant.ReferenceToMetaObject refMetaObj=(ch.interlis.ili2c.metamodel.Constant.ReferenceToMetaObject)e;
			PathOrInspFactor iomExpr=new PathOrInspFactor();
			ret=iomExpr;
			PathEl iomPathEl=new PathEl();
			iomPathEl.setKind(PathEl_Kind.MetaObject);
			ch.interlis.ili2c.metamodel.MetaObject metaObj=refMetaObj.getReferred();
			iomPathEl.setRef(getRef(metaObj));
			iomExpr.addPathEls(iomPathEl);
		} else if (e instanceof ch.interlis.ili2c.metamodel.Expression.Implication) {
			CompoundExpr iomExpr = new CompoundExpr();
			ret = iomExpr;
			iomExpr.setOperation(CompoundExpr_Operation.Implication);
			ch.interlis.ili2c.metamodel.Expression.Implication implication = (ch.interlis.ili2c.metamodel.Expression.Implication) e;
			iomExpr.addSubExpressions(visitExpression(implication.getLeft()));
			iomExpr.addSubExpressions(visitExpression(implication.getRight()));
		} else if (e instanceof ch.interlis.ili2c.metamodel.Expression.Multiplication) {
			CompoundExpr iomExpr = new CompoundExpr();
			ret = iomExpr;
			iomExpr.setOperation(CompoundExpr_Operation.Mult);
			ch.interlis.ili2c.metamodel.Expression.Multiplication multiplication = (ch.interlis.ili2c.metamodel.Expression.Multiplication) e;
			iomExpr.addSubExpressions(visitExpression(multiplication.getLeft()));
			iomExpr.addSubExpressions(visitExpression(multiplication.getRight()));
		} else if (e instanceof ch.interlis.ili2c.metamodel.Expression.Division) {
			CompoundExpr iomExpr = new CompoundExpr();
			ret = iomExpr;
			iomExpr.setOperation(CompoundExpr_Operation.Div);
			ch.interlis.ili2c.metamodel.Expression.Division division = (ch.interlis.ili2c.metamodel.Expression.Division) e;
			iomExpr.addSubExpressions(visitExpression(division.getLeft()));
			iomExpr.addSubExpressions(visitExpression(division.getRight()));
		} else if (e instanceof ch.interlis.ili2c.metamodel.Expression.Addition) {
			CompoundExpr iomExpr = new CompoundExpr();
			ret = iomExpr;
			iomExpr.setOperation(CompoundExpr_Operation.Add);
			ch.interlis.ili2c.metamodel.Expression.Addition addition = (ch.interlis.ili2c.metamodel.Expression.Addition) e;
			iomExpr.addSubExpressions(visitExpression(addition.getLeft()));
			iomExpr.addSubExpressions(visitExpression(addition.getRight()));
		} else if (e instanceof ch.interlis.ili2c.metamodel.Expression.Subtraction) {
			CompoundExpr iomExpr = new CompoundExpr();
			ret = iomExpr;
			iomExpr.setOperation(CompoundExpr_Operation.Sub);
			ch.interlis.ili2c.metamodel.Expression.Subtraction subtraction = (ch.interlis.ili2c.metamodel.Expression.Subtraction) e;
			iomExpr.addSubExpressions(visitExpression(subtraction.getLeft()));
			iomExpr.addSubExpressions(visitExpression(subtraction.getRight()));
		}else if(e instanceof ch.interlis.ili2c.metamodel.Expression.Conjunction){
		    CompoundExpr iomExpr=new CompoundExpr();
		    ret=iomExpr;
		    iomExpr.setOperation(CompoundExpr_Operation.And);
		    ch.interlis.ili2c.metamodel.Evaluable[] conjoined = ((ch.interlis.ili2c.metamodel.Expression.Conjunction) e).getConjoined ();
		    for (int i = 0; i < conjoined.length; i++)
		    {
		    	iomExpr.addSubExpressions(visitExpression (conjoined[i]));
		    }
		}else if(e instanceof ch.interlis.ili2c.metamodel.Expression.Disjunction){
		    CompoundExpr iomExpr=new CompoundExpr();
		    ret=iomExpr;
		    iomExpr.setOperation(CompoundExpr_Operation.Or);
		    ch.interlis.ili2c.metamodel.Evaluable[] disjoined = ((ch.interlis.ili2c.metamodel.Expression.Disjunction) e).getDisjoined();
		    for (int i = 0; i < disjoined.length; i++)
		    {
		    	iomExpr.addSubExpressions(visitExpression (disjoined[i]));
		    }
		}else if(e instanceof ch.interlis.ili2c.metamodel.Expression.Equality){
		    CompoundExpr iomExpr=new CompoundExpr();
		    ret=iomExpr;
		    iomExpr.setOperation(CompoundExpr_Operation.Relation_Equal);
		    ch.interlis.ili2c.metamodel.Evaluable left = ((ch.interlis.ili2c.metamodel.Expression.Equality) e).getLeft();
	    	iomExpr.addSubExpressions(visitExpression (left));
		    ch.interlis.ili2c.metamodel.Evaluable right = ((ch.interlis.ili2c.metamodel.Expression.Equality) e).getRight();
	    	iomExpr.addSubExpressions(visitExpression (right));
		}else if(e instanceof ch.interlis.ili2c.metamodel.Expression.GreaterThan){
		    CompoundExpr iomExpr=new CompoundExpr();
		    ret=iomExpr;
		    iomExpr.setOperation(CompoundExpr_Operation.Relation_Greater);
		    ch.interlis.ili2c.metamodel.Evaluable left = ((ch.interlis.ili2c.metamodel.Expression.GreaterThan) e).getLeft();
	    	iomExpr.addSubExpressions(visitExpression (left));
		    ch.interlis.ili2c.metamodel.Evaluable right = ((ch.interlis.ili2c.metamodel.Expression.GreaterThan) e).getRight();
	    	iomExpr.addSubExpressions(visitExpression (right));
		}else if(e instanceof ch.interlis.ili2c.metamodel.Expression.GreaterThanOrEqual){
		    CompoundExpr iomExpr=new CompoundExpr();
		    ret=iomExpr;
		    iomExpr.setOperation(CompoundExpr_Operation.Relation_GreaterOrEqual);
		    ch.interlis.ili2c.metamodel.Evaluable left = ((ch.interlis.ili2c.metamodel.Expression.GreaterThanOrEqual) e).getLeft();
	    	iomExpr.addSubExpressions(visitExpression (left));
		    ch.interlis.ili2c.metamodel.Evaluable right = ((ch.interlis.ili2c.metamodel.Expression.GreaterThanOrEqual) e).getRight();
	    	iomExpr.addSubExpressions(visitExpression (right));
		}else if(e instanceof ch.interlis.ili2c.metamodel.Expression.Inequality){
		    CompoundExpr iomExpr=new CompoundExpr();
		    ret=iomExpr;
		    iomExpr.setOperation(CompoundExpr_Operation.Relation_NotEqual);
		    ch.interlis.ili2c.metamodel.Evaluable left = ((ch.interlis.ili2c.metamodel.Expression.Inequality) e).getLeft();
	    	iomExpr.addSubExpressions(visitExpression (left));
		    ch.interlis.ili2c.metamodel.Evaluable right = ((ch.interlis.ili2c.metamodel.Expression.Inequality) e).getRight();
	    	iomExpr.addSubExpressions(visitExpression (right));
		}else if(e instanceof ch.interlis.ili2c.metamodel.Expression.LessThan){
		    CompoundExpr iomExpr=new CompoundExpr();
		    ret=iomExpr;
		    iomExpr.setOperation(CompoundExpr_Operation.Relation_Less);
		    ch.interlis.ili2c.metamodel.Evaluable left = ((ch.interlis.ili2c.metamodel.Expression.LessThan) e).getLeft();
	    	iomExpr.addSubExpressions(visitExpression (left));
		    ch.interlis.ili2c.metamodel.Evaluable right = ((ch.interlis.ili2c.metamodel.Expression.LessThan) e).getRight();
	    	iomExpr.addSubExpressions(visitExpression (right));
		}else if(e instanceof ch.interlis.ili2c.metamodel.Expression.LessThanOrEqual){
		    CompoundExpr iomExpr=new CompoundExpr();
		    ret=iomExpr;
		    iomExpr.setOperation(CompoundExpr_Operation.Relation_LessOrEqual);
		    ch.interlis.ili2c.metamodel.Evaluable left = ((ch.interlis.ili2c.metamodel.Expression.LessThanOrEqual) e).getLeft();
	    	iomExpr.addSubExpressions(visitExpression (left));
		    ch.interlis.ili2c.metamodel.Evaluable right = ((ch.interlis.ili2c.metamodel.Expression.LessThanOrEqual) e).getRight();
	    	iomExpr.addSubExpressions(visitExpression (right));
		}else if(e instanceof ch.interlis.ili2c.metamodel.Expression.Negation){
		    UnaryExpr iomExpr=new UnaryExpr();
		    ret=iomExpr;
		    iomExpr.setOperation(UnaryExpr_Operation.Not);
		    ch.interlis.ili2c.metamodel.Evaluable left = ((ch.interlis.ili2c.metamodel.Expression.Negation) e).getNegated();
	    	iomExpr.setSubExpression(visitExpression (left));
        }else if(e instanceof ch.interlis.ili2c.metamodel.Expression.Subexpression){
            UnaryExpr iomExpr=new UnaryExpr();
            ret=iomExpr;
            //iomExpr.setOperation(UnaryExpr_Operation.Nested);
            iomExpr.setattrvalue(UnaryExpr.tag_Operation,"Nested");
            ch.interlis.ili2c.metamodel.Evaluable left = ((ch.interlis.ili2c.metamodel.Expression.Subexpression) e).getSubexpression();
            iomExpr.setSubExpression(visitExpression (left));
		}else if(e instanceof ch.interlis.ili2c.metamodel.Expression.DefinedCheck){
		    UnaryExpr iomExpr=new UnaryExpr();
		    ret=iomExpr;
		    iomExpr.setOperation(UnaryExpr_Operation.Defined);
		    ch.interlis.ili2c.metamodel.Evaluable left = ((ch.interlis.ili2c.metamodel.Expression.DefinedCheck) e).getArgument();
	    	iomExpr.setSubExpression(visitExpression (left));
		}else if(e instanceof ch.interlis.ili2c.metamodel.FunctionCall){
			ch.interlis.ili2c.metamodel.FunctionCall f=(ch.interlis.ili2c.metamodel.FunctionCall)e;
		    FunctionCall iomFunc=new FunctionCall();
		    ret=iomFunc;
		    iomFunc.setFunction(getRef(f.getFunction()));
		    ch.interlis.ili2c.metamodel.Evaluable[] args = f.getArguments();
			ch.interlis.ili2c.metamodel.FormalArgument formalArgs[]=f.getFunction().getArguments();
		    for (int i = 0; i < args.length; i++)
		    {
				ch.interlis.ili2c.metamodel.FormalArgument formalArg=formalArgs[i];
		    	ActualArgument iomArg=new ActualArgument();
		    	iomArg.setFormalArgument(getRef(formalArg));
		    	if(args[i] instanceof ch.interlis.ili2c.metamodel.Objects){
			    	// ALL [ '(' RestrictedClassOrAssRef | ViewableRef ')' ]
		    		ch.interlis.ili2c.metamodel.Objects objs=(ch.interlis.ili2c.metamodel.Objects)args[i];
		    		iomArg.setKind(ActualArgument_Kind.AllOf);
		    		if(objs.getBase()==null){
		    			ClassRef iomRef=new ClassRef();
		    			iomRef.setRef(getRef(objs.getContext()));
				    	iomArg.addObjectClasses(iomRef);
				    }else{
		    			if(objs.iteratorRestrictedTo().hasNext()){
		    				Iterator refi=objs.iteratorRestrictedTo();
		    				while(refi.hasNext()){
		    					ch.interlis.ili2c.metamodel.Viewable ref=(ch.interlis.ili2c.metamodel.Viewable)refi.next();
				    			ClassRef iomRef=new ClassRef();
				    			iomRef.setRef(getRef(ref));
						    	iomArg.addObjectClasses(iomRef);		    			
		    				}
		    			}else{
			    			ClassRef iomRef=new ClassRef();
			    			iomRef.setRef(getRef(objs.getBase()));
					    	iomArg.addObjectClasses(iomRef);		    			
		    			}
		    		}
		    	}else{
			    	iomArg.setKind(ActualArgument_Kind.Expression);
			    	iomArg.setExpression(visitExpression (args[i]));
		    	}
		    	iomFunc.addArguments(iomArg);
		    }
			
		}else if(e instanceof ch.interlis.ili2c.metamodel.LengthOfReferencedText){
			// ignore it
		}else if(e instanceof ch.interlis.ili2c.metamodel.ParameterValue){
		    RuntimeParamRef iomExpr=new RuntimeParamRef();
		    ret=iomExpr;
		    ch.interlis.ili2c.metamodel.GraphicParameterDef param=((ch.interlis.ili2c.metamodel.ParameterValue) e).getParameter ();
		    iomExpr.setRuntimeParam(getRef(param));
		}else if(e instanceof ch.interlis.ili2c.metamodel.ObjectPath){
			ret = visitObjectPath((ch.interlis.ili2c.metamodel.ObjectPath)e);
			
		}else if(e instanceof ch.interlis.ili2c.metamodel.InspectionFactor){
			ch.interlis.ili2c.metamodel.InspectionFactor inspFactor=(ch.interlis.ili2c.metamodel.InspectionFactor)e;
			PathOrInspFactor iomExpr=new PathOrInspFactor();
		    ret = iomExpr;
		    if(inspFactor.getInspectionViewable()!=null){
		    	iomExpr.setInspection(getRef(inspFactor.getInspectionViewable()));
		    }else{
				// TODO ObjectPath INSPECTION OF
		    	//inspFactor.getDecomposedAttribute();
		    	//inspFactor.isAreaInspection();
		    	//inspFactor.getRenamedViewable();
		    }
		    if(inspFactor.getRestriction()!=null){
			    visitObjectPathEls(iomExpr,inspFactor.getRestriction());
		    }
			
			
		// }else if(e instanceof ch.interlis.ili2c.metamodel.ViewableAlias){ 
		    // a VieableAlias should not appear as part of an expression
			
		} else if (e instanceof ValueRefThis) {
			PathOrInspFactor iomExpr = new PathOrInspFactor();
			ret = iomExpr;

			PathEl iomPathEl = new PathEl();
			iomPathEl.setKind(PathEl_Kind.This);
			iomExpr.addPathEls(iomPathEl);
		}else{
			throw new IllegalArgumentException( "Unknown subclass: " + e.getClass() );
		}
		return ret;
	}

	private PathOrInspFactor visitObjectPath(ch.interlis.ili2c.metamodel.ObjectPath e) {
		PathOrInspFactor iomExpr=new PathOrInspFactor();
		return visitObjectPathEls(iomExpr,(ch.interlis.ili2c.metamodel.ObjectPath)e);
	}

	private PathOrInspFactor visitObjectPathEls(PathOrInspFactor iomExpr,ch.interlis.ili2c.metamodel.ObjectPath e) {
		ch.interlis.ili2c.metamodel.PathEl[] elv=e.getPathElements();
		for(int i=0;i<elv.length;i++){
			elv[i].getName();
			ch.interlis.ili2c.metamodel.PathEl el=elv[i];
			if(el instanceof ch.interlis.ili2c.metamodel.PathElThis){
		    	PathEl iomPathEl=new PathEl();
		    	iomPathEl.setKind(PathEl_Kind.This);
		    	iomExpr.addPathEls(iomPathEl);
			}else if(el instanceof ch.interlis.ili2c.metamodel.ThisArea){
		    	PathEl iomPathEl=new PathEl();
		    	iomPathEl.setKind(((ch.interlis.ili2c.metamodel.ThisArea)el).isThat()?PathEl_Kind.ThatArea:PathEl_Kind.ThisArea);
		    	iomExpr.addPathEls(iomPathEl);
			}else if(el instanceof ch.interlis.ili2c.metamodel.PathElParent){
		    	PathEl iomPathEl=new PathEl();
		    	iomPathEl.setKind(PathEl_Kind.Parent);
		    	iomExpr.addPathEls(iomPathEl);
			}else if(el instanceof ch.interlis.ili2c.metamodel.PathElRefAttr){
		    	PathEl iomPathEl=new PathEl();
		    	iomPathEl.setKind(PathEl_Kind.ReferenceAttr);
		    	ch.interlis.ili2c.metamodel.AttributeDef attr=((ch.interlis.ili2c.metamodel.PathElRefAttr)el).getAttr();
		    	iomPathEl.setRef(getAttrTid(attr));
		    	iomExpr.addPathEls(iomPathEl);
			}else if(el instanceof ch.interlis.ili2c.metamodel.AssociationPath){
		    	PathEl iomPathEl=new PathEl();
		    	iomPathEl.setKind(PathEl_Kind.AssocPath);
		    	ch.interlis.ili2c.metamodel.RoleDef targetRole=((ch.interlis.ili2c.metamodel.AssociationPath)el).getTargetRole();
		    	iomPathEl.setRef(getRoleTid(targetRole));
		    	iomExpr.addPathEls(iomPathEl);
			}else if(el instanceof ch.interlis.ili2c.metamodel.PathElAssocRole){
		    	PathEl iomPathEl=new PathEl();
		    	iomPathEl.setKind(PathEl_Kind.Role);
		    	ch.interlis.ili2c.metamodel.RoleDef targetRole=((ch.interlis.ili2c.metamodel.PathElAssocRole)el).getRole();
		    	iomPathEl.setRef(getRoleTid(targetRole));
		    	iomExpr.addPathEls(iomPathEl);
			}else if(el instanceof ch.interlis.ili2c.metamodel.PathElAbstractClassRole){
		    	PathEl iomPathEl=new PathEl();
		    	iomPathEl.setKind(PathEl_Kind.Role);
		    	ch.interlis.ili2c.metamodel.RoleDef targetRole=((ch.interlis.ili2c.metamodel.PathElAbstractClassRole)el).getRole();
		    	iomPathEl.setRef(getRoleTid(targetRole));
		    	iomExpr.addPathEls(iomPathEl);
			}else if(el instanceof ch.interlis.ili2c.metamodel.PathElBase){
		    	PathEl iomPathEl=new PathEl();
		    	iomPathEl.setKind(PathEl_Kind.ViewBase);
		    	ch.interlis.ili2c.metamodel.PathElBase base=(ch.interlis.ili2c.metamodel.PathElBase)el;
		    	iomPathEl.setRef(getRef(base.getCurrentViewable())+"."+base.getName());
		    	iomExpr.addPathEls(iomPathEl);
		    }else if(el instanceof ch.interlis.ili2c.metamodel.AggregationRef){ // AGGREGATES
		    	PathEl iomPathEl=new PathEl();
		    	iomPathEl.setKind(PathEl_Kind.Attribute);
		    	ch.interlis.ili2c.metamodel.AggregationRef attrRef=(ch.interlis.ili2c.metamodel.AggregationRef)el;
		    	iomPathEl.setRef(getRef(attrRef.getBase())+"."+AGGREGATES);
		    	iomExpr.addPathEls(iomPathEl);
			}else if(el instanceof ch.interlis.ili2c.metamodel.AttributeRef){
		    	PathEl iomPathEl=new PathEl();
		    	iomPathEl.setKind(PathEl_Kind.Attribute);
		    	ch.interlis.ili2c.metamodel.AttributeRef attrRef=(ch.interlis.ili2c.metamodel.AttributeRef)el;
		    	iomPathEl.setRef(getAttrTid(attrRef.getAttr()));
		    	iomExpr.addPathEls(iomPathEl);
			}else if(el instanceof ch.interlis.ili2c.metamodel.StructAttributeRef){
		    	PathEl iomPathEl=new PathEl();
		    	iomPathEl.setKind(PathEl_Kind.Attribute);
		    	ch.interlis.ili2c.metamodel.StructAttributeRef attrRef=(ch.interlis.ili2c.metamodel.StructAttributeRef)el;
		    	iomPathEl.setRef(getAttrTid(attrRef.getAttr()));
		    	if(attrRef.getIndex()==ch.interlis.ili2c.metamodel.StructAttributeRef.eFIRST){
		    		iomPathEl.setSpecIndex(PathEl_SpecIndex.First);
		    	}else if(attrRef.getIndex()==ch.interlis.ili2c.metamodel.StructAttributeRef.eLAST){
		    		iomPathEl.setSpecIndex(PathEl_SpecIndex.Last);
		    	}else{
		    		iomPathEl.setNumIndex((int)attrRef.getIndex());
		    	}
		    	iomExpr.addPathEls(iomPathEl);
			}else if(el instanceof ch.interlis.ili2c.metamodel.AxisAttributeRef){
		    	PathEl iomPathEl=new PathEl();
		    	iomPathEl.setKind(PathEl_Kind.Attribute);
		    	ch.interlis.ili2c.metamodel.AxisAttributeRef attrRef=(ch.interlis.ili2c.metamodel.AxisAttributeRef)el;
		    	iomPathEl.setRef(getAttrTid(attrRef.getAttr()));
				iomPathEl.setNumIndex(attrRef.getAxisNumber());
		    	iomExpr.addPathEls(iomPathEl);
			}else{
				throw new IllegalArgumentException( "Unknown subclass: " + el.getClass() );
			}
		}
		return iomExpr;
	}
	private void visitFunction(ch.interlis.ili2c.metamodel.Function func)
	throws IoxException
	{
		FunctionDef iomFunc = new FunctionDef(func.getScopedName(null) );
		iomFunc.setElementInPackage( getRef(func.getContainer()) );

		iomFunc.setName( func.getName() );
		
		if ( func.getDocumentation() != null ) {
			DocText doc = new DocText();
			doc.setText( func.getDocumentation() );
			iomFunc.addDocumentation( doc );
		}
		
		String explanation=func.getExplanation();
		if(explanation!=null) {
	        iomFunc.setExplanation(explanation);
		}
		
		ch.interlis.ili2c.metamodel.Type retType=func.getDomain();
		OutParam<String> typeTid=new OutParam<String>();
		if(retType instanceof ch.interlis.ili2c.metamodel.TypeAlias){
			ch.interlis.ili2c.metamodel.TypeAlias alias=(ch.interlis.ili2c.metamodel.TypeAlias)retType;
			ch.interlis.ili2c.metamodel.Domain domain=alias.getAliasing();
			if(domain==td.INTERLIS.URI || domain==td.INTERLIS.NAME  || domain==td.INTERLIS.BOOLEAN){
				visitFunctionReturnLocalType(typeTid,retType,func);
			}else{
				if(alias.isMandatory()){
					visitFunctionReturnLocalType(typeTid,domain.getType(),func);
				}else{
					// skip it (written as part of DomainDef)
					typeTid.value=getTypeTid(retType,func,null);
				}
			}
		}else{
			visitFunctionReturnLocalType(typeTid,retType,func);
		}
		iomFunc.setResultType(typeTid.value);
		
		ch.interlis.ili2c.metamodel.FormalArgument args[]=func.getArguments();
		for(int argi=0;argi<args.length;argi++){
			ch.interlis.ili2c.metamodel.FormalArgument arg=args[argi];
			Argument iomArg=new Argument(iomFunc.getobjectoid()+"."+arg.getName());
			iomArg.setKind(Argument_Kind.Type);
			iomArg.setName(arg.getName());
			iomArg.setFunction(iomFunc.getobjectoid(),argi+1);
			ch.interlis.ili2c.metamodel.Type argType=arg.getType();
			if(argType instanceof ch.interlis.ili2c.metamodel.TypeAlias){
			    OutParam<String> argTypeTid=new OutParam<String>();
				ch.interlis.ili2c.metamodel.TypeAlias alias=(ch.interlis.ili2c.metamodel.TypeAlias)argType;
				ch.interlis.ili2c.metamodel.Domain domain=alias.getAliasing();
				if(domain==td.INTERLIS.URI || domain==td.INTERLIS.NAME || domain==td.INTERLIS.BOOLEAN){
					visitFunctionArgumentLocalType(argTypeTid,argType,arg,func); 
				}else{
					if(alias.isMandatory()){
						visitFunctionArgumentLocalType(argTypeTid,domain.getType(),arg,func); 
					}else{
						// skip it (written as part of DomainDef)
						argTypeTid.value=getTypeTid(argType,arg,func);
					}
				}
				iomArg.setType(getTypeTid(arg.getType(),arg,func));
			}else if(argType instanceof ch.interlis.ili2c.metamodel.EnumValType){
				if(((ch.interlis.ili2c.metamodel.EnumValType)argType).isOnlyLeafs()){
					iomArg.setKind(Argument_Kind.EnumVal);
				}else{
					iomArg.setKind(Argument_Kind.EnumTreeVal);
				}
			}else{
				//EhiLogger.debug("arg "+arg.getName());
			    OutParam<String> argTypeTid=new OutParam<String>();
				visitFunctionArgumentLocalType(argTypeTid,argType,arg,func);
				iomArg.setType(argTypeTid.value);
			}
			out.write(new ObjectEvent(iomArg));
		}
		out.write(new ObjectEvent(iomFunc));
		visitMetaValues(getMetaValues(func),iomFunc.getobjectoid());

	}
	private void visitMetaDataUseDef(ch.interlis.ili2c.metamodel.MetaDataUseDef mb)
	throws IoxException
	{
		MetaBasketDef iomMb = new MetaBasketDef(mb.getScopedName(null) );
		iomMb.setElementInPackage( getRef(mb.getContainer()) );

		iomMb.setName( mb.getName() );
		
		if ( mb.getDocumentation() != null ) {
			DocText doc = new DocText();
			doc.setText( mb.getDocumentation() );
			iomMb.addDocumentation( doc );
		}
		if ( mb.getExtending() != null ) {
			iomMb.setSuper( getRef(mb.getExtending()));
		}
		iomMb.setAbstract(mb.isAbstract());
		iomMb.setFinal(mb.isFinal());
		iomMb.setGeneric(false);
		iomMb.setKind(mb.isSignData()?MetaBasketDef_Kind.SignB:MetaBasketDef_Kind.RefSystemB);
		iomMb.setMetaDataTopic(getRef(mb.getTopic())+"."+UNIT_META_NAME);
		out.write(new ObjectEvent(iomMb));
		visitMetaValues(getMetaValues(mb),iomMb.getobjectoid());
		
		// MetaObjectDef
		Iterator moi=mb.iterator();
		while(moi.hasNext()){
			ch.interlis.ili2c.metamodel.MetaObject mo=(ch.interlis.ili2c.metamodel.MetaObject)moi.next();
			MetaObjectDef iomMo=new MetaObjectDef(iomMb.getobjectoid()+"."+mo.getName());
			iomMo.setName(mo.getName());
			if ( mo.getDocumentation() != null ) {
				DocText doc = new DocText();
				doc.setText( mo.getDocumentation() );
				iomMo.addDocumentation( doc );
			}
			
			iomMo.set_class(getRef(mo.getTable()));
			iomMo.setMetaBasketDef(iomMb.getobjectoid());
			iomMo.setIsRefSystem(mo.getTable().isExtending(td.INTERLIS.REFSYSTEM));
			out.write(new ObjectEvent(iomMo));
			visitMetaValues(mo.getMetaValues(),iomMo.getobjectoid());
		}
	}
	private void visitUnit(ch.interlis.ili2c.metamodel.Unit unit)
	throws IoxException
	{
		Unit iomUnit = new Unit(unit.getScopedName(null) );
		iomUnit.setElementInPackage( getRef(unit.getContainer()) );

		iomUnit.setName( unit.getName() );
		
		if ( unit.getDocumentation() != null ) {
			DocText doc = new DocText();
			doc.setText( unit.getDocumentation() );
			iomUnit.addDocumentation( doc );
		}
		
		// ExtendableME 
		iomUnit.setAbstract( unit.isAbstract() );
		if ( unit.getExtending() != null ) {
			iomUnit.setSuper( getRef(unit.getExtending()));
		}
		iomUnit.setFinal( unit.isFinal() );
		iomUnit.setGeneric(false);
		
		if(unit instanceof ch.interlis.ili2c.metamodel.BaseUnit){
			iomUnit.setKind( Unit_Kind.BaseU );
		}else if(unit instanceof ch.interlis.ili2c.metamodel.ComposedUnit){
			ch.interlis.ili2c.metamodel.ComposedUnit composedUnit=(ch.interlis.ili2c.metamodel.ComposedUnit)unit;
			iomUnit.setKind( Unit_Kind.ComposedU );
			
			CompoundExpr completeExpression = null;
			
			
			// 1x CompoundExpr pro gleicher Operator
			// 1. Operator von CompoundExpr ignorieren
			
			ch.interlis.ili2c.metamodel.ComposedUnit.Composed[] units = composedUnit.getComposedUnits();
			char operator = '\0';
			for ( int i = 0; i < units.length; i++ ) {
				char op = units[i].getCompositionOperator();
				if ( op != operator ) {
					CompoundExpr newExpression = new CompoundExpr();
					if ( completeExpression != null ) {
						newExpression.addSubExpressions( completeExpression );
					}
					if(op=='*') {
	                    newExpression.setOperation( CompoundExpr_Operation.Mult );
					}else if (op=='/'){
	                    newExpression.setOperation( CompoundExpr_Operation.Div );
					}else {
	                    throw new IllegalArgumentException( "Unknown ComposedUnit operator: '" + op +"'");
					}
					operator = op;
					completeExpression = newExpression;
				}
				UnitRef unitRef = new UnitRef();
				unitRef.setUnit( getRef(units[i].getUnit()) );
				completeExpression.addSubExpressions( unitRef );
			}
			iomUnit.setDefinition( completeExpression );
		}else if(unit instanceof ch.interlis.ili2c.metamodel.DerivedUnit){
            ch.interlis.ili2c.metamodel.DerivedUnit derivedUnit = (ch.interlis.ili2c.metamodel.DerivedUnit) unit;
            iomUnit.setKind(Unit_Kind.DerivedU);
            if (derivedUnit instanceof ch.interlis.ili2c.metamodel.NumericallyDerivedUnit) {
                ch.interlis.ili2c.metamodel.NumericallyDerivedUnit numericallyDerivedUnit = (ch.interlis.ili2c.metamodel.NumericallyDerivedUnit) derivedUnit;
                ch.interlis.ili2c.metamodel.NumericallyDerivedUnit.Factor[] unitFactors = numericallyDerivedUnit
                        .getConversionFactors();
                CompoundExpr completeExpression = null;
                char operator = '\0';
                for (int i = 0; i < unitFactors.length; i++) {
                    char op = unitFactors[i].getConversionOperator();
                    if (op != operator) {
                        CompoundExpr newExpression = new CompoundExpr();
                        if (completeExpression != null) {
                            newExpression.addSubExpressions(completeExpression);
                        }
                        if (op == '*') {
                            newExpression.setOperation(CompoundExpr_Operation.Mult);
                        } else if (op == '/') {
                            newExpression.setOperation(CompoundExpr_Operation.Div);
                        } else {
                            throw new IllegalArgumentException("Unknown DerivedUnit operator: '" + op + "'");
                        }
                        operator = op;
                        completeExpression = newExpression;
                    }
                    Constant decConst = new Constant();
                    decConst.setType(Constant_Type.Numeric);
                    decConst.setValue(unitFactors[i].getConversionFactor().toString());
                    completeExpression.addSubExpressions(decConst);
                }
                iomUnit.setDefinition(completeExpression);
            } else if (derivedUnit instanceof ch.interlis.ili2c.metamodel.FunctionallyDerivedUnit) {
                ch.interlis.ili2c.metamodel.FunctionallyDerivedUnit functionallyDerivedUnit = (ch.interlis.ili2c.metamodel.FunctionallyDerivedUnit) derivedUnit;
                // Bsp fuer Explanation: " 10**(dB/20) * 0.00002 "
                UnitFunction funcExpr = new UnitFunction();
                funcExpr.setExplanation(functionallyDerivedUnit.getExplanation());
                iomUnit.setDefinition(funcExpr);
            } else {
                throw new IllegalArgumentException("Unknown subclass: " + derivedUnit.getClass());
            }
		}
		
		out.write(new ObjectEvent(iomUnit));
		visitMetaValues(getMetaValues(unit),iomUnit.getobjectoid());
		
	}
	private String getAttrTid(ch.interlis.ili2c.metamodel.AttributeDef attr) {
		return getRef(attr);
	}
	private String getRoleTid(ch.interlis.ili2c.metamodel.RoleDef role) {
		return getRef(role);
	}
	private static final String LOCAL_TYPE_NAME="TYPE";
	private static final String MVT_TYPE_NAME="MVT";
	private String getAttrTypeTid(ch.interlis.ili2c.metamodel.AttributeDef attr) {
		ch.interlis.ili2c.metamodel.Type type=attr.getDomain();
		return getTypeTid(type,attr,null);
	}
	private String getTypeTid(ch.interlis.ili2c.metamodel.Type type,ch.interlis.ili2c.metamodel.Element parent,ch.interlis.ili2c.metamodel.Function function)
	{
		if(parent instanceof ch.interlis.ili2c.metamodel.AttributeDef){
			ch.interlis.ili2c.metamodel.AttributeDef attr=(ch.interlis.ili2c.metamodel.AttributeDef)parent;
			if(type instanceof ch.interlis.ili2c.metamodel.TypeAlias){
				ch.interlis.ili2c.metamodel.TypeAlias alias=(ch.interlis.ili2c.metamodel.TypeAlias)type;
				ch.interlis.ili2c.metamodel.Domain domain=alias.getAliasing();
				if(domain!=td.INTERLIS.URI && domain!=td.INTERLIS.NAME && domain!=td.INTERLIS.BOOLEAN){
					if(alias.isMandatory()){
						return getRef(attr)+"."+LOCAL_TYPE_NAME;
					}
					return getRef(domain);
				}
			}
			return getRef(attr)+"."+LOCAL_TYPE_NAME;
		}else if(parent instanceof ch.interlis.ili2c.metamodel.Parameter){
				ch.interlis.ili2c.metamodel.Parameter param=(ch.interlis.ili2c.metamodel.Parameter)parent;
				if(type instanceof ch.interlis.ili2c.metamodel.TypeAlias){
					ch.interlis.ili2c.metamodel.TypeAlias alias=(ch.interlis.ili2c.metamodel.TypeAlias)type;
					ch.interlis.ili2c.metamodel.Domain domain=alias.getAliasing();
					if(domain!=td.INTERLIS.URI && domain!=td.INTERLIS.NAME  && domain!=td.INTERLIS.BOOLEAN){
						if(alias.isMandatory()){
							return getRef(param)+"."+LOCAL_TYPE_NAME;
						}
						return getRef(domain);
					}
				}
				return getRef(param)+"."+LOCAL_TYPE_NAME;
		}else if(parent instanceof ch.interlis.ili2c.metamodel.Domain){
			ch.interlis.ili2c.metamodel.Domain domain=(ch.interlis.ili2c.metamodel.Domain)parent;
			return getRef(domain);
		}else if(parent instanceof ch.interlis.ili2c.metamodel.Function){
			ch.interlis.ili2c.metamodel.Function func=(ch.interlis.ili2c.metamodel.Function)parent;
			if(type instanceof ch.interlis.ili2c.metamodel.TypeAlias){
				ch.interlis.ili2c.metamodel.TypeAlias alias=(ch.interlis.ili2c.metamodel.TypeAlias)type;
				ch.interlis.ili2c.metamodel.Domain domain=alias.getAliasing();
				if(domain!=td.INTERLIS.URI && domain!=td.INTERLIS.NAME  && domain!=td.INTERLIS.BOOLEAN){
					if(alias.isMandatory()){
						return getRef(func)+"."+LOCAL_TYPE_NAME;
					}
					return getRef(domain);
				}
			}
			return getRef(func)+"."+LOCAL_TYPE_NAME;
		}else if(parent instanceof ch.interlis.ili2c.metamodel.FormalArgument){
			ch.interlis.ili2c.metamodel.FormalArgument arg=(ch.interlis.ili2c.metamodel.FormalArgument)parent;
			if(type instanceof ch.interlis.ili2c.metamodel.TypeAlias){
				ch.interlis.ili2c.metamodel.TypeAlias alias=(ch.interlis.ili2c.metamodel.TypeAlias)type;
				ch.interlis.ili2c.metamodel.Domain domain=alias.getAliasing();
				if(domain!=td.INTERLIS.URI && domain!=td.INTERLIS.NAME  && domain!=td.INTERLIS.BOOLEAN){
					if(alias.isMandatory()){
						return getRef(arg)+"."+LOCAL_TYPE_NAME;
					}
					return getRef(domain);
				}
			}
			return getRef(arg)+"."+LOCAL_TYPE_NAME;
		}else{
			throw new IllegalArgumentException("unexpected parent type "+parent.getClass().getName());
		}
		
	}
	private Type visitType(String typeTid,ch.interlis.ili2c.metamodel.Type type)
	throws IoxException
	{
		Type iomType=null;
		if(false){ // type instanceof
		}else if(type instanceof ch.interlis.ili2c.metamodel.AttributePathType){
			AttributeRefType iomAttrRef=new AttributeRefType(typeTid);
			ch.interlis.ili2c.metamodel.AttributePathType attrRef=(ch.interlis.ili2c.metamodel.AttributePathType)type;
			// ARefOf
			if(attrRef.getArgRestriction()!=null){
				ch.interlis.ili2c.metamodel.FormalArgument arg=attrRef.getArgRestriction();
				iomAttrRef.setOf(getRef(arg));				
			}else if(attrRef.getAttrRestriction()!=null){
				ch.interlis.ili2c.metamodel.PathEl[] path=attrRef.getAttrRestriction().getPathElements();
				ch.interlis.ili2c.metamodel.AttributeRef last=(ch.interlis.ili2c.metamodel.AttributeRef)path[path.length-1];
				iomAttrRef.setOf(getAttrTid(last.getAttr()));
			}
			ch.interlis.ili2c.metamodel.Type[] typeRestv=attrRef.getTypeRestriction();
			if(typeRestv!=null){
				for(int typei=0;typei<typeRestv.length;typei++){
					ch.interlis.ili2c.metamodel.Type typeRest=typeRestv[typei];
					// TODO ARefRestriction
					//ARefRestriction iomAref=new ARefRestriction(null);
					//iomAref.setARef(iomAttrRef.getobjectoid());
					//iomAref.setType(getTypTid(typeRest));
					//visitType(typeRest);
				}
				
			}
			
			iomType=iomAttrRef;
		}else if(type instanceof ch.interlis.ili2c.metamodel.BlackboxType){
			BlackboxType iomText=new BlackboxType(typeTid);
			ch.interlis.ili2c.metamodel.BlackboxType bb=(ch.interlis.ili2c.metamodel.BlackboxType)type;
			iomText.setKind(bb.getKind()==ch.interlis.ili2c.metamodel.BlackboxType.eBINARY?BlackboxType_Kind.Binary:BlackboxType_Kind.Xml);
			iomType=iomText;
		}else if(type instanceof ch.interlis.ili2c.metamodel.AbstractCoordType){
			CoordType iomCoord=new CoordType(typeTid);
			ch.interlis.ili2c.metamodel.AbstractCoordType coord=(ch.interlis.ili2c.metamodel.AbstractCoordType)type;
			ch.interlis.ili2c.metamodel.NumericalType dimv[]=coord.getDimensions();
			for(int dimi=0;dimi<dimv.length;dimi++){
				String name="C"+Integer.toString(dimi+1);
				NumType iomNum=new NumType(iomCoord.getobjectoid()+"."+name);
				iomNum.setName(name);
				ch.interlis.ili2c.metamodel.NumericalType num=dimv[dimi];
				visitNumericalType(iomNum, num);
				iomNum.setMandatory(true);
				out.write(new ObjectEvent(iomNum));
				AxisSpec iomAxisSpec=new AxisSpec();
				iomAxisSpec.setAxis(iomNum.getobjectoid(),dimi+1);
				iomAxisSpec.setCoordType(iomCoord.getobjectoid());
				out.write(new ObjectEvent(iomAxisSpec));
			}
			iomCoord.setMulti(coord instanceof ch.interlis.ili2c.metamodel.MultiCoordType);
			iomCoord.setGeneric(coord.isGeneric());
			iomType=iomCoord;
		}else if(type instanceof ch.interlis.ili2c.metamodel.EnumerationType){
			EnumType iomEnumType=new EnumType(typeTid);
			ch.interlis.ili2c.metamodel.EnumerationType enumType=(ch.interlis.ili2c.metamodel.EnumerationType)type;
			ch.interlis.ili2c.metamodel.Enumeration enm=enumType.getEnumeration();
			if ( enumType.isCircular() ) {
				iomEnumType.setOrder( EnumType_Order.Circular );
			} else if ( enumType.isOrdered() ) {
				iomEnumType.setOrder( EnumType_Order.Ordered );
			} else {
				iomEnumType.setOrder( EnumType_Order.Unordered );
			}
			EnumNode iomTopNode=new EnumNode(iomEnumType.getobjectoid()+"."+ENUM_TOP);
			iomTopNode.setName(ENUM_TOP);
			iomTopNode.setEnumType(iomEnumType.getobjectoid(),1);
			iomTopNode.setAbstract(false);
			iomTopNode.setFinal(false);
			iomTopNode.setGeneric(false);
			out.write(new ObjectEvent(iomTopNode));
			visitEnumeration(iomTopNode.getobjectoid(),enm);
			iomType=iomEnumType;
		}else if(type instanceof ch.interlis.ili2c.metamodel.EnumTreeValueType){
			EnumTreeValueType iomEnumType=new EnumTreeValueType(typeTid);
			ch.interlis.ili2c.metamodel.EnumTreeValueType enm=(ch.interlis.ili2c.metamodel.EnumTreeValueType)type;
			iomEnumType.setET(getRef(enm.getEnumType()));
			iomType=iomEnumType;
		}else if(type instanceof ch.interlis.ili2c.metamodel.FormattedType){
			FormattedType iomFormat=new FormattedType(typeTid);
			ch.interlis.ili2c.metamodel.FormattedType format=(ch.interlis.ili2c.metamodel.FormattedType)type;
			if(format.getDefinedBaseDomain()!=null){
				// 'FORMAT' FormattedType-DomainRef
				iomFormat.setSuper( getRef(format.getDefinedBaseDomain()) );
			}
			ch.interlis.ili2c.metamodel.Table baseStruct=format.getBaseStruct();
			if(baseStruct!=null){
				iomFormat.setStruct(getRef(baseStruct));
			}
			String fmt=format.getFormat();
			if(fmt!=null){
				iomFormat.setFormat(fmt);
			}
			if(format.getMinimum()!=null){
				iomFormat.setMin(format.getMinimum());
				iomFormat.setMax(format.getMaximum());
			}
			iomType=iomFormat;
		}else if(type instanceof ch.interlis.ili2c.metamodel.NumericType){
			NumType iomNum=new NumType(typeTid);
			ch.interlis.ili2c.metamodel.NumericType num=(ch.interlis.ili2c.metamodel.NumericType)type;
			visitNumericType(iomNum, num);
			iomType=iomNum;
		}else if(type instanceof ch.interlis.ili2c.metamodel.ClassType){
			ClassRefType iomClassType=new ClassRefType(typeTid);
			ch.interlis.ili2c.metamodel.ClassType classtype=(ch.interlis.ili2c.metamodel.ClassType)type;
			BaseClass iomBaseClass=new BaseClass();
			iomBaseClass.setCRT(iomClassType.getobjectoid());
			if(classtype.isStructure()){
				iomBaseClass.setBaseClass(getRef(td.INTERLIS.ANYSTRUCTURE));
			}else{
				iomBaseClass.setBaseClass(getRef(td.INTERLIS.ANYCLASS));
			}
			out.write(new ObjectEvent(iomBaseClass));

			Iterator resti=classtype.iteratorRestrictedTo();
			while(resti.hasNext()){
				ClassRestriction iomRestriction=new ClassRestriction();
				ch.interlis.ili2c.metamodel.Table rest=(ch.interlis.ili2c.metamodel.Table)resti.next();
				iomRestriction.setClassRestriction(getRef(rest));
				iomRestriction.setCRTR(iomClassType.getobjectoid());
				out.write(new ObjectEvent(iomRestriction));
			}
			iomType=iomClassType;
		}else if(type instanceof ch.interlis.ili2c.metamodel.CompositionType){
			MultiValue iomMultiValue=new MultiValue(typeTid);
			ch.interlis.ili2c.metamodel.CompositionType comp=(ch.interlis.ili2c.metamodel.CompositionType)type;
			iomMultiValue.setOrdered(comp.isOrdered());
			ch.interlis.ili2c.metamodel.Cardinality card=comp.getCardinality();
			Multiplicity iomMultiplicity = visitCardinality(card);
			iomMultiValue.setMultiplicity(iomMultiplicity);
			iomMultiValue.setBaseType(getRef(comp.getComponentType()));
			Iterator resti=comp.iteratorRestrictedTo();
			while(resti.hasNext()){
				TypeRestriction iomRestriction=new TypeRestriction();
				ch.interlis.ili2c.metamodel.Table rest=(ch.interlis.ili2c.metamodel.Table)resti.next();
				iomRestriction.setTypeRestriction(getRef(rest));
				iomRestriction.setTRTR(iomMultiValue.getobjectoid());
				out.write(new ObjectEvent(iomRestriction));
			}
			iomType=iomMultiValue;
		}else if(type instanceof ch.interlis.ili2c.metamodel.LineType){
			LineType iomLineType=new LineType(typeTid);
			ch.interlis.ili2c.metamodel.LineType lineType=(ch.interlis.ili2c.metamodel.LineType)type;
			if(lineType.getControlPointDomain()!=null){
				iomLineType.setCoordType(getRef(lineType.getControlPointDomain()));
			}
			if(lineType instanceof ch.interlis.ili2c.metamodel.SurfaceOrAreaType){
				ch.interlis.ili2c.metamodel.SurfaceOrAreaType surface=(ch.interlis.ili2c.metamodel.SurfaceOrAreaType)lineType;
				if(surface.getLineAttributeStructure()!=null){
					iomLineType.setLAStructure(getRef(surface.getLineAttributeStructure()));
				}
				if(surface instanceof ch.interlis.ili2c.metamodel.SurfaceType){
					iomLineType.setKind(LineType_Kind.Surface);
				}else if(surface instanceof ch.interlis.ili2c.metamodel.AreaType){
					iomLineType.setKind(LineType_Kind.Area);
				}else{
					throw new IllegalArgumentException("unexpected type "+type.getClass().getName());
				}
				iomLineType.setMulti(false);
			}else if(lineType instanceof ch.interlis.ili2c.metamodel.MultiSurfaceOrAreaType){
				ch.interlis.ili2c.metamodel.MultiSurfaceOrAreaType surface=(ch.interlis.ili2c.metamodel.MultiSurfaceOrAreaType)lineType;
				if(surface.getLineAttributeStructure()!=null){
					iomLineType.setLAStructure(getRef(surface.getLineAttributeStructure()));
				}
				if(surface instanceof ch.interlis.ili2c.metamodel.MultiSurfaceType){
					iomLineType.setKind(LineType_Kind.Surface);
				}else if(surface instanceof ch.interlis.ili2c.metamodel.MultiAreaType){
					iomLineType.setKind(LineType_Kind.Area);
				}else{
					throw new IllegalArgumentException("unexpected type "+type.getClass().getName());
				}
				iomLineType.setMulti(true);
			}else if(lineType instanceof ch.interlis.ili2c.metamodel.PolylineType){
				ch.interlis.ili2c.metamodel.PolylineType polyline=(ch.interlis.ili2c.metamodel.PolylineType)lineType;
				iomLineType.setKind(polyline.isDirected()?LineType_Kind.DirectedPolyline:LineType_Kind.Polyline);
				iomLineType.setMulti(false);
			}else if(lineType instanceof ch.interlis.ili2c.metamodel.MultiPolylineType){
				ch.interlis.ili2c.metamodel.MultiPolylineType polyline=(ch.interlis.ili2c.metamodel.MultiPolylineType)lineType;
				iomLineType.setKind(polyline.isDirected()?LineType_Kind.DirectedPolyline:LineType_Kind.Polyline);
				iomLineType.setMulti(true);
			}else{
				throw new IllegalArgumentException("unexpected type "+type.getClass().getName());
			}
			if(lineType.getMaxOverlap()!=null){
				iomLineType.setMaxOverlap(lineType.getMaxOverlap().toString());
			}
			ch.interlis.ili2c.metamodel.LineForm lf[]=lineType.getLineForms();
			for(int lfi=0;lfi<lf.length;lfi++){
				LinesForm iomLf=new LinesForm();
				iomLf.setLineType(iomLineType.getobjectoid());
				iomLf.setLineForm(getRef(lf[lfi]));
				out.write(new ObjectEvent(iomLf));
			}
			iomType=iomLineType;
		}else if(type instanceof ch.interlis.ili2c.metamodel.MetaobjectType){
			ClassRefType iomMetaType=new ClassRefType(typeTid);
			ch.interlis.ili2c.metamodel.MetaobjectType meta=(ch.interlis.ili2c.metamodel.MetaobjectType)type;
			ch.interlis.ili2c.metamodel.Table ref=meta.getReferred();
			BaseClass iomBaseClass=new BaseClass();
			iomBaseClass.setCRT(iomMetaType.getobjectoid());
			if(ref==null){
				iomBaseClass.setBaseClass(getRef(td.INTERLIS.METAOBJECT));
			}else{
				iomBaseClass.setBaseClass(getRef(ref));
			}
			out.write(new ObjectEvent(iomBaseClass));
			iomType=iomMetaType;
		}else if(type instanceof ch.interlis.ili2c.metamodel.ObjectType){
			ch.interlis.ili2c.metamodel.ObjectType ot=(ch.interlis.ili2c.metamodel.ObjectType)type;
			ObjectType iomObjectType=new ObjectType(typeTid);
			iomObjectType.setMultiple(ot.isObjects());
			BaseClass iomBaseClass= new BaseClass();
			iomBaseClass.setCRT(iomObjectType.getobjectoid());
			iomBaseClass.setBaseClass(getRef(ot.getRef()));
			out.write(new ObjectEvent(iomBaseClass));
			Iterator ri=ot.iteratorRestrictedTo();
			while(ri.hasNext()){
				ch.interlis.ili2c.metamodel.AbstractClassDef r=(ch.interlis.ili2c.metamodel.AbstractClassDef)ri.next();
				ClassRestriction iomClassRestriction= new ClassRestriction();
				iomClassRestriction.setCRTR(iomObjectType.getobjectoid());
				iomClassRestriction.setClassRestriction(getRef(r));
				out.write(new ObjectEvent(iomClassRestriction));
			}
			iomType=iomObjectType;
		}else if(type instanceof ch.interlis.ili2c.metamodel.OIDType){
			ch.interlis.ili2c.metamodel.OIDType oidType=(ch.interlis.ili2c.metamodel.OIDType)type;
			if(oidType.getOIDType()==null){
				AnyOIDType iomOidType=new AnyOIDType(typeTid);
				iomType=iomOidType;
			}else if(oidType.getOIDType() instanceof ch.interlis.ili2c.metamodel.TextType){
				TextType iomOidType=new TextType(typeTid);
				ch.interlis.ili2c.metamodel.TextType text=(ch.interlis.ili2c.metamodel.TextType)oidType.getOIDType();
				visitTextType(iomOidType, text);
				iomType=iomOidType;
			}else if(oidType.getOIDType() instanceof ch.interlis.ili2c.metamodel.NumericType){
				NumType iomOidType=new NumType(typeTid);
				ch.interlis.ili2c.metamodel.NumericType num=(ch.interlis.ili2c.metamodel.NumericType)oidType.getOIDType();
				visitNumericType(iomOidType, num);
				iomType=iomOidType;				
			}else{
				throw new IllegalArgumentException("unexpected oid type "+oidType.getOIDType().getClass().getName());			
			}
		}else if(type instanceof ch.interlis.ili2c.metamodel.ReferenceType){
			ReferenceType iomRefType=new ReferenceType(typeTid);
			ch.interlis.ili2c.metamodel.ReferenceType ref=(ch.interlis.ili2c.metamodel.ReferenceType)type;

			ch.interlis.ili2c.metamodel.AbstractClassDef target=ref.getReferred();
			BaseClass iomBaseClass=new BaseClass();
			iomBaseClass.setCRT(iomRefType.getobjectoid());
			iomBaseClass.setBaseClass(getRef(target));
			out.write(new ObjectEvent(iomBaseClass));
			
			Iterator targeti=ref.iteratorRestrictedTo();
			while(targeti.hasNext()){
				target=(ch.interlis.ili2c.metamodel.AbstractClassDef)targeti.next();
				ClassRestriction iomRestriction=new ClassRestriction();
				iomRestriction.setClassRestriction(getRef(target));
				iomRestriction.setCRTR(iomRefType.getobjectoid());
				out.write(new ObjectEvent(iomRestriction));
			}
			
			iomRefType.setExternal(ref.isExternal());
			
			iomType=iomRefType;
		}else if(type instanceof ch.interlis.ili2c.metamodel.TextType){
			TextType iomText=new TextType(typeTid);
			ch.interlis.ili2c.metamodel.TextType text=(ch.interlis.ili2c.metamodel.TextType)type;
			visitTextType(iomText, text);
			iomType=iomText;
		}else if(type instanceof ch.interlis.ili2c.metamodel.StructuredUnitType){
			NumType iomText=new NumType(typeTid);
			ch.interlis.ili2c.metamodel.StructuredUnitType structNum=(ch.interlis.ili2c.metamodel.StructuredUnitType)type;
			iomText.setMax(structNum.getMaximum().toString());
			iomText.setMin(structNum.getMinimum().toString());
			iomType=iomText;
		}else if(type instanceof ch.interlis.ili2c.metamodel.TypeAlias){
				ch.interlis.ili2c.metamodel.TypeAlias alias=(ch.interlis.ili2c.metamodel.TypeAlias)type;
				ch.interlis.ili2c.metamodel.Domain domain=alias.getAliasing();
				if(domain==td.INTERLIS.URI){
					TextType iomText=new TextType(typeTid);
					ch.interlis.ili2c.metamodel.TextType text=(ch.interlis.ili2c.metamodel.TextType)domain.getType();
					iomText.setKind(TextType_Kind.Uri);
					int maxLen=text.getMaxLength();
					if(maxLen>=0){
						iomText.setMaxLength(maxLen);
					}
					iomType=iomText;
				}else if(domain==td.INTERLIS.NAME){
					TextType iomText=new TextType(typeTid);
					ch.interlis.ili2c.metamodel.TextType text=(ch.interlis.ili2c.metamodel.TextType)domain.getType();
					iomText.setKind(TextType_Kind.Text);
					int maxLen=text.getMaxLength();
					if(maxLen>=0){
						iomText.setMaxLength(maxLen);
					}
					iomType=iomText;
				}else if(domain==td.INTERLIS.BOOLEAN){
					BooleanType iomBoolean=new BooleanType(typeTid);
					iomType=iomBoolean;
				}else if(domain==td.INTERLIS.INTERLIS_1_DATE){
					TextType iomText=new TextType(typeTid);
					iomText.setKind(TextType_Kind.Text);
					iomText.setMaxLength(8);
					iomText.setFinal(true);
					iomType=iomText;
				}else{
					throw new IllegalArgumentException("unexpected type alias "+domain.getScopedName(null));
				}
		}else{
			EhiLogger.logAdaption("unexpected type "+type.getClass().getName()+"; mapped to TEXT");
			TextType iomText=new TextType(typeTid);
			iomText.setKind(TextType_Kind.Text);
			iomType=iomText;
		}
		return iomType;
	}
	private void visitAttrLocalType(OutParam<String> typeTid,ch.interlis.ili2c.metamodel.Type type,ch.interlis.ili2c.metamodel.AttributeDef attr,String domainTid)
	throws IoxException
	{
		typeTid.value=getTypeTid(type,attr,null);
		Type iomType=visitType(typeTid.value,type);
		iomType.setName(LOCAL_TYPE_NAME);
		if(domainTid!=null){
			// not yet set?
			if(iomType.getattrvaluecount("Super")==0){ 
				iomType.setSuper(domainTid); //FIXME
			}else{
				// replace ref
			    ch.interlis.iom.IomObject ref=iomType.getattrobj("Super",0);
			    ref.setobjectrefoid(domainTid);
			}
		}else{
			ch.interlis.ili2c.metamodel.AttributeDef baseAttr=(ch.interlis.ili2c.metamodel.AttributeDef)attr.getExtending();
			if(baseAttr!=null){
				// not yet set?
				if(iomType.getattrvaluecount("Super")==0){ 
					iomType.setSuper(getTypeTid(baseAttr.getDomain(), baseAttr, null));
				}
			}
		}
		iomType.setAbstract( type.isAbstract() );
		iomType.setFinal( false );
		iomType.setGeneric(false);
		if(iomType instanceof DomainType){
			((DomainType)iomType).setMandatory(attr.getDomain().isMandatoryConsideringAliases());
		}
		if(!(iomType instanceof MultiValue) && type.getCardinality().getMaximum()>1){
			MultiValue iomMultiValue=new MultiValue(attr.getContainer().getScopedName(null)+"."+attr.getName()+"."+MVT_TYPE_NAME);
			iomMultiValue.setName(MVT_TYPE_NAME);
			iomMultiValue.setLTParent(getAttrTid(attr));
			iomMultiValue.setOrdered(type.isOrdered());
			ch.interlis.ili2c.metamodel.Cardinality card=type.getCardinality();
			Multiplicity iomMultiplicity = visitCardinality(card);
			iomMultiValue.setMultiplicity(iomMultiplicity);
			iomMultiValue.setBaseType(typeTid.value);
			typeTid.value=iomMultiValue.getobjectoid();
			out.write(new ObjectEvent(iomMultiValue));
		}else{
			iomType.setLTParent(getAttrTid(attr));
		}
		//EhiLogger.debug("iomType "+iomType.toString());
		out.write(new ObjectEvent(iomType));
	}
	private void visitDomainType(OutParam<String> typeTid,ch.interlis.ili2c.metamodel.Type type,ch.interlis.ili2c.metamodel.Domain domain)
	throws IoxException
	{
		typeTid.value=getTypeTid(type,domain,null);
		Type iomType=visitType(typeTid.value,type);
		DomainType iomDomain=(DomainType)iomType;
		iomDomain.setElementInPackage( getRef(domain.getContainer()) );
		iomDomain.setName( domain.getName() );
		
		if ( domain.getDocumentation() != null ) {
			DocText doc = new DocText();
			doc.setText( domain.getDocumentation() );
			iomDomain.addDocumentation( doc );
		}
		
		ch.interlis.ili2c.metamodel.Domain extending=(ch.interlis.ili2c.metamodel.Domain)domain.getExtending();
		if(extending!=null){
			if(iomDomain.getattrobj("Super",0)==null){ 
				iomDomain.setSuper( getRef(extending) );
			}
		}
		iomDomain.setAbstract( domain.isAbstract() );
		iomDomain.setFinal( domain.isFinal() );
		visitMetaValues(getMetaValues(domain),iomDomain.getobjectoid());
		iomDomain.setMandatory(type.isMandatory());
		out.write(new ObjectEvent(iomType));
	}
	
	private void visitFunctionReturnLocalType(OutParam<String> typeTid,ch.interlis.ili2c.metamodel.Type type,ch.interlis.ili2c.metamodel.Function func)
	throws IoxException
	{
		typeTid.value=getTypeTid(type,func,null);
		Type iomType=visitType(typeTid.value,type);
		iomType.setLFTParent(getRef(func));
		iomType.setName(LOCAL_TYPE_NAME);
		iomType.setAbstract( type.isAbstract() );
		iomType.setFinal( false );
		iomType.setGeneric(false);
		if(iomType instanceof DomainType){
			((DomainType)iomType).setMandatory(func.getDomain().isMandatoryConsideringAliases());
		}
		out.write(new ObjectEvent(iomType));
	}
	private void visitFunctionArgumentLocalType(OutParam<String> typeTid,ch.interlis.ili2c.metamodel.Type type,ch.interlis.ili2c.metamodel.FormalArgument arg,ch.interlis.ili2c.metamodel.Function function)
	throws IoxException
	{
		typeTid.value=getTypeTid(type,arg,function);
		Type iomType=visitType(typeTid.value,type);
		iomType.setLFTParent(getRef(function));
		iomType.setName(arg.getName());
		iomType.setAbstract( type.isAbstract() );
		iomType.setFinal( false );
		iomType.setGeneric(false);
		if(iomType instanceof DomainType){
			((DomainType)iomType).setMandatory(arg.getType().isMandatoryConsideringAliases());
		}
		out.write(new ObjectEvent(iomType));
	}
	private void visitParameterLocalType(OutParam<String> typeTid,ch.interlis.ili2c.metamodel.Type type,ch.interlis.ili2c.metamodel.Parameter param)
	throws IoxException
	{
		typeTid.value=getTypeTid(type,param,null);
		Type iomType=visitType(typeTid.value,type);
		iomType.setLTParent(getRef(param));
		iomType.setName(LOCAL_TYPE_NAME);
		ch.interlis.ili2c.metamodel.Parameter baseParam=(ch.interlis.ili2c.metamodel.Parameter)param.getExtending();
		if(baseParam!=null){
			iomType.setSuper(getTypeTid(baseParam.getType(), baseParam, null));
		}
		iomType.setAbstract( type.isAbstract() );
		iomType.setFinal( false );
		iomType.setGeneric(false);
		if(iomType instanceof DomainType){
			((DomainType)iomType).setMandatory(param.getType().isMandatoryConsideringAliases());
		}
		out.write(new ObjectEvent(iomType));
	}

	private void visitTextType(TextType iomText, ch.interlis.ili2c.metamodel.TextType text) {
		iomText.setKind(text.isNormalized()?TextType_Kind.Text:TextType_Kind.MText);
		int maxLen=text.getMaxLength();
		if(maxLen>=0){
			iomText.setMaxLength(maxLen);
		}
	}

	private void visitNumericType(NumType iomNum, ch.interlis.ili2c.metamodel.NumericType num) {
		visitNumericalType(iomNum,num);
	}
	private void visitNumericalType(NumType iomNum, ch.interlis.ili2c.metamodel.NumericalType num) {
		if(!num.isAbstract()){
			if(num instanceof ch.interlis.ili2c.metamodel.NumericType){
				iomNum.setMin(((ch.interlis.ili2c.metamodel.NumericType)num).getMinimum().toString());
				iomNum.setMax(((ch.interlis.ili2c.metamodel.NumericType)num).getMaximum().toString());
			}else if(num instanceof ch.interlis.ili2c.metamodel.StructuredUnitType){
				iomNum.setMin(((ch.interlis.ili2c.metamodel.StructuredUnitType)num).getMinimum().toString());
				iomNum.setMax(((ch.interlis.ili2c.metamodel.StructuredUnitType)num).getMaximum().toString());
			}else{
				throw new IllegalArgumentException();
			}
			iomNum.setAbstract(false);
			iomNum.setFinal(false);
		}else{
			iomNum.setAbstract(true);
			iomNum.setFinal(false);
		}
		iomNum.setGeneric(false);
		iomNum.setCircular(num.isCircular());
		if(num.getRotation()!=ch.interlis.ili2c.metamodel.NumericType.ROTATION_NONE){
			iomNum.setClockwise(num.getRotation()==ch.interlis.ili2c.metamodel.NumericType.ROTATION_CLOCKWISE?true:false);
		}
		if(num.getUnit()!=null){
			iomNum.setUnit(getRef(num.getUnit()));
		}
		ch.interlis.ili2c.metamodel.RefSystemRef refSysRef=num.getReferenceSystem();
		if(refSysRef!=null){
			NumsRefSys iomRefSysRef=new NumsRefSys();
			if(refSysRef instanceof ch.interlis.ili2c.metamodel.RefSystemRef.CoordDomain){
				ch.interlis.ili2c.metamodel.RefSystemRef.CoordDomain rs=(ch.interlis.ili2c.metamodel.RefSystemRef.CoordDomain)refSysRef;
				iomRefSysRef.setobjectrefoid(getRef(rs.getReferredDomain()));
			}else if(refSysRef instanceof ch.interlis.ili2c.metamodel.RefSystemRef.CoordDomainAxis){
				ch.interlis.ili2c.metamodel.RefSystemRef.CoordDomainAxis rs=(ch.interlis.ili2c.metamodel.RefSystemRef.CoordDomainAxis)refSysRef;
				iomRefSysRef.setobjectrefoid(getRef(rs.getReferredDomain()));
				iomRefSysRef.setAxis(rs.getAxisNumber());
			}else if(refSysRef instanceof ch.interlis.ili2c.metamodel.RefSystemRef.CoordSystem){
				ch.interlis.ili2c.metamodel.RefSystemRef.CoordSystem rs=(ch.interlis.ili2c.metamodel.RefSystemRef.CoordSystem)refSysRef;
				iomRefSysRef.setobjectrefoid(getRef(rs.getSystem()));
			}else if(refSysRef instanceof ch.interlis.ili2c.metamodel.RefSystemRef.CoordSystemAxis){
				ch.interlis.ili2c.metamodel.RefSystemRef.CoordSystemAxis rs=(ch.interlis.ili2c.metamodel.RefSystemRef.CoordSystemAxis)refSysRef;
				iomRefSysRef.setobjectrefoid(getRef(rs.getSystem()));
				iomRefSysRef.setAxis(rs.getAxisNumber());
			}else{
				throw new IllegalArgumentException("unexpected type "+refSysRef.getClass().getName());			
				
			}
			iomNum.addattrobj("RefSys",iomRefSysRef);
		}
	}
	private void visitEnumeration(String parentOid,ch.interlis.ili2c.metamodel.Enumeration enm)
	throws IoxException
	{
		Iterator ei=enm.getElements();
		int orderpos=1;
		while(ei.hasNext()){
			ch.interlis.ili2c.metamodel.Enumeration.Element e=(ch.interlis.ili2c.metamodel.Enumeration.Element)ei.next();
			EnumNode iomE=new EnumNode(parentOid+"."+e.getName());
			iomE.setName(e.getName());
			iomE.setParentNode(parentOid,orderpos++);
			iomE.setAbstract(false);
			iomE.setFinal(false);
			iomE.setGeneric(false);
			out.write(new ObjectEvent(iomE));
			visitMetaValues(e.getMetaValues(),iomE.getobjectoid());
			ch.interlis.ili2c.metamodel.Enumeration sub=e.getSubEnumeration();
			if(sub!=null){
				visitEnumeration(iomE.getobjectoid(),sub);
			}
		}
		
	}
    private void visitTranslatedEnumeration(ch.interlis.models.IlisMeta16.ModelTranslation.Translation iomModel,String parentOid,ch.interlis.ili2c.metamodel.Enumeration enm)
    throws IoxException
    {
        Iterator ei=enm.getElements();
        while(ei.hasNext()){
            ch.interlis.ili2c.metamodel.Enumeration.Element e=(ch.interlis.ili2c.metamodel.Enumeration.Element)ei.next();
            String rootOid=parentOid+"."+e.getElementInRootLanguage().getName();
            ch.interlis.models.IlisMeta16.ModelTranslation.METranslation iomEle=new ch.interlis.models.IlisMeta16.ModelTranslation.METranslation();
            iomEle.setOf(rootOid);
            iomEle.setTranslatedName(e.getName());
            String doc=e.getDocumentation();
            if(doc!=null) {
                ch.interlis.models.IlisMeta16.ModelTranslation.DocTextTranslation iomDoc=new ch.interlis.models.IlisMeta16.ModelTranslation.DocTextTranslation();
                iomDoc.setText(doc);
                iomEle.addTranslatedDoc(iomDoc);
            }
            iomModel.addTranslations(iomEle);
            ch.interlis.ili2c.metamodel.Enumeration sub=e.getSubEnumeration();
            if(sub!=null){
                visitTranslatedEnumeration(iomModel,rootOid,sub);
            }
        }
    }

	private void visitContextDef(ch.interlis.ili2c.metamodel.ContextDef contextDef)
	throws IoxException
	{
		String contextOid = contextDef.getScopedName();
		Context iomContext = new Context(contextOid);
		iomContext.setName(contextDef.getName());
		if (contextDef.getDocumentation() != null) {
			DocText doc = new DocText();
			doc.setText(contextDef.getDocumentation());
			iomContext.addDocumentation(doc);
		}
		out.write(new ObjectEvent(iomContext));

        String genericDefOid = contextOid + "." + contextDef.getGeneric().getName();
        GenericDef iomGenericDef = new GenericDef(genericDefOid);
        iomGenericDef.setContext(contextOid);
        iomGenericDef.setGenericDomain(getRef(contextDef.getGeneric()));
        out.write(new ObjectEvent(iomGenericDef));

        for (ch.interlis.ili2c.metamodel.Domain concrete : contextDef.getConcretes()) {
            ConcreteForGeneric iomConcrete = new ConcreteForGeneric();
            iomConcrete.setGenericDef(genericDefOid);
            iomConcrete.setConcreteDomain(getRef(concrete));
            out.write(new ObjectEvent(iomConcrete));
        }
        visitMetaValues(getMetaValues(contextDef),iomContext.getobjectoid());
	}

    private void setupLanguageMapping(ch.interlis.ili2c.metamodel.Container<Element> container)
    throws IoxException
    {
        Iterator<Element> it = container.iterator();
        
        while ( it.hasNext() ) {
            Element ele = it.next();
            EhiLogger.traceState(ele.toString());
            setupLanguageMappingElement(ele);
            if(ele instanceof ch.interlis.ili2c.metamodel.Container) {
                setupLanguageMapping((ch.interlis.ili2c.metamodel.Container)ele);
            }
        }
    }
    
    private java.util.Map<ch.interlis.ili2c.metamodel.Element,java.util.List<ch.interlis.ili2c.metamodel.Element>> elements=new java.util.HashMap<ch.interlis.ili2c.metamodel.Element,java.util.List<ch.interlis.ili2c.metamodel.Element>>();
    private void setupLanguageMappingElement(ch.interlis.ili2c.metamodel.Element ele)
    throws IoxException
    {
        ch.interlis.ili2c.metamodel.Element rootEle=ele.getTranslationOfOrSame();
        java.util.List<ch.interlis.ili2c.metamodel.Element> translations=elements.get(rootEle);
        if(translations==null) {
            translations=new java.util.ArrayList<ch.interlis.ili2c.metamodel.Element>();
            translations.add(rootEle);
            elements.put(rootEle,translations);
        }
        if(rootEle!=ele) {
            if(!translations.contains(ele)) {
                translations.add(ele);
            }
        }
    }
    private ch.ehi.basics.settings.Settings getMetaValues(Element srcEle) {
        Element rootEle=srcEle.getElementInRootLanguage();
        ch.ehi.basics.settings.Settings ret=new ch.ehi.basics.settings.Settings();
        for(Element ele:elements.get(rootEle)) {
            ch.ehi.basics.settings.Settings src=ele.getMetaValues();
            for(String name:src.getValues()) {
                String value=src.getValue(name);
                if(value!=null) {
                    String retValue=ret.getValue(name);
                    if(retValue==null) {
                        // not yet set
                        ret.setValue(name,value);
                    }
                }
            }
        }
        return ret;
    }
    private void visitTranslatedElements(ch.interlis.models.IlisMeta16.ModelTranslation.Translation iomModel,ch.interlis.ili2c.metamodel.Container<Element> container)
    throws IoxException
    {
        Iterator<Element> it = container.iterator();
        
        while ( it.hasNext() ) {
            Element ele = it.next();
            EhiLogger.traceState(ele.toString());
            ch.interlis.models.IlisMeta16.ModelTranslation.METranslation iomEle=new ch.interlis.models.IlisMeta16.ModelTranslation.METranslation();
            iomEle.setOf(ele.getElementInRootLanguage().getScopedName());
            iomEle.setTranslatedName(ele.getName());
            String doc=ele.getDocumentation();
            if(doc!=null) {
                ch.interlis.models.IlisMeta16.ModelTranslation.DocTextTranslation iomDoc=new ch.interlis.models.IlisMeta16.ModelTranslation.DocTextTranslation();
                iomDoc.setText(doc);
                iomEle.addTranslatedDoc(iomDoc);
            }
            iomModel.addTranslations(iomEle);
            if(ele instanceof ch.interlis.ili2c.metamodel.Container) {
                visitTranslatedElements(iomModel,(ch.interlis.ili2c.metamodel.Container)ele);
            }else if (ele instanceof AttributeDef) {
                AttributeDef attr = (AttributeDef) ele;
                // If exist
                if (attr.getDomain() instanceof EnumerationType) {
                    AttributeDef rootAttr=(AttributeDef)attr.getElementInRootLanguage();
                    String idPrefix=getTypeTid(rootAttr.getDomain(),rootAttr,null)+"."+ENUM_TOP;
                    ch.interlis.ili2c.metamodel.Enumeration enm=((EnumerationType)attr.getDomain()).getEnumeration();
                    visitTranslatedEnumeration(iomModel,idPrefix,enm);
                }
            }else if (ele instanceof Domain) {
                Domain domain = (Domain) ele;
                if (domain.getType() instanceof EnumerationType) {
                    Domain rootDomain=(Domain)domain.getElementInRootLanguage();
                    String idPrefix=getTypeTid(rootDomain.getType(),rootDomain,null)+"."+ENUM_TOP;
                    ch.interlis.ili2c.metamodel.Enumeration enm=((EnumerationType)domain.getType()).getEnumeration();
                    visitTranslatedEnumeration(iomModel,idPrefix,enm);
                }
            }
        }
    }
	private void visitElements(ch.interlis.ili2c.metamodel.Container<Element> container)
	throws IoxException
	{
		Iterator<Element> it = container.iterator();
		
		while ( it.hasNext() ) {
			Element next = it.next();
			EhiLogger.traceState(next.toString());
			if(next instanceof ch.interlis.ili2c.metamodel.Topic){
				visitTopic((ch.interlis.ili2c.metamodel.Topic)next);
			}else if(next instanceof ch.interlis.ili2c.metamodel.AbstractClassDef){
				visitViewable((ch.interlis.ili2c.metamodel.AbstractClassDef)next);
			}else if(next instanceof ch.interlis.ili2c.metamodel.View){
				visitViewable((ch.interlis.ili2c.metamodel.View)next);
			}else if(next instanceof ch.interlis.ili2c.metamodel.Domain){
				visitDomain((ch.interlis.ili2c.metamodel.Domain)next);
			}else if(next instanceof ch.interlis.ili2c.metamodel.Unit){
				visitUnit((ch.interlis.ili2c.metamodel.Unit)next);
			}else if(next instanceof ch.interlis.ili2c.metamodel.LineForm){
				visitLineForm((ch.interlis.ili2c.metamodel.LineForm)next);
			}else if(next instanceof ch.interlis.ili2c.metamodel.Function){
				visitFunction((ch.interlis.ili2c.metamodel.Function)next);
			}else if(next instanceof ch.interlis.ili2c.metamodel.MetaDataUseDef){
				visitMetaDataUseDef((ch.interlis.ili2c.metamodel.MetaDataUseDef)next);
			}else if(next instanceof ch.interlis.ili2c.metamodel.Graphic){
				visitGraphic((ch.interlis.ili2c.metamodel.Graphic)next);
			}else if(next instanceof ch.interlis.ili2c.metamodel.ContextDef){
				visitContextDef((ch.interlis.ili2c.metamodel.ContextDef)next);
			}
		}
	
	}
	private static ViewableProperties getIoxMapping()
	{
	   ViewableProperties mapping=INTERLIS_.getIoxMapping();
	   mapping.addAll(ILISMETA16.getIoxMapping());

		  return mapping;
	}
}
