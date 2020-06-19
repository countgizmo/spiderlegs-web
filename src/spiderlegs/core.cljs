(ns ^:figwheel-hooks spiderlegs.core
  (:require [reagent.dom :as rdom]
            [reagent.core :as r]
            [re-frame.core :as re-frame]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.coercion.spec :as rss]
            [spiderlegs.events :as events]
            [spiderlegs.views :as views]))


(def debug?
  ^boolean js/goog.DEBUG)


(defn dev-setup []
  (when debug?
    (enable-console-print!)
    (println "dev mode")))

(defonce match (r/atom nil))

(defn current-page []
  [:div
   {:class-name "mx-auto bg-gray-100 min-h-screen"}
   [:header
    {:class-name "bg-gray-900 text-gray-300 py-2 flex"}
    [:div
     [:a
      {:href "/random-note"
       :class-name "hover:text-gray-100 ml-10"}
      "Random Note"]]
    [:div
     [:a
      {:href "/fret-hero"
       :class-name "hover:text-gray-100 cursor-pointer ml-10"}
      "Fret Hero"]]]
   (when @match
     (let [view (:view (:data @match))]
       [view @match]))])


(def routes
  [["/"
    {:name ::frontpage
     :view views/frontpage}]

   ["/fret-hero"
    {:name ::fret-hero
     :view views/fret-hero}]

   ["/random-note"
    {:name ::random-note
     :view views/random-note}]])

(defn ^:after-load mount
  []
  (rfe/start!
   (rf/router routes {:data {:coercion rss/coercion}})
   (fn [m] (reset! match m))
   {:use-fragment false})
  (rdom/render
   [current-page]
   (.getElementById js/document "app")))

(defn ^:export main
  []
  (re-frame/dispatch-sync [::events/initialise-db])
  (dev-setup)
  (mount))
