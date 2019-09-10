<<<<<<< HEAD
# initial
-	building the Dockerfile `docker build -t clj-graal .`, compiles the project
in testgraal to a binary.
-	to get compiled binary either use docker cp or mount a volume where the
binary is made `/home/clj/app/target/testgraal-0.1.0-SNAPSHOT-standalone`


# credit
-	blueprint for project laid down by https://www.astrecipes.net/blog/2018/07/20/cmd-line-apps-with-clojure-and-graalvm/
=======
# testgraal

A Clojure library designed to ... well, that part is up to you.

## Usage
-	build 
``
./build.sh
```
-	to use
```
./run.sh --help
```
-	this scripts uses entyrpoint/command tricks to essentially turn the compiled
clj app into an "executable" docker container. If this is not what you want I 
recommend starting the container, and using "docker cp" to extract the executable
or using the -v parameter to get the binary out of the running container.
## Compare
To test the difference between the two...
```
docker run -it --entrypoint /bin/bash testgraal
```
time java -jar ./target/testgraal --help
```
	on my hardware: `target/testgraal --help  0.00s user 0.00s system 96% cpu 0.004 total`
versus
```
lein uberjar
time java -jar ./target/testgraal-0.1.0-SNAPSHOT-standalone.jar --help
```
	on my hardware: `java -jar testgraal-0.1.0-SNAPSHOT-standalone.jar --help  3.68s user 0.19s system 216% cpu 1.787 total`

## License

Copyright © 2019 FIXME

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
>>>>>>> 530df0a... incorporated native image plugin and added build instructions
