# The Coveros Mobile App
## Purpose
The aim of this application is to facilitate the instruction of the Mobile Application Testing course, brand and advertise Coveros and its services, and demonstrate Coveros' competency in mobile application development.

## Important Gradle tasks
For all of the following tasks, you will need the ADK installed and an emulator with Android API 24 or 25 that is connected to ADB and running.

### `installDebug`
**Description**

This task installs the latest debug version of the APK on any running emulator that is connected to ADB.

**Prerequisites**

 - Emulator (with or without an APK installed)

### `testDebugUnitTest`
**Description**

This task runs all unit and Appium tests.

**Prerequisites**

 - Emulator with APK installed
 - Appium installed and server running
 - SonarQube installed
 
### `connectedDebugAndroidTest`
**Description**

This task runs all instrumented tests.

**Prerequisites**

- Emulator with APK installed

### `aggregateTestData`
**Description**

This task gathers test results and coverage for unit, instrumented, and Appium tests; necessary for accurate SonarQube analysis.

**Prerequisites**

- `testDebugUnitTest` and `connectedDebugAndroidTest` tasks have been previously run

### `fullSonarAnalysis`
**Description**

This task runs all of the previous tasks (`testDebugUnitTest`, `connectedDebugAndroidTest`, `aggregateTestData`), and also SonarQube analysis.

**Prerequisites**

 - Emulator (this task will automatically install APK)
 - Appium installed and server running
 - SonarQube installed
