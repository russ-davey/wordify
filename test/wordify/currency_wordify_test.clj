(ns wordify.currency-wordify-test
  (:require [clojure.test :refer :all]
            [wordify.number-wordify :as target]))

(deftest currency-happy-paths
  (let [test-currencies ["£0.23" "£1" "£1.23" "£5.67" "£567.32" "£1234567.53" "£1.00" "£1.01" "$1.54" "$1.01"]
        results (map target/currency-number->words test-currencies)]
    (is (= "twenty three pence" (first results)))
    (is (= "one pound" (second results)))
    (is (= "one pound and twenty three pence" (nth results 2)))
    (is (= "five pounds and sixty seven pence" (nth results 3)))
    (is (= "five hundred and sixty seven pounds and thirty two pence" (nth results 4)))
    (is (= "one million two hundred and thirty four thousand five hundred and sixty seven pounds and fifty three pence" (nth results 5)))
    (is (= "one pound" (nth results 6)))
    (is (= "one pound and one penny" (nth results 7)))
    (is (= "one dollar and fifty four cents" (nth results 8)))
    (is (= "one dollar and one cent" (nth results 9)))))

(deftest currency-unhappy-paths
  (testing "no currency symbol"
    (is (= nil (target/currency-number->words "1.23")))))