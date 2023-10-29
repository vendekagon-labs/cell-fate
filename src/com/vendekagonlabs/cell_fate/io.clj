(ns com.vendekagonlabs.cell-fate.io
  (:require [clojure.java.io :as io]
            [clojure.string :as str]
            [charred.api :as charred]))


(defn read-space-delimited
  [file-path]
  (with-open [reader (io/reader file-path)]
    (doall
      (charred/read-csv reader :separator \space))))

(defn read-barcodes
  [barcodes-path]
  (-> barcodes-path
      (io/file)
      (slurp)
      (str/split-lines)
      (vec)))

(defn read-genes
  [genes-path]
  (let [genes-lines (-> genes-path
                        (io/file)
                        (slurp)
                        (str/split-lines))]
    (->> genes-lines
         (map #(str/split % #"\t"))
         (mapv second))))

(defn read-sparse-matrix
  [sparse-matrix-path]
  (->> sparse-matrix-path
       (read-space-delimited)
       (drop 2)
       (map (partial mapv #(Integer/parseInt %)))))

(defn read-matrix-market
  [mtx-root-path]
  (let [barcodes-f (str mtx-root-path "barcodes.tsv")
        genes-f (str mtx-root-path "genes.tsv")
        sparse-mtx-f (str mtx-root-path "matrix.mtx")
        barcodes (read-barcodes barcodes-f)
        genes (read-genes genes-f)
        matrix-raw (read-sparse-matrix sparse-mtx-f)]
    matrix-raw
    (map (fn [[gene-ind cell-ind raw-count]]
           ;; dec b/c matrix market is one-based indexing,
           ;; we have to drop it down to zero-based for clj
           [(barcodes (dec cell-ind))
            (genes (dec gene-ind))
            raw-count])
         matrix-raw)))


(comment
  (def mtx-root "data/filtered_gene_bc_matrices/hg19/")
  (def cell-matrix (read-matrix-market mtx-root))
  (take 3 cell-matrix))