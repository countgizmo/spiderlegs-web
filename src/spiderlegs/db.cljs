(ns spiderlegs.db)


(def notes
  ["A" "A#/Bb" "B" "C" "C#/Db" "D" "D#/Eb" "E" "F"
   "F#/Gb" "G" "G#/Ab"])

(def tuning
  ["E" "B" "G" "D" "A" "E"])


(defn fret-position->note
  [string fret]
  (let [start-position (.indexOf (to-array notes) (nth tuning (dec string)))
        note-position (+ start-position fret)]
    (nth notes (mod note-position (count notes)))))


(defn generate-random-fret-position
  [max-strings max-frets]
  {:string (inc (rand-int max-strings))
   :fret   (inc (rand-int max-frets))})

(def default-db
  (merge {}
         (generate-random-fret-position 6 5)))


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


(defn active-fret-position
  [db]
  (ffirst (filter
           (fn [[_k v]]
             (true? v))
           (fretboard db))))


(defn clear-fretboard
  [db]
  (dissoc db :fretboard))


(defn clear-answer
  [db]
  (dissoc db :answer))


(defn activate-fretboard-position
  [db string fret]
  (assoc-in db [:fretboard {:string  string
                            :fret    fret}] true))


(defn submit-answer
  [db answer]
  (assoc db :answer answer))


(defn answer
  [db]
  (:answer db))
