Spark, Cassandra and Kafka Prototypes
-------------------------------------
>The purpose of the project is to test Spark, Spark-Cassandra Connector and Kafka-Spark Streaming features using
 Scala 2.11.7. And, yes, this will hurt.;)

***

Homebrew
--------
>Install Homebrew on OSX. [How-To] (http://coolestguidesontheplanet.com/installing-homebrew-os-x-yosemite-10-10-package-manager-unix-apps/)

Installation
------------
>Install the following packages via Homebrew:

1. brew tap homebrew/services [Homebrew Services] (https://robots.thoughtbot.com/starting-and-stopping-background-services-with-homebrew)
2. brew install scala
3. brew install sbt
4. brew install cassandra
5. brew install zookeeper
6. brew install gradle

Environment
-----------
>The following environment variables should be in your .bash_profile

- export JAVA_HOME="/Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home"
- export CASSANDRA_HOME="/usr/local/Cellar/cassandra/2.1.6/libexec"
- export KAFKA_HOME="/Users/javawerks/workspace/apache/kafka"
- export SCALA_VERSION="2.11.7"
- export SCALA_BINARY_VERSION="2.11"
- export SCALA_LIB="/usr/local/Cellar/scala/2.11.7/libexec/lib"
- export SPARK_SCALA_VERSION="2.11"
- export SPARK_HOME="/Users/javawerks/workspace/apache/spark"
- export SPARK_LAUNCHER="$SPARK_HOME/launcher/target"
- export PATH=${JAVA_HOME}/bin:${CASSANDRA_HOME}/bin:${KAFKA_HOME}/bin:${SPARK_HOME}/bin:${SPARK_HOME}/sbin:/usr/local/bin:/usr/local/sbin:$PATH

Spark
-----
>Install Spark from github. The brew and apache distros are Scala 2.10 oriented.

1. git clone https://github.com/apache/spark
2. dev/change-version-to-2.11.sh
3. mvn -Pyarn -Phadoop-2.6 -Dscala-2.11 -DskipTests clean package

>See [Scala 2.11 Support Instructions] (http://spark.apache.org/docs/latest/building-spark.html#building-for-scala-211)

Kafka
-----
>Install Kafka from github. The brew and apached distros are Scala 2.10 oriented.

1. git clone https://github.com/apache/kafka
2. gradle
3. gradle -PscalaVersion=2.11.7 jar
4. edit $KAFKA_HOME/config/server.properties log.dirs=$KAFKA_HOME/logs
5. mkdir $KAFKA_HOME/logs

>You can also edit scalaVersion=2.11.7 in gradle.properties. Provide a fully qualified path for Kafka server.properties log.dirs

Configuration
-------------
1. log4j.properties
2. spark.properties

>The test versions are fine as is. The main version of log4j.properties is only used within your IDE. To enable Spark
logging, copy main/resources/log4j.properties to $SPARK_HOME/conf ( where you will see a tempalte version ). Tune as required.

Services
--------
>Start:

1. brew services start cassandra
2. brew services start zookeeper
3. nohup $KAFKA_HOME/bin/kafka-server-start.sh $KAFKA_HOME/config/server.properties > $KAFKA_HOME/logs/kafka.nohup&

>Homebrew uses launchctl/launchd to ensure services are booted up on each system start, unless you stop them. Take a
 look at the apache.kafka.plist in this project. Homebrew service plist files are ideally stored in ~/Library/LaunchAgents
 See [launchd] (http://launchd.info) for details. Ideally you should use Xcode to edited a plist.

>Stop:

1. brew services stop cassandra
2. brew services stop zookeeper
3. $KAFKA_HOME/bin/kafka-server-stop.sh

Tests
-----
>Test results can be viewed at ./target/output/test. See the Output section below.

1. sbt test

Assembly and Submit
-------------------
1. sbt assembly
2. spark-submit --class spark.SparkApp --master local[*] ./target/scala-2.11/spark-app-0.1.jar

>[Submitting Spark Applications] (https://spark.apache.org/docs/latest/submitting-applications.html)

Assembly and Run
----------------
1. sbt assembly
2. java -cp $SCALA_LIB/scala-library.jar:$SPARK_LAUNCHER/spark-launcher_2.11-1.5.0-SNAPSHOT.jar:./target/scala-2.11/spark-app-0.1.jar sc.SparkAppLauncher

>This is not an ideal option. Moreover, an uber jar is problematic. Instead, go with assembly and submit whenever possible.
That said, a lightweight uber jar, composing scala-library, spark-launcher and spark app classes/resources is a viable
option with SparkLauncher, which ultimately calls spark-submit --- which, in addition to a large number of Spark built
dependencies, loads the spark assembly uber jar.

1. sbt run
2. [1] spark.SparkApp   [2] spark.SparkAppLauncher

>Optional: Above, select option 1. Selecting option 2 fails.

Logging
-------
>Spark depends on log4j. Providing a log4j.properties file works great during testing and lauching of a spark app within an IDE.
Spark, however, ignores a jar-based log4j.properties file whether a job is run by spark-submit.sh or SparkLauncher. You have to
place a log4j.properties file in the $SPARK_HOME/conf directory. A log4j.properties.template file is provided in the same directory.

Output
------
>Output is directed to these directories:

1. ./target/output/test
2. ./target/output/main
