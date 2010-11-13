POINT LOCATION
Computational geometry project 2 - Fall 2010
Aarhus University

Authors : Guillaume Depoyant & Michaël Ludmann
Lecturer : Gerth Stølting Brodal

===============================================================================
How to get the source code/executables, report, other test datas, and this
README file  :

- go on the URL : http://code.google.com/p/point-location/
	and "Downloads" for the full report
		"Source" then "Browse" for the source code (trunk/point-location)
-or use Subversion to check out a read-only working copy :
    svn checkout http://point-location.googlecode.com/svn/trunk/ point-location-read-only
	

===============================================================================
How to run the project :

- run pointLocation.jar
- or use Eclipse (or another IDE) to run it) and download the whole source code

===============================================================================
Implementation language :

Java SE-1.6
JDK Compiler compliance level 1.6

===============================================================================

Libraries used :

JRE System Library [jre6]
Package java.util :
	http://download.oracle.com/javase/1.4.2/docs/api/java/util/package-summary.html	
Java AWT library

===============================================================================
Operating system used while developing software :

Windows 7

===============================================================================
How to use the program itself :

Please follow instructions in the report.

===============================================================================
Creating test files :

No specific test files. Just input segments OR, to imput many segments at the 
same time : 
Browse the source code and in DrawingArea.java, in the constructor method,
uncomment the commented lines. This will draw a defined (var : NBSEGMENTS)
amount of the same translated segments on the board.
