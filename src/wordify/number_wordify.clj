(ns wordify.number-wordify
  (:require [clojure.string :as string]
            [wordify.conversion-utils :refer [safe-parse-int safe-parse-big-int]])
  (:import (clojure.lang BigInt)))

(def ^:private lower-numbers {"0" "zero"
                              "1" "one"
                              "2" "two"
                              "3" "three"
                              "4" "four"
                              "5" "five"
                              "6" "six"
                              "7" "seven"
                              "8" "eight"
                              "9" "nine"
                              "10" "ten"
                              "11" "eleven"
                              "12" "twelve"
                              "13" "thirteen"
                              "14" "fourteen"
                              "15" "fifteen"
                              "16" "sixteen"
                              "17" "seventeen"
                              "18" "eighteen"
                              "19" "nineteen"
                              "20" "twenty"})

(def ^:private higher-numbers {"2" "twenty"
                               "3" "thirty"
                               "4" "forty"
                               "5" "fifty"
                               "6" "sixty"
                               "7" "seventy"
                               "8" "eighty"
                               "9" "ninety"})

(def ^:private large-numbers {42 "tredecillion"
                              39 "duodecillion"
                              36 "undecillion"
                              33 "decillion"
                              30 "nonillion"
                              27 "octillion"
                              24 "septillion"
                              21 "sextillion"
                              18 "quintillion"
                              15 "quadrillion"
                              12 "trillion"
                              9 "billion"
                              6 "million"
                              3 "thousand"
                              2 "hundred"})

(defn- between-zero-and-one-hundred?
  [i]
  (when i
    (and (> i 0) (< i 100))))

(defn- get-magnitude-number
  [n]
  (-> n str count (- 1)))

(defn- get-order-of-magnitude
  "given an integer convert to a base10 integer and return the matching magnitude and large number string
  from the large-numbers map"
  [i]
  (when-not (zero? i)
    (let [magnitude (get-magnitude-number i)]
      (first (-> (filter (fn [[large-number-int _]]
                           (>= magnitude large-number-int))
                         large-numbers)
                 sort
                 reverse)))))

(defn- has-no-remainder?
  "given an integer divides by 10, returns true if there are no remainders and false if there are"
  [i] (zero? (rem i 10)))

(defn- within-acceptable-params?
  "given a number, validates that it is within acceptable parameters (i.e. is an int)"
  [number]
  (let [int-or-big-int (or (int? number)
                           (instance? BigInt number))
        within-range (<= (get-magnitude-number number) (+ (apply max (keys large-numbers)) 2))]
    (every? true? [int-or-big-int within-range])))

(defn- int->words-converter
  [i]
  (-> (let [s (str i)
            str-len (count s)]
        (cond
          (< i 0) (str "negative "
                       (int->words-converter (- i)))
          (<= i 20) (lower-numbers s)
          (< i 100) (let [first-word (subs s 0 1)
                          second-word (subs s 1 2)]
                      (if (has-no-remainder? i)
                        (higher-numbers first-word)
                        (str (higher-numbers first-word) " " (lower-numbers second-word))))
          :else (let [[magnitude large-number-str] (get-order-of-magnitude i)
                      offset (- str-len magnitude)
                      first-word (-> (subs s 0 offset)
                                     safe-parse-int)
                      rest-words (-> (subs s offset str-len)
                                     safe-parse-big-int)
                      conjunctive (if (between-zero-and-one-hundred? rest-words)
                                    " and "
                                    " ")]
                  (if (zero? rest-words)
                    (str (int->words-converter first-word) " " large-number-str)
                    (str (int->words-converter first-word)
                         " "
                         large-number-str
                         conjunctive
                         (int->words-converter rest-words))))))
      string/trim))

(defn int->words
  [i]
  (when (within-acceptable-params? i)
    (int->words-converter i)))

(defn string->words
  [i]
  (when-let [number (safe-parse-big-int i)]
    (int->words number)))