(ns components.menu-bar
  (:require [app.state :refer [app-state]]))

(defn component []
  (let [active? (fn [p] (when (= p (:page @app-state)) {:class "active"}))]
    [:nav {:class "navbar navbar-default navbar-fixed-top"}
      [:div {:class "container"}
        [:div {:class "navbar-header"}
          [:ul {:class "nav navbar-nav"}
            [:li {:class "dropdown"}
              [:button {:class "navbar-brand" :href "#" :data-toggle "dropdown"}
                [:img {:id "logo-pivx" :src "assets/PIVX.png" :alt "PIVX logo"}]
                [:span {:class "caret"}]]
              [:ul {:class "dropdown-menu" :role "menu"}
                [:li [:a {:href "#"} "Dash (work in progress)"]]
                [:li [:a {:href "#"} "Crown (work in progress)"]]
                [:li [:a {:href "#"} "Mue (work in progress)"]]]]]]

  [:div {:class "collapse navbar-collapse"}

    [:ul {:class "nav navbar-nav"}
      [:li (active? :calculator) [:a {:href "#/"} "Calculator"]]
      [:li [:a {:target "_blank" :href "https://docs.google.com/forms/d/e/1FAIpQLSdzlYN3CGdHr-qRdH5IVmbeTFDsRenDQ36EhrSpSXYgRYxsVw/viewform"} "API " [:sub "beta"]]]
      [:li [:a {:target "_blank" :href "https://docs.google.com/forms/d/1_NUGLWjWjujyYGiTylLmlyBa-BSvens6tKCsPSGUxR8/viewform"} "Staking " [:sub "beta"]]]
      [:li (active? :about) [:a {:href "#/about"} "About"]]]]]]))
