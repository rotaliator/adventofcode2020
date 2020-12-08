(ns adventofcode2020.day7
  (:require [clojure.string :as str]
            [clojure.java.io :as io]
            [clojure.set :refer [union]]))

(def test-input (->
                 "light red bags contain 1 bright white bag, 2 muted yellow bags.
dark orange bags contain 3 bright white bags, 4 muted yellow bags.
bright white bags contain 1 shiny gold bag.
muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.
shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.
dark olive bags contain 3 faded blue bags, 4 dotted black bags.
vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.
faded blue bags contain no other bags.
dotted black bags contain no other bags."
                 str/split-lines))

(def input (-> "day7.txt" io/resource io/reader line-seq))

(defn parse-line [l]
  (let [[bag content] (str/split l #" bags contain ")
        contains      (re-seq #"(\d+) (\w+ \w+)\D+" content)
        contains-map  (into {} (map (juxt last #(Integer/parseInt (second %))) contains))]
    [bag contains-map]))

(def parsed-input (into {} (mapv parse-line input)))

(def input-as-map-rev (->> (for [[k vals] parsed-input]
                                 (for [[bag _] vals]
                                   {bag [k]}))
                          (flatten)
                          (apply merge-with concat)))

(defn in-bags [bag-name]
  (get input-as-map-rev bag-name))

(count (loop [bags #{}
              [bag-to-find & bags-rest] ["shiny gold"]]
         (let [in (in-bags bag-to-find)]
           (if (or (seq in) (seq bags-rest))
             (recur (union bags (set in)) (concat in bags-rest))
             bags))))
;; => 177
;; part 2

(def test-input2
  (->
   "shiny gold bags contain 2 dark red bags.
dark red bags contain 2 dark orange bags.
dark orange bags contain 2 dark yellow bags.
dark yellow bags contain 2 dark green bags.
dark green bags contain 2 dark blue bags.
dark blue bags contain 2 dark violet bags.
dark violet bags contain no other bags."
   str/split-lines))

(def parsed-input2 (into {} (mapv parse-line test-input2)))

(defn bags-in-bag [bag]
  (apply +
         (for [[b quan] (get parsed-input bag)]
           (+ quan (* quan (bags-in-bag b))))))

(bags-in-bag "shiny gold")
;; => 34988
