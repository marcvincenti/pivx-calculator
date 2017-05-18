(ns providers.calculator
  (:require [app.utils :as u]
            [app.state :refer [app-state]]))

(def ^:private blocks-per-hour 60)
(def ^:private blocks-per-day (* 24 blocks-per-hour))
(def ^:private blocks-per-month (* 30 blocks-per-day))

(defn ^:private calculate-stake-reward
  "Return stake reward"
  [block-value mn-reward]
  (- (* 0.90 block-value) mn-reward))

(defn ^:private calculate-mn-reward
  "Return mn reward"
  [masternodes-count tot-supply block-value]
  (let [mn-supply (* masternodes-count 10000)
        mn-rate (/ mn-supply tot-supply)
        updated-block-value (* block-value 0.9)]
    (cond
      (<= mn-rate 0.01) (* updated-block-value 0.90)
      (<= mn-rate 0.02) (* updated-block-value 0.88)
      (<= mn-rate 0.03) (* updated-block-value 0.87)
      (<= mn-rate 0.04) (* updated-block-value 0.86)
      (<= mn-rate 0.05) (* updated-block-value 0.85)
      (<= mn-rate 0.06) (* updated-block-value 0.84)
      (<= mn-rate 0.07) (* updated-block-value 0.83)
      (<= mn-rate 0.08) (* updated-block-value 0.82)
      (<= mn-rate 0.09) (* updated-block-value 0.81)
      (<= mn-rate 0.10) (* updated-block-value 0.80)
      (<= mn-rate 0.11) (* updated-block-value 0.79)
      (<= mn-rate 0.12) (* updated-block-value 0.78)
      (<= mn-rate 0.13) (* updated-block-value 0.77)
      (<= mn-rate 0.14) (* updated-block-value 0.76)
      (<= mn-rate 0.15) (* updated-block-value 0.75)
      (<= mn-rate 0.16) (* updated-block-value 0.74)
      (<= mn-rate 0.17) (* updated-block-value 0.73)
      (<= mn-rate 0.18) (* updated-block-value 0.72)
      (<= mn-rate 0.19) (* updated-block-value 0.71)
      (<= mn-rate 0.20) (* updated-block-value 0.70)
      (<= mn-rate 0.21) (* updated-block-value 0.69)
      (<= mn-rate 0.22) (* updated-block-value 0.68)
      (<= mn-rate 0.23) (* updated-block-value 0.67)
      (<= mn-rate 0.24) (* updated-block-value 0.66)
      (<= mn-rate 0.25) (* updated-block-value 0.65)
      (<= mn-rate 0.26) (* updated-block-value 0.64)
      (<= mn-rate 0.27) (* updated-block-value 0.63)
      (<= mn-rate 0.28) (* updated-block-value 0.62)
      (<= mn-rate 0.29) (* updated-block-value 0.61)
      (<= mn-rate 0.30) (* updated-block-value 0.60)
      (<= mn-rate 0.31) (* updated-block-value 0.59)
      (<= mn-rate 0.32) (* updated-block-value 0.58)
      (<= mn-rate 0.33) (* updated-block-value 0.57)
      (<= mn-rate 0.34) (* updated-block-value 0.56)
      (<= mn-rate 0.35) (* updated-block-value 0.55)
      (<= mn-rate 0.363) (* updated-block-value 0.54)
      (<= mn-rate 0.376) (* updated-block-value 0.53)
      (<= mn-rate 0.389) (* updated-block-value 0.52)
      (<= mn-rate 0.402) (* updated-block-value 0.51)
      (<= mn-rate 0.415) (* updated-block-value 0.50)
      (<= mn-rate 0.428) (* updated-block-value 0.49)
      (<= mn-rate 0.441) (* updated-block-value 0.48)
      (<= mn-rate 0.454) (* updated-block-value 0.47)
      (<= mn-rate 0.467) (* updated-block-value 0.46)
      (<= mn-rate 0.48) (* updated-block-value 0.45)
      (<= mn-rate 0.493) (* updated-block-value 0.44)
      (<= mn-rate 0.506) (* updated-block-value 0.43)
      (<= mn-rate 0.519) (* updated-block-value 0.42)
      (<= mn-rate 0.532) (* updated-block-value 0.41)
      (<= mn-rate 0.545) (* updated-block-value 0.40)
      (<= mn-rate 0.558) (* updated-block-value 0.39)
      (<= mn-rate 0.571) (* updated-block-value 0.38)
      (<= mn-rate 0.584) (* updated-block-value 0.37)
      (<= mn-rate 0.597) (* updated-block-value 0.36)
      (<= mn-rate 0.61) (* updated-block-value 0.35)
      (<= mn-rate 0.623) (* updated-block-value 0.34)
      (<= mn-rate 0.636) (* updated-block-value 0.33)
      (<= mn-rate 0.649) (* updated-block-value 0.32)
      (<= mn-rate 0.662) (* updated-block-value 0.31)
      (<= mn-rate 0.675) (* updated-block-value 0.30)
      (<= mn-rate 0.688) (* updated-block-value 0.29)
      (<= mn-rate 0.701) (* updated-block-value 0.28)
      (<= mn-rate 0.714) (* updated-block-value 0.27)
      (<= mn-rate 0.727) (* updated-block-value 0.26)
      (<= mn-rate 0.74) (* updated-block-value 0.25)
      (<= mn-rate 0.753) (* updated-block-value 0.24)
      (<= mn-rate 0.766) (* updated-block-value 0.23)
      (<= mn-rate 0.779) (* updated-block-value 0.22)
      (<= mn-rate 0.792) (* updated-block-value 0.21)
      (<= mn-rate 0.805) (* updated-block-value 0.20)
      (<= mn-rate 0.818) (* updated-block-value 0.19)
      (<= mn-rate 0.831) (* updated-block-value 0.18)
      (<= mn-rate 0.844) (* updated-block-value 0.17)
      (<= mn-rate 0.857) (* updated-block-value 0.16)
      (<= mn-rate 0.87) (* updated-block-value 0.15)
      (<= mn-rate 0.883) (* updated-block-value 0.14)
      (<= mn-rate 0.896) (* updated-block-value 0.13)
      (<= mn-rate 0.909) (* updated-block-value 0.12)
      (<= mn-rate 0.922) (* updated-block-value 0.11)
      (<= mn-rate 0.935) (* updated-block-value 0.10)
      (<= mn-rate 0.945) (* updated-block-value 0.09)
      (<= mn-rate 0.961) (* updated-block-value 0.08)
      (<= mn-rate 0.974) (* updated-block-value 0.07)
      (<= mn-rate 0.987) (* updated-block-value 0.06)
      (<= mn-rate 0.99) (* updated-block-value 0.05)
      :else (* updated-block-value 0.01))))

