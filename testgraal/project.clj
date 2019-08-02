(defproject testgraal "0.1.0-SNAPSHOT"
  :description "My first test with GraalVM"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [cli-matic "0.3.7"]
                 [org.clojure/core.async "0.4.500"]]
  :main testgraal.core
  :aot  :all)
