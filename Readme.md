# MYOB Coding Challenge
## Payslip application

### Dependencies

This application is built on Java 1.8 and Gradle.

Other dependencies (e.g. spring-boot) are managed by Gradle - as long as you build the application using a relatively recent version, these will be automatically downloaded.

### Running the application

The application can be built using Gradle as the build and dependency management tool.
Run `./gradlew clean build` from the application root directory to build a self-executable jar. The can be run using `java -jar ./build/libs/payslip.jar`

### Assumptions

The requirements refer to a "payment start date" in data to be provided, but does not mention an "end date".
I am assuming that this means the "date" simply identifies the month of the payment period - the actual day-of-month will be ignored in my solution.
