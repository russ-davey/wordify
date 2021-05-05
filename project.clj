(defproject wordify "0.1.0"
  :description "A Clojure library designed to convert numbers into the language word equivalents"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.2"]]

  :main wordify.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})