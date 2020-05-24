(ns spiderlegs.db)


(def default-db
  (merge {}
         {:string (inc (rand-int 6))
          :fret   (inc (rand-int 20))}))


(defn random-string-fret
  [db]
  (select-keys db [:string :fret]))


(defn fretboard
  [db]
  (:fretboard db))


(defn fretboard-position-active?
  [db string fret]
  (true? (get-in db [:fretboard {:string string :fret fret}])))


(defn toggle-fretboard-position
  [db string fret]
  (update-in db [:fretboard {:string string
                            :fret fret}] not))
