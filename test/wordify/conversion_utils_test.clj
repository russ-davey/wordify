(ns wordify.conversion-utils-test
  (:require [clojure.test :refer :all]
            [wordify.conversion-utils :as target]))

(deftest parse-string-into-integers
  (testing "basic"
    (let [result (target/safe-parse-long "343243243")]
      (is (= 343243243 result)))))

(deftest parse-strings-into-big-int
  (let [result (target/safe-parse-big-integer "9999999999999999999")]
    (is (= 9999999999999999999 result))))