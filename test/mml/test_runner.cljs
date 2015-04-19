(ns test-runner
  (:require [birdduck.mml.core-test]
            [cljs.test :as test :refer-macros [run-tests] :refer [report]]))

(enable-console-print!)

(defmethod report [::test/default :summary] [m]
  (println "\nRan" (:test m) "tests containing"
           (+ (:pass m) (:fail m) (:error m)) "assertions.")
  (println (:fail m) "failures," (:error m) "errors.")
  (aset js/window "test-failures" (+ (:fail m) (:error m))))

(defn runner []
  "This will run all tests in the provided name spaces."
  (test/run-tests
   (test/empty-env ::test/default)
   'birdduck.mml.core-test))
