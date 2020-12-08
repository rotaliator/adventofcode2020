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
        contains-map  (into {} (map (juxt last second) contains))]
    [bag contains-map]))

(def parsed-input (mapv parse-line input))

(def input-as-map (into {} parsed-input))

(def input-as-map-rev (->> (for [[k vals] input-as-map]
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
