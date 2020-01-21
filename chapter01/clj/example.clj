(ns example
  (:require [clojure.string :as str])
  (:import (java.text NumberFormat)
           (java.util Locale)))

(defn statement [invoice plays]
  (let [total-amount (volatile! 0)
        volume-credits (volatile! 0)
        fmt (NumberFormat/getCurrencyInstance Locale/US)
        result (reduce (fn [result perf]
                         (let [play (get plays (:play-id perf))
                               this-amount (case (:type play)
                                             :tragedy (cond-> 40000
                                                        (> (:audience perf) 30)
                                                        (+ (* 1000 (- (:audience perf) 30))))
                                             :comedy (cond-> (+ 30000 (* 300 (:audience perf)))
                                                       (> (:audience perf) 20)
                                                       (+ 10000 (* 500 (- (:audience perf) 20))))
                                             (throw (Exception. (str "Unknown type: " (:type play)))))]
                           ;; ボリューム特典のポイントを加算
                           (vswap! volume-credits + (cond-> (max (- (:audience perf) 30) 0)
                                                      ;; 喜劇のときは10人につき、さらにポイントを加算
                                                      (= :comedy (:type play))
                                                      (+ (long (/ (:audience perf) 5)))))
                           (vswap! total-amount + this-amount)
                           ;; 注文の内訳を出力
                           (conj result (format "  %s: %s (%s seats)\n"
                                                (:name play)
                                                (.format fmt (/ this-amount 100))
                                                (:audience perf)))))
                       [(str "Statement for " (:customer invoice) "\n")]
                       (:performances invoice))]
    (str/join (conj result
                    (str "Amount owed is " (.format fmt (/ @total-amount 100)) "\n")
                    (str "You earned " @volume-credits " credits\n")))))

(def plays
  {"hamlet" {:name "Hamlet"
             :type :tragedy}
   "as-like" {:name "As You Like It"
              :type :comedy}
   "othello" {:name "Othello"
              :type :tragedy}})

(def invoices
  [{:customer "BigCo"
    :performances [{:play-id "hamlet"
                    :audience 55}
                   {:play-id "as-like"
                    :audience 35}
                   {:play-id "othello"
                    :audience 40}]}])

(defn -main []
  (doseq [invoice invoices]
    (println (statement invoice plays))))
