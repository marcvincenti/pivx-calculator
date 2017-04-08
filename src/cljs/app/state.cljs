(ns app.state
  (:require [reagent.core :as r]))

(def app-state (r/atom {
  :calc {:automatic? true
         :active-staking 62.5}
  }))
