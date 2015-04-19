(ns mml.core
  (:refer-clojure :exclude [rest repeat]))

(defprotocol IMml
  "A protocol for serializing to Music Macro Language (MML)"
  (->mml [this]))

(defn mml?
  "Returns true if x implements IMml."
  [x]
  (satisfies? IMml x))

(defn ^:private mml-symbol
  "Wraps parameters as MML symbols."
  [& rest]
  (reify
    IMml
    (->mml [_]
      (reduce #(str %1 (cond
                        (or (seq? %2) (coll? %2)) (->mml (apply mml-symbol %2))
                        (mml? %2) (->mml %2)
                        :else %2))
              ""
              rest))))

(defn tempo
  "Generates a tempo at the specified Beats per Minute (BPM)."
  [bpm]
  (if-not (and (number? bpm) (>= bpm 32) (<= bpm 255))
    (throw (js/Error. "bpm must be a number no less than 32 and no greater than 255")))
    (mml-symbol "t" bpm))

(defn volume
  "Generates a volume at the specified level."
  [level]
  (if-not (and (number? level) (>= level 1) (<= level 15))
    (throw (js/Error. "volume must be a number no less than 1 and no greater than 15"))
    (mml-symbol "v" level)))

(defn octave
  "Generates an octave given the specified instrument level."
  [n]
  (if-not (and (number? n) (>= n 1) (<= n 8))
    (throw (js/Error. "octave must be a number no less than 1 and no greater than 8"))
    (mml-symbol "o" n)))

(defn length
  "Generates a default note length."
  [length]
  (if-not (and (number? length) (>= length 1) (<= length 64))
    (throw (js/Error. "length must be a number no less than 1 and no greater than 64"))
    (mml-symbol "l" length)))

(defn quantize
  "Generates quantization of notes on a scale from one to eight."
  [length]
  (if-not (and (number? length) (>= length 1) (<= length 8))
    (throw (js/Error. "quantize must be a number no less than 1 and no greater than 8"))
    (mml-symbol "q" length)))

(defn rest
  "Generates a rest (an interval of silence)."
  []
  (mml-symbol "r"))

(defn note
  "Generates a note (a pitch) from a string or keyword representation."
  [n]
  (mml-symbol (cond
               (string? n) n
               (keyword? n) (.toLowerCase (name n))
               :else (throw (js/Error. "note must be a string or keyword")))))

(defn ^:private wrap-with-note-or-mml-symbol
  "Wraps symbol into a note if it cannot be serialized already."
  [symbol]
  (if-not (mml? symbol)
    (note symbol)
    symbol))

(defn sharp
  "Generates a note raised by a half step."
  [note]
  (mml-symbol (wrap-with-note-or-mml-symbol note) "+"))

(defn flat
  "Generates a note lowered by a half step."
  [note]
  (mml-symbol (wrap-with-note-or-mml-symbol note) "-"))

(defn asc
  "Generates a note ascended an octave, all following notes will remain ascended."
  [symbol]
  (mml-symbol "<" (wrap-with-note-or-mml-symbol symbol)))

(defn desc
  "Generates a note descended an octave, all following notes will remain descended."
  [symbol]
  (mml-symbol ">" (wrap-with-note-or-mml-symbol symbol)))

(defn dot
  "Generates notes or rests with durations increased by half the current."
  ([symbol] (dot 1 symbol))
  ([n symbol]
   (if-not (and (number? n) (> n 0))
     (throw (js/Error. "n must be a number no less than one"))
     (mml-symbol (wrap-with-note-or-mml-symbol symbol)
                 (reduce str "" (for [d (range n)] "."))))))

(defn duration
  "Applies a duration or length to a sequence of notes and rests."
  [d & rest]
  (reify
    IMml
    (->mml [_]
      (reduce #(str % (->mml (wrap-with-note-or-mml-symbol %2)) d) "" rest))))

(defn sixty-fourth
  "Generates sixty-fourth notes or rests."
  [& rest]
  (apply (partial duration 64) rest))

(defn thirty-second
  "Generates thirty-second notes or rests."
  [& rest]
  (apply (partial duration 32) rest))

(defn sixteenth
  "Generates sixteenth notes or rests."
  [& rest]
  (apply (partial duration 16) rest))

(defn eighth
  "Generates eighth notes or rests."
  [& rest]
  (apply (partial duration 8) rest))

(defn quarter
  "Generates quarter notes or rests."
  [& rest]
  (apply (partial duration 4) rest))

(defn half
  "Generates half notes or rests."
  [& rest]
  (apply (partial duration 2) rest))

(defn whole
  "Generates whole notes or rests."
  [& rest]
  (apply (partial duration 1) rest))

(defn phrase
  "A grouping of arbitrary MML."
  [& rest]
  (map #(wrap-with-note-or-mml-symbol %) rest))

(defn measure
  "A convenience method for grouping notes and rests into measures, or bars."
  [& rest]
  (reify
    IMml
    (->mml [_]
      (reduce #(str % (when-not (empty? %) " ") (->mml %2))
              ""
              (apply phrase rest)))))

(defn repeat
  "Repeats the given set of parameters n times."
  [n & rest]
  (reify
    IMml
    (->mml [_]
      (str "["
           (->mml (apply measure rest))
           "]"
           (when (> n 1) n)))))

(defn mml
  "Serializes parameters into Music Macro Language (MML)."
  [& rest]
  (->mml (apply measure rest)))
