(ns spiderlegs.subs
  (:require [spiderlegs.db :as db]
            [re-frame.core :as rf]))


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
