(ns adventofcode2020.day8
  (:require [clojure.java.io :as io]))

(def test-input (->> "day9_sample.txt" io/resource io/reader line-seq (mapv #(Long/parseLong %))))
(def input (->> "day9.txt" io/resource io/reader line-seq (mapv #(Long/parseLong %))))

(defn valid-for-preamble? [preamble n]
  (some #(= n %) (for [a preamble
                       b preamble
                       :when (not= a b)]
                   (+ a b))))

(defn invalid-input [input preamble-size]
  (let [valid? (fn [x]
                 (when-not (valid-for-preamble? (take preamble-size x) (last x))
                   (last x)))]
    (->> (partition (inc preamble-size) 1 input)
         (map valid?)
         (filter identity)
         (first))))

(invalid-input test-input 5)
;; => 127
(invalid-input input 25)
;; => 1398413738
