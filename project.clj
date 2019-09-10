(defproject testgraal "0.1.0-SNAPSHOT"
  :description "My first test with GraalVM"
  :plugins [[io.taylorwood/lein-native-image "0.3.1"]]    ;; or in ~/.lein/profiles.clj
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [cli-matic "0.3.7"]
                 [org.clojure/core.async "0.4.500"]]
  :jvm-opts ["-Dclojure.compiler.direct-linking=true"]
  :main testgraal.core
  :aot :all
  :native-image {:name "testgraal.bin"                 ;; name of output image, optional
                ;; graal-bin "/path/to/graalvm/" ;; path to GraalVM home, optional
                 :opts ["--report-unsupported-elements-at-runtime"
                                    "--initialize-at-build-time"
                        "-J-Xmx3Ga"
                        "-J-Xms3G"]}           ;; pass-thru args to GraalVM native-image, optional
  )
