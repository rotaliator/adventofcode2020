(ns adventofcode2020.day8
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def test-input (-> "day8_sample.txt" io/resource io/reader line-seq))
(def input (-> "day8.txt" io/resource io/reader line-seq))

(defn parse-input [input]
  (->> input
       (map #(str/split % #"\s"))
       (mapv (fn [[op n]] [(keyword op) (read-string n)]))))

(def parsed-test-input (parse-input test-input))
(def parsed-input (parse-input input))

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

;; part 2

(defn run-repaired [input]
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
          nil)
        a))))

(def fix {:jmp :nop
          :nop :jmp})

(defn fix-at-instr [input i]
  (update-in input [i 0] fix))

(defn repeirable [input]
  (->> input
       (map-indexed vector)
       (filter (comp #{:nop :jmp} first second))
       (mapv first)))

(defn run-with-fix [input]
  (->> (map (comp run-repaired (partial fix-at-instr input))
            (repeirable input))
       (filter identity)
       (first)))

(comment
  (run-with-fix parsed-test-input)

  (run-with-fix parsed-input)
;; => 1056
  )










(fix-at-instr 7)




(run-repaired (fix-at-instr 7))
