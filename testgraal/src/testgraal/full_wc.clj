(ns testgraal.full_wc
  (:require [clojure.core.async :as async :refer [>! <! go-loop chan close! <!!]])
  (:import (java.io BufferedReader FileReader FileInputStream BufferedInputStream InputStreamReader)))

(def one-meg (* 1024 1024))

(defn ^FileInputStream input-stream [^String fname]
  (FileInputStream. fname))

(defn count-newlines [^bytes barray]
  (let [num-bytes (alength barray)]
    (loop [i 0
           newlines 0]
      (if (>= i num-bytes)
        newlines
        (if (= 10 (aget ^bytes barray i))
          (recur (inc i)
                 (inc newlines))
          (recur (inc i)
                 newlines))))))

(defn wc-doot
  "get wc for file"
  [file]
  (with-open [file-stream (FileInputStream. file)]
    (let [channel (chan 500)
          pr (clojure.pprint/pprint "meow")
        counters (for [_ (range 4)]
                   (go-loop [newline-count 0]
                     (let [barray (async/<! channel)]
                       (if (nil? barray)
                         newline-count
                         (recur (+ newline-count
                                   (count-newlines barray)))))))]
    (go-loop []
      (let [barray (byte-array one-meg)
            bytes-read (.read file-stream barray)]

        ;; this put will block if there are more than 500MBs waiting in channel
        ;; so as to not engorge the heap (learnt the hard way)
        (>! channel barray)

        (if (> bytes-read 0)                                ;; .read returns a -1 on EOF
          (recur)
          (close! channel))))

    (reduce + (map <!! counters)))))

(wc-doot "/run/media/price/99261abe-e432-4914-87b7-822514c46e19/june_t_and_e_logs/06_19-logs/sp2tne_06_19_2/results_builder.log")
