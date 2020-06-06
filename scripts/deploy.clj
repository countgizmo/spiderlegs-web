#!/usr/bin/env bb

(require '[clojure.java.io :as io]
         '[clojure.java.shell :as shell])


(println "Build JS")
(shell/sh "clj" "-m" "figwheel.main" "-bo" "prod")


(println "Copy inde.html")
(io/copy (io/file "resources/public/index.html")
         (io/file "docs/index.html"))

(println "Copy main.js")
(io/copy (io/file "resources/public/js/prod/main.js")
         (io/file "docs/js/main.js"))

(println "Copy main.css")
(io/copy (io/file "resources/public/css/main.css")
         (io/file "docs/css/main.css"))

(println "Done!")
