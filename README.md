# Ion-Gateway

## Prerequisites
* Java 11
* Docker daemon

## Building
To just compile and build the projects:
```bash
./gradlew assemble
```
To do a full build with tests and the formatter:
```bash
./gradlew build
```

### Build Checks
#### OWASP
```bash
./gradlew dependencyCheckAnalyze --info
```
The report for each project can be found at build/reports/dependency-check-report.html.

#### Style
The build can fail because the static analysis tool, Spotless, detects an issue. To correct most Spotless issues:
```bash
./gradlew spotlessApply
```

For more information about spotless checks see
[here](https://github.com/diffplug/spotless/tree/master/plugin-gradle#custom-rules).

// TODO: Running the container and connecting to internal docker services
