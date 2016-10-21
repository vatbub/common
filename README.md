# common
Common classes for FOKProjects

[![Build Status](https://travis-ci.org/vatbub/common.svg?branch=master)](https://travis-ci.org/vatbub/common)     [![Coverage Status](https://coveralls.io/repos/github/vatbub/common/badge.svg?branch=master)](https://coveralls.io/github/vatbub/common?branch=master) [![Maven Central](https://img.shields.io/maven-central/v/com.github.vatbub/common.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22com.github.vatbub%22%20AND%20a%3A%22common%22)

This repository holds the Maven Artifact `fokprojects.common` which currently contains classes for:
  - Self-Update of a java app (in developement)
  - Saving preferences
  - Logging (using `java.util.logging`)
  - Comparing version numbers
  - A basic dialog to notify the user of an update
  
Learn how to use all of this functionality, go to the [wiki](../../wiki).
  
To add this artifact as a maven dependency, add the following repositories and dependency to your pom:

```xml
<dependencies>
	...
	<dependency>
		<groupId>com.github.vatbub</groupId>
		<artifactId>common</artifactId>
		<version>0.0.18</version>
	</dependency>
	...
</dependencies>
	...
```
  
## Build the current snapshot
1. Clone this repository
2. Run `mvn install`

## Build the latest release
Repeat the steps mentioned above but switch to the `release` branch by running `git checkout release` prior to runing `mvn install`.

