(ns pages.calculator
  (:require [app.state :refer [app-state]]
            [app.utils :as u]
            [providers.calculator :as c]))

(defn user-form []
  [:form
    [:div {:class "form-group"}
      [:div {:class "input-group"}
        [:div {:class "input-group-addon"} "PIVX Available"]
        [:input {:type "number" :class "form-control"
                 :placeholder "Enter the number of PIVX you have."
                 :on-change #(swap! app-state assoc-in [:calc :pivx]
                                  (-> % .-target .-value))
                 :value (get-in @app-state [:calc :pivx])}]]]
    [:div {:class "form-group"}
      [:div {:class "form-check"}
       [:label {:class "form-check-label"}
         [:input {:type "checkbox" :class "form-check-input"
                  :checked (get-in @app-state [:calc :automatic?])
                  :on-change #(swap! app-state assoc-in [:calc :automatic?]
                      (not (get-in @app-state [:calc :automatic?])))}]
         " Fill automatically inputs"]]]])

(defn api-form []
  (let [active? (get-in @app-state [:calc :automatic?])]
  [:form
    [:div {:class "form-group"}
      [:div {:class "input-group"}
        [:div {:class "input-group-addon"} "Number of Masternodes"]
        [:input (into {:type "number" :class "form-control"
                       :on-change #(swap! app-state assoc-in [:calc :masternodes]
                                       (-> % .-target .-value))
                       :value (get-in @app-state [:calc :masternodes])}
                  (when (get-in @app-state [:calc :automatic?])
                    {:disabled "true"}))]]]
    [:div {:class "form-group"}
      [:div {:class "input-group"}
        [:div {:class "input-group-addon"} "Total PIVX supply"]
        [:input (into {:type "number" :class "form-control"
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
        {:keys [masternode staking]} (c/calculate-block-reward mn-count supply)
        max-personnal-masternodes (int (/ pivx 10000))
        waiting-time-masternode (c/waiting-time-masternode max-personnal-masternodes mn-count)]
    [:ul {:class "list-group"}
      [:li {:class "list-group-item"} [:b "Masternode : "]
        (if (> max-personnal-masternodes 0)
          (str "You will earn " (u/format-number masternode) " PIVX every " 
               (u/format-number waiting-time-masternode) " hours with "
               max-personnal-masternodes " masternodes.")
          "You must have at least 10000 PIVX to get a masternode.")]
      [:li {:class "list-group-item"} [:b "Staking reward : "]
        (str (u/format-number staking) " PIVX")]]))

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
