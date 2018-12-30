# cftemplategenerator
Java based Generator for Cloud formation template

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.blockwiseph/cftemplategenerator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.blockwiseph/cftemplategenerator)

[![Javadocs](http://www.javadoc.io/badge/com.github.blockwiseph/cftemplategenerator.svg)](http://www.javadoc.io/doc/com.github.blockwiseph/cftemplategenerator)


### To Use this cloud formation template generator:

* TBD


### For package owners:

To make changes and deploy to repository, the following need to be done:

* Make sure you have maven installed. 
* Make sure you have a JIRA account, that is authorized to deploy to our maven central repo.
* Add JIRA credentials to `settings.xml` of maven file based on https://central.sonatype.org/pages/apache-maven.html
* Install gpg on your machine, and make sure a key is generated.
* To Generate a key, Execute `gpg-keygen` and add gpg credentials to settings.xml based on above link
* Upload key to ubuntu keyserver by `gpg --keyserver hkp://keyserver.ubuntu.com --send-keys YOUR_KEY_ID`


To deploy:

* run the command `export GPG_TTY=$(tty)`
* run `mvn clean deploy` for only a SNAPSHOT Deployment.
* The deployed version can be seen in: https://oss.sonatype.org/content/repositories/snapshots/com/github/blockwiseph/cftemplategenerator

For Release Deployment:

* `mvn versions:set -DnewVersion=1.0.0` this will modify pom.xml and other places. Commit this change to Git.
* run `mvn clean deploy -P release`


