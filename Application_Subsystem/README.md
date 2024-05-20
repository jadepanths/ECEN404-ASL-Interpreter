# ASLator

# Ray Cook | 525003962

This software was developed in Kotlin in the Android Studio IDE. To build the app on your own device, please refer to the following steps:

1. Download and extract this section of the repository
2. Launch Android Studio (application was developed in build no. AI-213.7172.25.2113.9014738, built on August 31, 2022)
3. Click on File -> New -> Import Project... at the top right.
4. Navigate to where you extracted this repository to and select the "build.gradle" file.
5. Android Studio should create the new project properly at this point and leave you with options to either run it on a virtual device or upload it to a physical one.
6. To change the IP address of the host server (Where the model resides, the gifs/imgs are stored, etc.) change it accordingly in the following two locations:  
	app>src>main>res>values>strings.xml, line 37  
	app>src>main>java>com>example>aslator>shared>utils>NetUpload.kt, line 294
