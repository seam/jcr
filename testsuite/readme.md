#Seam JCR Test Suite

##Running the testsuite on the default container (Embedded Weld) with Modeshape

    mvn clean verify 

##Running the testsuite on the default container (Embedded Weld) with Jackrabbit

    mvn clean verify -Pseam-jcr-jackrabbit

##Running the testsuite on JBoss AS 7

    mvn clean verify -Darquillian=jbossas-managed-7

Note that jbossas-managed-7 profile runs both the Jackrabbit and the Modeshape tests

