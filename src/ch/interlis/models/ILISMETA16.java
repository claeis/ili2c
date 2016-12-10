package ch.interlis.models;
public class ILISMETA16{
  private ILISMETA16() {}
  public final static String MODEL= "IlisMeta16";
  public final static String ModelData= "IlisMeta16.ModelData";
  public final static String ModelTranslation= "IlisMeta16.ModelTranslation";
  public static ch.interlis.iom_j.xtf.XtfModel getXtfModel(){ return new ch.interlis.iom_j.xtf.XtfModel("IlisMeta16","http://interlis.ch","2016-12-10"); }
  static public ch.interlis.iox.IoxFactory getIoxFactory()
  {
    return new ch.interlis.iox.IoxFactory(){
      public ch.interlis.iom.IomObject createIomObject(String type,String oid) throws ch.interlis.iox.IoxException {
      if(type.equals("IlisMeta16.ModelData.Context"))return new ch.interlis.models.IlisMeta16.ModelData.Context(oid);
      if(type.equals("IlisMeta16.ModelData.UnaryExpr"))return new ch.interlis.models.IlisMeta16.ModelData.UnaryExpr();
      if(type.equals("IlisMeta16.ModelData.SetConstraint"))return new ch.interlis.models.IlisMeta16.ModelData.SetConstraint(oid);
      if(type.equals("IlisMeta16.ModelData.RuntimeParamRef"))return new ch.interlis.models.IlisMeta16.ModelData.RuntimeParamRef();
      if(type.equals("IlisMeta16.ModelData.Argument"))return new ch.interlis.models.IlisMeta16.ModelData.Argument(oid);
      if(type.equals("IlisMeta16.ModelData.BaseClass"))return new ch.interlis.models.IlisMeta16.ModelData.BaseClass(oid);
      if(type.equals("IlisMeta16.ModelData.TypeRelatedType"))return new ch.interlis.models.IlisMeta16.ModelData.TypeRelatedType(oid);
      if(type.equals("IlisMeta16.ModelData.DocText"))return new ch.interlis.models.IlisMeta16.ModelData.DocText();
      if(type.equals("IlisMeta16.ModelData.TransferElement"))return new ch.interlis.models.IlisMeta16.ModelData.TransferElement(oid);
      if(type.equals("IlisMeta16.ModelData.Ili1TransferElement"))return new ch.interlis.models.IlisMeta16.ModelData.Ili1TransferElement(oid);
      if(type.equals("IlisMeta16.ModelTranslation.METranslation"))return new ch.interlis.models.IlisMeta16.ModelTranslation.METranslation();
      if(type.equals("IlisMeta16.ModelData.BlackboxType"))return new ch.interlis.models.IlisMeta16.ModelData.BlackboxType(oid);
      if(type.equals("IlisMeta16.ModelData.Import"))return new ch.interlis.models.IlisMeta16.ModelData.Import(oid);
      if(type.equals("IlisMeta16.ModelData.ClassRef"))return new ch.interlis.models.IlisMeta16.ModelData.ClassRef();
      if(type.equals("IlisMeta16.ModelData.BooleanType"))return new ch.interlis.models.IlisMeta16.ModelData.BooleanType(oid);
      if(type.equals("IlisMeta16.ModelData.Class"))return new ch.interlis.models.IlisMeta16.ModelData.Class(oid);
      if(type.equals("IlisMeta16.ModelData.ConcreteForGeneric"))return new ch.interlis.models.IlisMeta16.ModelData.ConcreteForGeneric(oid);
      if(type.equals("IlisMeta16.ModelData.ClassConst"))return new ch.interlis.models.IlisMeta16.ModelData.ClassConst();
      if(type.equals("IlisMeta16.ModelData.EnumTreeValueType"))return new ch.interlis.models.IlisMeta16.ModelData.EnumTreeValueType(oid);
      if(type.equals("IlisMeta16.ModelData.Type"))return new ch.interlis.models.IlisMeta16.ModelData.Type(oid);
      if(type.equals("IlisMeta16.ModelData.ObjectType"))return new ch.interlis.models.IlisMeta16.ModelData.ObjectType(oid);
      if(type.equals("IlisMeta16.ModelData.ActualArgument"))return new ch.interlis.models.IlisMeta16.ModelData.ActualArgument();
      if(type.equals("IlisMeta16.ModelData.EnumNode"))return new ch.interlis.models.IlisMeta16.ModelData.EnumNode(oid);
      if(type.equals("IlisMeta16.ModelData.Constraint"))return new ch.interlis.models.IlisMeta16.ModelData.Constraint(oid);
      if(type.equals("IlisMeta16.ModelData.ReferenceType"))return new ch.interlis.models.IlisMeta16.ModelData.ReferenceType(oid);
      if(type.equals("IlisMeta16.ModelData.ClassRefType"))return new ch.interlis.models.IlisMeta16.ModelData.ClassRefType(oid);
      if(type.equals("IlisMeta16.ModelData.UnitFunction"))return new ch.interlis.models.IlisMeta16.ModelData.UnitFunction();
      if(type.equals("IlisMeta16.ModelData.UniqueConstraint"))return new ch.interlis.models.IlisMeta16.ModelData.UniqueConstraint(oid);
      if(type.equals("IlisMeta16.ModelData.MetaAttribute"))return new ch.interlis.models.IlisMeta16.ModelData.MetaAttribute(oid);
      if(type.equals("IlisMeta16.ModelData.AttributeConst"))return new ch.interlis.models.IlisMeta16.ModelData.AttributeConst();
      if(type.equals("IlisMeta16.ModelData.NumType"))return new ch.interlis.models.IlisMeta16.ModelData.NumType(oid);
      if(type.equals("IlisMeta16.ModelData.FormattedType"))return new ch.interlis.models.IlisMeta16.ModelData.FormattedType(oid);
      if(type.equals("IlisMeta16.ModelData.TextType"))return new ch.interlis.models.IlisMeta16.ModelData.TextType(oid);
      if(type.equals("IlisMeta16.ModelData.PathEl"))return new ch.interlis.models.IlisMeta16.ModelData.PathEl();
      if(type.equals("IlisMeta16.ModelData.AxisSpec"))return new ch.interlis.models.IlisMeta16.ModelData.AxisSpec(oid);
      if(type.equals("IlisMeta16.ModelData.DataUnit"))return new ch.interlis.models.IlisMeta16.ModelData.DataUnit(oid);
      if(type.equals("IlisMeta16.ModelData.ARefRestriction"))return new ch.interlis.models.IlisMeta16.ModelData.ARefRestriction(oid);
      if(type.equals("IlisMeta16.ModelData.MetaBasketDef"))return new ch.interlis.models.IlisMeta16.ModelData.MetaBasketDef(oid);
      if(type.equals("IlisMeta16.ModelData.LinesForm"))return new ch.interlis.models.IlisMeta16.ModelData.LinesForm(oid);
      if(type.equals("IlisMeta16.ModelData.AttrOrParam"))return new ch.interlis.models.IlisMeta16.ModelData.AttrOrParam(oid);
      if(type.equals("IlisMeta16.ModelData.PathOrInspFactor"))return new ch.interlis.models.IlisMeta16.ModelData.PathOrInspFactor();
      if(type.equals("IlisMeta16.ModelData.LineForm"))return new ch.interlis.models.IlisMeta16.ModelData.LineForm(oid);
      if(type.equals("IlisMeta16.ModelData.CompoundExpr"))return new ch.interlis.models.IlisMeta16.ModelData.CompoundExpr();
      if(type.equals("IlisMeta16.ModelData.ClassRestriction"))return new ch.interlis.models.IlisMeta16.ModelData.ClassRestriction(oid);
      if(type.equals("IlisMeta16.ModelData.AnyOIDType"))return new ch.interlis.models.IlisMeta16.ModelData.AnyOIDType(oid);
      if(type.equals("IlisMeta16.ModelData.DomainType"))return new ch.interlis.models.IlisMeta16.ModelData.DomainType(oid);
      if(type.equals("IlisMeta16.ModelData.LineType"))return new ch.interlis.models.IlisMeta16.ModelData.LineType(oid);
      if(type.equals("IlisMeta16.ModelData.ExplicitAssocAccess"))return new ch.interlis.models.IlisMeta16.ModelData.ExplicitAssocAccess(oid);
      if(type.equals("IlisMeta16.ModelData.AllowedInBasket"))return new ch.interlis.models.IlisMeta16.ModelData.AllowedInBasket(oid);
      if(type.equals("IlisMeta16.ModelData.Ili1Format"))return new ch.interlis.models.IlisMeta16.ModelData.Ili1Format();
      if(type.equals("IlisMeta16.ModelData.SignParamAssignment"))return new ch.interlis.models.IlisMeta16.ModelData.SignParamAssignment();
      if(type.equals("IlisMeta16.ModelData.TypeRestriction"))return new ch.interlis.models.IlisMeta16.ModelData.TypeRestriction(oid);
      if(type.equals("IlisMeta16.ModelData.MetaObjectDef"))return new ch.interlis.models.IlisMeta16.ModelData.MetaObjectDef(oid);
      if(type.equals("IlisMeta16.ModelData.ExistenceConstraint"))return new ch.interlis.models.IlisMeta16.ModelData.ExistenceConstraint(oid);
      if(type.equals("IlisMeta16.ModelData.SimpleConstraint"))return new ch.interlis.models.IlisMeta16.ModelData.SimpleConstraint(oid);
      if(type.equals("IlisMeta16.ModelData.MultiValue"))return new ch.interlis.models.IlisMeta16.ModelData.MultiValue(oid);
      if(type.equals("IlisMeta16.ModelData.RenamedBaseView"))return new ch.interlis.models.IlisMeta16.ModelData.RenamedBaseView(oid);
      if(type.equals("IlisMeta16.ModelData.Dependency"))return new ch.interlis.models.IlisMeta16.ModelData.Dependency(oid);
      if(type.equals("IlisMeta16.ModelData.UnitRef"))return new ch.interlis.models.IlisMeta16.ModelData.UnitRef();
      if(type.equals("IlisMeta16.ModelData.FunctionDef"))return new ch.interlis.models.IlisMeta16.ModelData.FunctionDef(oid);
      if(type.equals("IlisMeta16.ModelData.SubModel"))return new ch.interlis.models.IlisMeta16.ModelData.SubModel(oid);
      if(type.equals("IlisMeta16.ModelData.MetaElement"))return new ch.interlis.models.IlisMeta16.ModelData.MetaElement(oid);
      if(type.equals("IlisMeta16.ModelData.ClassRelatedType"))return new ch.interlis.models.IlisMeta16.ModelData.ClassRelatedType(oid);
      if(type.equals("IlisMeta16.ModelData.Model"))return new ch.interlis.models.IlisMeta16.ModelData.Model(oid);
      if(type.equals("IlisMeta16.ModelData.EnumType"))return new ch.interlis.models.IlisMeta16.ModelData.EnumType(oid);
      if(type.equals("IlisMeta16.ModelTranslation.DocTextTranslation"))return new ch.interlis.models.IlisMeta16.ModelTranslation.DocTextTranslation();
      if(type.equals("IlisMeta16.ModelData.CondSignParamAssignment"))return new ch.interlis.models.IlisMeta16.ModelData.CondSignParamAssignment();
      if(type.equals("IlisMeta16.ModelData.EnumAssignment"))return new ch.interlis.models.IlisMeta16.ModelData.EnumAssignment();
      if(type.equals("IlisMeta16.ModelData.Package"))return new ch.interlis.models.IlisMeta16.ModelData.Package(oid);
      if(type.equals("IlisMeta16.ModelData.EnumMapping"))return new ch.interlis.models.IlisMeta16.ModelData.EnumMapping();
      if(type.equals("IlisMeta16.ModelData.Multiplicity"))return new ch.interlis.models.IlisMeta16.ModelData.Multiplicity();
      if(type.equals("IlisMeta16.ModelData.AttributeRefType"))return new ch.interlis.models.IlisMeta16.ModelData.AttributeRefType(oid);
      if(type.equals("IlisMeta16.ModelData.AssocAcc"))return new ch.interlis.models.IlisMeta16.ModelData.AssocAcc(oid);
      if(type.equals("IlisMeta16.ModelTranslation.Translation"))return new ch.interlis.models.IlisMeta16.ModelTranslation.Translation(oid);
      if(type.equals("IlisMeta16.ModelData.CoordType"))return new ch.interlis.models.IlisMeta16.ModelData.CoordType(oid);
      if(type.equals("IlisMeta16.ModelData.Expression"))return new ch.interlis.models.IlisMeta16.ModelData.Expression();
      if(type.equals("IlisMeta16.ModelData.Role"))return new ch.interlis.models.IlisMeta16.ModelData.Role(oid);
      if(type.equals("IlisMeta16.ModelData.DrawingRule"))return new ch.interlis.models.IlisMeta16.ModelData.DrawingRule(oid);
      if(type.equals("IlisMeta16.ModelData.ExtendableME"))return new ch.interlis.models.IlisMeta16.ModelData.ExtendableME(oid);
      if(type.equals("IlisMeta16.ModelData.View"))return new ch.interlis.models.IlisMeta16.ModelData.View(oid);
      if(type.equals("IlisMeta16.ModelData.Factor"))return new ch.interlis.models.IlisMeta16.ModelData.Factor();
      if(type.equals("IlisMeta16.ModelData.Unit"))return new ch.interlis.models.IlisMeta16.ModelData.Unit(oid);
      if(type.equals("IlisMeta16.ModelData.ExistenceDef"))return new ch.interlis.models.IlisMeta16.ModelData.ExistenceDef();
      if(type.equals("IlisMeta16.ModelData.Graphic"))return new ch.interlis.models.IlisMeta16.ModelData.Graphic(oid);
      if(type.equals("IlisMeta16.ModelData.Constant"))return new ch.interlis.models.IlisMeta16.ModelData.Constant();
      if(type.equals("IlisMeta16.ModelData.FunctionCall"))return new ch.interlis.models.IlisMeta16.ModelData.FunctionCall();
      return null;
      }
    };
  }
  static public ch.interlis.iom_j.ViewableProperties getIoxMapping()
  {
    ch.interlis.iom_j.ViewableProperties mapping=new ch.interlis.iom_j.ViewableProperties();
    mapping.defineClass("IlisMeta16.ModelData.Context", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      });
    mapping.defineClass("IlisMeta16.ModelData.UnaryExpr", new String[]{   "Operation"
      ,"SubExpression"
      });
    mapping.defineClass("IlisMeta16.ModelData.SetConstraint", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"ToClass"
      ,"ToDomain"
      ,"Where"
      ,"Constraint"
      });
    mapping.defineClass("IlisMeta16.ModelData.RuntimeParamRef", new String[]{   "RuntimeParam"
      });
    mapping.defineClass("IlisMeta16.ModelData.Argument", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Kind"
      ,"Function"
      ,"Type"
      });
    mapping.defineClass("IlisMeta16.ModelData.BaseClass", new String[]{   "CRT"
      ,"BaseClass"
      });
    mapping.defineClass("IlisMeta16.ModelData.TypeRelatedType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"BaseType"
      });
    mapping.defineClass("IlisMeta16.ModelData.LineFormStructure", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.FormalArgument", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.DocText", new String[]{   "Name"
      ,"Text"
      });
    mapping.defineClass("IlisMeta16.ModelData.AssocRole", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.TransferElement", new String[]{   "TransferClass"
      ,"TransferElement"
      });
    mapping.defineClass("IlisMeta16.ModelData.Ili1TransferElement", new String[]{   "Ili1TransferClass"
      ,"Ili1RefAttr"
      });
    mapping.defineClass("IlisMeta16.ModelTranslation.METranslation", new String[]{   "Of"
      ,"TranslatedName"
      ,"TranslatedDoc"
      });
    mapping.defineClass("IlisMeta16.ModelData.AssocAccTarget", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.BlackboxType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"Kind"
      });
    mapping.defineClass("IlisMeta16.ModelData.Import", new String[]{   "ImportingP"
      ,"ImportedP"
      });
    mapping.defineClass("IlisMeta16.ModelData.ClassRef", new String[]{   "Ref"
      });
    mapping.defineClass("IlisMeta16.ModelData.ObjectOID", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.LineAttr", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.BooleanType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      });
    mapping.defineClass("IlisMeta16.ModelData.AssocAccOrigin", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.Class", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Kind"
      ,"Multiplicity"
      ,"EmbeddedRoleTransfer"
      ,"ili1OptionalTable"
      ,"Oid"
      ,"View"
      });
    mapping.defineClass("IlisMeta16.ModelData.ConcreteForGeneric", new String[]{   "GenericDef"
      ,"ConcreteDomain"
      });
    mapping.defineClass("IlisMeta16.ModelData.ClassConst", new String[]{   "Class"
      });
    mapping.defineClass("IlisMeta16.ModelData.EnumTreeValueType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"ET"
      });
    mapping.defineClass("IlisMeta16.ModelData.BasketOID", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.Type", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      });
    mapping.defineClass("IlisMeta16.ModelData.ObjectType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"Multiple"
      });
    mapping.defineClass("IlisMeta16.ModelData.ARefOf", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.GraphicBase", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.GraphicRule", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.ActualArgument", new String[]{   "FormalArgument"
      ,"Kind"
      ,"Expression"
      ,"ObjectClasses"
      });
    mapping.defineClass("IlisMeta16.ModelData.SubNode", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.EnumNode", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"EnumType"
      ,"ParentNode"
      });
    mapping.defineClass("IlisMeta16.ModelData.NumsRefSys", new String[]{   "Axis"
      });
    mapping.defineClass("IlisMeta16.ModelData.Constraint", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"ToClass"
      ,"ToDomain"
      });
    mapping.defineClass("IlisMeta16.ModelData.ReferenceType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"External"
      });
    mapping.defineClass("IlisMeta16.ModelData.ClassRefType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      });
    mapping.defineClass("IlisMeta16.ModelData.UnitFunction", new String[]{   "Explanation"
      });
    mapping.defineClass("IlisMeta16.ModelData.BaseType", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.UniqueConstraint", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"ToClass"
      ,"ToDomain"
      ,"Where"
      ,"Kind"
      ,"UniqueDef"
      });
    mapping.defineClass("IlisMeta16.ModelData.SignClass", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.BaseViewRef", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.AttrOrParamType", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.MetaAttribute", new String[]{   "Name"
      ,"Value"
      ,"MetaElement"
      });
    mapping.defineClass("IlisMeta16.ModelData.BaseViewDef", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.ArgumentType", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.DomainConstraint", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.AttributeConst", new String[]{   "Attribute"
      });
    mapping.defineClass("IlisMeta16.ModelData.LocalType", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.NumType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"Min"
      ,"Max"
      ,"Circular"
      ,"Clockwise"
      ,"RefSys"
      ,"Unit"
      });
    mapping.defineClass("IlisMeta16.ModelData.FormattedType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"Min"
      ,"Max"
      ,"Circular"
      ,"Clockwise"
      ,"RefSys"
      ,"Unit"
      ,"Format"
      ,"Struct"
      });
    mapping.defineClass("IlisMeta16.ModelData.TextType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"Kind"
      ,"MaxLength"
      });
    mapping.defineClass("IlisMeta16.ModelData.PathEl", new String[]{   "Kind"
      ,"Ref"
      ,"NumIndex"
      ,"SpecIndex"
      });
    mapping.defineClass("IlisMeta16.ModelData.AxisSpec", new String[]{   "CoordType"
      ,"Axis"
      });
    mapping.defineClass("IlisMeta16.ModelData.MetaObjectClass", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.DataUnit", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"ViewUnit"
      ,"DataUnitName"
      ,"Oid"
      });
    mapping.defineClass("IlisMeta16.ModelData.ARefRestriction", new String[]{   "ARef"
      ,"Type"
      });
    mapping.defineClass("IlisMeta16.ModelData.MetaBasketDef", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"Kind"
      ,"MetaDataTopic"
      });
    mapping.defineClass("IlisMeta16.ModelData.MetaAttributes", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.DerivedAssoc", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.LinesForm", new String[]{   "LineType"
      ,"LineForm"
      });
    mapping.defineClass("IlisMeta16.ModelData.AttrOrParam", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"SubdivisionKind"
      ,"Transient"
      ,"Derivates"
      ,"AttrParent"
      ,"ParamParent"
      ,"Type"
      });
    mapping.defineClass("IlisMeta16.ModelData.ClassParam", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.TopNode", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.MetaDataUnit", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.PathOrInspFactor", new String[]{   "PathEls"
      ,"Inspection"
      });
    mapping.defineClass("IlisMeta16.ModelData.LineForm", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Structure"
      });
    mapping.defineClass("IlisMeta16.ModelData.CompoundExpr", new String[]{   "Operation"
      ,"SubExpressions"
      });
    mapping.defineClass("IlisMeta16.ModelData.ClassRestriction", new String[]{   "CRTR"
      ,"ClassRestriction"
      });
    mapping.defineClass("IlisMeta16.ModelData.ExplicitAssocAcc", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.AnyOIDType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      });
    mapping.defineClass("IlisMeta16.ModelData.DomainType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      });
    mapping.defineClass("IlisMeta16.ModelData.LineType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"Kind"
      ,"MaxOverlap"
      ,"Multi"
      ,"CoordType"
      ,"LAStructure"
      });
    mapping.defineClass("IlisMeta16.ModelData.ExplicitAssocAccess", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"AssocAccOf"
      ,"OriginRole"
      ,"TargetRole"
      });
    mapping.defineClass("IlisMeta16.ModelData.AllowedInBasket", new String[]{   "OfDataUnit"
      ,"ClassInBasket"
      });
    mapping.defineClass("IlisMeta16.ModelData.Ili1Format", new String[]{   "isFree"
      ,"LineSize"
      ,"tidSize"
      ,"blankCode"
      ,"undefinedCode"
      ,"continueCode"
      ,"Font"
      ,"tidKind"
      ,"tidExplanation"
      });
    mapping.defineClass("IlisMeta16.ModelData.SignParamAssignment", new String[]{   "Param"
      ,"Assignment"
      });
    mapping.defineClass("IlisMeta16.ModelData.TypeRestriction", new String[]{   "TRTR"
      ,"TypeRestriction"
      });
    mapping.defineClass("IlisMeta16.ModelData.LocalFType", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.MetaObjectDef", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"IsRefSystem"
      ,"Class"
      ,"MetaBasketDef"
      });
    mapping.defineClass("IlisMeta16.ModelData.LineCoord", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.ExistenceConstraint", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"ToClass"
      ,"ToDomain"
      ,"Attr"
      ,"ExistsIn"
      });
    mapping.defineClass("IlisMeta16.ModelData.TreeValueTypeOf", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.SimpleConstraint", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"ToClass"
      ,"ToDomain"
      ,"Kind"
      ,"Percentage"
      ,"LogicalExpression"
      });
    mapping.defineClass("IlisMeta16.ModelData.MultiValue", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"BaseType"
      ,"Ordered"
      ,"Multiplicity"
      });
    mapping.defineClass("IlisMeta16.ModelData.RenamedBaseView", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"OrNull"
      ,"BaseView"
      ,"View"
      });
    mapping.defineClass("IlisMeta16.ModelData.Dependency", new String[]{   "Using"
      ,"Dependent"
      });
    mapping.defineClass("IlisMeta16.ModelData.UnitRef", new String[]{   "Unit"
      });
    mapping.defineClass("IlisMeta16.ModelData.FunctionDef", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Explanation"
      ,"ResultType"
      });
    mapping.defineClass("IlisMeta16.ModelData.SubModel", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      });
    mapping.defineClass("IlisMeta16.ModelData.MetaElement", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      });
    mapping.defineClass("IlisMeta16.ModelData.ClassRelatedType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      });
    mapping.defineClass("IlisMeta16.ModelData.MetaBasketMembers", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.Model", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"iliVersion"
      ,"Contracted"
      ,"Kind"
      ,"Language"
      ,"At"
      ,"Version"
      ,"NoIncrementalTransfer"
      ,"CharSetIANAName"
      ,"xmlns"
      ,"ili1Transfername"
      ,"ili1Format"
      });
    mapping.defineClass("IlisMeta16.ModelData.EnumType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"Order"
      });
    mapping.defineClass("IlisMeta16.ModelData.PackageElements", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.Inheritance", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelTranslation.DocTextTranslation", new String[]{   "Text"
      });
    mapping.defineClass("IlisMeta16.ModelData.CondSignParamAssignment", new String[]{   "Where"
      ,"Assignments"
      });
    mapping.defineClass("IlisMeta16.ModelData.EnumAssignment", new String[]{   "ValueToAssign"
      ,"MinEnumValue"
      ,"MaxEnumValue"
      });
    mapping.defineClass("IlisMeta16.ModelData.ClassAttr", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.Package", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      });
    mapping.defineClass("IlisMeta16.ModelData.EnumMapping", new String[]{   "EnumValue"
      ,"Cases"
      });
    mapping.defineClass("IlisMeta16.ModelData.Multiplicity", new String[]{   "Min"
      ,"Max"
      });
    mapping.defineClass("IlisMeta16.ModelData.NumUnit", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.AttributeRefType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"Of"
      });
    mapping.defineClass("IlisMeta16.ModelData.AssocAcc", new String[]{   "Class"
      ,"AssocAcc"
      });
    mapping.defineClass("IlisMeta16.ModelTranslation.Translation", new String[]{   "Language"
      ,"Translations"
      });
    mapping.defineClass("IlisMeta16.ModelData.CoordType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"NullAxis"
      ,"PiHalfAxis"
      ,"Multi"
      });
    mapping.defineClass("IlisMeta16.ModelData.Expression", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.Role", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Context"
      ,"External"
      ,"Strongness"
      ,"Ordered"
      ,"Multiplicity"
      ,"Derivates"
      ,"EmbeddedTransfer"
      ,"Association"
      });
    mapping.defineClass("IlisMeta16.ModelData.GenericDef", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.DrawingRule", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"Rule"
      ,"Class"
      ,"Graphic"
      });
    mapping.defineClass("IlisMeta16.ModelData.ExtendableME", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      });
    mapping.defineClass("IlisMeta16.ModelData.ResultType", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.StructOfFormat", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.ClassConstraint", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.View", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Kind"
      ,"Multiplicity"
      ,"EmbeddedRoleTransfer"
      ,"ili1OptionalTable"
      ,"Oid"
      ,"View"
      ,"FormationKind"
      ,"FormationParameter"
      ,"Where"
      ,"Transient"
      });
    mapping.defineClass("IlisMeta16.ModelData.Factor", new String[]{  });
    mapping.defineClass("IlisMeta16.ModelData.Unit", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"Kind"
      ,"Definition"
      });
    mapping.defineClass("IlisMeta16.ModelData.ExistenceDef", new String[]{   "PathEls"
      ,"Inspection"
      ,"Viewable"
      });
    mapping.defineClass("IlisMeta16.ModelData.Graphic", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Generic"
      ,"Final"
      ,"Super"
      ,"Where"
      ,"Base"
      });
    mapping.defineClass("IlisMeta16.ModelData.Constant", new String[]{   "Value"
      ,"Type"
      });
    mapping.defineClass("IlisMeta16.ModelData.FunctionCall", new String[]{   "Function"
      ,"Arguments"
      });
    return mapping;
  }
}
