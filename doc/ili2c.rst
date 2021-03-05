======================
ili2c-Anleitung
======================

Überblick
=========

ili2c ist der INTERLIS-Compiler und ein in Java erstelltes Programm, das eine
Interlis-Modelldatei (ili) überprüft (ob das Modell der 
INTERLIS-Sprache entspricht).

Zusätzlich umfasst ili2c Hilfsfunktionen betrf. 
Modellen (z.B. zum Modell passendes XML-Schema herleiten).

Log-Meldungen
-------------
Die Log-Meldungen sollen dem Benutzer zeigen, was das Programm macht.
Am Anfang erscheinen Angaben zur Programm-Version.
Falls das Programm ohne Fehler durchläuft, wird das am Ende ausgegeben.::
	
  Info: ili2c-1.0.0
  ...
  Info: lookup model <CoordSys>
  ...
  Info: ...compiler run done

Bei einem Fehler wird das am Ende des Programms vermerkt. Der eigentliche 
Fehler wird aber in der Regel schon früher ausgegeben.::
	
  Info: ili2c-1.0.0
  ...
  Info: lookup model <CoordSys>
  ...
  Error: ASSOCIATION should end with "END"
  ...
  Error: ...compiler run failed

Laufzeitanforderungen
---------------------

Das Programm setzt Java 1.6 voraus.

Lizenz
------

GNU Lesser General Public License

Funktionsweise
==============

In den folgenden Abschnitten wird die Funktionsweise anhand einzelner
Anwendungsfälle beispielhaft beschrieben. Die detaillierte Beschreibung
einzelner Funktionen ist im Kapitel „Referenz“ zu finden.

Je nach Betriebssystem kann das Programm auch einfach durch Doppelklick mit linker Maustaste 
auf  ```ili2c.jar``` gestartet werden.

Beispiele
---------

Fall 1
~~~~~~

Es wird eine INTERLIS 1-Datei validiert/geprüft.

``java -jar ili2c.jar path/to/model.ili``

Fall 2
~~~~~~

Es wird eine INTERLIS 2-Datei validiert/geprüft.

``java -jar ili2c.jar path/to/model.ili``

Der Compiler stellt selber fest, in welcher Sprachversion das Modell ist.

Fall 3
~~~~~~

Es wird eine INTERLIS 2-Datei validiert/geprüft, wobei die Fehlermeldungen 
in eine Text-Datei geschrieben werden.

``java -jar ili2c.jar --log result.log path/to/model.ili``

Die Fehlermeldungen werden in die Datei result.log geschrieben.


Fall 4
~~~~~~

Es erscheint eine Bildschirmmaske, mit deren Hilfe die zu validierende Datei 
ausgewählt und die Validierung gestartet werden kann.

``java -jar ili2c.jar``


Referenz
========

In den folgenden Abschnitten werden einzelne Aspekte detailliert, aber
isoliert, beschrieben. Die Funktionsweise als Ganzes wird anhand
einzelner Anwendungsfälle beispielhaft im Kapitel „Funktionsweise“
(weiter oben) beschrieben.

Aufruf-Syntax
-------------

``java -jar ili2c.jar [Options] [file]``

Ohne Kommandozeilenargumente erscheint die Bildschirmmaske, mit deren Hilfe die zu validierende Datei 
ausgewählt und die Validierung gestartet werden kann.

Der Rückgabewert ist wie folgt:

  - 0 Validierung ok, keine Fehler festgestellt
  - !0 Validierung nicht ok, Fehler festgestellt

Optionen:

+---------------------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| Option                                      | Beschreibung                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           |
+=============================================+========================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================================+
| ``--modeldir path``                         | Dateipfade, die Modell-Dateien (ili-Dateien) enthalten. Mehrere Pfade können durch Semikolon ‚;‘ getrennt werden. Es sind auch URLs von Modell-Repositories möglich. Default ist                                                                                                                                                                                                                                                                                                                                                       |
|                                             |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
|                                             | %ILI\_DIR;http://models.interlis.ch/;%JAR\_DIR                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
|                                             |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
|                                             | %ILI\_DIR ist ein Platzhalter für das Verzeichnis mit der ili-Datei der Kommandozeile (Hauptmodell).                                                                                                                                                                                                                                                                                                                                                                                                                                   |
|                                             |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
|                                             | %JAR\_DIR ist ein Platzhalter für das Verzeichnis des ili2c Programms (ili2c.jar Datei).                                                                                                                                                                                                                                                                                                                                                                                                                                               |
|                                             |                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        |
|                                             | Beim Auflösen eines IMPORTs wird die INTERLIS Sprachversion des Hauptmodells berücksichtigt, so dass also z.B. das Modell Units für ili2.2 oder ili2.3 unterschieden wird.                                                                                                                                                                                                                                                                                                                                                             |
+---------------------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| ``--log filename``                          | Schreibt die log-Meldungen in eine Text-Datei.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         |
+---------------------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| ``--proxy host``                            | Proxy Server für den Zugriff auf Modell Repositories                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
+---------------------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| ``--proxyPort port``                        | Proxy Port für den Zugriff auf Modell Repositories                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
+---------------------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| ``--trace``                                 | Erzeugt zusätzliche Log-Meldungen (wichtig für Programm-Fehleranalysen)                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
+---------------------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| ``--help``                                  | Zeigt einen kurzen Hilfetext an.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
+---------------------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+
| ``--version``                               | Zeigt die Version des Programmes an.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                   |
+---------------------------------------------+----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------+


