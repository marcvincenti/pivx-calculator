(ns app.utils
  (:require [cljs.pprint :as pprint]))

(defn format-number
  "Format a float number"
  [n]
  (pprint/cl-format nil  "~,2f" n))
