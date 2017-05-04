(ns providers.api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [app.state :refer [app-state]]))

(def cur-available ["BTC", "EUR", "USD"])
(def ^:private url "https://318h5of2kh.execute-api.eu-west-1.amazonaws.com/dev/currency/pivx")

(defn update-data
  "Load masternodes data from the blockchain"
  []
  (go (let [response (<! (http/get url {:with-credentials? false}))]
    (when (:success response)
      (let [mn (get-in response [:body :masternodes_count])
            supply (get-in response [:body :available_supply])
            btc (get-in response [:body :price_btc])
            eur (get-in response [:body :price_eur])
            usd (get-in response [:body :price_usd])]
        (do
          (swap! app-state assoc-in [:calc :masternodes] mn)
          (swap! app-state assoc-in [:calc :supply] supply)
          (swap! app-state assoc-in [:currencies :btc] btc)
          (swap! app-state assoc-in [:currencies :eur] eur)
          (swap! app-state assoc-in [:currencies :usd] usd)))))))
