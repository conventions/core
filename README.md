Conventions Core
===========

Conventions Framework is a set of components based on top of JSF 2.x, CDI, EJB(optional), Primefaces and Hibernate build to facilitate the development of rich web based applications.

[Conventions Showcase Demo](http://conventions-rpestano.rhcloud.com/conventions/)
----------------------
 
 Getting Started
------------------- 

## The above steps will help you add Conventions Framework to your project

 1 -  **Architectural dependences**
***


* **[CDI](http://docs.jboss.org/weld/reference/latest/en-US/html_single/)**: Conventions uses context and dependency injection to provide some of its features so your server must have it enabled, if you don't use an EE aplication server you can also use conventions, see [this example](https://github.com/rmpestano/conventions-issuetracker) which runs under tomcat.
* **[PrimeFaces](http://primefaces.org)**: Conventions is highly dependent on primefaces 3.x components such as datatable to offer true pagination out of the box, it also has its own components based on top of primefaces such as inputsTexts, modals, combos etc..
* **[PrimeFaces Extensions](http://code.google.com/p/primefaces-extensions/)**: should be in your classpath if you use <conventions:resetbutton component or layout.xhtml template.
* **[Hibernate](http://hibernate.org)**: Conventions Dao and Service layers are highly dependend on hibernate 4.x API
* **[http://myfaces.apache.org/extensions/cdi/download.html](CODI)**: is used to control transactions (@Transactional) in CustomHibernateService, if you use Custom Services, CODI must be in in your classpath.


 **2 -  Classpath**
***


 **2.1 - using maven:**
        
´

         <!-- Conventions -->

        <dependency>
            <groupId>org.conventionsframework</groupId>
            <artifactId>conventions-core</artifactId>
            <version>0.9.4 (or 0.9.5-SNAPSHOT)</version>
            
            <!-- for Snapshots you must declare the sonatype snapshot repository 
            
            <repositories> 
              <repository>
               <id>sonatype snapshots</id>
               <url>https://oss.sonatype.org/content/repositories/snapshots/</url>
               <snapshots><enabled>true</enabled></snapshots>
              </repository>
           </repositories>
           
            -->
            
        </dependency>
 
      <!-- primefaces -->
 
        <dependency>
            <groupId>org.primefaces</groupId>
            <artifactId>primefaces</artifactId>
            <version>3.1 or above</version>
        </dependency>
 

       <!--Java EE -->
 
        <dependency>
            <groupId>javax</groupId>
            <artifactId>javaee-web-api</artifactId>
            <version>6.0</version>
            <scope>provided</scope>
        </dependency>
 
        <!-- CDI -->
 
         <dependency>
            <groupId>javax.enterprise</groupId>
            <artifactId>cdi-api</artifactId>
            <version>1.0-SP4</version>
            <scope>provided</scope>
        </dependency>
          
        <!-- JSF -->

       <!-- mojarra --> 
 
        <dependency>
            <groupId>com.sun.faces</groupId>
            <artifactId>jsf-api</artifactId>
            <version>2.x</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>com.sun.faces</groupId>
            <artifactId>jsf-impl</artifactId>
            <version>2.x</version>
            <scope>provided</scope>
        </dependency>
 

       OR Myfaces 

        <!-- myfaces --> 

        <!--dependency>
            <groupId>org.apache.myfaces.core</groupId>
            <artifactId>myfaces-api</artifactId>
            <version>2.x</version>
        </dependency>
        
        <dependency>
            <groupId>org.apache.myfaces.core</groupId>
            <artifactId>myfaces-impl</artifactId>
            <version>2.x</version>
        </dependency-->     
   
     <!-- HIBERNATE DEPENDENCIES depend of your runtime -->
     
     <!-- HIBERNATE on Glassfish -->
   
                <dependency>
                    <groupId>org.hibernate.javax.persistence</groupId>
                    <artifactId>hibernate-jpa-2.0-api</artifactId>
                    <version>1.0.1.Final</version>
                </dependency>
        
                <dependency>
                    <groupId>org.hibernate</groupId>
                    <artifactId>hibernate-entitymanager</artifactId>
                    <version>4.x</version>
                </dependency>
                
                <dependency>
                    <groupId>org.hibernate</groupId>
                    <artifactId>hibernate-core</artifactId>
                    <version>4.x</version>
                </dependency>

                <dependency>
                    <groupId>org.hibernate</groupId>
                    <artifactId>hibernate-validator</artifactId>
                    <version>4.3.0.Final</version>
                </dependency>     
 
        <!-- HIBERNATE on JBOSS AS -->
 
            <dependency>
                    <groupId>org.hibernate</groupId>
                    <artifactId>hibernate-entitymanager</artifactId>
                    <version>4.x</version>
                    <scope>provided</scope>
                </dependency>

                <dependency>
                    <groupId>org.hibernate</groupId>
                    <artifactId>hibernate-validator</artifactId>
                    <version>4.3.0.Final</version>
                    <scope>provided</scope>
                </dependency>
      
                <dependency>
                    <groupId>org.hibernate.javax.persistence</groupId>
                    <artifactId>hibernate-jpa-2.0-api</artifactId>
                    <scope>provided</scope>
                    <version>1.0.1.Final</version>
                </dependency>
 
 
    <!-- primefaces repo -->
 
         <repository>
            <id>prime-repo</id>
            <name>Prime Technology Maven Repository</name>
            <url>http://repository.primefaces.org</url>
            <layout>default</layout>
        </repository> 
´

**2.2 - without maven:** you can download [conventions jar] (/rmpestano/conventions-core/downloads) in the download section, also you will need to download and add Hibernate, Primefaces and Apache Commons Lang3 to your project.


**3 -  Components**
***
 * Composite components are available at 

 
    `xmlns:cc='http://java.sun.com/jsf/composite/components/conventions'`


namespace.

  * renderer based components are available at

    

   `xmlns:conventions='http://conventions.org/ui'`

 
namespace.    