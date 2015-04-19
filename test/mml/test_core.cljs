(ns mml.core-test
  (:require [cljs.test]
            [mml.core :as core])
  (:require-macros [cljs.test :refer [is deftest testing]]))

(enable-console-print!)

(defn mml? [f & args]
  (satisfies? core/IMml (apply f args)))

(deftest tempo-test
  (testing "satisfies IMml protocol"
    (is (mml? core/tempo 180)))
  (testing "serializes to mml with numeric input"
    (is (= (core/->mml (core/tempo 180)) "t180"))))

(deftest volume-test
  (testing "satisfies IMml protocol"
    (is (mml? core/volume 11)))
  (testing "serializes to mml with numeric input"
    (is (= (core/->mml (core/volume 11)) "v11"))))

(deftest octave-test
  (testing "satisfies IMml protocol"
    (is (mml? core/octave 7)))
  (testing "serializes to mml with numeric input"
    (is (= (core/->mml (core/octave 7)) "o7"))))

(deftest rest-test
  (testing "satisfies IMml protocol"
    (is (mml? core/rest)))
  (testing "serializes to mml"
    (is (= (core/->mml (core/rest)) "r"))))

(deftest note-test
  (testing "satisfies IMml protocol"
    (is (mml? core/note "a")))
  (testing "serializes to MML with string input"
    (is (= (core/->mml (core/note "c")) "c")))
  (testing "serializes to MML with lower case keyword input"
    (is (= (core/->mml (core/note :f)) "f")))
  (testing "serializes to MML with upper case keyword input"
    (is (= (core/->mml (core/note :D)) "d"))))

(deftest sharp-test
  (testing "satisfies IMml protocol"
    (is (mml? core/sharp "a")))
  (testing "serializes to MML with note as string"
    (is (= (core/->mml (core/sharp "c")) "c+")))
  (testing "serializes to MML with note as keyword"
    (is (= (core/->mml (core/sharp :f)) "f+")))
  (testing "serializes to MML with note"
    (is (= (core/->mml (core/sharp (core/note "d"))) "d+"))))

(deftest flat-test
  (testing "satisfies IMml protocol"
    (is (mml? core/flat "a")))
  (testing "serializes to MML with note as string"
    (is (= (core/->mml (core/flat "c")) "c-")))
  (testing "serializes to MML with note as keyword"
    (is (= (core/->mml (core/flat :f)) "f-")))
  (testing "serializes to MML with note"
    (is (= (core/->mml (core/flat (core/note "d"))) "d-"))))

(deftest thirty-second-test
  (testing "satisfies IMml protocol"
    (is (mml? core/thirty-second "a")))
  (testing "serializes to MML with note as string"
    (is (= (core/->mml (core/thirty-second "c")) "c32")))
  (testing "serializes to MML with note as keyword"
    (is (= (core/->mml (core/thirty-second :f)) "f32")))
  (testing "serializes to MML with note"
    (is (= (core/->mml (core/thirty-second (core/note "d"))) "d32")))
  (testing "serializes to MML with notes"
    (is (= (core/->mml (core/thirty-second :a "b" (core/note "c"))) "a32b32c32")))
  (testing "serializes to MML with notes and symbols"
    (is (= (core/->mml (core/thirty-second (core/flat "a") "b" (core/rest))) "a-32b32r32"))))

(deftest sixteenth-test
  (testing "satisfies IMml protocol"
    (is (mml? core/sixteenth "a")))
  (testing "serializes to MML with note as string"
    (is (= (core/->mml (core/sixteenth "c")) "c16")))
  (testing "serializes to MML with note as keyword"
    (is (= (core/->mml (core/sixteenth :f)) "f16")))
  (testing "serializes to MML with note"
    (is (= (core/->mml (core/sixteenth (core/note "d"))) "d16")))
  (testing "serializes to MML with notes"
    (is (= (core/->mml (core/sixteenth :a "b" (core/note "c"))) "a16b16c16")))
  (testing "serializes to MML with notes and symbols"
    (is (= (core/->mml (core/sixteenth (core/flat "a") "b" (core/rest))) "a-16b16r16"))))

