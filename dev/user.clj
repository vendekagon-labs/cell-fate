(ns user
  (:require [com.vendekagonlabs.cell-fate.io :as cfio]
            [com.vendekagonlabs.cell-fate.dimensionality-reduction :as cfdr]))

(comment
  :example-analysis-flow
  ;; read matrix market sparse format into dense tech.v3.dataset
  (def mtx-root "data/filtered_gene_bc_matrices/hg19/")
  (def pbmc-ds (cfio/matrix-market->dataset mtx-root)))
