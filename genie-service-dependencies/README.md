# Genie Service Dependencies

微服务POM的依赖聚合

提供一套配合好的工具jar包组合

```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <artifactId>genie-service-dependencies</artifactId>
                <groupId>io.github.dexter9000.genie</groupId>
                <version>1.0-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```
