(ns spiderlegs.views
  (:require [spiderlegs.events :as events]
            [spiderlegs.subs :as subs]
            [re-frame.core :as rf]))


(defn fretcell
  [string fret]
  (let [width   60
        height  30
        x       (* fret width)
        y       (* height string)
        color   "black"
        active? (rf/subscribe [::subs/fretboard-position-active? string fret])]
    [:svg
     [:rect {:width  width
             :height height
             :stroke "white"
             :x      x
             :y      y
             :fill   "white"}]
     [:line {:x1     x           :y1 (+ y (/ height 2))
             :x2     (+ x width) :y2 (+ y (/ height 2))
             :stroke color}]
     [:line {:x1     (+ x width) :y1 y
             :x2     (+ x width) :y2 (+ y height)
             :stroke color}]
     (when @active?
       [:circle {:cx     (+ x (/ width 2))
                 :cy     (+ y (/ height 2))
                 :r      10
                 :stroke "purple"
                 :fill   "purple"}])]))


(defn fret-dot
  [fret]
  [:circle {:r 5
            :stroke "black"
            :fill "gray"
            :cx (- (+ 60 (* fret 60))
                   30)
            :cy 120}])

(defn fretboard
  []
  [:div
   [:svg {:style {:border     "1px solid"
                  :background "white"
                  :width      "800px"
                  :height     "250px"}}
    [:rect {:width  5
            :height 200
            :stroke "black"
            :x      55
            :y      20}]
    (doall
     (for [string (range 1 7)
           fret   (range 1 13)]
       ^{:key (str string ":" fret)} [fretcell string fret]))
    (doall
     (map (fn [fret]
            ^{:key (str "fret-dot-" fret)} [fret-dot fret])
          [3 5 7 9]))
    [:circle {:r 5
              :stroke "black"
              :fill "gray"
              :cx (- (+ 60 (* 12 60))
                     30)
              :cy 90}]
    [:circle {:r 5
              :stroke "black"
              :fill "gray"
              :cx (- (+ 60 (* 12 60))
                     30)
              :cy 150}]]])


(defn answer-panel
  []
  (let [notes  @(rf/subscribe [::subs/all-notes])
        answer @(rf/subscribe [::subs/answer])]
    (into [:div
           {:class-name "my-4"}]
          (doall
           (map
            (fn [note]
              [:button {:class-name (str "bg-gray-700 hover:bg-blue-700 text-white font-bold py-4 px-4 rounded mr-2 inline-block"
                                         (if (= note answer)
                                           "selected-answer-btn"
                                           "answer-btn"))
                        :on-click #(rf/dispatch [::events/submit-answer note])} note])
            notes)))))


(defn title-page
  []
  (let [answer          @(rf/subscribe [::subs/answer])
        expected-answer @(rf/subscribe [::subs/expected-answer])
        answered? (and answer expected-answer)]
    [:div
     {:class-name "pt-4 max-h-full"}
     [:div {:class-name "mb-4 text-2xl text-gray-900"}
      "Name that note!"]
     [fretboard]
     [answer-panel]
     (when answered?
       (if (= answer expected-answer)
         [:div {:class-name "text-green-700 font-bold text-xl"} "Correct!"]
         [:div {:class-name "text-gray-900 font-bold text-xl"}
          "The note is: "
          [:span {:class-name "text-red-700 font-bold text-xl"} expected-answer]]))
     [:button
      {:on-click #(rf/dispatch [::events/activate-random-fret-position 5])
       :class-name "mt-8 bg-gray-700 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mb-4"}
      "Again!"]
     ]))


(comment

  (.addEventListener js/document "keypress" #(rf/dispatch [::events/generate-rand-string-fret]))
  )
