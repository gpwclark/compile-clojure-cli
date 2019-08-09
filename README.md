# initial
-	building the Dockerfile `docker build -t clj-graal .`, compiles the project
in testgraal to a binary.
-	to get compiled binary either use docker cp or mount a volume where the
binary is made `/home/clj/app/target/testgraal-0.1.0-SNAPSHOT-standalone`


# credit
-	blueprint for project laid down by https://www.astrecipes.net/blog/2018/07/20/cmd-line-apps-with-clojure-and-graalvm/