(deftest eighth-test
  (testing "satisfies IMml protocol"
    (is (mml? core/eighth "a")))
  (testing "serializes to MML with note as string"
    (is (= (core/->mml (core/eighth "c")) "c8")))
  (testing "serializes to MML with note as keyword"
    (is (= (core/->mml (core/eighth :f)) "f8")))
  (testing "serializes to MML with note"
    (is (= (core/->mml (core/eighth (core/note "d"))) "d8")))
  (testing "serializes to MML with notes"
    (is (= (core/->mml (core/eighth :a "b" (core/note "c"))) "a8b8c8")))
  (testing "serializes to MML with notes and symbols"
    (is (= (core/->mml (core/eighth (core/flat "a") "b" (core/rest))) "a-8b8r8"))))

(deftest quarter-test
  (testing "satisfies IMml protocol"
    (is (mml? core/quarter "a")))
  (testing "serializes to MML with note as string"
    (is (= (core/->mml (core/quarter "c")) "c4")))
  (testing "serializes to MML with note as keyword"
    (is (= (core/->mml (core/quarter :f)) "f4")))
  (testing "serializes to MML with note"
    (is (= (core/->mml (core/quarter (core/note "d"))) "d4")))
  (testing "serializes to MML with notes"
    (is (= (core/->mml (core/quarter :a "b" (core/note "c"))) "a4b4c4")))
  (testing "serializes to MML with notes and symbols"
    (is (= (core/->mml (core/quarter (core/flat "a") "b" (core/rest))) "a-4b4r4"))))

(deftest half-test
  (testing "satisfies IMml protocol"
    (is (mml? core/half "a")))
  (testing "serializes to MML with note as string"
    (is (= (core/->mml (core/half "c")) "c2")))
  (testing "serializes to MML with note as keyword"
    (is (= (core/->mml (core/half :f)) "f2")))
  (testing "serializes to MML with note"
    (is (= (core/->mml (core/half (core/note "d"))) "d2")))
  (testing "serializes to MML with notes"
    (is (= (core/->mml (core/half :a "b" (core/note "c"))) "a2b2c2")))
  (testing "serializes to MML with notes and symbols"
    (is (= (core/->mml (core/half (core/flat "a") "b" (core/rest))) "a-2b2r2"))))

(deftest whole-test
  (testing "satisfies IMml protocol"
    (is (mml? core/whole "a")))
  (testing "serializes to MML with note as string"
    (is (= (core/->mml (core/whole "c")) "c1")))
  (testing "serializes to MML with note as keyword"
    (is (= (core/->mml (core/whole :f)) "f1")))
  (testing "serializes to MML with note"
    (is (= (core/->mml (core/whole (core/note "d"))) "d1")))
  (testing "serializes to MML with notes"
    (is (= (core/->mml (core/whole :a "b" (core/note "c"))) "a1b1c1")))
  (testing "serializes to MML with notes and symbols"
    (is (= (core/->mml (core/whole (core/flat "a") "b" (core/rest))) "a-1b1r1"))))

(deftest measure-test
  (testing "satisfies IMml protocol"
    (is (mml? core/measure "a")))
  (testing "serializes to MML with note as string"
    (is (= (core/->mml (core/measure "c")) "c")))
  (testing "serializes to MML with note as keyword"
    (is (= (core/->mml (core/measure :f)) "f")))
  (testing "serializes to MML with note"
    (is (= (core/->mml (core/measure (core/note "d"))) "d")))
  (testing "serializes to MML with notes"
    (is (= (core/->mml (core/measure :a "b" (core/note "c"))) "a b c")))
  (testing "serializes to MML with notes and symbols"
    (is (= (core/->mml (core/measure (core/flat "a") "b" (core/rest))) "a- b r"))))

(deftest mml-test
  (testing "serializes parameters into MML"
    (is (= (core/mml
            (core/tempo 72) (core/volume 3)
            (core/octave 1)
            (core/measure
             (core/desc :a) (core/asc :a) (core/sharp :g) (core/desc (core/sharp :g)))
            (core/measure
             (core/sharp :f) (core/asc (core/sharp :f)) :e (core/desc :e))
            (core/measure
             (core/desc :d) (core/asc :d) (core/sharp :d) (core/desc (core/sharp :d)))
            (core/measure
             :e (core/asc :e) :d (core/desc :d)))
           "t72 v3 o1 >a <a g+ >g+ f+ <f+ e >e >d <d d+ >d+ e <e d >d"))))
