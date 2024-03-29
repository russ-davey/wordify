(defproject wordify "0.1.1"
  :description "A Clojure library designed to convert numbers and currency into the language word equivalents"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.11.1"]]

  :main wordify.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})