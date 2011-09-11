#Seam JCR Test Suite

##Running the testsuite on the default container (Embedded Weld) with Modeshape

    mvn clean verify 

##Running the testsuite on the default container (Embedded Weld) with Jackrabbit

    mvn clean verify -Pseam-jcr-jackrabbit

##Running the testsuite on all containers
    
    mvn clean verify -DallTests

##Running the testsuite on JBoss AS 7 only

    mvn clean verify -Djbossas-managed-7

Note that jbossas-managed-7 profile runs both the Jackrabbit and the Modeshape tests

##Contents

common/ directory contains the source of tests common to all the containers. Sources of container-specific tests are located in the respective container modules.



