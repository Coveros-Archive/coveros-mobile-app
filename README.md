# The Coveros Mobile App
## Purpose
The aim of this application is to facilitate the instruction of the Mobile Application Testing course, brand and advertise Coveros and its services, and demonstrate Coveros' competency in mobile application development.
## Important Gradle tasks
To run these following tasks, be sure to install the ADK, SonarQube, Appium, and have running an emulator with the APK installed. 
- `fullSonarAnalysis` runs all tests (i.e. unit, instrumented, and Appium) with test coverage and SonarQube analysis

- `connectedDebugAndroidTest` runs instrumented tests

- `testDebugUnitTest` runs unit and Appium tests

- `aggregateTestData` gathers test results and coverage for unit, instrumented, and Appium tests; necessary for accurate SonarQube analysis

- `installDebug` installs the debug version of the APK on any running emulator that is connected to ADB