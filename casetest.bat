@echo off
setlocal

SET README=%1
MKDIR build\tests
c:\Python21\python.exe ili2tst.py %README% build\tests build\jar\ili2c.jar
:end