INTERLIS im jEdit
*****************

$JEDIT ist ein Platzhalter und entspricht dem jEdit Installationsverzeichnissen auf Ihrem System.

Syntax-Unterstützung
********************
Syntax-Unterstützung für INTERLIS ist in der Standard-Distribution von jEdit enthalten.

Kompilieren
***********
Es sind die jEdit-Plugins Console und ErrorList erforderlich. 
Diese beiden Plugins können via den jEdit-Menupunkt Plugins>Plugin Manager... installiert werden. 

- Das Verzeichnis $JEDIT/macros/INTERLIS erstellen
- Die Datei compile.bsh ins Verzeichnis $JEDIT/macros/INTERLIS kopieren
- In der Datei compile.bsh in der folgenden Zeile den Pfad auf ili2c.jar anpassen

String strCmd = new String("java -jar \"C:\\Programme\\INTERLIS\\ili2c\\ili2c.jar\" --without-warnings \"" + strPath + "\"");

Via den jEdit-Menupunkt Macros>INTERLIS>Compile kann nun die aktuelle INTERLIS-Modelldatei kompiliert werden.

