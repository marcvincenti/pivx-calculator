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
  (let [mn-supply (* masternodes-count 10000)]
    (cond
      (<= mn-supply (* tot-supply 0.01)) (* block-value 0.90)
      (<= mn-supply (* tot-supply 0.02)) (* block-value 0.88)
      (<= mn-supply (* tot-supply 0.03)) (* block-value 0.87)
      (<= mn-supply (* tot-supply 0.04)) (* block-value 0.86)
      (<= mn-supply (* tot-supply 0.05)) (* block-value 0.85)
      (<= mn-supply (* tot-supply 0.06)) (* block-value 0.84)
      (<= mn-supply (* tot-supply 0.07)) (* block-value 0.83)
      (<= mn-supply (* tot-supply 0.08)) (* block-value 0.82)
      (<= mn-supply (* tot-supply 0.09)) (* block-value 0.81)
      (<= mn-supply (* tot-supply 0.10)) (* block-value 0.80)
      (<= mn-supply (* tot-supply 0.11)) (* block-value 0.79)
      (<= mn-supply (* tot-supply 0.12)) (* block-value 0.78)
      (<= mn-supply (* tot-supply 0.13)) (* block-value 0.77)
      (<= mn-supply (* tot-supply 0.14)) (* block-value 0.76)
      (<= mn-supply (* tot-supply 0.15)) (* block-value 0.75)
      (<= mn-supply (* tot-supply 0.16)) (* block-value 0.74)
      (<= mn-supply (* tot-supply 0.17)) (* block-value 0.73)
      (<= mn-supply (* tot-supply 0.18)) (* block-value 0.72)
      (<= mn-supply (* tot-supply 0.19)) (* block-value 0.71)
      (<= mn-supply (* tot-supply 0.20)) (* block-value 0.70)
      (<= mn-supply (* tot-supply 0.21)) (* block-value 0.69)
      (<= mn-supply (* tot-supply 0.22)) (* block-value 0.68)
      (<= mn-supply (* tot-supply 0.23)) (* block-value 0.67)
      (<= mn-supply (* tot-supply 0.24)) (* block-value 0.66)
      (<= mn-supply (* tot-supply 0.25)) (* block-value 0.65)
      (<= mn-supply (* tot-supply 0.26)) (* block-value 0.64)
      (<= mn-supply (* tot-supply 0.27)) (* block-value 0.63)
      (<= mn-supply (* tot-supply 0.28)) (* block-value 0.62)
      (<= mn-supply (* tot-supply 0.29)) (* block-value 0.61)
      (<= mn-supply (* tot-supply 0.30)) (* block-value 0.60)
      (<= mn-supply (* tot-supply 0.31)) (* block-value 0.59)
      (<= mn-supply (* tot-supply 0.32)) (* block-value 0.58)
      (<= mn-supply (* tot-supply 0.33)) (* block-value 0.57)
      (<= mn-supply (* tot-supply 0.34)) (* block-value 0.56)
      (<= mn-supply (* tot-supply 0.35)) (* block-value 0.55)
      (<= mn-supply (* tot-supply 0.363)) (* block-value 0.54)
      (<= mn-supply (* tot-supply 0.376)) (* block-value 0.53)
      (<= mn-supply (* tot-supply 0.389)) (* block-value 0.52)
      (<= mn-supply (* tot-supply 0.402)) (* block-value 0.51)
      (<= mn-supply (* tot-supply 0.415)) (* block-value 0.50)
      (<= mn-supply (* tot-supply 0.428)) (* block-value 0.49)
      (<= mn-supply (* tot-supply 0.441)) (* block-value 0.48)
      (<= mn-supply (* tot-supply 0.454)) (* block-value 0.47)
      (<= mn-supply (* tot-supply 0.467)) (* block-value 0.46)
      (<= mn-supply (* tot-supply 0.48)) (* block-value 0.45)
      (<= mn-supply (* tot-supply 0.493)) (* block-value 0.44)
      (<= mn-supply (* tot-supply 0.506)) (* block-value 0.43)
      (<= mn-supply (* tot-supply 0.519)) (* block-value 0.42)
      (<= mn-supply (* tot-supply 0.532)) (* block-value 0.41)
      (<= mn-supply (* tot-supply 0.545)) (* block-value 0.40)
      (<= mn-supply (* tot-supply 0.558)) (* block-value 0.39)
      (<= mn-supply (* tot-supply 0.571)) (* block-value 0.38)
      (<= mn-supply (* tot-supply 0.584)) (* block-value 0.37)
      (<= mn-supply (* tot-supply 0.597)) (* block-value 0.36)
      (<= mn-supply (* tot-supply 0.61)) (* block-value 0.35)
      (<= mn-supply (* tot-supply 0.623)) (* block-value 0.34)
      (<= mn-supply (* tot-supply 0.636)) (* block-value 0.33)
      (<= mn-supply (* tot-supply 0.649)) (* block-value 0.32)
      (<= mn-supply (* tot-supply 0.662)) (* block-value 0.31)
      (<= mn-supply (* tot-supply 0.675)) (* block-value 0.30)
      (<= mn-supply (* tot-supply 0.688)) (* block-value 0.29)
      (<= mn-supply (* tot-supply 0.701)) (* block-value 0.28)
      (<= mn-supply (* tot-supply 0.714)) (* block-value 0.27)
      (<= mn-supply (* tot-supply 0.727)) (* block-value 0.26)
      (<= mn-supply (* tot-supply 0.74)) (* block-value 0.25)
      (<= mn-supply (* tot-supply 0.753)) (* block-value 0.24)
      (<= mn-supply (* tot-supply 0.766)) (* block-value 0.23)
      (<= mn-supply (* tot-supply 0.779)) (* block-value 0.22)
      (<= mn-supply (* tot-supply 0.792)) (* block-value 0.21)
      (<= mn-supply (* tot-supply 0.805)) (* block-value 0.20)
      (<= mn-supply (* tot-supply 0.818)) (* block-value 0.19)
      (<= mn-supply (* tot-supply 0.831)) (* block-value 0.18)
      (<= mn-supply (* tot-supply 0.844)) (* block-value 0.17)
      (<= mn-supply (* tot-supply 0.857)) (* block-value 0.16)
      (<= mn-supply (* tot-supply 0.87)) (* block-value 0.15)
      (<= mn-supply (* tot-supply 0.883)) (* block-value 0.14)
      (<= mn-supply (* tot-supply 0.896)) (* block-value 0.13)
      (<= mn-supply (* tot-supply 0.909)) (* block-value 0.12)
      (<= mn-supply (* tot-supply 0.922)) (* block-value 0.11)
      (<= mn-supply (* tot-supply 0.935)) (* block-value 0.10)
      (<= mn-supply (* tot-supply 0.945)) (* block-value 0.09)
      (<= mn-supply (* tot-supply 0.961)) (* block-value 0.08)
      (<= mn-supply (* tot-supply 0.974)) (* block-value 0.07)
      (<= mn-supply (* tot-supply 0.987)) (* block-value 0.06)
      (<= mn-supply (* tot-supply 0.99)) (* block-value 0.05)
      :else (* block-value 0.01))))

(defn calculate-block-reward
  "Return mn and stake rewards"
   [masternodes-count tot-supply]
  (let [block-value 10
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
