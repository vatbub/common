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
      <id>fokReleaseRepo</id>
      <name>FOK Release Repository</name>
      <url>http://vatbub.bplaced.net/mavenRepo/release/</url>
    </repository>
    <repository>
      <id>fokSnapshotRepo</id>
      <name>FOK Snapshot Repository</name>
      <url>http://vatbub.bplaced.net/mavenRepo/snapshots/</url>
    </repository>
  ...
  </repositories>
  ...
  <dependencies>
		...
		<dependency>
			<groupId>fokprojects</groupId>
			<artifactId>common</artifactId>
			<version>0.0.2</version>
		</dependency>
		...
	</dependencies>
	...
```
  
  
