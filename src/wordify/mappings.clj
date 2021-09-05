(ns wordify.mappings)

(def lower-numbers {"0" "zero"
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

(def higher-numbers {"2" "twenty"
               "3" "thirty"
               "4" "forty"
               "5" "fifty"
               "6" "sixty"
               "7" "seventy"
               "8" "eighty"
               "9" "ninety"})

(def orders-of-magnitude {93 "trigintillion"
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
                          9  "billion"
                          6  "million"
                          3  "thousand"
                          2  "hundred"})

;TODO: work on plurals
(def large-symbol->currency
  {"£" ["pound" "pence"]
   "$" ["dollar" "cent"]
   "€" ["euro" "cent"]
   "¥" "yen"})