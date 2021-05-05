(ns wordify.core
  (:gen-class)
  (:require [wordify.number-wordify :refer [int->words]]))

(comment
  " ROADMAP:
  Support decimals?
  Go beyond 1 trillion
  multi-lingual?
  Phone number to words i.e. 01254 = zero one two five four"
  )

(defn wordify-number
  "Given an integer of zero or above, returns the equivalent number in British English words,
  otherwise returns nil"
  [i]
  (int->words i))