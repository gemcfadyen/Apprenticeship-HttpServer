# Apprenticeship-HttpServer
HTTP Server adhering to the Http Cob Spec

[![Build Status](https://travis-ci.org/gemcfadyen/Apprenticeship-HttpServer.svg?branch=master)](https://travis-ci.org/gemcfadyen/Apprenticeship-HttpServer)    [![Coverage Status](https://coveralls.io/repos/github/gemcfadyen/Apprenticeship-HttpServer/badge.svg?branch=master)](https://coveralls.io/github/gemcfadyen/Apprenticeship-HttpServer?branch=master)

## Setup this Http Server

#### Clone the project
`git clone git@github.com:gemcfadyen/Apprenticeship-HttpServer.git`

#### cd into the root folder
`cd Apprenticeship-HttpServer`

#### build using gradle
`./gradlew clean build`
(you may wish to exclude the folder `vendor` from being compiled. You can do this in IntelliJ by right clicking on the vendor folder and choosing 'Mark Directory As Excluded')


## Setup the Acceptance tests which run against this server

#### Navigate to the vendor cob-spec directory             ----------------your {home.directory}/Documents

`cd vendor/cob-spec`

#### Package the cob-spec tests

`mvn package`

#### Starting Fitnesse Server

##### Start the Fitnesse server on port 9090.

`java -jar fitnesse.jar -p 9090`

##### Open your browser and go to http://localhost:9090. You should see the Cob Spec website.

#### Configuring Cob Spec

The original 8th Light cob spec suite can be found [here](https://github.com/8thlight/cob_spec). The copy in this repository runs against the httpServer that this repository builds.

#### Http Server

Your server jar needs to take two command line arguments.

-p which specifies the port to listen on. Default is 5000.
-d which specifies the directory to serve files from. Default is the PUBLIC_DIR variable configured in Fitnesse, which points to the vendor/cob_spec/public folder in this repository.
Running Tests

To run all tests, click the Suite button. If you have not already started your server, the test suite will start it for you.
To run the simple http request tests, first click the ResponseTestSuite link, then click the Suite button.
To run the tests that require threading, first click the SimultaniousTestSuite link, then click the Suite button.
To run a test individually, first click on the test link, then click the Test button.

For info, you can start your server directly using the following command:
`java -jar {enter-the-path-you-cloned-the-project}/build/libs/httpServer-1.0-SNAPSHOT.jar`

