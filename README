So far, you have used gradle for "single project" setups, where you have one program
that you run. For RISC, you probably want a "multi project" setup, where you can have
multiple programs (e.g., a server and a client) and they can share some common code.

As "messing with the details of gradle setup" is not a primary learning objective of
this course, we have provided the enclosed gradle setup to get you started.  If you look
inside the gradle-skeleton directory, you will see three directories

 - client : the code for the client
 - server : the code for the server
 - shared : for code that is shared between the client and the server

If you take a look, you will see that we have setup a few placeholder classes:

 - shared/src/main/java/edu/duke/ece651/teamX/shared/MyName.java
   is a class shared between the client and server.  This placeholder
   class just has getName(), but in RISC you might put things here
   like Territory, Map, etc.
 - client/src/main/java/edu/duke/ece651/teamX/client/App.java
   is the "client" code.  It makes use of the shared class MyName
   and calls MyName.getName() its getMessage() method.
 - server/src/main/java/edu/duke/ece651/teamX/server/App.java
   is the "server" code.  It also uses MyName.getName() for
   its getMessage() method. (Note there is nothing special
   about getMessage() nor do real client/servers need those---
   our placeholders just print a message).

You can run the client fro the top-level (gradle-skeleton directory)
with

./gradlew run-client

which will print:

> Task :client:run
Hello from the client for teamX

(and then some gradle warnings about features deprecated in 8.0)

Likewise you can run the server with

./gradlew run-server

which will print:

> Task :server:run
Hello from the server for teamX

(and then the gradle deprecation warnings)

Note that everything here is in a package for "teamX".  Best practices
are to replace that with your team (like team1, team2, team3) so
that your package names are unique.

If you open the code for the provided files in Emacs and run test
coverage (C-c C-t) you will get test coverage for that particular
sub-project.  E.g., if you are editing server code, you will get
test coverage for the server code.  If you are editing shared
code, you will get test coverage for the shared code. That is
as-intended.

However, you will need to make a small change to your CI scripts
to aggregate the reports and get them from the right place.

First, the dcoverage.el in factor server is slightly outdated
(which is no big deal for that project), so you should use the
one installed in your dev-setup (~/.emacs.d/dcoverage/dcoverage.el).
We've also included that in this directory.

Second, the scripts/test.sh file in the CI/CD tutorial (factor server) says:

#!/bin/bash

./gradlew build || exit 1
./gradlew cloverGenerateReport || exit 1
scripts/coverage_summary.sh
cp -r app/build/reports/clover/html/* /coverage-out/ || exit 1

This script needs two changes:

#!/bin/bash

./gradlew build || exit 1
./gradlew cloverGenerateReport || exit 1
# Change 1: run cloverAggregateReport
./gradlew cloverAggregateReport || exit 1
scripts/coverage_summary.sh
# Change 2: remove "app/" from this path
cp -r build/reports/clover/html/* /coverage-out/ || exit 1


We've included that updated file here for you as well.

With that, you should be able to get your multi-project gradle setup,
and get your CI/CD setup!












