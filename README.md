# ili2c - The INTERLIS Compiler
Checks the syntactical correctness of an [INTERLIS](https://www.interlis.ch/content/index.php?language=e "Interlis - The tool to describe, integrate and coordinate geodata.") data model.

## License
ili2c is licensed under the [LGPL](https://www.gnu.org/licenses/lgpl.txt) (Lesser GNU Public License).

## System Configuration
For the current version of ili2c, you will need a JAVA software development kit (JDK) version 1.6 or a more recent version
must be installed on your system. A free version of the JAVA software development kit (JDK) is available [here](http://www.oracle.com/technetwork/java/javase/downloads/index.html) .
Also required is the build tool ant. [Download](http://ant.apache.org) and install it as documented there.

## Installation
In order to install the INTERLIS Compiler, extract the ZIP file into a new directory.

## How to compile it?
To compile the INTERLIS Compiler, change to the newly created directory and enter the following command at the commandline prompt:

- Unix
~~~
export CLASSPATH="lib/antlr.jar:$CLASSPATH"
ant jar
~~~
- Windows
~~~
set CLASSPATH=lib\antlr.jar;%CLASSPATH%;.;
ant jar
~~~
To build a binary distribution, use
~~~
ant bindist
~~~
