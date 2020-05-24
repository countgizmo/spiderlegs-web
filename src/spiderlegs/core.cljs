(ns ^:figwheel-hooks spiderlegs.core
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [spiderlegs.events :as events]
            [spiderlegs.views :as views]))


(def debug?
  ^boolean js/goog.DEBUG)


(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))


(defn ^:after-load mount
  []
  (r/render [views/title-page]
            (.getElementById js/document "app")))

(defn ^:export main
  []
  (rf/dispatch-sync [::events/initialise-db])
  (dev-setup)
  (mount))
