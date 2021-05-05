(ns wordify-numbers.core
  (:gen-class)
  (:require [wordify-numbers.number-stringify :refer [int->words]]))

(comment
  " ROADMAP:
  Support negative ints
  Support decimals?
  Go beyond 1 trillion"
  )

(defn wordify-integers
  "Given an integer of zero or above, returns the equivalent number in British English words,
  otherwise returns nil"
  [i]
  (int->words i))