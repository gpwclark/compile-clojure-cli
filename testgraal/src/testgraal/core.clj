(ns testgraal.core
  (:require [cli-matic.core :refer [run-cmd]]
            [testgraal.full_wc :as wc])
  (:gen-class))

(defn add_numbers
  "Sums A and B together, and prints it in base `base`"
  [{:keys [a1 a2 base]}]
  (println
    (Integer/toString (+ a1 a2) base)))

(defn subtract_numbers
  "Subtracts B from A, and prints it in base `base` "
  [{:keys [pa pb base]}]
  (println
    (Integer/toString (- pa pb) base)))

(defn trim-logs
  "Given a filepath, a regexp for filenames, and a reduction factor, shrink
  the logs appropriately. Default suffix to save original log is by suffixing
  the original filename with .orig."
  [{:keys [fp base]}]
   (let [word-count (wc/wc fp)]
     (println (apply str word-count ":" fp))))

(def CONFIGURATION
  {:app         {:command     "toycalc"
                 :description "A command-line toy calculator"
                 :version     "0.1"}
   :global-opts [{:option  "base"
                  :as      "The number base for output"
                  :type    :int
                  :default 10}]
   :commands    [{:command     "add" :short "a"
                  :description ["Adds two numbers together"
                                ""
                                "Looks great, doesn't it?"]
                  :opts        [{:option "a1" :short "a" :env "AA" :as "First addendum" :type :int :default 0}
                                {:option "a2" :short "b" :as "Second addendum" :type :int :default 0}]
                  :runs        add_numbers}
                 {:command     "sub"  :short "s"
                  :description "Subtracts parameter B from A"
                  :opts        [{:option "pa" :short "a" :as "Parameter A" :type :int :default 0}
                               {:option "pb" :short "b" :as "Parameter B" :type :int :default 0}]
                  :runs        subtract_numbers}
                 {:command     "trim-logs" :short "t"
                  :description "Trims logs to specified percentage of original."
                  :opts        [{:option "fp" :short "f" :as "filename" :type :string  :default "/tmp"}
                                 ]
                  :runs        trim-logs}]})

(defn -main
  "This is our entry point.
  Just pass parameters and configuration.
  Commands (functions) will be invoked as appropriate."
  [& args]
  (run-cmd args CONFIGURATION))

