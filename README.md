AppDUtilReports
===============
The AppDUtilReports is going to be a set of tools that will help AppDynamics users manage their environment. The first tool available will be the ControllHC, which will read a configuration xml file and execute a series of REST calls to determine which business transactions, backends and/or EUM objects are not collecting data. The tool first requests data for the last 4 hours and compares the total request count against the min request count. The objects that fail to have the minimum number requests are then checked for the last 24 hours and if they fail the minimum count again they are check for the last 48 hours. The tool then produces either standard output or an Excel file with the findings. The information can then be used to determine which business transactions, backends and EUM objects can be deleted. 

The second tool offered is the License Check that will inform the admin of issues when the license usage is being exceeded by a particular tier. The License Check tool has two major components: 
1. The configuration file that defines the controller, user and the notification 
2. The base file that defines what we are checking for 


Requirements:
-------------
The AppDUtilReports solution is dependent on the version 2.5.4+ of the AppDRESTAPI-SDK (https://github.com/Appdynamics/AppDRESTAPI-SDK.git) and the AppDSimpleCrypto (https://github.com/Appdynamics/AppDSimpleCrypto) solutions these should be cloned and built before building this package. It is recommended that all three packages share a base directory, this will make the dependency checking easier. Compile and package the AppDRESTAPI-SDK to insure files that are needed are present. Following the instructions in the README.md for the AppDSimpleCrypto and build the package. Once this package has been cloned edit the file called ‘one_time_git.properties’, insure the location of AppDRESTAPI-SDK, AppDSimpleCrypto and  the version of the jar files are correct. The file ‘one_time_git.properties’ should not be synced with the git repository after it has been edited because the settings should only apply to your environment.

The file contains five variables please insure that they are correct (insure that no extra spaces are present): 

  appd_rest_base=../AppDRESTAPI-SDK 

  appd_rest_jar=AppDRESTAPI-SDK_2.5.4.jar

  appd_simple_crypto_base=../AppDSimpleCrypto

  appd_simple_crypto_lib_dep=commons-codec_1_9-1.9.jar

  appd_simple_crypto_jar=AppDSimpleCyprto_1.0.1.jar



Building:
---------

To build the package run the following command within the AppDUtilReports directory:
 ant -f AppD_build.xml

This will create a directory called execLib with all of the necessary libraries to run the tool.


Pre-Execution:
--------------

####ControllerHC
Please use the example configuration xml file provided in src/org/appdynamics/utilreports/conf/HCExample.xml and copy it to the project base directory. Using the AppDSimpleCrypto tool encrypt the password for the REST user that you are going to use. Edit the HCExample.xml and update it with the proper information, using the encrypted password and other needed information.

####LicenseCheck
Please use the example configuration xml file provided in src/org/appdynamics/licenseCheck/resources/example_config.xml. If the output is being sent through email and the mail server requires authentication then you will need to use the AppDSimpleCrypto to hash the password for the mail server as well as the AppDynamics user. Once the authentication is properly set you can create the base file by running the tool with the '-b <FILE>' to create the initial base file. The format of the base file is AppDynamics Application, AppDynamics Tier, number of application agents and number of machine agents. The user can test the execution of email by providing the '-m' option on the configuration file has been properly filled in. The second method of notification is posting a REST event into AppDynamics. Just provide the AppDynamics application name and an event with the information will be sent.
   
#### MarkHistorical
Please use the example configuration xml file provided in src/org/appdynamics/markH/file/MarkHistorical_Example.xml. Using the AppDSimpleCrypto tool encrypt the password for the REST user that you are going to use. Edit the MarkHistorical_Example.xml and update it with the proper information, using the encrypted password and other needed information.

Execution:
----------

####ControllerHC
Usage:

java -cp "execLib/*" org.appdynamics.utilreports.ControllerHC -C HCExample.xml


####LicenseCheck
Usage:
java -cp "execLib/*" org.appdynamics.licenseCheck.LicenseCheck -C LCExample.xml [-b <FILEPATH>] [-m]


####MarkH
Usage:
java -cp "execLib/*" org.appdynamics.markH.AppDMH -C MarkH/MarkHistorical_Example.xml 


Support:
--------
If you have any question please email me gilbert.solorzano@appdynamics.com.
