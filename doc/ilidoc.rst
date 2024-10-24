======================
ilidoc-Beschreibung
======================

Überblick
=========

Diese Spezifikation beschreibt Regeln für eine maschinenlesbare Modell-Dokumentation 
als Teil eines INTERLIS-Datenmodells. 
Mit den Regeln, die in der vorliegenden Spezifikation definiert werden, 
können Dokumentationstexte in ein INTERLIS-Datenmodell integriert werden, 
ohne dass dafür die INTERLIS-Sprachspezifikation [1]_ angepasst werden muss.

Die Syntaxregeln für ilidoc werden in das Regelwerk von INTERLIS eingepasst. 
Insbesondere wird definiert, an welchen Stellen in der bestehenden 
INTERLIS-Spezifikation [1]_ ilidoc-Texte definiert werden können.

Vorteile
--------
Die Definition von Dokumentation direkt im Datenmodell hat den Vorteil, 
dass wesentliche Zusatzinformationen
unmittelbar bei denjenigen INTERLIS-Konstrukten erfasst werden können, 
die sie beschreiben sollen. 

Die vorliegende Spezifikation ist so konzipiert, dass ilidoc 
ohne Änderung der INTERLIS-Spezifikation [1]_ angewendet werden kann. 
ilidoc und die 
INTERLIS Sprache können sich somit unabhängig voneinander weiterentwickeln.

Im Unterschied zu normalem Blockkommentar, der ausschliesslich 
informativen Charakter hat, kann ilidoc gemäss den folgenden 
Syntaxregeln von entsprechend befähigten 
Softwarewerkzeugen (z.B. ili2db) interpretiert und ggf. für die weitere Vearbeitung 
weitergegeben werden.

Konzept
=======
Um die bestehende Sprachdefinition nicht ändern zu müssen, 
werden die ilidoc-Texte innerhalb von INTERLIS-Blockkommentaren formuliert. 
Ein INTERLIS-Blockkommentar ist gemäss [1]_ wie folgt definiert: 

   Der Blockkommentar wird durch einen Schrägstrich und einen Stern eingeleitet; abgeschlossen wird er
   durch einen Stern und einen Schrägstrich. Er darf sich über mehrere Zeilen hinweg erstrecken und seinerseits
   Zeilenkommentare enthalten. Geschachtelte Blockkommentare sind zugelassen.

Um ilidoc-Texte von echtem Kommentar unterscheiden zu können, ist als drittes 
Zeichen zwingend das Zeichen ``*``, einzufügen. 
Das heisst, ein Blockkommentar, der ilidoc-Text enthält, beginnt mit ``/**``.

Zu welchem Meta-Element (gemäss INTERLIS 2-Metamodell [2]_) ein solcher ilidoc-Text 
gehört, ergibt sich aus der Position des Blockkommentars. 
Der ilidoc-Text bezieht sich auf das unmittelbar nachfolgende Sprachkonstrukt 
wie beispielsweise eine Modell-, Themen- oder Klassendefinition.


Syntax
=======

Ein ilidoc-Text wird durch einen Schrägstrich und zwei Sterne eingeleitet ``/**``; abgeschlossen wird er
durch einen Stern und einen Schrägstrich ``*/``. Er darf sich über mehrere Zeilen hinweg erstrecken::
   
    IliDoc = '/**' any character except */ '*/'.

Positionierung innerhalb der INTERLIS-Sprache
=============================================
In den folgenden Ausschnitten aus Syntaxregeln aus dem INTERLIS-Referenzhandbuch [1]_ 
wird definiert, an welcher Stelle ilidoc-Texte vorkommen dürfen. 
ilidoc-Texte an anderen Stellen werden als reguläre 
Blockkommentare aufgefasst und deshalb ignoriert.

Modell-Definition::

   ModelDef = { IliDoc } [ 'CONTRACTED' ]
       [ 'TYPE' | 'REFSYSTEM' | 'SYMBOLOGY' ] 'MODEL' Model-Name ...

Topic-Definition::
	
    TopicDef = { IliDoc } [ 'VIEW' ] 'TOPIC' Topic-Name ...

Klassen-Definition::
	
    ClassDef = { IliDoc } 'CLASS' Class-Name ...

Struktur-Definition::
	
    StructureDef = { IliDoc } 'STRUCTURE' Structure-Name ...

Attribut-Definition::
	
    AttributeDef = { IliDoc } [ [ 'CONTINUOUS' ] 'SUBDIVISION' ]
            Attribute-Name ...

Beziehung-Definition::
	
    AssociationDef = { IliDoc } 'ASSOCIATION' Association-Name ...

Rollen-Definition in Assoziationen::
	
    RoleDef = { IliDoc } Role-Name ...

Wertebereichs-Definition::
	
    DomainDef = 'DOMAIN' { { IliDoc } Domain-Name ...

Aufzähltyp-Element::

    EnumElement = { IliDoc } EnumElement-Name ...


Linienform-Definition::
	
   LineFormTypeDef = 'LINE' 'FORM' { { IliDoc } 
      LineFormType-Name ...

Einheiten-Definition::
	
   UnitDef = 'UNIT' { { IliDoc } Unit-Name ...

Definition eines Metaobjekt-Behälters::

   MetaDataBasketDef = { IliDoc } ( 'SIGN' | 'REFSYSTEM' )
     'BASKET' Basket-Name Properties <FINAL>
     [ 'EXTENDS' MetaDataBasketRef ] '~' TopicRef 
     { 'OBJECTS' 'OF' Class-Name ':' 
     { IliDoc } MetaObject-Name 
     { ',' { IliDoc } MetaObject-Name } } ';'.

Parameter-Definition::

    ParameterDef = { IliDoc } Parameter-Name ...

Laufzeitparameter-Definition::
	
    RunTimeParameterDef = 'PARAMETER' { { IliDoc } 
               RunTimeParameter-Name ...

Definition einer Konsistenzbedingung::
	
    ConstraintDef = { IliDoc } ( MandatoryConstraint | ...

Funktions-Definition::
	
    FunctionDef = { IliDoc } 'FUNCTION' Function-Name ...

Definition einer Sicht (View)::
	
    ViewDef = { IliDoc } 'VIEW' View-Name ...

Grafik-Definition::
	
    GraphicDef = { IliDoc } 'GRAPHIC' Graphic-Name ...



Beispiel
========
Folgendes Beispiel soll den Gebrauch von ilidoc-Text illustrieren::

	INTERLIS 2.3;
	
	/** Datenmodell der A.
	 */
	MODEL ModelA (de)
	AT "mailto:ceis@localhost"
	VERSION "2022-11-24"  =
	
	  /** Alle Objekte A zu einer Gemeinde.
	   */
	  TOPIC TopicA =
	
		/** Ein A.
		 */
		CLASS ClassA =
		  /** Für allgemeine Bemerkungen.
		   */
		  AttributeA : MTEXT;
		END ClassA;
	
	  END TopicA;
	
	END ModelA.

.. [1] INTERLIS 2-Referenzhandbuch

.. [2] INTERLIS 2-Metamodell