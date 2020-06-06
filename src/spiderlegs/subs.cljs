(ns spiderlegs.subs
  (:require [spiderlegs.db :as db]
            [re-frame.core :as rf]))


(rf/reg-sub
 ::all-notes
 (fn [_ _]
   db/notes))


(rf/reg-sub
 ::random-string-fret
 (fn [db _]
   (db/random-string-fret db)))


(rf/reg-sub
 ::fretboard
 (fn [db _]
   (db/fretboard db)))


(rf/reg-sub
 ::fretboard-position-active?
 (fn [db [_ string fret]]
   (db/fretboard-position-active? db string fret)))


(rf/reg-sub
 ::answer
 (fn [db _]
   (db/answer db)))


(rf/reg-sub
 ::expected-answer
 (fn [db _]
   (let [{:keys [string fret] :as active-fret} (db/active-fret-position db)]
     (when-not (empty? active-fret)
       (db/fret-position->note string fret)))))
