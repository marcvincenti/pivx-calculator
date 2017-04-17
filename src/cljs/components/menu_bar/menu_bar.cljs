(ns components.menu-bar
  (:require [app.state :refer [app-state]]))

(defn component []
  (let [active? (fn [p] (when (= p (:page @app-state)) {:class "active"}))]
    [:nav {:class "navbar navbar-default navbar-fixed-top"}
      [:div {:class "container"}
        [:div {:class "navbar-header"}
          [:button {:type "button" :class "navbar-toggle collapsed"
                    :data-toggle "collapse" :aria-expanded "false"
                    :data-target "#bs-example-navbar-collapse-1"}
            [:span {:class "icon-bar"}]
            [:span {:class "icon-bar"}]
            [:span {:class "icon-bar"}]]
          [:a {:class "navbar-brand" :href "#/"}
            [:img {:id "logo-pivx" :src "assets/PIVX.png" :alt "PIVX logo"}]]]

  [:div {:class "collapse navbar-collapse" :id "bs-example-navbar-collapse-1"}

    [:ul {:class "nav navbar-nav"}
      [:li (active? :calculator) [:a {:href "#/"} "Calculator"]]
      [:li [:a {:target "_blank" :href "https://docs.google.com/forms/d/e/1FAIpQLSdzlYN3CGdHr-qRdH5IVmbeTFDsRenDQ36EhrSpSXYgRYxsVw/viewform"} "API " [:sub "beta"]]]
      [:li (active? :about) [:a {:href "#/about"} "About"]]]]]]))
