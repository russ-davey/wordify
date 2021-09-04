(ns wordify.number-wordify
  (:require [wordify.conversion-utils :refer [safe-parse-int safe-parse-big-int]]
            [clojure.string :as str])
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

(def ^:private large-numbers {93 "trigintillion"
                              90 "novemvigintillion"
                              87 "octovigintillion"
                              84 "septenvigintillion"
                              81 "sesvigintillion"
                              78 "quinvigintillion"
                              75 "quattuorvigintillion"
                              72 "tresvigintillion"
                              69 "duovigintillion"
                              66 "unvigintillion"
                              63 "vigintillion"
                              60 "novemdecillion"
                              57 "octodecillion"
                              54 "septendecillion"
                              51 "sexdecillion"
                              48 "quindecillion"
                              45 "quattuordecillion"
                              42 "tredecillion"
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

(def ^:private large-numbers-extended
  (reduce (fn [new-map [mag-int mag]]
            (if (> mag-int 2)
              (merge new-map {(+ 1 mag-int) (str "ten-" mag)
                              (+ 2 mag-int) (str "hundred-" mag)})
              new-map))
          (merge large-numbers {1 "ten"})
          large-numbers))

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
  ([i]
   (get-order-of-magnitude i large-numbers))
  ([i number-map]
   (when-not (zero? i)
     (let [magnitude (get-magnitude-number i)]
       (first (-> (filter (fn [[large-number-int _]]
                            (>= magnitude large-number-int))
                          number-map)
                  sort
                  reverse))))))

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

(defn- int->words
  [i]
  (let [s (str i)
        str-len (count s)]
    (cond
      (< i 0) (str "negative "
                   (int->words (- i)))
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
              (when-not (= i rest-words)
                (if (zero? rest-words)
                  (str (int->words first-word) " " large-number-str)
                  (str (int->words first-word)
                       " "
                       large-number-str
                       conjunctive
                       (int->words rest-words))))))))

(defn int-number->words
  [i]
  (when (within-acceptable-params? i)
    (int->words i)))

(defn string-number->words
  [i]
  (when-let [number (safe-parse-big-int i)]
    (int-number->words number)))

(defn double-number->words
  [i]
  (when-let [[int-number dec-number] (str/split (str i) #"\.")]
    (let [[_ magnitude] (get-order-of-magnitude (-> dec-number safe-parse-big-int (* 10))
                                                large-numbers-extended)]
      (str (string-number->words int-number)
           " and "
           (string-number->words dec-number)
           (when magnitude
             (str " " magnitude "ths"))))))

(def ^:private large-symbol->currency
  {"£" ["pound" "pence"]
   "$" "dollar"
   "€" "euro"
   "¥" "yen"})

(defn currency-number->words
  [c]
  (let [[large-symbol small-symbol] (large-symbol->currency (subs c 0 1))
        value (str/trim (subs c 1))
        [large-value small-value] (str/split value #"\.")]
    (str (string-number->words large-value) " " large-symbol
         (when (not (= "1" large-value))
           "s")
         (when small-value
           (str " and " (string-number->words small-value) " " small-symbol)))))