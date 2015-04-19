(defproject com.birdduck.mml "0.1.0"
  :description "A Music Macro Language (MML) DSL for ClojureScript."
  :url "https://github.com/birdduck/mml"
  :license {:name "MIT"
            :url "http://opensource.org/licenses/MIT"}

  :dependencies [[org.clojure/clojure "1.7.0-beta1"]
                 [org.clojure/clojurescript "0.0-3196"]]

  :node-dependencies [[phantomjs "1.9.16"]]

  :plugins [[lein-cljsbuild "1.0.4"]
            [lein-npm "0.5.0"]]

  :source-paths ["src" "target/classes"]

  :clean-targets ["out" "out-adv"]

  :cljsbuild {
    :builds [{:id "test"
              :source-paths ["src" "test"]
              :compiler {
                :output-to "target-test/test.js"
                :output-dir "target-test/test-out"
                :source-map "target-test/test.map"
                :optimizations :whitespace
                :pretty-print true}}]
    :test-commands {"phantomjs" ["node_modules/.bin/phantomjs" "test/vendor/unit-test.js" "test/vendor/unit-test.html"]}})
