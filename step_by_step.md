# Step by Step Guide

## Create project structure

```bash
$ mvn archetype:generate -DgroupId=org.koenighotze\
 -DartifactId=jee7helloworld \
 -DarchetypeArtifactId=maven-archetype-webapp \
 -DinteractiveMode=false
```



## Add JEE7 Dependencies

In [pom.xml](https://gist.github.com/koenighotze/bedce5cec0f7c7148da8) add:

```xml
<dependency>
  <groupId>javax</groupId>
  <artifactId>javaee-api</artifactId>
  <version>7.0</version>
  <scope>provided</scope>
</dependency>

```

## Add Wildfly Plugin

In [pom.xml](https://gist.github.com/koenighotze/bedce5cec0f7c7148da8) add:

```xml
<plugin>
  <groupId>org.wildfly.plugins</groupId>
  <artifactId>wildfly-maven-plugin</artifactId>
  <version>1.0.2.Final</version>
</plugin>

```

## Try it ... Run It

```shell
$ mvn wildfly:run
$ mvn
$ open http://localhost:8080/jee7helloworld
```

## Setup JSF

Copy the following to [`web.xml`](https://gist.github.com/koenighotze/73d1625e7c51250bd7c1)

```xml
<<?xml version="1.0" encoding="UTF-8"?>
<web-app version="3.1" xmlns="http://xmlns.jcp.org/xml/ns/javaee" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd">
  <servlet>
    <servlet-name>Faces Servlet</servlet-name>
    <servlet-class>javax.faces.webapp.FacesServlet</servlet-class>
    <load-on-startup>1</load-on-startup>
  </servlet>

  <servlet-mapping>
    <servlet-name>Faces Servlet</servlet-name>
    <url-pattern>*.xhtml</url-pattern>
  </servlet-mapping>
</web-app>
```

## Create index.xhtml
Copy the following into (`src/main/resources/webapp/index.xhtml`)[https://gist.github.com/koenighotze/f4d534052aff939dabcb]

```xml
<!DOCTYPE html>
<html
  xmlns="http://www.w3.org/1999/xhtml"
  xmlns:jsf="http://xmlns.jcp.org/jsf">
  <body>
    <form jsf:id="form">
    What is your name: <input type="text" jsf:value="#{hello.name}"/>
    <input type="submit" value="Greet me" jsf:action="hello.xhtml"/>
    </form>
  </body>
</html>
```

## Create hello.xhtml

Copy the following into (`src/main/resources/webapp/hello.xhtml`)[https://gist.github.com/koenighotze/07af8e874540e78a6beb]

```xml
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<body>
Hello #{hello.name}
</body>
</html>

```

## Add Model
Copy the following into a new class (`src/main/java/hello/Hello.java)[https://gist.github.com/koenighotze/3f869a3c46aec29b0a17]

```java
package hello;

import javax.enterprise.inject.Model;
import java.io.Serializable;

@Model
public class Hello implements Serializable {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

## Run Application

```shell
$ mvn wildfly:run
$ open http://localhost:8080/jee7helloworld
```

## Add Controller and CRUD

Add JPA persistene by creating (`src/main/resources/META-INF/persistence.xml`)[https://gist.github.com/koenighotze/305fceff59a5a1987a01]

```xml
<?xml version="1.0" encoding="UTF-8"?>
<persistence version="2.1" xmlns="http://xmlns.jcp.org/xml/ns/persistence"
             xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
             xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/persistence http://xmlns.jcp.org/xml/ns/persistence/persistence_2_1.xsd">
  <persistence-unit name="JEE7HelloWorld-Booking" transaction-type="JTA">
    <properties>
      <property name="javax.persistence.schema-generation.database.action" value="drop-and-create"/>
    </properties>
  </persistence-unit>
</persistence>

```


Add ORM to `Hello.java`

```java
@Model
@Entity
public class Hello implements Serializable {
    @Id
    private String name;
```

Add the following to a new class (`src/main/java/hello/HelloController.java`)[https://gist.github.com/koenighotze/4e5195adc8671896323a]

```java
package hello;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaQuery;
import javax.transaction.Transactional;
import java.util.List;

@Named
@ApplicationScoped
public class HelloController {
    @Inject
    private Hello hello;

    @PersistenceContext
    private EntityManager em;

    public List<Hello> helloSoFar() {
        CriteriaQuery<Hello> cq = this.em.getCriteriaBuilder().createQuery(Hello.class);
        cq.select(cq.from(Hello.class));
        return this.em.createQuery(cq).getResultList();
    }

    @Transactional
    public void storeName(Hello hello) {
        em.persist(hello);
    }
}

```

Add the following to (`index.xhtml`)[https://gist.github.com/koenighotze/46408dcab0dfac530874]

```xml

  <br/>
  <div>
    Hellos thus far:<br/>
    #{helloController.helloSoFar()}
  </div>
```

## Run Application

```shell
$ mvn wildfly:run
$ open http://localhost:8080/jee7helloworld
```



## And now REST

...



## Run Application...again

```shell
$ mvn wildfly:run
$ open http://localhost:8080/jee7helloworld
```

Try `curl` on the REST service:

```shell
$ curl ....TODO

```