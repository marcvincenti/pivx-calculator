(ns providers.currency
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs-http.client :as http]
            [cljs.core.async :refer [<!]]
            [app.state :refer [app-state]]))

(def available ["AUD", "BRL", "CAD", "CHF", "CNY", "EUR", "GBP", "HKD", "IDR",
                "INR", "JPY", "KRW", "MXN", "RUB", "USD"])

(defn update-currency-data
  "Load masternodes data from the blockchain"
  []
  (go (let [c (get-in @app-state [:calc :currency :symbol])
            url (str "https://api.coinmarketcap.com/v1/ticker/pivx/?convert=" c)
            response (<! (http/get url))]
    (when (:success response)
      (.log js/console response)))))
