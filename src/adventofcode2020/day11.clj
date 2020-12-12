(ns adventofcode2020.day10
  (:require [clojure.java.io :as io]))

(def test-input (->> "day11_sample.txt" io/resource io/reader line-seq vec))
(def input (->> "day11.txt" io/resource io/reader line-seq vec))

(defn adjacent-seats [input x y]
  (let [neigh-delta [[-1 -1] [0 -1] [1 -1]
                     [-1  0]        [1  0]
                     [-1  1] [0  1] [1  1]]
        neighbours  (map (fn [[a b]] [(+ a x) (+ b y)]) neigh-delta)]
    (->> neighbours
         (map #(get-in input %))
         (filter identity)
         (frequencies)
         (merge {\L 0 \. 0 \# 0}))))

(defn game-of-chairs-step [input]
  (let [width  (count (first input))
        height (count input)]
    (into []
          (for [y (range width)]
            (into []
                  (for [x (range height)]
                    (let [seat      (get-in input [x y])
                          adj-seats (when (not= seat \.) (adjacent-seats input x y))
                          new-seat  (cond
                                      (and (= seat \L) (= 0 (get adj-seats \#)))
                                      \#

                                      (and (= seat \#) (<= 4 (get adj-seats \#)))
                                      \L

                                      :else
                                      seat)]
                           new-seat)))))))

(defn all-seats [input]
  (-> input
      (flatten)
      (frequencies)))

(defn part1 [input]
  (println)
  (-> (reduce (fn [acc _]
                (print ".")
                (let [new-input (game-of-chairs-step acc)]
                  (if (not= (all-seats acc) (all-seats new-input))
                    new-input
                    (reduced new-input)))
                ) input (range))
      (flatten)
      (frequencies)
      (get \#)))

(part1 test-input)
;; => 37
(part1 input)
;; => 2164
