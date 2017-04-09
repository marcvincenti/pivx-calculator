(ns providers.masternodes
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [app.state :refer [app-state]]))

(defn update-masternodes-data
  "Load masternodes data from the blockchain"
  []
  (go (let [url "http://178.254.23.111/~pub/DN/DN_masternode_payments_stats.html"
            response (<! (http/get url))]
    (when (:success response)
      (.log js/console response)))))
