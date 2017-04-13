# MYOB Coding Challenge
## Payslip application

### Dependencies

This application is built on Java 1.8 and Gradle.

Other dependencies (e.g. spring-boot) are managed by Gradle - as long as you build the application using a relatively recent version, these will be automatically downloaded.

### Running the application

The application can be built using Gradle as the build and dependency management tool.
Run `./gradlew clean build` from the application root directory to build a self-executable jar. The can be run using `java -jar ./build/libs/payslip.jar`

There are 2 endpoints exposed when the application is running.
A JSON-based endpoint for mapping a single request to a payslip, and a CSV-based endpoint for mapping payslips in bulk.

To map a single payslip, you can use curl (or your API tool of choice): `curl -H 'Content-Type: application/json' -X PUT -d '{"firstName": "abc","lastName": "def", "annualSalary":123456,  "superRate": 9,  "paymentDate": "2017-04-11"}' http://localhost:8080/payslip`

To map bulk payslips, use the following curl command (a sample valid input is provided): `curl -H 'Content-Type: text/csv' -X PUT --data-binary @- http://localhost:8080/payslipCsv < input.csv`

### Assumptions

The requirements refer to a "payment start date" in data to be provided, but does not mention an "end date".
I am assuming that this means the "date" simply identifies the month of the payment period - the actual day-of-month will be ignored in my solution.
