# ili2c - The INTERLIS Compiler
Checks the syntactical correctness of an [INTERLIS](https://www.interlis.ch/content/index.php?language=e "Interlis - The tool to describe, integrate and coordinate geodata.") data model.

## Build Status
- master branch [![build status](https://www.travis-ci.org/claeis/ili2c.svg?branch=master)](https://www.travis-ci.org/claeis/ili2c)
- stable branch [![build status](https://www.travis-ci.org/claeis/ili2c.svg?branch=stable)](https://www.travis-ci.org/claeis/ili2c)

## License
ili2c is licensed under the [LGPL](https://www.gnu.org/licenses/lgpl.txt) (Lesser GNU Public License).

## System Configuration
For the current version of ili2c, you will need a JRE (Java Runtime Environment) installed on your system, version 1.6 or later. Any OpenJDK based JRE will do.
The JRE (Java Runtime Environment) can be downloaded from the Website <http://www.java.com/>.

## Installing ili2c
To install the ilivalidator, choose a directory and extract the distribution file there. 

## Running ili2c
The ili2c can be started with

    java -jar ili2c.jar [options] file.ili

## Building from source
To build the `ili2c.jar`, use

    gradle jar test usrdoc ilidoc

To build a binary distribution, use

    gradle bindist

### Development dependencies
* JDK 1.6 or higher (OpenJDK will do)
* Gradle
* Python and docutils installed (`pip install docutils`)
    * rst2html command is used by `userdoc` gradle task
    * rst2html location can be provided in file _user.properties_

## Documentation
- [doc/ili2c.rst](doc/ili2c.rst)
- [doc/ilidoc.rst](doc/ilidoc.rst)
