(ns adventofcode2020.day10
  (:require [clojure.java.io :as io]))

(def test-input (->> "day10_sample.txt" io/resource io/reader line-seq (mapv #(Long/parseLong %))))
(def input (->> "day10.txt" io/resource io/reader line-seq (mapv #(Long/parseLong %))))

(->> (sort input)
     (partition 2 1)
     (map (fn [[a b]] (- b a)))
     (frequencies)
     (map (comp inc val))
     (reduce *))
;; => 2738
