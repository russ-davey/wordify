(ns wordify-numbers.number-stringify
  (:require [clojure.string :as string]))

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

(def ^:private large-numbers {18 "quintillion"
                              15 "quadrillion"
                              12 "trillion"
                              9 "billion"
                              6 "million"
                              3 "thousand"
                              2 "hundred"})

(defn- safe-parse-long
  "given a string attempts to parse it as an integer"
  [str]
  (try
    (Long/parseLong str)
    (catch NumberFormatException _
      nil)))

(defn- between-zero-and-one-hundred?
  [i]
  (when i
    (and (> i 0) (< i 100))))

(defn- get-order-of-magnitude
  "given an integer convert to a base10 integer and return the matching magnitude and large number string
  from the large-numbers map"
  [i]
  (when-not (zero? i)
    (let [magnitude (int (Math/log10 i))]
      (first (filter (fn [[large-number-int _]]
                       (>= magnitude large-number-int))
                     large-numbers)))))

(defn- has-no-remainder?
  "given an integer divides by 10, returns true if there are no remainders and false if there are"
  [i] (zero? (rem i 10)))

(defn- within-acceptable-params?
  "given a number, validates that it is within acceptable parameters (i.e. is an int)"
  [number]
  (and (int? number)
       (not (neg-int? number))
       (< number 999999999999998)))

(defn int->words
  [i]
  (when (within-acceptable-params? i)
    (-> (let [s (str i)
              str-len (count s)]
          (cond
            (<= i 20) (lower-numbers s)
            (< i 100) (let [first-word (subs s 0 1)
                            second-word (subs s 1 2)]
                        (if (has-no-remainder? i)
                          (higher-numbers first-word)
                          (str (higher-numbers first-word) " " (lower-numbers second-word))))
            :else (let [[magnitude large-number-str] (get-order-of-magnitude i)
                        offset (- str-len magnitude)
                        first-word (-> (subs s 0 offset)
                                       safe-parse-long)
                        rest-words-int (-> (subs s offset str-len)
                                           safe-parse-long)
                        rest-words (when-not (zero? rest-words-int)
                                     rest-words-int)
                        conjunctive (if (between-zero-and-one-hundred? rest-words)
                                      " and "
                                      " ")]
                    (if (has-no-remainder? i)
                      (str (int->words first-word) " " large-number-str)
                      (str (int->words first-word)
                           " "
                           large-number-str
                           conjunctive
                           (int->words rest-words))))))
        string/trim)))