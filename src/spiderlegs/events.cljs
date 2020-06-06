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
   (merge db (db/generate-random-fret-position 6 12))))


(rf/reg-event-db
 ::touch-fretboard-position
 standard-interceptors
 (fn [db [_ string fret]]
   (db/toggle-fretboard-position db string fret)))


(rf/reg-event-db
 ::activate-random-fret-position
 standard-interceptors
 (fn [db [_ level]]
   (let [{:keys [string fret]} (db/generate-random-fret-position 6 level)]
     (-> db
         (db/clear-fretboard)
         (db/clear-answer)
         (db/activate-fretboard-position string fret )))))


(rf/reg-event-db
 ::submit-answer
 standard-interceptors
 (fn [db [_ answer]]
   (db/submit-answer db answer)))
