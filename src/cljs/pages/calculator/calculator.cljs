(ns pages.calculator
  (:require [app.state :refer [app-state]]
            [providers.calculator :as c]))

(defn user-form []
  [:form
    [:div {:class "form-group"}
      [:div {:class "input-group"}
        [:div {:class "input-group-addon"} "PIVX Available"]
        [:input {:type "number" :class "form-control"
                 :placeholder "Enter the nummber of PIVX you have."
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
  (let [active? (get-in @app-state [:calc :automatic?])] (fn []
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
                    {:disabled "true"}))]]]
   ])))

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
          [api-form]]]]])
