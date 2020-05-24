(ns spiderlegs.events
  (:require [spiderlegs.db :as db]
            [re-frame.core :as rf]))


(def standard-interceptors  [(when ^boolean js/goog.DEBUG rf/debug)])


(rf/reg-event-db
 ::initialise-db
 standard-interceptors
 (fn [_ _]
   db/default-db))


(rf/reg-event-db
 ::generate-rand-string-fret
 standard-interceptors
 (fn [db _]
   (merge db {:string (inc (rand-int 6))
              :fret   (inc (rand-int 20))})))


(rf/reg-event-db
 ::touch-fretboard-position
 standard-interceptors
 (fn [db [_ string fret]]
   (db/toggle-fretboard-position db string fret)))
