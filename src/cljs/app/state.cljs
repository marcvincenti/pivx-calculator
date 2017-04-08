(ns app.state
  (:require [reagent.core :as r]))

(defonce app-state (r/atom {
  :calc {:automatic? true
         :active-staking 62.5}
  }))
