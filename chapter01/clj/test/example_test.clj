(ns example-test
  (:require [clojure.test :as t]
            [example :as sut]))

(t/deftest test-statement
  (t/is (= ["Statement for BigCo
  Hamlet: $650.00 (55 seats)
  As You Like It: $580.00 (35 seats)
  Othello: $500.00 (40 seats)
Amount owed is $1,730.00
You earned 47 credits
"]
           (for [invoice sut/invoices]
             (sut/statement invoice sut/plays)))))
