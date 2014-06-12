AppDUtilReports
===============
The AppDUtilReports is going to be a set of tools that will help AppDynamics users manage their environment. The first tool available will be the ControllHC, which will read a configuration xml and execute a series of REST calls to determine which business transactions, backends and/or EUM objects are not collecting data. The tool first request data for the last 4 hours and compares the total request count against the min request count. The objects that fail to have the minimum number are then check for the last 24 hours and if they fail the minimum again they are check for the last 48 hours. The toold then produces either standard output or an Excel file with the finding. The information can then be used to determine which business transactions, backends and EUM objects can be deleted. 

Requirements:
-------------
The AppDUtilReports solution is dependent on the AppDRESTAPI-SDK (https://github.com/Appdynamics/AppDRESTAPI-SDK.git) and the AppDSimpleCrypto (https://github.com/Appdynamics/AppDSimpleCrypto) solutions these should be cloned and built before building this package. It is recommended that all three packages share a base directory, this will make the dependency checking easier. Compile and package the AppDRESTAPI-SDK to insure files that are needed are present. Following the instructions in the README.md for the AppDSimpleCrypto and build the package. Once this package has been cloned edit the file called ‘one_time_git.properties’, insure the location of AppDRESTAPI-SDK, AppDSimpleCrypto and  the version of the jar files are correct. The file ‘one_time_git.properties’ should not be synced with the git repository after it has been edited because the settings should only apply to your environment.

The file contains five variables please insure that they are correct (insure that no extra spaces are present): 

  appd_rest_base=../AppDRESTAPI-SDK 

  appd_rest_jar=RESTAPI_1.0.7.jar

  appd_simple_crypto_base=../AppDSimpleCrypto

  appd_simple_crypto_lib_dep=commons-codec-1.9.jar

  appd_simple_crypto_jar=AppDSimpleCyprto_1.0.0.jar


Building:
---------

To build the package run the following command within the AppDUtilReports directory:
 ant -f AppD_build.xml

This will create a directory called execLib with all of the necessary libraries to run the tool

Pre-Execution:
--------------

Please use the example configuration file provided in src/org/appdynamics/utilreports/conf/HCExample.xml and copy it to base directory. Using the AppDSimpleCrypto tool encrypt the password for the user that you are going to use. Edit the HCExample.xml with the proper information, using the encrypted password and other needed information.

Usage:

java -cp "execLib/*" org.appdynamics.utilreports.ControllerHC -C HCExample.xml


