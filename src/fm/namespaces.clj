(ns fm.namespaces 
  (:use
    fm.namespaces.core
    cj.shell
    [clojure.contrib.string :only [replace-re]]))

(let [lssrc (ls "-R src")]
    (def *namespaces* (separa-nss lssrc)))

(def *filesclj* (map ns->path *namespaces*))

(defn exists-ns? 
  [name]
  (some #(= name %) *namespaces*))

(defn load-ns
  ([name]
   (do
     (load-file (str "src/" (ns->path namespace)))
     (use (symbol namespace))))
  ([name & others]
   (let [allns (conj others name)]
     (apply load-ns allns)))
  ([]
   (apply load-ns *namespaces*)))


