(ns adventofcode2020.day6
  (:require [clojure.string :as str]))

(def test-input
  "abc

a
b
c

ab
ac

a
a
a
a

b")

(def input (slurp "resources/day6.txt"))

(comment
  (->> (str/split input #"\R\R")
       (map #(str/replace % "\n" ""))
       (map set)
       (map count)
       (reduce +))
  ;; => 6799
  )
