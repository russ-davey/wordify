(ns wordify.core
  (:gen-class)
  (:require [wordify.number-wordify :refer [int-number->words string-number->words
                                            double-number->words]]))

(comment
  "ROADMAP:
  Support decimals?
  multi-lingual?
  Phone number to words i.e. 01254 = zero one two five four"
  )

(defn wordify-number
  "Given an integer of zero or above, returns the equivalent number in British English words,
  otherwise returns nil"
  [n]
  (println n)
  (condp apply [n]
    string? (string-number->words n)
    double? (double-number->words n)
    (int-number->words n)))