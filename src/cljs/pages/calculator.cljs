(ns pages.calculator
  (:require [app.state :refer [app-state]]
            [app.utils :as u]
            [providers.calculator :as c]
            [providers.api :as api]))

(defn user-form []
  [:form
    [:div {:class "form-group"}
      [:div {:class "input-group"}
        [:div {:class "input-group-addon"} "PIVX Balance"]
        [:input {:type "number" :class "form-control"
                 :placeholder "Enter the number of PIVX you have."
                 :on-change #(swap! app-state assoc-in [:calc :pivx]
                                  (-> % .-target .-value))
                 :value (get-in @app-state [:calc :pivx])}]]]
   [:div {:class "form-group"}
     [:div {:class "input-group"}
       [:div {:class "input-group-addon"} "Currency"]
        [:select {:class "form-control"
                  :on-change #(swap! app-state assoc-in
                                [:calc :currency]
                                (-> % .-target .-value))
                  :value (get-in @app-state [:calc :currency])}
          (for [c api/cur-available] ^{:key c} [:option c])]]]
    [:div {:class "form-group"}
      [:div {:class "form-check"}
       [:label {:class "form-check-label"}
         [:input {:type "checkbox" :class "form-check-input"
                  :checked (get-in @app-state [:calc :automatic?])
                  :on-change #(swap! app-state assoc-in [:calc :automatic?]
                      (not (get-in @app-state [:calc :automatic?])))}]
         " Fill automatically PIVX stats"]]]])

(defn api-form []
  (let [active? (get-in @app-state [:calc :automatic?])]
  [:form
    [:div {:class "form-group"}
      [:div {:class "input-group"}
        [:div {:class "input-group-addon"} "Number of Masternodes"]
        [:input (into {:type "number" :class "form-control"
                       :placeholder "Loading..."
                       :on-change #(swap! app-state assoc-in [:calc :masternodes]
                                       (-> % .-target .-value))
                       :value (get-in @app-state [:calc :masternodes])}
                  (when (get-in @app-state [:calc :automatic?])
                    {:disabled "true"}))]]]
    [:div {:class "form-group"}
      [:div {:class "input-group"}
        [:div {:class "input-group-addon"} "Total PIVX supply"]
        [:input (into {:type "number" :class "form-control"
                       :placeholder "Loading..."
                       :on-change #(swap! app-state assoc-in [:calc :supply]
                                      (-> % .-target .-value))
                       :value (get-in @app-state [:calc :supply])}
                  (when (get-in @app-state [:calc :automatic?])
                    {:disabled "true"}))]]]
    [:div {:class "form-group"}
      [:div {:class "input-group"}
        [:div {:class "input-group-addon"} "Total staking percentage"]
        [:input (into {:type "number" :class "form-control"
                       :on-change #(swap! app-state assoc-in [:calc :active-staking]
                                      (-> % .-target .-value))
                       :value (get-in @app-state [:calc :active-staking])}
                  (when (get-in @app-state [:calc :automatic?])
                    {:disabled "true"}))]]]]))

(defn results []
  (let [mn-count (get-in @app-state [:calc :masternodes])
        supply (get-in @app-state [:calc :supply])
        pivx (get-in @app-state [:calc :pivx])
        active-staking (get-in @app-state [:calc :active-staking])
        {:keys [masternode staking]} (c/calculate-block-reward mn-count supply)
        staking-pivx (* (/ active-staking 100) (- supply (* mn-count 10000)))
        max-personnal-masternodes (int (/ pivx 10000))
        waiting-time-masternode (c/waiting-time-masternode max-personnal-masternodes mn-count)
        waiting-time-staking (c/waiting-time-staking pivx staking-pivx)
        mn-r (* (/ masternode mn-count) max-personnal-masternodes)
        staking-r (* (/ pivx staking-pivx) staking)
        mn-score (/ (* 100 mn-r) (+ mn-r staking-r))
        st-score (- 100 mn-score)
        mn-cost (* 10000 max-personnal-masternodes)
        mn-monthly (c/monthly-revenue-mn max-personnal-masternodes mn-count masternode)
        st-monthly (c/monthly-revenue-st pivx staking-pivx staking)]
    [:div {:class "panel panel-default"}
      [:div {:class "panel-heading"} "Results"]
      [:table {:class "table"} [:tbody
        [:tr [:th {:class "col-sm-2"}]
             [:th {:class "col-sm-5"}
              (str max-personnal-masternodes " Masternodes")]
             [:th {:class "col-sm-5"} "Staking"]]
        [:tr [:th "Overview"] [:td {:colSpan 2}
          [:div {:class "progress"}
            [:div {:class "progress-bar progress-bar-success" :role "progressbar"
                   :aria-valuenow mn-score :aria-valuemin "0" :aria-valuemax "100"
                   :style {:width (str mn-score "%")}} "Masternodes"]
            [:div {:class "progress-bar" :role "progressbar"
                   :aria-valuenow st-score :aria-valuemin "0" :aria-valuemax "100"
                   :style {:width (str st-score "%")}} "Staking"]]]]
        [:tr [:th "Requirements"]
          [:td (str max-personnal-masternodes "0K PIVX ") [:sub (c/pivx-to-currency mn-cost)]]
          [:td "> 1 PIVX"]]
        [:tr [:th "Reward"]
          [:td (str (u/format-number masternode) " PIVX ") [:sub (c/pivx-to-currency masternode)]]
          [:td (str (u/format-number staking) " PIVX ") [:sub (c/pivx-to-currency staking)]]]
        [:tr [:th "Average waiting time"]
          [:td
            (if (> max-personnal-masternodes 0)
              waiting-time-masternode
              "-")]
          [:td waiting-time-staking]]
        [:tr [:th "Monthly revenue"]
          [:td (str (u/format-number mn-monthly) " PIVX ") [:sub (c/pivx-to-currency mn-monthly)]]
          [:td (str (u/format-number st-monthly) " PIVX ") [:sub (c/pivx-to-currency st-monthly)]]]]]]))

(defn component []
  [:div {:class "container"}
    [:h1 {:class "page-header"} "Rewards calculator"]
    [:div {:class "container col-sm-6"}
      [:div {:class "panel panel-default"}
        [:div {:class "panel-body"}
          [user-form]]]]
    [:div {:class "container col-sm-6"}
      [:div {:class "panel panel-default"}
        [:div {:class "panel-body"}
          [api-form]]]]
    [:div {:class "col-sm-12"}
      [results]]])
