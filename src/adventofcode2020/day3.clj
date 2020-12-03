(ns adventofcode2020.day3
  (:require [clojure.java.io :as io]))

(def input (-> "day3.txt"
               io/resource
               io/reader
               line-seq
               vec))

(def sample-input
  ["..##......."
   "#...#...#.."
   ".#....#..#."
   "..#.#...#.#"
   ".#...##..#."
   "..#.##....."
   ".#.#.#....#"
   ".#........#"
   "#.##...#..."
   "#...##....#"
   ".#..#...#.#"])

(def width (count (first input)))
(def height (count input))

(defn get-x-y [x y input]
  (let [x (mod x width)
        y y]
    (get-in input [y x])))

(defn count-trees [dx dy input]
  (loop [count 0
         x     0
         y     0]
    (if (< y height)
      (recur (if (= (get-x-y x y input) \#) (inc count) count) (+ x dx) (+ y dy))
      count)))

(def slopes [[1 1]
             [3 1]
             [5 1]
             [7 1]
             [1 2]])

(comment
  (count-trees 3 1 input)
;; => 220
  (reduce * (map #(count-trees (first %) (second %) input) slopes))
;; => 2138320800
  )
