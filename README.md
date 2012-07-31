JspTest
=======

JspTest is a JUnit 3 extension for testing JavaServer Pages (JSP) outside a J2EE container. Internally, it uses the Jasper JSP compiler from the Jakarta Tomcat project and the Java compiler distributed as part of the system's default JDK.

JspTest is hosted at GitHub and is being distributed through the official Maven repository. The easiest way to get it into use (provided that you're using [Maven](http://maven.apache.org/)) is to add the following dependency to your POM file:

    <dependency>
      <groupId>net.sf.jsptest</groupId>
      <artifactId>jsptest-jsp20</artifactId>
      <version>VERSION</version>
    </dependency>

...where _VERSION_ should be replaced by whatever is the latest version number available in the [central Maven repository](http://central.maven.org/maven2/com/github/jsptest/).

There's a full example of a POM file for a Maven project over [here](https://github.com/jsptest/jsptest/blob/master/jsptest-samples/jsptest-samples-jsp20/pom.xml).

If you don't use Maven, you could consider [Ivy](http://ant.apache.org/ivy/) or [Buildr](http://incubator.apache.org/buildr/), or you could just download the .jar files manually from the Maven repository.