(defproject pivx-calculator "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/clojurescript "1.8.51"]
                 [reagent "0.5.1"]
                 [secretary "1.2.3"]]
  :min-lein-version "2.5.3"
  :source-paths ["src/clj"]
  :clean-targets ^{:protect false} ["resources/public/js" "target"]
  :plugins [[lein-cljsbuild "1.1.4"]]
  :cljsbuild {:builds [{:id           "dev"
                        :source-paths ["src/cljs/"]
                        :compiler     {:main app.core
                                       :asset-path "js/out"
                                       :externs    ["externs.js"]
                                       :output-to  "resources/public/js/app.js"
                                       :output-dir "resources/public/js/out"}}
                       {:id           "prod"
                        :source-paths ["src/cljs/"]
                        :compiler {:main            app.core
                                   :externs         ["externs.js"]
                                   :output-to       "resources/public/js/app.js"
                                   :optimizations   :advanced
                                   :closure-defines {goog.DEBUG false}
                                   :pretty-print    false}}]})
