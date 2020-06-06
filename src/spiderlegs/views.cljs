(ns spiderlegs.views
  (:require [spiderlegs.events :as events]
            [spiderlegs.subs :as subs]
            [re-frame.core :as rf]))


(defn fretcell
  [string fret]
  (let [width    60
        height   30
        x        (* fret width)
        y        (* height string)
        color    "black"
        active?  (rf/subscribe [::subs/fretboard-position-active? string fret])]
    [:svg
     [:rect {:width    width
             :height   height
             :stroke   color
             :x        x :y y
             :fill     "white"}]
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
                 :fill     "purple"}])
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


(defn answer-panel
  []
  (let [notes  @(rf/subscribe [::subs/all-notes])
        answer @(rf/subscribe [::subs/answer])]
    (into [:div]
          (doall
           (map
            (fn [note]
              [:button {:class-name (if (= note answer)
                                      "selected-answer-btn"
                                      "answer-btn")
                        :on-click #(rf/dispatch [::events/submit-answer note])} note])
            notes)))))


(defn title-page
  []
  (let [answer          @(rf/subscribe [::subs/answer])
        expected-answer @(rf/subscribe [::subs/expected-answer])
        answered? (and answer expected-answer)]
    [:div
     [:div "Name that note!"]
     [:button
      {:on-click #(rf/dispatch [::events/activate-random-fret-position 5])}
      "Again!"]
     [fretboard]
     [answer-panel]
     (when answered?
       (if (= answer expected-answer)
         [:div {:class-name "good-answer-indicator"} "Correct"]
         [:div {:class-name "bad-answer-indicator"} "Incorrect"]))
     (when answered?
       [:div (str "The note is: " expected-answer)])]))


(comment

  (.addEventListener js/document "keypress" #(rf/dispatch [::events/generate-rand-string-fret]))
  )