INTERLIS-Metaattribute
~~~~~~~~~~~~~~~~~~~~~~
Einzelne Funktionen des Compiler nutzen Meta-Attribute. 
Metaattribute stehen unmittelbar vor dem Modellelement das sie betreffen und beginnen mit ``!!@``.
Falls der Wert (rechts von ```=```) aus mehreren durch Leerstellen getrennten Wörtern besteht, muss er mit Gänsefüsschen eingerahmt werden (```"..."```).

+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| Modelelement     | Metaattribut                                | Beschreibung                                                                      |
+==================+=============================================+===================================================================================+
| ConstraintDef    | ::                                          | Name des Constraints (ili2.3 oder bei ili2.4 falls constraint kein name hat).     |
|                  |                                             | Wenn ein ConstraintDef keinen expliziten Namen hat, wird für der Name             |
|                  |                                             | aus der interne Id des Constraints erzeugt. Die                                   |
|                  |                                             | interne Id ist eine aufsteigende Zahl und beginnt pro Klasse mit 1. Das           |
|                  |                                             | erste Constraint einer Klasse heisst also ``Constraint1``,                        |
|                  |                                             | das Zweite ``Constraint2`` usw.                                                   |
|                  |  name                                       |                                                                                   |
|                  |                                             | ::                                                                                |
|                  |                                             |                                                                                   |
|                  |                                             |   !!@ name = c1023                                                                |
|                  |                                             |                                                                                   |
|                  |                                             |                                                                                   |
|                  |                                             |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| AttributeDef,    | ::                                          | Der EPSG Code des Geometrieattributs (bzw. des Wertebereichs)                     |
| DomainDef        |                                             |                                                                                   |
|                  |  CRS                                        | ::                                                                                |
|                  |                                             |                                                                                   |
|                  |                                             |   !!@CRS=EPSG:2056                                                                |
|                  |                                             |   Coord2_LV95 = COORD                                                             |
|                  |                                             |      2460000.000 .. 2870000.000,                                                  |
|                  |                                             |      1045000.000 .. 1310000.000;                                                  |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| ModelDef         | ::                                          |  Wert für das Attribut technicalContact im ilimodels.xml Eintrag des Modells.     |
|                  |                                             |                                                                                   |
|                  |  technicalContact                           |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| ModelDef         | ::                                          | GeoIV-Identifikator dieses Modells. Mehrere Einträge durch Komma getrennt.        |
|                  |                                             | Beispiel:                                                                         |
|                  |  IDGeoIV                                    |                                                                                   |
|                  |                                             | ::                                                                                |
|                  |                                             |                                                                                   |
|                  |                                             |   !!@ IDGeoIV="114.1, 114.3"                                                      |
|                  |                                             |                                                                                   |
|                  |                                             | Der Wert wird auch für das Attribut tags im ilimodels.xml Eintrag des Modells     |
|                  |                                             | verwendet.                                                                        |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| ModelDef         | ::                                          |  Wert für das Attribut furtherInformation im ilimodels.xml Eintrag des Modells.   |
|                  |                                             |                                                                                   |
|                  |  furtherInformation                         |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| ModelDef         | ::                                          |  Wert für das Attribut tags im ilimodels.xml Eintrag des Modells.                 |
|                  |                                             |                                                                                   |
|                  |  tags                                       |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| ModelDef         | ::                                          |  Wert für das Attribut precursorVersion im ilimodels.xml Eintrag des Modells.     |
|                  |                                             |                                                                                   |
|                  |  precursorVersion                           |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| ModelDef         | ::                                          |  Wert für das Attribut furtherMetadata im ilimodels.xml Eintrag des Modells.      |
|                  |                                             |                                                                                   |
|                  |  furtherMetadata                            |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| ModelDef         | ::                                          |  Wert für das Attribut Original im ilimodels.xml Eintrag des Modells.             |
|                  |                                             |                                                                                   |
|                  |  Original                                   |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| ModelDef         | ::                                          |  Nur INTERLIS 1: Name des Modells in der Ursprungssprache.                        |
|                  |                                             |  Wie TRANSLATION OF in INTERLIS 2.                                                |
|                  |  ili2c.translationOf                        |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| ModelDef         | ::                                          | Definiert den minimal zu unterstützenden Zeichenumfang.                           |
|                  |                                             | Das ist aber keine Angabe zur Zeichenkodierung.                                   |
|                  |  ili2c.textMinimalCharset                   | Die Zeichenkodierung ergibt sich aufgrund des Transferformates,                   |
|                  |                                             | d.h. für XML muss mindestens UTF-8 unterstützt werden.                            |
|                  |                                             |                                                                                   |
|                  |                                             | Gilt für: alle TextType innerhalb des Modells                                     |
|                  |                                             |                                                                                   |
|                  |                                             | Mögliche Werte: ili23AnnexB oder Namen                                            |
|                  |                                             | gem. http://www.iana.org/assignments/character-sets z.B. windows-1252             |
|                  |                                             | oder ISO-8859-15                                                                  |
|                  |                                             |                                                                                   |
|                  |                                             | Default: ili23AnnexB                                                              |
|                  |                                             |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| ModelDef         | ::                                          | Definiert, ob im generierten XML-Schema                                           |
|                  |                                             | alle Typen aus dem Modell INTERLIS generiert werden,                              |
|                  |                                             | oder nur die für diese Modelle effektiv benötigten.                               |
|                  |  ili2c.ili23xsd.addAllInterlisTypesDefault  | Nur die Einstellung des "letzten" Modells (von keinem anderen abhängig)           |
|                  |                                             | wird ausgewertet.                                                                 |
|                  |                                             |                                                                                   |
|                  |                                             | Mögliche Werte: true oder false                                                   |
|                  |                                             |                                                                                   |
|                  |                                             | Default: false                                                                    |
|                  |                                             |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| ModelDef         | ::                                          | Definiert, ob im generierten XML-Schema,                                          |
|                  |                                             | das ALIAS Sub-Element in der HEADERSECTION zulässig ist, oder nicht.              |
|                  |  ili2c.ili23xsd.addAliasTableDefault        | Nur die Einstellung des "letzten" Modells (von keinem anderen abhängig)           |
|                  |                                             | wird ausgewertet.                                                                 |
|                  |                                             |                                                                                   |
|                  |                                             | Mögliche Werte: true oder false                                                   |
|                  |                                             |                                                                                   |
|                  |                                             | Default: false                                                                    |
|                  |                                             |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| TopicDef,        | ::                                          | Definiert, ob polymorpher Transfer (bei mehrsprachigen Modelle oder               |
| ModelDef         |                                             | erweiterten Topics) zulässig ist, oder nicht.                                     |
|                  |  ili2c.ili23xml.supportPolymorphicRead      |                                                                                   |
|                  |                                             | Wenn das Metaattribt beim ModelDef steht, gilt es für alle darin enthaltenen      |
|                  |                                             | TopicDef.                                                                         |
|                  |                                             |                                                                                   |
|                  |                                             | Mögliche Werte: true oder false                                                   |
|                  |                                             |                                                                                   |
|                  |                                             | Default: false                                                                    |
|                  |                                             |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| TopicDef,        | ::                                          | Definiert, ob inkonsistenter Transfer (z.B. ein Ausschnitt) zulässig ist,         |
| ModelDef         |                                             | oder nicht.                                                                       |
|                  |  ili2c.ili23xml.supportInconsistentTransfer |                                                                                   |
|                  |                                             | Wenn das Metaattribt beim ModelDef steht, gilt es für alle darin enthaltenen      |
|                  |                                             | TopicDef.                                                                         |
|                  |                                             |                                                                                   |
|                  |                                             | Mögliche Werte: true oder false                                                   |
|                  |                                             |                                                                                   |
|                  |                                             | Default: false                                                                    |
|                  |                                             |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| TopicDef,        | ::                                          | Definiert, ob inkrementeller Transfer zulässig ist, oder nicht.                   |
| ModelDef         |                                             |                                                                                   |
|                  |  ili2c.ili23xml.supportIncrementalTransfer  |                                                                                   |
|                  |                                             | Wenn das Metaattribt beim ModelDef steht, gilt es für alle darin enthaltenen      |
|                  |                                             | TopicDef.                                                                         |
|                  |                                             |                                                                                   |
|                  |                                             | Mögliche Werte: true oder false                                                   |
|                  |                                             |                                                                                   |
|                  |                                             | Default: false                                                                    |
|                  |                                             |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+
| TopicDef,        | ::                                          | Definiert, ob das BID XML-Attribut bei den Objekten im Transfer zulässig          |
| ModelDef         |                                             | ist, oder nicht.                                                                  |
|                  |  ili2c.ili23xml.supportSourceBasketId       | Das BID XML-Attribut bei den Behältern ist unabhängig davon immer vorhanden.      |
|                  |                                             | Wenn das Metaattribt beim ModelDef steht, gilt es für alle darin enthaltenen      |
|                  |                                             | TopicDef.                                                                         |
|                  |                                             |                                                                                   |
|                  |                                             | Mögliche Werte: true oder false                                                   |
|                  |                                             |                                                                                   |
|                  |                                             | Default: false                                                                    |
|                  |                                             |                                                                                   |
+------------------+---------------------------------------------+-----------------------------------------------------------------------------------+

Ein Modell kann beliebige weitere Metaattribute enthalten; diese werden 
durch ili2c gelesen, und je nach Ausgabeformat wieder ausgegeben (z.B. bei -oIMD16).

In eCH-0118 werden weitere Meta-Attribute spezifiziert. Diese werden bei der Verwendung von -oILIGML2 durch ili2c benutzt.