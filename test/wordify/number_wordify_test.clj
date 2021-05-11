(ns wordify.number-wordify-test
  (:require [clojure.test :refer :all]
            [wordify.number-wordify :as target]
            [wordify.data.test-data :as td]))

(deftest negative-numbers-test
  (let [results (target/int-number->words -101)]
    (is (= "negative one hundred and one" results))))

(deftest lower-number-test
  (let [test-numbers (range 0 21)
        results (map target/int-number->words test-numbers)]
    (is (= (take 21 td/numbers-as-words) results))))

(deftest higher-number-test
  (let [test-numbers (range 21 100)
        results (map target/int-number->words test-numbers)]
    (is (= (drop 21 td/numbers-as-words) results))))

(deftest large-number-test
  (testing "hundreds"
    (let [test-numbers [105 123 500]
          results (map target/int-number->words test-numbers)]
      (is (= "one hundred and five" (first results)))
      (is (= "one hundred and twenty three" (second results)))
      (is (= "five hundred" (nth results 2)))))

  (testing "thousands"
    (let [test-numbers [1005 1042 1105]
          results (map target/int-number->words test-numbers)]
      (is (= "one thousand and five" (first results)))
      (is (= "one thousand and forty two" (second results)))
      (is (= "one thousand one hundred and five" (nth results 2)))))

  (testing "tens of thousands"
    (let [test-numbers [10005 50123]
          results (map target/int-number->words test-numbers)]
      (is (= "ten thousand and five" (first results)))
      (is (= "fifty thousand one hundred and twenty three" (second results)))))

  (testing "hundreds of thousands"
    (let [test-numbers [112345 500000 500123]
          results (map target/int-number->words test-numbers)]
      (is (= "one hundred and twelve thousand three hundred and forty five" (first results)))
      (is (= "five hundred thousand" (second results)))
      (is (= "five hundred thousand one hundred and twenty three" (nth results 2)))))

  (testing "millions"
    (let [test-numbers [56945781 999999999]
          results (map target/int-number->words test-numbers)]
      (is (= "fifty six million nine hundred and forty five thousand seven hundred and eighty one" (first results)))
      (is (= "nine hundred and ninety nine million nine hundred and ninety nine thousand nine hundred and ninety nine" (second results))))))

(deftest extra-large-number-test
  (testing "billions"
    (let [test-numbers [7439647773 74396477730]
          results (map target/int-number->words test-numbers)]
      (is (= "seven billion four hundred and thirty nine million six hundred and forty seven thousand seven hundred and seventy three" (first results)))
      (is (= "seventy four billion three hundred and ninety six million four hundred and seventy seven thousand seven hundred and thirty" (second results)))))

  (testing "quadrillion"
    (let [test-numbers [9999999999999999]
          results (map target/int-number->words test-numbers)]
      (is (= "nine quadrillion nine hundred and ninety nine trillion nine hundred and ninety nine billion nine hundred and ninety nine million nine hundred and ninety nine thousand nine hundred and ninety nine" (first results)))))

  (testing "quintillion"
    (let [test-numbers [9999999999999999999]
          results (map target/int-number->words test-numbers)]
      (is (= "nine quintillion nine hundred and ninety nine quadrillion nine hundred and ninety nine trillion nine hundred and ninety nine billion nine hundred and ninety nine million nine hundred and ninety nine thousand nine hundred and ninety nine" (first results)))))

  (testing "tredecillion"
    (let [test-numbers (bigint (apply str (repeat 45 "9")))
          results (target/int-number->words test-numbers)]
      (is (= "nine hundred and ninety nine tredecillion nine hundred and ninety nine duodecillion nine hundred and ninety nine undecillion nine hundred and ninety nine decillion nine hundred and ninety nine nonillion nine hundred and ninety nine octillion nine hundred and ninety nine septillion nine hundred and ninety nine sextillion nine hundred and ninety nine quintillion nine hundred and ninety nine quadrillion nine hundred and ninety nine trillion nine hundred and ninety nine billion nine hundred and ninety nine million nine hundred and ninety nine thousand nine hundred and ninety nine"
             results))))

  (testing "vigintillion"
    (let [test-numbers (bigint (str "1" (apply str (repeat 65 "0"))))
          results (target/int-number->words test-numbers)]
      (is (= "one hundred vigintillion"
             results)))))

(deftest numbers-as-strings)

(deftest numbers-as-doubles
  (let [test-numbers [1.2 1.23 5.6 567.232]
        results (map target/double-number->words test-numbers)]
    (is (= "one and two tenths" (first results)))
    (is (= "one and twenty three hundredths" (second results)))
    (is (= "five and six tenths" (nth results 2)))
    (is (= "five hundred and sixty seven and two hundred and thirty two thousandths" (nth results 3)))))

(deftest unhappy-paths
  ; given a string returns nil
  (is (= nil (target/int-number->words "1")))
  ; given a string without numbers returns nil
  (is (= nil (target/int-number->words "dsfdsfds")))
  ; given a keyword returns nil
  (is (= nil (target/int-number->words :34)))
  ; given a float returns nil
  (is (= nil (target/int-number->words 100.03434)))
  ; given the max number, does not return nil
  (is (not (nil? (target/int-number->words 7439647657730540004))))
  ; given a number above max returns nil
  (is (= nil (target/int-number->words (bigint (apply str (repeat 47 "9")))))))