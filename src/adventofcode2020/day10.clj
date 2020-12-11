(ns adventofcode2020.day10
  (:require [clojure.java.io :as io]))

(def test-input1 (->> "day10_sample1.txt" io/resource io/reader line-seq (mapv #(Long/parseLong %))))
(def test-input (->> "day10_sample.txt" io/resource io/reader line-seq (mapv #(Long/parseLong %))))
(def input (->> "day10.txt" io/resource io/reader line-seq (mapv #(Long/parseLong %))))

(->> (sort input)
     (partition 2 1)
     (map (fn [[a b]] (- b a)))
     (frequencies)
     (map (comp inc val))
     (reduce *))
;; => 2738

;; part 2

;; Solution: https://old.reddit.com/r/Clojure/comments/kar4rz/some_toying_around_with_advent_of_code_day_10/gfcn0wo/

(defn part2 [input]
  (let [input        (sort (conj input 0 (+ 3 (apply max input))))
        diffs        (map - (rest input) input)
        permutations {1 2 ; Two possibilities when size one sequence. Include or not include.
                      2 4 ; Four possibilities for size 2 sequence
                      3 7 ; Seven not eight because one can't ommit all (step larger than 4)
                      }]
    (->> diffs
         (partition-by identity)
         (filter #(= (first %) 1)) ; no size two sequence :)
         (keep #(seq (rest %)))
         (map #(get permutations (count %)))
         (apply *))))

(part2 test-input)
;; => 19208

(part2 input)
;; => 74049191673856

;; part2 naive solution ;)

(defn last-element  [input]
  (+ 3 (apply max input)))

(defn full-input [last-element input]
  (vec (concat [0] (-> input sort) [last-element])))

(defn diffs-for-step [input window-size]
  (->> (sort (full-input (last-element input) input))
       (partition window-size 1)
       (map (fn [x] (- (last x) (first x))))
       (filter #(< % 3))
       (frequencies)))

(defn middles [input window-size]
  (->> (sort (full-input (last-element input) input))
       (partition window-size 1)
       (reduce (fn [acc x]
                 (let [diff (- (last x) (first x))]
                   (if (< diff 3)
                     (update acc diff conj (second x))
                     acc
                     )))
               {})))

(defn proper-combination? [last-element input]
  (->> (full-input last-element input)
       (sort)
       (partition 2 1)
       (map (fn [[a b]] (- b a)))
       (every? #(<= % 3))))

(defn cart
  ([xs]
   xs)
  ([xs ys]
   (mapcat (fn [x] (map (fn [y] (list x y)) ys)) xs))
  ([xs ys & more]
   (mapcat (fn [x] (map (fn [z] (cons x z)) (apply cart (cons ys more)))) xs)))

(defn positions-to-remove [size n]
  (if (> n 1)
    (->> (apply cart (repeat n (range size)))
         (filter #(apply distinct? %))
         (map set)
         set)
    (map (comp set vector) (range size))))

(defn vec-remove
  [coll pos]
  (vec (concat (subvec coll 0 pos) (subvec coll (inc pos)))))

(defn with-removed-at-positions [input positions]
  (->> (map-indexed vector input)
       (remove (fn [[i _]] (contains? positions i)))
       (mapv second)))

(defn count-valid-for-n-removed [input n]
  (let [last-element  (+ 3 (apply max input))
        size          (count input)
        pos-to-remove (positions-to-remove size n)]
    (->> pos-to-remove
         (map #(with-removed-at-positions input %))
         (filter (partial proper-combination? last-element))
         (count))))
