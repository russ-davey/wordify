(ns wordify.number-wordify
  (:require [wordify.conversion-utils :refer [safe-parse-int safe-parse-big-int]]
            [wordify.mappings :as mappings]
            [clojure.string :as str])
  (:import (clojure.lang BigInt)))

(def ^:private large-numbers-extended
  (reduce (fn [new-map [mag-int mag]]
            (if (> mag-int 2)
              (merge new-map {(+ 1 mag-int) (str "ten-" mag)
                              (+ 2 mag-int) (str "hundred-" mag)})
              new-map))
          (merge mappings/orders-of-magnitude {1 "ten"})
          mappings/orders-of-magnitude))

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
   (get-order-of-magnitude i mappings/orders-of-magnitude))
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
        within-range (<= (get-magnitude-number number) (+ (apply max (keys mappings/orders-of-magnitude)) 2))]
    (every? true? [int-or-big-int within-range])))

(defn- int->words
  [i]
  (let [s (str i)
        str-len (count s)]
    (cond
      (< i 0) (str "negative "
                   (int->words (- i)))
      (<= i 20) (mappings/lower-numbers s)
      (< i 100) (let [first-word (subs s 0 1)
                      second-word (subs s 1 2)]
                  (if (has-no-remainder? i)
                    (mappings/higher-numbers first-word)
                    (str (mappings/higher-numbers first-word) " " (mappings/lower-numbers second-word))))
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

(defn- display-large-value
  [large-value large-symbol]
  (when large-value
    (let [large-value-int (safe-parse-big-int large-value)]
      (when (> large-value-int 0)
        (str (string-number->words large-value) " " large-symbol
             (when (not (= 1 large-value-int))
               "s"))))))

(defn- display-small-value
  [small-value small-symbol]
  (when small-value
    (let [small-value-int (safe-parse-int small-value)]
      (when (> small-value-int 0)
        (str (string-number->words small-value) " " small-symbol)))))

(defn currency-number->words
  [c]
  (when-let [[large-symbol small-symbol] (mappings/large-symbol->currency (subs c 0 1))]
    (let [value (str/trim (subs c 1))
          [large-value small-value] (str/split value #"\.")
          large-value-output (display-large-value large-value large-symbol)
          small-value-output (display-small-value small-value small-symbol)]
      (str large-value-output
           (when (and large-value-output small-value-output)
             " and ")
           small-value-output))))