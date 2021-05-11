(ns wordify.core
  (:gen-class)
  (:require [wordify.number-wordify :refer [int-number->words string-number->words
                                            double-number->words]]))

(defn wordify-number
  "Given an integer of zero or above, returns the equivalent number in British English words,
  otherwise returns nil"
  [n]
  (condp apply [n]
    string? (string-number->words n)
    double? (double-number->words n)
    (int-number->words n)))