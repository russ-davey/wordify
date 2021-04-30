(ns wordify-numbers.number-stringify-test
  (:require [clojure.test :refer :all]
            [wordify-numbers.number-stringify :as target]
            [wordify-numbers.data.test-data :as td]))

(deftest lower-number-test
  (let [test-numbers (range 0 21)
        results (map target/int->words test-numbers)]
    (is (= (take 21 td/numbers-as-words) results))))

(deftest higher-number-test
  (let [test-numbers (range 21 100)
        results (map target/int->words test-numbers)]
    (is (= (drop 21 td/numbers-as-words) results))))

(deftest large-number-test
  (testing "hundreds"
    (let [test-numbers [105 123 500]
          results (map target/int->words test-numbers)]
      (is (= "one hundred and five" (first results)))
      (is (= "one hundred and twenty three" (second results)))
      (is (= "five hundred" (nth results 2)))))

  (testing "thousands"
    (let [test-numbers [1005 1042 1105]
          results (map target/int->words test-numbers)]
      (is (= "one thousand and five" (first results)))
      (is (= "one thousand and forty two" (second results)))
      (is (= "one thousand one hundred and five" (nth results 2)))))

  (testing "tens of thousands"
    (let [test-numbers [10005 50123]
          results (map target/int->words test-numbers)]
      (is (= "ten thousand and five" (first results)))
      (is (= "fifty thousand one hundred and twenty three" (second results)))))

  (testing "hundreds of thousands"
    (let [test-numbers [112345 500000 500123]
          results (map target/int->words test-numbers)]
      (is (= "one hundred and twelve thousand three hundred and forty five" (first results)))
      (is (= "five hundred thousand" (second results)))
      (is (= "five hundred thousand one hundred and twenty three" (nth results 2)))))

  (testing "millions"
    (let [test-numbers [56945781 999999999]
          results (map target/int->words test-numbers)]
      (is (= "fifty six million nine hundred and forty five thousand seven hundred and eighty one" (first results)))
      (is (= "nine hundred and ninety nine million nine hundred and ninety nine thousand nine hundred and ninety nine" (second results))))))

(deftest unhappy-paths
  ; given a string returns nil
  (is (= nil (target/int->words "1")))
  ; given a negative int returns nil
  (is (= nil (target/int->words -1)))
  ; given a float returns nil
  (is (= nil (target/int->words 100.03434)))
  ; given the max number, does not return nil
  (is (not (nil? (target/int->words 999999999999997))))
  ; given a number above max returns nil
  (is (= nil (target/int->words 999999999999999))))