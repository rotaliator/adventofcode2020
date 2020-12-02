(ns adventofcode2020.day1
  (:require [clojure.string :as str]))

(def input (->> "resources/day1.txt" slurp str/split-lines (mapv read-string)))

(def input' [1721
             979
             366
             299
             675
             1456])

(comment
  (->> (for [i     input
             j     input
             :when (not= i j)]
         (when (= 2020 (+ i j))
           (* i j)))
       (filter identity)
       (first))
  ;; => 974304
  )

;;part 2
(comment
  (->> (for [i     input
             j     input
             k     input
             :when (not= i j k)]
         (when (= 2020 (+ i j k))
           (* i j k)))
       (filter identity)
       (first))
  ;; => 236430480
  )
