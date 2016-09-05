# common
Common classes for FOKProjects

This repository holds the Maven Artifact `fokprojects.common` which currently contains classes for:
  - Self-Update of a java app (in developement)
  
To add this artifact as a maven dependency, add the following repositories and dependency to your pom:

```xml
...
<repositories>
    ...
        <repository>
            <snapshots>
                <enabled>false</enabled>
            </snapshots>
            <id>bintray-vatbub-fokprojectsSnapshots</id>
            <name>bintray</name>
            <url>http://dl.bintray.com/vatbub/fokprojectsSnapshots</url>
        </repository>
        <repository>
            <snapshots />
            <id>snapshots</id>
            <name>libs-snapshot</name>
            <url>https://oss.jfrog.org/artifactory/libs-snapshot</url>
        </repository>
    </repositories>
    ...
  <dependencies>
		...
		<dependency>
			<groupId>fokprojects</groupId>
			<artifactId>common</artifactId>
			<version>0.0.12</version>
		</dependency>
		...
	</dependencies>
	...
```
  
  
