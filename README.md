## 简介
本人实践DDD分层架构的过程中，个人觉得DDD的架构下能够让我们在业务的视角下去码代码，比较三层架构更能够体现我们的业务，代码也更加不会腐化。**但是，**在一个团队中，并不是所有人都愿意`吸纳`这一架构，甚至抵触。特别是ddd`新手` 期间，容易习惯性的去编码如跨层调用、命名等等。  
基于此，本插件应运而生，用于限制ddd架构下分层的依赖关系（当然别的架构要限制层之间的依赖关系也是可以的）。

## 如何使用
**pom配置：**

```
<build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>

            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-checkstyle-plugin</artifactId>
                <version>2.17</version>
                <configuration>
                    <configLocation>../checkstyle.xml</configLocation>
                    <packageNamesLocation>com/hyl/ddd/check/style/packagenames.xml</packageNamesLocation>
                    <failOnViolation>true</failOnViolation>
                    <violationSeverity>warning</violationSeverity>
                    <failsOnError>true</failsOnError>
                    <linkXRef>false</linkXRef>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>com.hyl</groupId>
                        <artifactId>custom-code-style</artifactId>
                        <version>1.0.1-SNAPSHOT</version>
                    </dependency>
                    <dependency>
                        <groupId>com.puppycrawl.tools</groupId>
                        <artifactId>checkstyle</artifactId>
                        <version>7.3</version>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <!-- 该插件执行的生命周期点，在initialize时进行执行 -->
                        <phase>initialize</phase>
                        <!-- 执行目标，要想具体了解目标的概念需要了解maven的插件生命周期，这里不再展开，下方会有推荐阅读的链接 -->
                        <goals>
                            <goal>checkstyle</goal><!-- checkstyle只会统计数量。check会在控制台输出具体结果 -->
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

    <reporting>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-checkstyle-plugin</artifactId>
                <version>2.17</version>
                <configuration>
                    <!-- 同样的checkstyle校验配置文件 -->
                    <configLocation>../checkstyle.xml</configLocation>
                    <packageNamesLocation>com/hyl/ddd/check/style/packagenames.xml</packageNamesLocation>
                </configuration>
            </plugin>
        </plugins>
    </reporting>
```

**配置<configLocation>中的配置文件：**
**checkstyle.xm**

```
<?xml version="1.0" ?>

<!DOCTYPE module PUBLIC
        "-//Puppy Crawl//DTD Check Configuration 1.2//EN"
        "http://www.puppycrawl.com/dtds/configuration_1_2.dtd">

<module name="Checker">
    <property name="charset" value="UTF-8"/>
    <property name="severity" value="warning"/>
    <module name="TreeWalker">
        <module name="com.hyl.ddd.check.style.call.ClassCallRelationCheck">
            <property name="allowCallBasePackage" value="com.hyl.test"/>
            <property name="allowCallPackages" value="application->domain,interfaces->application"/>
            <property name="ignorePackagePatterns" value="start.*,application.ccc.Test2"/>
            <property name="commonPackagePatterns" value="common.*"/>
        </module>
    </module>
</module>
```
配置含义见注释。


**执行插件：**
找到maven的plugins执行checkstyle:check即可  






