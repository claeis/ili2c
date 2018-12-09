package ch.interlis.models;
public class ILISMETA07{
  private ILISMETA07() {}
  public final static String MODEL= "IlisMeta07";
  public final static String ModelData= "IlisMeta07.ModelData";
  public final static String ModelTranslation= "IlisMeta07.ModelTranslation";
  public static ch.interlis.iom_j.xtf.XtfModel getXtfModel(){ return new ch.interlis.iom_j.xtf.XtfModel("IlisMeta07","http://interlis.ch","2011-12-22"); }
  static public ch.interlis.iox.IoxFactory getIoxFactory()
  {
    return new ch.interlis.iox.IoxFactory(){
      public ch.interlis.iom.IomObject createIomObject(String type,String oid) throws ch.interlis.iox.IoxException {
      if(type.equals("IlisMeta07.ModelData.BaseClass"))return new ch.interlis.models.IlisMeta07.ModelData.BaseClass(oid);
      if(type.equals("IlisMeta07.ModelData.ClassRelatedType"))return new ch.interlis.models.IlisMeta07.ModelData.ClassRelatedType(oid);
      if(type.equals("IlisMeta07.ModelData.Package"))return new ch.interlis.models.IlisMeta07.ModelData.Package(oid);
      if(type.equals("IlisMeta07.ModelData.ActualArgument"))return new ch.interlis.models.IlisMeta07.ModelData.ActualArgument();
      if(type.equals("IlisMeta07.ModelData.MetaAttribute"))return new ch.interlis.models.IlisMeta07.ModelData.MetaAttribute(oid);
      if(type.equals("IlisMeta07.ModelData.Role"))return new ch.interlis.models.IlisMeta07.ModelData.Role(oid);
      if(type.equals("IlisMeta07.ModelData.DocText"))return new ch.interlis.models.IlisMeta07.ModelData.DocText();
      if(type.equals("IlisMeta07.ModelData.TransferElement"))return new ch.interlis.models.IlisMeta07.ModelData.TransferElement(oid);
      if(type.equals("IlisMeta07.ModelData.ClassRef"))return new ch.interlis.models.IlisMeta07.ModelData.ClassRef();
      if(type.equals("IlisMeta07.ModelData.Constant"))return new ch.interlis.models.IlisMeta07.ModelData.Constant();
      if(type.equals("IlisMeta07.ModelData.DataUnit"))return new ch.interlis.models.IlisMeta07.ModelData.DataUnit(oid);
      if(type.equals("IlisMeta07.ModelData.Unit"))return new ch.interlis.models.IlisMeta07.ModelData.Unit(oid);
      if(type.equals("IlisMeta07.ModelTranslation.Translation"))return new ch.interlis.models.IlisMeta07.ModelTranslation.Translation(oid);
      if(type.equals("IlisMeta07.ModelData.UniqueConstraint"))return new ch.interlis.models.IlisMeta07.ModelData.UniqueConstraint();
      if(type.equals("IlisMeta07.ModelData.Expression"))return new ch.interlis.models.IlisMeta07.ModelData.Expression();
      if(type.equals("IlisMeta07.ModelData.ClassRestriction"))return new ch.interlis.models.IlisMeta07.ModelData.ClassRestriction(oid);
      if(type.equals("IlisMeta07.ModelData.TextType"))return new ch.interlis.models.IlisMeta07.ModelData.TextType(oid);
      if(type.equals("IlisMeta07.ModelData.AssocAcc"))return new ch.interlis.models.IlisMeta07.ModelData.AssocAcc(oid);
      if(type.equals("IlisMeta07.ModelData.RenamedBaseView"))return new ch.interlis.models.IlisMeta07.ModelData.RenamedBaseView(oid);
      if(type.equals("IlisMeta07.ModelData.Ili1TransferElement"))return new ch.interlis.models.IlisMeta07.ModelData.Ili1TransferElement(oid);
      if(type.equals("IlisMeta07.ModelData.MultiValue"))return new ch.interlis.models.IlisMeta07.ModelData.MultiValue(oid);
      if(type.equals("IlisMeta07.ModelData.FunctionCall"))return new ch.interlis.models.IlisMeta07.ModelData.FunctionCall();
      if(type.equals("IlisMeta07.ModelData.UnitFunction"))return new ch.interlis.models.IlisMeta07.ModelData.UnitFunction();
      if(type.equals("IlisMeta07.ModelData.TypeRelatedType"))return new ch.interlis.models.IlisMeta07.ModelData.TypeRelatedType(oid);
      if(type.equals("IlisMeta07.ModelData.ExplicitAssocAccess"))return new ch.interlis.models.IlisMeta07.ModelData.ExplicitAssocAccess(oid);
      if(type.equals("IlisMeta07.ModelData.RuntimeParamRef"))return new ch.interlis.models.IlisMeta07.ModelData.RuntimeParamRef();
      if(type.equals("IlisMeta07.ModelData.AllowedInBasket"))return new ch.interlis.models.IlisMeta07.ModelData.AllowedInBasket(oid);
      if(type.equals("IlisMeta07.ModelData.MetaBasketDef"))return new ch.interlis.models.IlisMeta07.ModelData.MetaBasketDef(oid);
      if(type.equals("IlisMeta07.ModelData.EnumMapping"))return new ch.interlis.models.IlisMeta07.ModelData.EnumMapping();
      if(type.equals("IlisMeta07.ModelData.SimpleConstraint"))return new ch.interlis.models.IlisMeta07.ModelData.SimpleConstraint();
      if(type.equals("IlisMeta07.ModelData.MetaElement"))return new ch.interlis.models.IlisMeta07.ModelData.MetaElement(oid);
      if(type.equals("IlisMeta07.ModelData.NumType"))return new ch.interlis.models.IlisMeta07.ModelData.NumType(oid);
      if(type.equals("IlisMeta07.ModelData.Dependency"))return new ch.interlis.models.IlisMeta07.ModelData.Dependency(oid);
      if(type.equals("IlisMeta07.ModelData.EnumAssignment"))return new ch.interlis.models.IlisMeta07.ModelData.EnumAssignment();
      if(type.equals("IlisMeta07.ModelData.EnumNode"))return new ch.interlis.models.IlisMeta07.ModelData.EnumNode(oid);
      if(type.equals("IlisMeta07.ModelData.TypeRestriction"))return new ch.interlis.models.IlisMeta07.ModelData.TypeRestriction(oid);
      if(type.equals("IlisMeta07.ModelData.MetaObjectDef"))return new ch.interlis.models.IlisMeta07.ModelData.MetaObjectDef(oid);
      if(type.equals("IlisMeta07.ModelData.View"))return new ch.interlis.models.IlisMeta07.ModelData.View(oid);
      if(type.equals("IlisMeta07.ModelData.ClassRefType"))return new ch.interlis.models.IlisMeta07.ModelData.ClassRefType(oid);
      if(type.equals("IlisMeta07.ModelData.SetConstraint"))return new ch.interlis.models.IlisMeta07.ModelData.SetConstraint();
      if(type.equals("IlisMeta07.ModelData.AttrOrParam"))return new ch.interlis.models.IlisMeta07.ModelData.AttrOrParam(oid);
      if(type.equals("IlisMeta07.ModelData.SubModel"))return new ch.interlis.models.IlisMeta07.ModelData.SubModel(oid);
      if(type.equals("IlisMeta07.ModelData.CompoundExpr"))return new ch.interlis.models.IlisMeta07.ModelData.CompoundExpr();
      if(type.equals("IlisMeta07.ModelData.LineForm"))return new ch.interlis.models.IlisMeta07.ModelData.LineForm(oid);
      if(type.equals("IlisMeta07.ModelData.Factor"))return new ch.interlis.models.IlisMeta07.ModelData.Factor();
      if(type.equals("IlisMeta07.ModelData.Type"))return new ch.interlis.models.IlisMeta07.ModelData.Type(oid);
      if(type.equals("IlisMeta07.ModelData.ExistenceDef"))return new ch.interlis.models.IlisMeta07.ModelData.ExistenceDef();
      if(type.equals("IlisMeta07.ModelData.ExtendableME"))return new ch.interlis.models.IlisMeta07.ModelData.ExtendableME(oid);
      if(type.equals("IlisMeta07.ModelData.BlackboxType"))return new ch.interlis.models.IlisMeta07.ModelData.BlackboxType(oid);
      if(type.equals("IlisMeta07.ModelData.UnitRef"))return new ch.interlis.models.IlisMeta07.ModelData.UnitRef();
      if(type.equals("IlisMeta07.ModelData.AttributeConst"))return new ch.interlis.models.IlisMeta07.ModelData.AttributeConst();
      if(type.equals("IlisMeta07.ModelTranslation.METranslation"))return new ch.interlis.models.IlisMeta07.ModelTranslation.METranslation();
      if(type.equals("IlisMeta07.ModelData.Ili1Format"))return new ch.interlis.models.IlisMeta07.ModelData.Ili1Format();
      if(type.equals("IlisMeta07.ModelData.LinesForm"))return new ch.interlis.models.IlisMeta07.ModelData.LinesForm(oid);
      if(type.equals("IlisMeta07.ModelData.FormattedType"))return new ch.interlis.models.IlisMeta07.ModelData.FormattedType(oid);
      if(type.equals("IlisMeta07.ModelData.LineType"))return new ch.interlis.models.IlisMeta07.ModelData.LineType(oid);
      if(type.equals("IlisMeta07.ModelData.BooleanType"))return new ch.interlis.models.IlisMeta07.ModelData.BooleanType(oid);
      if(type.equals("IlisMeta07.ModelData.EnumType"))return new ch.interlis.models.IlisMeta07.ModelData.EnumType(oid);
      if(type.equals("IlisMeta07.ModelData.Import"))return new ch.interlis.models.IlisMeta07.ModelData.Import(oid);
      if(type.equals("IlisMeta07.ModelData.AxisSpec"))return new ch.interlis.models.IlisMeta07.ModelData.AxisSpec(oid);
      if(type.equals("IlisMeta07.ModelData.PathOrInspFactor"))return new ch.interlis.models.IlisMeta07.ModelData.PathOrInspFactor();
      if(type.equals("IlisMeta07.ModelData.PathEl"))return new ch.interlis.models.IlisMeta07.ModelData.PathEl();
      if(type.equals("IlisMeta07.ModelData.Argument"))return new ch.interlis.models.IlisMeta07.ModelData.Argument(oid);
      if(type.equals("IlisMeta07.ModelData.Class"))return new ch.interlis.models.IlisMeta07.ModelData.Class(oid);
      if(type.equals("IlisMeta07.ModelData.Multiplicity"))return new ch.interlis.models.IlisMeta07.ModelData.Multiplicity();
      if(type.equals("IlisMeta07.ModelTranslation.DocTextTranslation"))return new ch.interlis.models.IlisMeta07.ModelTranslation.DocTextTranslation();
      if(type.equals("IlisMeta07.ModelData.CondSignParamAssignment"))return new ch.interlis.models.IlisMeta07.ModelData.CondSignParamAssignment();
      if(type.equals("IlisMeta07.ModelData.UnaryExpr"))return new ch.interlis.models.IlisMeta07.ModelData.UnaryExpr();
      if(type.equals("IlisMeta07.ModelData.SignParamAssignment"))return new ch.interlis.models.IlisMeta07.ModelData.SignParamAssignment();
      if(type.equals("IlisMeta07.ModelData.ReferenceType"))return new ch.interlis.models.IlisMeta07.ModelData.ReferenceType(oid);
      if(type.equals("IlisMeta07.ModelData.Constraint"))return new ch.interlis.models.IlisMeta07.ModelData.Constraint();
      if(type.equals("IlisMeta07.ModelData.EnumTreeValueType"))return new ch.interlis.models.IlisMeta07.ModelData.EnumTreeValueType(oid);
      if(type.equals("IlisMeta07.ModelData.Graphic"))return new ch.interlis.models.IlisMeta07.ModelData.Graphic(oid);
      if(type.equals("IlisMeta07.ModelData.DrawingRule"))return new ch.interlis.models.IlisMeta07.ModelData.DrawingRule(oid);
      if(type.equals("IlisMeta07.ModelData.FunctionDef"))return new ch.interlis.models.IlisMeta07.ModelData.FunctionDef(oid);
      if(type.equals("IlisMeta07.ModelData.ARefRestriction"))return new ch.interlis.models.IlisMeta07.ModelData.ARefRestriction(oid);
      if(type.equals("IlisMeta07.ModelData.Model"))return new ch.interlis.models.IlisMeta07.ModelData.Model(oid);
      if(type.equals("IlisMeta07.ModelData.ExistenceConstraint"))return new ch.interlis.models.IlisMeta07.ModelData.ExistenceConstraint();
      if(type.equals("IlisMeta07.ModelData.DomainType"))return new ch.interlis.models.IlisMeta07.ModelData.DomainType(oid);
      if(type.equals("IlisMeta07.ModelData.AnyOIDType"))return new ch.interlis.models.IlisMeta07.ModelData.AnyOIDType(oid);
      if(type.equals("IlisMeta07.ModelData.ClassConst"))return new ch.interlis.models.IlisMeta07.ModelData.ClassConst();
      if(type.equals("IlisMeta07.ModelData.AttributeRefType"))return new ch.interlis.models.IlisMeta07.ModelData.AttributeRefType(oid);
      if(type.equals("IlisMeta07.ModelData.ObjectType"))return new ch.interlis.models.IlisMeta07.ModelData.ObjectType(oid);
      if(type.equals("IlisMeta07.ModelData.CoordType"))return new ch.interlis.models.IlisMeta07.ModelData.CoordType(oid);
      return null;
      }
    };
  }
  static public ch.interlis.iom_j.ViewableProperties getIoxMapping()
  {
    ch.interlis.iom_j.ViewableProperties mapping=new ch.interlis.iom_j.ViewableProperties();
    java.util.HashMap<String,String> nameMap=new java.util.HashMap<String,String>();
    nameMap.put("IlisMeta07.ModelData","ModelData");
    nameMap.put("IlisMeta07.ModelTranslation","ModelTranslation");
    nameMap.put("IlisMeta07.ModelData.ObjectOID", "ObjectOID");
    mapping.defineClass("IlisMeta07.ModelData.ObjectOID", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.BaseClass", "BaseClass");
    mapping.defineClass("IlisMeta07.ModelData.BaseClass", new String[]{   "CRT"
      ,"BaseClass"
      });
    nameMap.put("IlisMeta07.ModelData.ClassRelatedType", "ClassRelatedType");
    mapping.defineClass("IlisMeta07.ModelData.ClassRelatedType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      });
    nameMap.put("IlisMeta07.ModelData.SubNode", "SubNode");
    mapping.defineClass("IlisMeta07.ModelData.SubNode", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.MetaDataUnit", "MetaDataUnit");
    mapping.defineClass("IlisMeta07.ModelData.MetaDataUnit", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.Package", "Package");
    mapping.defineClass("IlisMeta07.ModelData.Package", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      });
    nameMap.put("IlisMeta07.ModelData.MetaAttributes", "MetaAttributes");
    mapping.defineClass("IlisMeta07.ModelData.MetaAttributes", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.LineAttr", "LineAttr");
    mapping.defineClass("IlisMeta07.ModelData.LineAttr", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.ActualArgument", "ActualArgument");
    mapping.defineClass("IlisMeta07.ModelData.ActualArgument", new String[]{   "FormalArgument"
      ,"Kind"
      ,"Expression"
      ,"ObjectClasses"
      });
    nameMap.put("IlisMeta07.ModelData.AttrOrParamType", "AttrOrParamType");
    mapping.defineClass("IlisMeta07.ModelData.AttrOrParamType", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.MetaAttribute", "MetaAttribute");
    mapping.defineClass("IlisMeta07.ModelData.MetaAttribute", new String[]{   "Name"
      ,"Value"
      ,"MetaElement"
      });
    nameMap.put("IlisMeta07.ModelData.Role", "Role");
    mapping.defineClass("IlisMeta07.ModelData.Role", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"External"
      ,"Strongness"
      ,"Ordered"
      ,"Multiplicity"
      ,"Derivates"
      ,"EmbeddedTransfer"
      ,"Association"
      });
    nameMap.put("IlisMeta07.ModelData.DocText", "DocText");
    mapping.defineClass("IlisMeta07.ModelData.DocText", new String[]{   "Name"
      ,"Text"
      });
    nameMap.put("IlisMeta07.ModelData.TransferElement", "TransferElement");
    mapping.defineClass("IlisMeta07.ModelData.TransferElement", new String[]{   "TransferClass"
      ,"TransferElement"
      });
    nameMap.put("IlisMeta07.ModelData.ClassRef", "ClassRef");
    mapping.defineClass("IlisMeta07.ModelData.ClassRef", new String[]{   "Ref"
      });
    nameMap.put("IlisMeta07.ModelData.AssocAccOrigin", "AssocAccOrigin");
    mapping.defineClass("IlisMeta07.ModelData.AssocAccOrigin", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.Constant", "Constant");
    mapping.defineClass("IlisMeta07.ModelData.Constant", new String[]{   "Value"
      ,"Type"
      });
    nameMap.put("IlisMeta07.ModelData.DataUnit", "DataUnit");
    mapping.defineClass("IlisMeta07.ModelData.DataUnit", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"ViewUnit"
      ,"DataUnitName"
      ,"Oid"
      });
    nameMap.put("IlisMeta07.ModelData.Unit", "Unit");
    mapping.defineClass("IlisMeta07.ModelData.Unit", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"Kind"
      ,"Definition"
      });
    nameMap.put("IlisMeta07.ModelTranslation.Translation", "Translation");
    mapping.defineClass("IlisMeta07.ModelTranslation.Translation", new String[]{   "Language"
      ,"Translations"
      });
    nameMap.put("IlisMeta07.ModelData.UniqueConstraint", "UniqueConstraint");
    mapping.defineClass("IlisMeta07.ModelData.UniqueConstraint", new String[]{   "Where"
      ,"Kind"
      ,"UniqueDef"
      });
    nameMap.put("IlisMeta07.ModelData.AssocAccTarget", "AssocAccTarget");
    mapping.defineClass("IlisMeta07.ModelData.AssocAccTarget", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.Expression", "Expression");
    mapping.defineClass("IlisMeta07.ModelData.Expression", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.NumUnit", "NumUnit");
    mapping.defineClass("IlisMeta07.ModelData.NumUnit", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.DerivedAssoc", "DerivedAssoc");
    mapping.defineClass("IlisMeta07.ModelData.DerivedAssoc", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.LocalFType", "LocalFType");
    mapping.defineClass("IlisMeta07.ModelData.LocalFType", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.ClassRestriction", "ClassRestriction");
    mapping.defineClass("IlisMeta07.ModelData.ClassRestriction", new String[]{   "CRTR"
      ,"ClassRestriction"
      });
    nameMap.put("IlisMeta07.ModelData.MetaObjectClass", "MetaObjectClass");
    mapping.defineClass("IlisMeta07.ModelData.MetaObjectClass", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.TextType", "TextType");
    mapping.defineClass("IlisMeta07.ModelData.TextType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Kind"
      ,"MaxLength"
      });
    nameMap.put("IlisMeta07.ModelData.LineCoord", "LineCoord");
    mapping.defineClass("IlisMeta07.ModelData.LineCoord", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.LineFormStructure", "LineFormStructure");
    mapping.defineClass("IlisMeta07.ModelData.LineFormStructure", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.AssocAcc", "AssocAcc");
    mapping.defineClass("IlisMeta07.ModelData.AssocAcc", new String[]{   "Class"
      ,"AssocAcc"
      });
    nameMap.put("IlisMeta07.ModelData.ARefOf", "ARefOf");
    mapping.defineClass("IlisMeta07.ModelData.ARefOf", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.RenamedBaseView", "RenamedBaseView");
    mapping.defineClass("IlisMeta07.ModelData.RenamedBaseView", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"OrNull"
      ,"BaseView"
      ,"View"
      });
    nameMap.put("IlisMeta07.ModelData.Ili1TransferElement", "Ili1TransferElement");
    mapping.defineClass("IlisMeta07.ModelData.Ili1TransferElement", new String[]{   "Ili1TransferClass"
      ,"Ili1RefAttr"
      });
    nameMap.put("IlisMeta07.ModelData.ArgumentType", "ArgumentType");
    mapping.defineClass("IlisMeta07.ModelData.ArgumentType", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.BaseViewDef", "BaseViewDef");
    mapping.defineClass("IlisMeta07.ModelData.BaseViewDef", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.MultiValue", "MultiValue");
    mapping.defineClass("IlisMeta07.ModelData.MultiValue", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"BaseType"
      ,"Ordered"
      ,"Multiplicity"
      });
    nameMap.put("IlisMeta07.ModelData.GraphicRule", "GraphicRule");
    mapping.defineClass("IlisMeta07.ModelData.GraphicRule", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.FunctionCall", "FunctionCall");
    mapping.defineClass("IlisMeta07.ModelData.FunctionCall", new String[]{   "Function"
      ,"Arguments"
      });
    nameMap.put("IlisMeta07.ModelData.UnitFunction", "UnitFunction");
    mapping.defineClass("IlisMeta07.ModelData.UnitFunction", new String[]{   "Explanation"
      });
    nameMap.put("IlisMeta07.ModelData.TypeRelatedType", "TypeRelatedType");
    mapping.defineClass("IlisMeta07.ModelData.TypeRelatedType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"BaseType"
      });
    nameMap.put("IlisMeta07.ModelData.ExplicitAssocAccess", "ExplicitAssocAccess");
    mapping.defineClass("IlisMeta07.ModelData.ExplicitAssocAccess", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"AssocAccOf"
      ,"OriginRole"
      ,"TargetRole"
      });
    nameMap.put("IlisMeta07.ModelData.RuntimeParamRef", "RuntimeParamRef");
    mapping.defineClass("IlisMeta07.ModelData.RuntimeParamRef", new String[]{   "RuntimeParam"
      });
    nameMap.put("IlisMeta07.ModelData.AllowedInBasket", "AllowedInBasket");
    mapping.defineClass("IlisMeta07.ModelData.AllowedInBasket", new String[]{   "OfDataUnit"
      ,"ClassInBasket"
      });
    nameMap.put("IlisMeta07.ModelData.MetaBasketDef", "MetaBasketDef");
    mapping.defineClass("IlisMeta07.ModelData.MetaBasketDef", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"Kind"
      ,"MetaDataTopic"
      });
    nameMap.put("IlisMeta07.ModelData.EnumMapping", "EnumMapping");
    mapping.defineClass("IlisMeta07.ModelData.EnumMapping", new String[]{   "EnumValue"
      ,"Cases"
      });
    nameMap.put("IlisMeta07.ModelData.SimpleConstraint", "SimpleConstraint");
    mapping.defineClass("IlisMeta07.ModelData.SimpleConstraint", new String[]{   "Kind"
      ,"Percentage"
      ,"LogicalExpression"
      });
    nameMap.put("IlisMeta07.ModelData.MetaElement", "MetaElement");
    mapping.defineClass("IlisMeta07.ModelData.MetaElement", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      });
    nameMap.put("IlisMeta07.ModelData.NumType", "NumType");
    mapping.defineClass("IlisMeta07.ModelData.NumType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Min"
      ,"Max"
      ,"Circular"
      ,"Clockwise"
      ,"RefSys"
      ,"Unit"
      });
    nameMap.put("IlisMeta07.ModelData.Dependency", "Dependency");
    mapping.defineClass("IlisMeta07.ModelData.Dependency", new String[]{   "Using"
      ,"Dependent"
      });
    nameMap.put("IlisMeta07.ModelData.EnumAssignment", "EnumAssignment");
    mapping.defineClass("IlisMeta07.ModelData.EnumAssignment", new String[]{   "ValueToAssign"
      ,"MinEnumValue"
      ,"MaxEnumValue"
      });
    nameMap.put("IlisMeta07.ModelData.EnumNode", "EnumNode");
    mapping.defineClass("IlisMeta07.ModelData.EnumNode", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"EnumType"
      ,"ParentNode"
      });
    nameMap.put("IlisMeta07.ModelData.TypeRestriction", "TypeRestriction");
    mapping.defineClass("IlisMeta07.ModelData.TypeRestriction", new String[]{   "TRTR"
      ,"TypeRestriction"
      });
    nameMap.put("IlisMeta07.ModelData.MetaObjectDef", "MetaObjectDef");
    mapping.defineClass("IlisMeta07.ModelData.MetaObjectDef", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"IsRefSystem"
      ,"Class"
      ,"MetaBasketDef"
      });
    nameMap.put("IlisMeta07.ModelData.NumsRefSys", "NumsRefSys");
    mapping.defineClass("IlisMeta07.ModelData.NumsRefSys", new String[]{   "Axis"
      });
    nameMap.put("IlisMeta07.ModelData.ClassParam", "ClassParam");
    mapping.defineClass("IlisMeta07.ModelData.ClassParam", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.View", "View");
    mapping.defineClass("IlisMeta07.ModelData.View", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Kind"
      ,"Multiplicity"
      ,"Constraints"
      ,"EmbeddedRoleTransfer"
      ,"ili1OptionalTable"
      ,"Oid"
      ,"View"
      ,"FormationKind"
      ,"FormationParameter"
      ,"Where"
      ,"Transient"
      });
    nameMap.put("IlisMeta07.ModelData.BaseType", "BaseType");
    mapping.defineClass("IlisMeta07.ModelData.BaseType", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.ClassRefType", "ClassRefType");
    mapping.defineClass("IlisMeta07.ModelData.ClassRefType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      });
    nameMap.put("IlisMeta07.ModelData.MetaBasketMembers", "MetaBasketMembers");
    mapping.defineClass("IlisMeta07.ModelData.MetaBasketMembers", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.SetConstraint", "SetConstraint");
    mapping.defineClass("IlisMeta07.ModelData.SetConstraint", new String[]{   "Where"
      ,"Constraint"
      });
    nameMap.put("IlisMeta07.ModelData.AttrOrParam", "AttrOrParam");
    mapping.defineClass("IlisMeta07.ModelData.AttrOrParam", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"SubdivisionKind"
      ,"Transient"
      ,"Derivates"
      ,"AttrParent"
      ,"ParamParent"
      ,"Type"
      });
    nameMap.put("IlisMeta07.ModelData.SubModel", "SubModel");
    mapping.defineClass("IlisMeta07.ModelData.SubModel", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      });
    nameMap.put("IlisMeta07.ModelData.CompoundExpr", "CompoundExpr");
    mapping.defineClass("IlisMeta07.ModelData.CompoundExpr", new String[]{   "Operation"
      ,"SubExpressions"
      });
    nameMap.put("IlisMeta07.ModelData.TreeValueTypeOf", "TreeValueTypeOf");
    mapping.defineClass("IlisMeta07.ModelData.TreeValueTypeOf", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.ClassAttr", "ClassAttr");
    mapping.defineClass("IlisMeta07.ModelData.ClassAttr", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.LineForm", "LineForm");
    mapping.defineClass("IlisMeta07.ModelData.LineForm", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Structure"
      });
    nameMap.put("IlisMeta07.ModelData.StructOfFormat", "StructOfFormat");
    mapping.defineClass("IlisMeta07.ModelData.StructOfFormat", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.Factor", "Factor");
    mapping.defineClass("IlisMeta07.ModelData.Factor", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.BaseViewRef", "BaseViewRef");
    mapping.defineClass("IlisMeta07.ModelData.BaseViewRef", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.Type", "Type");
    mapping.defineClass("IlisMeta07.ModelData.Type", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      });
    nameMap.put("IlisMeta07.ModelData.ExistenceDef", "ExistenceDef");
    mapping.defineClass("IlisMeta07.ModelData.ExistenceDef", new String[]{   "PathEls"
      ,"Inspection"
      ,"Viewable"
      });
    nameMap.put("IlisMeta07.ModelData.ExtendableME", "ExtendableME");
    mapping.defineClass("IlisMeta07.ModelData.ExtendableME", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      });
    nameMap.put("IlisMeta07.ModelData.BlackboxType", "BlackboxType");
    mapping.defineClass("IlisMeta07.ModelData.BlackboxType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Kind"
      });
    nameMap.put("IlisMeta07.ModelData.FormalArgument", "FormalArgument");
    mapping.defineClass("IlisMeta07.ModelData.FormalArgument", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.UnitRef", "UnitRef");
    mapping.defineClass("IlisMeta07.ModelData.UnitRef", new String[]{   "Unit"
      });
    nameMap.put("IlisMeta07.ModelData.AttributeConst", "AttributeConst");
    mapping.defineClass("IlisMeta07.ModelData.AttributeConst", new String[]{   "Attribute"
      });
    nameMap.put("IlisMeta07.ModelData.GraphicBase", "GraphicBase");
    mapping.defineClass("IlisMeta07.ModelData.GraphicBase", new String[]{  });
    nameMap.put("IlisMeta07.ModelTranslation.METranslation", "METranslation");
    mapping.defineClass("IlisMeta07.ModelTranslation.METranslation", new String[]{   "Of"
      ,"TranslatedName"
      ,"TranslatedDoc"
      });
    nameMap.put("IlisMeta07.ModelData.Inheritance", "Inheritance");
    mapping.defineClass("IlisMeta07.ModelData.Inheritance", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.ResultType", "ResultType");
    mapping.defineClass("IlisMeta07.ModelData.ResultType", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.Ili1Format", "Ili1Format");
    mapping.defineClass("IlisMeta07.ModelData.Ili1Format", new String[]{   "isFree"
      ,"LineSize"
      ,"tidSize"
      ,"blankCode"
      ,"undefinedCode"
      ,"continueCode"
      ,"Font"
      ,"tidKind"
      ,"tidExplanation"
      });
    nameMap.put("IlisMeta07.ModelData.LinesForm", "LinesForm");
    mapping.defineClass("IlisMeta07.ModelData.LinesForm", new String[]{   "LineType"
      ,"LineForm"
      });
    nameMap.put("IlisMeta07.ModelData.FormattedType", "FormattedType");
    mapping.defineClass("IlisMeta07.ModelData.FormattedType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Min"
      ,"Max"
      ,"Circular"
      ,"Clockwise"
      ,"RefSys"
      ,"Unit"
      ,"Format"
      ,"Struct"
      });
    nameMap.put("IlisMeta07.ModelData.LineType", "LineType");
    mapping.defineClass("IlisMeta07.ModelData.LineType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Kind"
      ,"MaxOverlap"
      ,"CoordType"
      ,"LAStructure"
      });
    nameMap.put("IlisMeta07.ModelData.BooleanType", "BooleanType");
    mapping.defineClass("IlisMeta07.ModelData.BooleanType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      });
    nameMap.put("IlisMeta07.ModelData.EnumType", "EnumType");
    mapping.defineClass("IlisMeta07.ModelData.EnumType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Order"
      });
    nameMap.put("IlisMeta07.ModelData.Import", "Import");
    mapping.defineClass("IlisMeta07.ModelData.Import", new String[]{   "ImportingP"
      ,"ImportedP"
      });
    nameMap.put("IlisMeta07.ModelData.AxisSpec", "AxisSpec");
    mapping.defineClass("IlisMeta07.ModelData.AxisSpec", new String[]{   "CoordType"
      ,"Axis"
      });
    nameMap.put("IlisMeta07.ModelData.PathOrInspFactor", "PathOrInspFactor");
    mapping.defineClass("IlisMeta07.ModelData.PathOrInspFactor", new String[]{   "PathEls"
      ,"Inspection"
      });
    nameMap.put("IlisMeta07.ModelData.PathEl", "PathEl");
    mapping.defineClass("IlisMeta07.ModelData.PathEl", new String[]{   "Kind"
      ,"Ref"
      ,"NumIndex"
      ,"SpecIndex"
      });
    nameMap.put("IlisMeta07.ModelData.Argument", "Argument");
    mapping.defineClass("IlisMeta07.ModelData.Argument", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Kind"
      ,"Function"
      ,"Type"
      });
    nameMap.put("IlisMeta07.ModelData.Class", "Class");
    mapping.defineClass("IlisMeta07.ModelData.Class", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Kind"
      ,"Multiplicity"
      ,"Constraints"
      ,"EmbeddedRoleTransfer"
      ,"ili1OptionalTable"
      ,"Oid"
      ,"View"
      });
    nameMap.put("IlisMeta07.ModelData.Multiplicity", "Multiplicity");
    mapping.defineClass("IlisMeta07.ModelData.Multiplicity", new String[]{   "Min"
      ,"Max"
      });
    nameMap.put("IlisMeta07.ModelTranslation.DocTextTranslation", "DocTextTranslation");
    mapping.defineClass("IlisMeta07.ModelTranslation.DocTextTranslation", new String[]{   "Text"
      });
    nameMap.put("IlisMeta07.ModelData.CondSignParamAssignment", "CondSignParamAssignment");
    mapping.defineClass("IlisMeta07.ModelData.CondSignParamAssignment", new String[]{   "Where"
      ,"Assignments"
      });
    nameMap.put("IlisMeta07.ModelData.UnaryExpr", "UnaryExpr");
    mapping.defineClass("IlisMeta07.ModelData.UnaryExpr", new String[]{   "Operation"
      ,"SubExpression"
      });
    nameMap.put("IlisMeta07.ModelData.PackageElements", "PackageElements");
    mapping.defineClass("IlisMeta07.ModelData.PackageElements", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.SignParamAssignment", "SignParamAssignment");
    mapping.defineClass("IlisMeta07.ModelData.SignParamAssignment", new String[]{   "Param"
      ,"Assignment"
      });
    nameMap.put("IlisMeta07.ModelData.ReferenceType", "ReferenceType");
    mapping.defineClass("IlisMeta07.ModelData.ReferenceType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"External"
      });
    nameMap.put("IlisMeta07.ModelData.LocalType", "LocalType");
    mapping.defineClass("IlisMeta07.ModelData.LocalType", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.Constraint", "Constraint");
    mapping.defineClass("IlisMeta07.ModelData.Constraint", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.ExplicitAssocAcc", "ExplicitAssocAcc");
    mapping.defineClass("IlisMeta07.ModelData.ExplicitAssocAcc", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.EnumTreeValueType", "EnumTreeValueType");
    mapping.defineClass("IlisMeta07.ModelData.EnumTreeValueType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"ET"
      });
    nameMap.put("IlisMeta07.ModelData.Graphic", "Graphic");
    mapping.defineClass("IlisMeta07.ModelData.Graphic", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"Where"
      ,"Base"
      });
    nameMap.put("IlisMeta07.ModelData.SignClass", "SignClass");
    mapping.defineClass("IlisMeta07.ModelData.SignClass", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.AssocRole", "AssocRole");
    mapping.defineClass("IlisMeta07.ModelData.AssocRole", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.TopNode", "TopNode");
    mapping.defineClass("IlisMeta07.ModelData.TopNode", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.DrawingRule", "DrawingRule");
    mapping.defineClass("IlisMeta07.ModelData.DrawingRule", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"Rule"
      ,"Class"
      ,"Graphic"
      });
    nameMap.put("IlisMeta07.ModelData.BasketOID", "ModelData.BasketOID");
    mapping.defineClass("IlisMeta07.ModelData.BasketOID", new String[]{  });
    nameMap.put("IlisMeta07.ModelData.FunctionDef", "FunctionDef");
    mapping.defineClass("IlisMeta07.ModelData.FunctionDef", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Explanation"
      ,"ResultType"
      });
    nameMap.put("IlisMeta07.ModelData.ARefRestriction", "ARefRestriction");
    mapping.defineClass("IlisMeta07.ModelData.ARefRestriction", new String[]{   "ARef"
      ,"Type"
      });
    nameMap.put("IlisMeta07.ModelData.Model", "Model");
    mapping.defineClass("IlisMeta07.ModelData.Model", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"iliVersion"
      ,"Contracted"
      ,"Kind"
      ,"Language"
      ,"At"
      ,"Version"
      ,"ili1Transfername"
      ,"ili1Format"
      });
    nameMap.put("IlisMeta07.ModelData.ExistenceConstraint", "ExistenceConstraint");
    mapping.defineClass("IlisMeta07.ModelData.ExistenceConstraint", new String[]{   "Attr"
      ,"ExistsIn"
      });
    nameMap.put("IlisMeta07.ModelData.DomainType", "DomainType");
    mapping.defineClass("IlisMeta07.ModelData.DomainType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      });
    nameMap.put("IlisMeta07.ModelData.AnyOIDType", "AnyOIDType");
    mapping.defineClass("IlisMeta07.ModelData.AnyOIDType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      });
    nameMap.put("IlisMeta07.ModelData.ClassConst", "ClassConst");
    mapping.defineClass("IlisMeta07.ModelData.ClassConst", new String[]{   "Class"
      });
    nameMap.put("IlisMeta07.ModelData.AttributeRefType", "AttributeRefType");
    mapping.defineClass("IlisMeta07.ModelData.AttributeRefType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Of"
      });
    nameMap.put("IlisMeta07.ModelData.ObjectType", "ObjectType");
    mapping.defineClass("IlisMeta07.ModelData.ObjectType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"Multiple"
      });
    nameMap.put("IlisMeta07.ModelData.CoordType", "CoordType");
    mapping.defineClass("IlisMeta07.ModelData.CoordType", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      ,"Mandatory"
      ,"NullAxis"
      ,"PiHalfAxis"
      });
    mapping.setXtf24nameMapping(nameMap);
    return mapping;
  }
}
