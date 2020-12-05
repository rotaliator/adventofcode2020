(ns adventofcode2020.day5
  (:require [clojure.string :as str]
            [clojure.java.io :as io]))

(def test-input
  ["BFFFBBFRRR"
   "FFFBBBFRRR"
   "BBFFBBFRLL"])

(def input (-> "day5.txt"
               io/resource
               io/reader
               line-seq))

(defn to-seat-number [code]
  (-> code
      (str/replace  #"[RB]" "1")
      (str/replace  #"[FL]" "0")
      (as-> i (. Integer parseInt i 2))))

(comment
  (apply max (map to-seat-number test-input))
  (apply max (map to-seat-number input))
;; => 861
  )
