(ns adventofcode2020.day8
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def input (-> "day8.txt" io/resource io/reader line-seq))
(def test-input (-> "nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6" str/split-lines))

(def parsed-input (->> input
                       (map #(str/split % #"\s"))
                       (mapv (fn [[op n]] [(keyword op) (read-string n)]))))

(defn run [input]
  (loop [a   0
         i   0
         ins #{}]
    (let [[opcode value] (get input i [nil nil])]
      (if opcode
        (if-not (contains? ins i)
          (case opcode
            :nop (recur a           (inc i)     (conj ins i))
            :jmp (recur a           (+ i value) (conj ins i))
            :acc (recur (+ a value) (inc i)     (conj ins i)))
          a)
        a))))

(comment
  (run parsed-input)
;; => 1814
  )
