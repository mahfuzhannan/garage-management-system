# garage-management-system
University group project to develop a functional garage management system

================================ READ ME FILE ================================

NAME: 		Garage Management Information System
AUTHORS:	Mahfuz Hannan, George Richardson, Haricharan Sampat, Stefan Ionascu, Vasilis Costa
DESC:		This is a piece of java software that allows the user to abstractly 
			manage a garage; using the interface to display and manage data.

*Note:	All icons and images used in the software are property and owned by 
		Flaticon.com and we have no authority; ownership or copyrights for 
		images used.


============
REQUIREMENTS
============

Installation of the software is not necessary; although JAVA 8 will need to be
installed on the machine running the software. Software has been developed and
tested using JAVA 8 and will ONLY run using JAVA 8. Has been tested on Linux, 
Windows and Mac machines.

Please ensure you have read all the manuals prior to the use of the software. 
A few features may not be understood unless referenced from the manual.

The default username and password for the Garage Management Information System
is given in the file login_details.txt.

To test all features of the given requirements such as, Passing/Failing an 
Mot, Customer Accounts reminders etc. Please ensure to go to Settings > 
Click on "Reset Database with Test Data"; this will add data into the database
to allow testing specifi features.


========================
BUILD OUTPUT DESCRIPTION
========================

When you build an Java application project that has a main class, the IDE
automatically copies all of the JAR
files on the projects classpath to your projects dist/lib folder. The IDE
also adds each of the JAR files to the Class-Path element in the application
JAR files manifest file (MANIFEST.MF).

To run the project from the command line, go to the dist folder and
type the following:

java -jar "GMSIS.jar" 

To distribute this project, zip up the dist folder (including the lib folder)
and distribute the ZIP file.

Notes:

* If two JAR files on the project classpath have the same name, only the first
JAR file is copied to the lib folder.
* Only JAR files are copied to the lib folder.
If the classpath contains other types of files or folders, these files (folders)
are not copied.
* If a library on the projects classpath also has a Class-Path element
specified in the manifest,the content of the Class-Path element has to be on
the projects runtime path.
* To set a main class in a standard Java project, right-click the project node
in the Projects window and choose Properties. Then click Run and enter the
class name in the Main Class field. Alternatively, you can manually type the
class name in the manifest Main-Class element.
