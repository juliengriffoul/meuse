(ns meuse.crate-file
  "Manipulates the crate file."
  (:require [clojure.java.io :as io]))

(defn crate-file-path
  "Get the path to the crate file on disk."
  [base-path crate-name crate-version]
  (.getPath (io/file base-path crate-name crate-version "download")))

(defn write-crate-file
  "Takes a crate file and its metadata, writes the crate file on disk."
  [base-path {:keys [raw-metadata crate-file]}]
  (let [path (crate-file-path base-path
                              (:name raw-metadata)
                              (:vers raw-metadata))]
    (io/make-parents path)
    (with-open [w (io/output-stream path)]
      (.write w crate-file))))