(defn calculate-block-reward
  "Return mn and stake rewards"
   [masternodes-count tot-supply]
  (let [block-value 5
        mn-reward (calculate-mn-reward masternodes-count tot-supply block-value)
        st-reward (calculate-stake-reward block-value mn-reward)]
    {:masternode mn-reward :staking st-reward}))

(defn waiting-time-masternode
  "Return average waiting time in hours for a masternode payment"
  [personnal-masternodes-count total-masternodes-count]
  (let [in-hours (/ total-masternodes-count
                    (* personnal-masternodes-count blocks-per-hour))
        in-days (/ total-masternodes-count
                (* personnal-masternodes-count blocks-per-day))]
    (if (> in-days 1)
      (str (u/format-number in-days) " days")
      (str (u/format-number in-hours) " hours"))))

(defn waiting-time-staking
  "Return average waiting time in hours for staking payment"
  [pivx total-staking-pivx]
  (let [in-hours (/ total-staking-pivx (* pivx blocks-per-hour))
        in-days (/ total-staking-pivx (* pivx blocks-per-day))]
    (if (> in-days 1)
      (str (u/format-number in-days) " days")
      (str (u/format-number in-hours) " hours"))))

(defn monthly-revenue-mn
  "Return average revenue in a month for a masternode"
  [own-mn total-mn reward-mn]
  (/ (* own-mn blocks-per-month reward-mn) total-mn))

(defn monthly-revenue-st
  "Return average revenue in a month for stakers"
  [own-pivx total-staking reward-st]
  (/ (* own-pivx blocks-per-month reward-st) total-staking))

(defn pivx-to-currency
  "Return a string with the value of pivx in the currency"
  [pivx]
  (let [cur (get-in @app-state [:calc :currency])
        pivx-val (get-in @app-state [:currencies (keyword (clojure.string/lower-case cur))])
        total (* pivx pivx-val)]
    (str (u/format-number total) " " cur)))
