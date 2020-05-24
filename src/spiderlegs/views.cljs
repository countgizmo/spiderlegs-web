(ns spiderlegs.views
  (:require [spiderlegs.events :as events]
            [spiderlegs.subs :as subs]
            [re-frame.core :as rf]
            [reagent.core :as r]))


(.addEventListener js/document "keypress" #(rf/dispatch [::events/generate-rand-string-fret]))


(defn fretcell
  [string fret]
  (let [width    60
        height   30
        x        (* fret width)
        y        (* height string)
        color    "black"
        click-fn #(rf/dispatch [::events/touch-fretboard-position string fret])
        active?  (rf/subscribe [::subs/fretboard-position-active? string fret])]
    [:svg
     [:rect {:width    width
             :height   height
             :stroke   color
             :x        x :y y
             :fill     "white"
             :on-click click-fn}]
     [:line {:x1     x           :y1 y
             :x2     (+ x width) :y2 y
             :stroke "white"}]
     [:line {:x1     x           :y1 (+ y (/ height 2))
             :x2     (+ x width) :y2 (+ y (/ height 2))
             :stroke color}]
     (when @active?
       [:circle {:cx       (+ x (/ width 2))
                 :cy       (+ y (/ height 2))
                 :r        10
                 :stroke   "purple"
                 :fill     "purple"
                 :on-click click-fn}])
     [:line {:x1     x           :y1 (+ y height)
             :x2     (+ x width) :y2 (+ y height)
             :stroke "white"}]]))


(defn fretboard
  []
  [:div
   [:svg  {:style {:border "1px solid"
                   :background "white"
                   :width "800px"
                   :height "400px"}}
    [:rect {:width 5 :height 200
            :stroke "black"
            :x 55 :y 20}]
    (doall
     (for [string (range 1 7)
           fret (range 1 13)]
       ^{:key (str string ":" fret)} [fretcell string fret]))]])


(defn title-page
  []
  (let [{:keys [string fret]} @(rf/subscribe [::subs/random-string-fret])]
    [:div "Find the note and play all octaves:"
     [:div "String: " string]
     [:div "Fret: " fret]
     [:button
      {:on-click #(rf/dispatch [::events/generate-rand-string-fret])}
      "Again!"]
     [fretboard]]))
