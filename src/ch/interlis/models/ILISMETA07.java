package ch.interlis.models;
public class ILISMETA07{
  private ILISMETA07() {}
  public final static String MODEL= "IlisMeta07";
  public final static String ModelData= "IlisMeta07.ModelData";
  public final static String ModelTranslation= "IlisMeta07.ModelTranslation";
  public static ch.interlis.iom_j.xtf.XtfModel getXtfModel(){ return new ch.interlis.iom_j.xtf.XtfModel("IlisMeta07","http://interlis.ch","2008-02-05"); }
  static public ch.interlis.iom_j.ViewableProperties getIoxMapping()
  {
    ch.interlis.iom_j.ViewableProperties mapping=new ch.interlis.iom_j.ViewableProperties();
    mapping.defineClass("IlisMeta07.ModelData.ObjectOID", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.BaseClass", new String[]{   "CRT"
      ,"BaseClass"
      });
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
    mapping.defineClass("IlisMeta07.ModelData.SubNode", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.MetaDataUnit", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.Package", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      });
    mapping.defineClass("IlisMeta07.ModelData.MetaAttributes", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.LineAttr", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.ActualArgument", new String[]{   "FormalArgument"
      ,"Kind"
      ,"Expression"
      ,"ObjectClasses"
      });
    mapping.defineClass("IlisMeta07.ModelData.AttrOrParamType", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.MetaAttribute", new String[]{   "Name"
      ,"Value"
      ,"MetaElement"
      });
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
    mapping.defineClass("IlisMeta07.ModelData.DocText", new String[]{   "Name"
      ,"Text"
      });
    mapping.defineClass("IlisMeta07.ModelData.TransferElement", new String[]{   "TransferClass"
      ,"TransferElement"
      });
    mapping.defineClass("IlisMeta07.ModelData.ClassRef", new String[]{   "Ref"
      });
    mapping.defineClass("IlisMeta07.ModelData.AssocAccOrigin", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.Constant", new String[]{   "Value"
      ,"Type"
      });
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
    mapping.defineClass("IlisMeta07.ModelData.Unit", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"Kind"
      ,"Definition"
      });
    mapping.defineClass("IlisMeta07.ModelTranslation.Translation", new String[]{   "Language"
      ,"Translations"
      });
    mapping.defineClass("IlisMeta07.ModelData.UniqueConstraint", new String[]{   "Where"
      ,"Kind"
      ,"UniqueDef"
      });
    mapping.defineClass("IlisMeta07.ModelData.AssocAccTarget", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.Expression", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.NumUnit", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.DerivedAssoc", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.LocalFType", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.ClassRestriction", new String[]{   "CRTR"
      ,"ClassRestriction"
      });
    mapping.defineClass("IlisMeta07.ModelData.MetaObjectClass", new String[]{  });
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
    mapping.defineClass("IlisMeta07.ModelData.LineCoord", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.LineFormStructure", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.AssocAcc", new String[]{   "Class"
      ,"AssocAcc"
      });
    mapping.defineClass("IlisMeta07.ModelData.ARefOf", new String[]{  });
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
    mapping.defineClass("IlisMeta07.ModelData.Ili1TransferElement", new String[]{   "Ili1TransferClass"
      ,"Ili1RefAttr"
      });
    mapping.defineClass("IlisMeta07.ModelData.ArgumentType", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.BaseViewDef", new String[]{  });
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
    mapping.defineClass("IlisMeta07.ModelData.GraphicRule", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.FunctionCall", new String[]{   "Function"
      ,"Arguments"
      });
    mapping.defineClass("IlisMeta07.ModelData.UnitFunction", new String[]{   "Explanation"
      });
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
    mapping.defineClass("IlisMeta07.ModelData.RuntimeParamRef", new String[]{   "RuntimeParam"
      });
    mapping.defineClass("IlisMeta07.ModelData.AllowedInBasket", new String[]{   "OfDataUnit"
      ,"ClassInBasket"
      });
    mapping.defineClass("IlisMeta07.ModelData.MetaBasketDef", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"Kind"
      ,"MetaDataTopic"
      });
    mapping.defineClass("IlisMeta07.ModelData.EnumMapping", new String[]{   "EnumValue"
      ,"Cases"
      });
    mapping.defineClass("IlisMeta07.ModelData.SimpleConstraint", new String[]{   "Kind"
      ,"Percentage"
      ,"LogicalExpression"
      });
    mapping.defineClass("IlisMeta07.ModelData.MetaElement", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      });
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
    mapping.defineClass("IlisMeta07.ModelData.Dependency", new String[]{   "Using"
      ,"Dependent"
      });
    mapping.defineClass("IlisMeta07.ModelData.EnumAssignment", new String[]{   "ValueToAssign"
      ,"MinEnumValue"
      ,"MaxEnumValue"
      });
    mapping.defineClass("IlisMeta07.ModelData.EnumNode", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"EnumType"
      ,"ParentNode"
      });
    mapping.defineClass("IlisMeta07.ModelData.TypeRestriction", new String[]{   "TRTR"
      ,"TypeRestriction"
      });
    mapping.defineClass("IlisMeta07.ModelData.MetaObjectDef", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"IsRefSystem"
      ,"Class"
      ,"MetaBasketDef"
      });
    mapping.defineClass("IlisMeta07.ModelData.NumsRefSys", new String[]{   "Axis"
      });
    mapping.defineClass("IlisMeta07.ModelData.ClassParam", new String[]{  });
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
    mapping.defineClass("IlisMeta07.ModelData.BaseType", new String[]{  });
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
    mapping.defineClass("IlisMeta07.ModelData.MetaBasketMembers", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.SetConstraint", new String[]{   "Where"
      ,"Constraint"
      });
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
    mapping.defineClass("IlisMeta07.ModelData.SubModel", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      });
    mapping.defineClass("IlisMeta07.ModelData.CompoundExpr", new String[]{   "Operation"
      ,"SubExpressions"
      });
    mapping.defineClass("IlisMeta07.ModelData.TreeValueTypeOf", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.ClassAttr", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.LineForm", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Structure"
      });
    mapping.defineClass("IlisMeta07.ModelData.StructOfFormat", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.Factor", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.BaseViewRef", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.Type", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"LFTParent"
      ,"LTParent"
      });
    mapping.defineClass("IlisMeta07.ModelData.ExistenceDef", new String[]{   "PathEls"
      ,"Inspection"
      ,"Viewable"
      });
    mapping.defineClass("IlisMeta07.ModelData.ExtendableME", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      });
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
    mapping.defineClass("IlisMeta07.ModelData.FormalArgument", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.UnitRef", new String[]{   "Unit"
      });
    mapping.defineClass("IlisMeta07.ModelData.GraphicBase", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelTranslation.METranslation", new String[]{   "Of"
      ,"TranslatedName"
      ,"TranslatedDoc"
      });
    mapping.defineClass("IlisMeta07.ModelData.Inheritance", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.ResultType", new String[]{  });
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
    mapping.defineClass("IlisMeta07.ModelData.LinesForm", new String[]{   "LineType"
      ,"LineForm"
      });
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
    mapping.defineClass("IlisMeta07.ModelData.Import", new String[]{   "ImportingP"
      ,"ImportedP"
      });
    mapping.defineClass("IlisMeta07.ModelData.AxisSpec", new String[]{   "CoordType"
      ,"Axis"
      });
    mapping.defineClass("IlisMeta07.ModelData.PathOrInspFactor", new String[]{   "PathEls"
      ,"Inspection"
      });
    mapping.defineClass("IlisMeta07.ModelData.PathEl", new String[]{   "Kind"
      ,"Ref"
      ,"NumIndex"
      ,"SpecIndex"
      });
    mapping.defineClass("IlisMeta07.ModelData.Argument", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Kind"
      ,"Function"
      ,"Type"
      });
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
    mapping.defineClass("IlisMeta07.ModelData.Multiplicity", new String[]{   "Min"
      ,"Max"
      });
    mapping.defineClass("IlisMeta07.ModelTranslation.DocTextTranslation", new String[]{   "Text"
      });
    mapping.defineClass("IlisMeta07.ModelData.CondSignParamAssignment", new String[]{   "Where"
      ,"Assignments"
      });
    mapping.defineClass("IlisMeta07.ModelData.UnaryExpr", new String[]{   "Operation"
      ,"SubExpression"
      });
    mapping.defineClass("IlisMeta07.ModelData.PackageElements", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.SignParamAssignment", new String[]{   "Param"
      ,"Assignment"
      });
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
    mapping.defineClass("IlisMeta07.ModelData.LocalType", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.Constraint", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.ExplicitAssocAcc", new String[]{  });
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
    mapping.defineClass("IlisMeta07.ModelData.Graphic", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Abstract"
      ,"Final"
      ,"Super"
      ,"Where"
      ,"Base"
      });
    mapping.defineClass("IlisMeta07.ModelData.SignClass", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.AssocRole", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.TopNode", new String[]{  });
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
    mapping.defineClass("IlisMeta07.ModelData.BasketOID", new String[]{  });
    mapping.defineClass("IlisMeta07.ModelData.FunctionDef", new String[]{   "Name"
      ,"Documentation"
      ,"ElementInPackage"
      ,"Explanation"
      ,"ResultType"
      });
    mapping.defineClass("IlisMeta07.ModelData.ARefRestriction", new String[]{   "ARef"
      ,"Type"
      });
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
    mapping.defineClass("IlisMeta07.ModelData.ExistenceConstraint", new String[]{   "ExistsIn"
      });
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
    return mapping;
  }
}
