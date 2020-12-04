(ns adventofcode2020.day3
  (:require [clojure.string :as str]
            [clojure.set :refer [subset?]]
            [clojure.spec.alpha :as s]))

(def sample-input (str/split "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd
byr:1937 iyr:2017 cid:147 hgt:183cm

iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884
hcl:#cfa07d byr:1929

hcl:#ae17e1 iyr:2013
eyr:2024
ecl:brn pid:760753108 byr:1931
hgt:179cm

hcl:#cfa07d eyr:2025 pid:166559648
iyr:2011 ecl:brn hgt:59in" #"\n\n"))

(def input (-> "resources/day4.txt" slurp (str/split #"\n\n")))

(defn passport->map [passport]
  (-> passport
      (str/replace #"\n" " ")
      (str/split #" ")
      (->> (map #(str/split % #":")))
      (->> (map (fn [[k v]] [(keyword (namespace-munge *ns*) k) v])))
      (->> (into {}))))

(defn valid-passport? [passport-map]
  (let [required-keys #{::ecl ::pid ::eyr ::hcl ::byr ::iyr ::hgt}
        passport-keys (set (keys passport-map))]
    (subset? required-keys passport-keys)))

(comment
  (valid-passport? (-> sample-input first passport->map))
  (->> input
       (map passport->map)
       (filter valid-passport?)
       count)
  ;; => 208
  )

;; Part 2

;; byr (Birth Year) - four digits; at least 1920 and at most 2002.
(s/def ::byr (fn [x]
               (and (= 4 (.length x))
                    (re-matches #"^\d+" x)
                    (<= 1920 (. Integer parseInt x) 2002))))

;; iyr (Issue Year) - four digits; at least 2010 and at most 2020.
(s/def ::iyr (fn [x]
               (and (= 4 (.length x))
                    (re-matches #"^\d+" x)
                    (<= 2010 (. Integer parseInt x) 2020))))

;; eyr (Expiration Year) - four digits; at least 2020 and at most 2030.
(s/def ::eyr (fn [x]
               (and (= 4 (.length x))
                    (re-matches #"^\d+" x)
                    (<= 2020 (. Integer parseInt x) 2030))))

;; hgt (Height) - a number followed by either cm or in:
;; If cm, the number must be at least 150 and at most 193.
;; If in, the number must be at least 59 and at most 76.
(s/def ::hgt (fn [x]
               (let [[_ val uom] (re-matches #"^(\d+)(in|cm)" x)]
                 (case uom
                   "cm" (<= 150 (. Integer parseInt val) 193)
                   "in" (<= 59 (. Integer parseInt val) 76)
                   false))))

;; hcl (Hair Color) - a # followed by exactly six characters 0-9 or a-f.
(s/def ::hcl #(re-matches #"^#[0-9a-f]{6}" %))

;; ecl (Eye Color) - exactly one of: amb blu brn gry grn hzl oth.
(s/def ::ecl #(get #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"} %))

;; pid (Passport ID) - a nine-digit number, including leading zeroes.
(s/def ::pid #(re-matches #"^[0-9]{9}" %))

;; cid (Country ID) - ignored, missing or not.
(s/def ::cid (constantly true))

(s/def ::passport
  (s/keys :req [::ecl ::pid ::eyr ::hcl ::byr ::iyr ::hgt]
          :opt [::cid]))

(defn valid-passport-2? [passport-map]
  (s/valid? ::passport passport-map))

(comment
  (valid-passport-2? (-> sample-input second passport->map))
  (->> input
       (map passport->map)
       (filter valid-passport-2?)
       count)
;; => 167
  )
