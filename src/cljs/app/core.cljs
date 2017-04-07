(ns app.core
  (:require-macros [secretary.core :refer [defroute]])
  (:import goog.History)
  (:require [secretary.core :as secretary]
            [goog.events :as events]
            [goog.history.EventType :as EventType]
            [reagent.core :as r]
            [app.state :refer [app-state]]
            [components.menu-bar :as menu-bar]
            [pages.about :as about]
            [pages.calculator :as calculator]
            [pages.masternode :as masternode]
            [pages.staking :as staking]))

;Adding Browser History
(defn hook-browser-navigation! []
  (doto (History.)
    (events/listen
     EventType/NAVIGATE
     (fn [event]
       (secretary/dispatch! (.-token event))))
    (.setEnabled true)))

;Page routes definition
(defn app-routes []
  (secretary/set-config! :prefix "#")
  (defroute "/" [] (swap! app-state assoc :page :calculator))
  (defroute "/staking" [] (swap! app-state assoc :page :staking))
  (defroute "/masternode" [] (swap! app-state assoc :page :masternode))
  (defroute "/about" [] (swap! app-state assoc :page :about))
  (hook-browser-navigation!))

;Current-page multimethod : return which page to display based on app-state
(defmulti current-page #(@app-state :page))
(defmethod current-page :calculator [] [calculator/component])
(defmethod current-page :staking  [] [staking/component])
(defmethod current-page :masternode [] [masternode/component])
(defmethod current-page :about [] [about/component])
(defmethod current-page :default  [] [:div])

;Root function to run cljs app
(defn ^:export run []
  (app-routes)
  (r/render [menu-bar/component] (.getElementById js/document "menu-bar"))
  (r/render [current-page]
    (.getElementById js/document "app-container")))
