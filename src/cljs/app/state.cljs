(ns app.state
  (:require [reagent.core :as r]))

(defonce app-state (r/atom {
  :calc {:automatic? false
         :active-staking 62.5
         :pivx 10000
         :masternodes 2117
         :supply 52865730
         :currency {:symbol "USD"}}
  }))
