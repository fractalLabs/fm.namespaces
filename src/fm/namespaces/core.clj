(ns fm.namespaces.core
  (:require [clojure.string :only [replace join] :as st]))

(defn ns->path [n]
  "de name-space.core a name_space/core.clj"
  (let [onlist (map #(st/replace % "-" "_")
                    (re-seq #"[^.]+"  n))]
    (str "src/" (st/join "/" onlist) ".clj")))

(defn path->ns
  "de name_space/core.clj a name-space.core"
  [p]
  (let [onlist (map #(st/replace % "_" "-")
                    (re-seq #"[^/]+" (st/replace p ".clj" "")))]
    (st/join "." onlist)))

(defn fromns
  "como el ls -R src regresa mucha basura, primero separaramos los ns por directorios
  suponiendo que list es algo similar a:
  '(\"src:\" \"leiningen\" \"search\" \"search.clj\" \"src/leiningen:\" \"src/search:\" \"aaa\" \"core.clj\" \"read.clj\" \"src/search/aaa:\" \"au.clj\")"
  [list]
  (let [dir (first list)
        files (take-while #(not (re-find #":" %)) (rest list))
        namespace (st/replace (st/replace dir ":" "") #"src/?" "")
        namespace (if (= "" namespace) "" (str namespace "."))
        namespace (path->ns namespace)
        cljfiles (map path->ns (map #(st/replace % ".clj" "") (filter #(re-find #".clj" %) files)))]
    (map #(str namespace %) cljfiles)))

(defn separa-nss [list]
  (loop [list list
         result '( )]
    (if (empty? list) result
      (if (re-find #"src" (first list))
        (recur (rest list) (concat result (fromns list)))
        (recur (rest list) result)))))
