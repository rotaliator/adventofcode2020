(ns adventofcode2020.day2
  (:require [clojure.string :as str]))

(def input (->> "resources/day2.txt"
                slurp
                str/split-lines
                (mapv #(str/split % #"[-: ]"))
                (mapv (fn [x] {:from (read-string (first x))
                               :to   (read-string (second x))
                               :char (first (get x 2))
                               :pass (get x 4)}))))

(def input' (->> ["1-3 a: abcde"
                 "1-3 b: cdefg"
                 "2-9 c: ccccccccc"]
                 (mapv #(str/split % #"[-: ]"))
                 (mapv (fn [x] {:from (read-string (first x))
                                :to   (read-string (second x))
                                :char (first (get x 2))
                                :pass (get x 4)}))))

(defn valid-password? [{:keys [from to char pass]}]
  (let [char-count (count (filter #(= char %) pass))]
    (<= from char-count to)))

(comment
  (count (filter valid-password? input))
  ;; => 546
  )

;;part 2

(defn valid-password-2? [{:keys [from to char pass]}]
  (let [c1 (get pass (dec from))
        c2 (get pass (dec to))]
    (= 1 (count (filter #(= char %) [c1 c2])))))

(comment
 (valid-password-2? {:from 13, :to 17, :char \s, :pass "ssssssssssssgsssj"})
 (valid-password-2? {:from 1, :to 3, :char \a, :pass "abcde"})

 (count (filter valid-password-2? input))
 ;; => 275
 )
