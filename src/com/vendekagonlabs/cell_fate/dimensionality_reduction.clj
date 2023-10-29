(ns com.vendekagonlabs.cell-fate.dimensionality-reduction
  (:require [clojure.set :as set]
            [tech.v3.dataset :as ds]
            [scicloj.ml.core :as ml]
            [scicloj.ml.metamorph :as mm]))

(defn pca [ds]
  (let [exclude-cols #{:barcode}
        ds-cols (set (ds/column-names ds))
        pca-cols (vec (set/difference ds-cols exclude-cols))
        ;; todo: standard scaler, then PCA, outputs models
        dr-alg (mm/reduce-dimensions :pca-cov 40 pca-cols {})
        ml-fn (ml/pipeline dr-alg)
        train-ctx (ml-fn {:metamorph/data ds
                          :metamorph/mode :fit})]))


;; data
;; (pca-fit data params) -> {:loadings loadings, :xform xform-fn}
;; (xform-fn data) -> pca-projection-1
;; (xform-fn new-data) -> pca-projection-2


(comment
  (require '[com.vendekagonlabs.cell-fate.io :as cfio])
  (def mtx-root "data/filtered_gene_bc_matrices/hg19/")
  (def pbmc-ds (cfio/matrix-market->dataset mtx-root))
  (take 3 (ds/rows pbmc-ds))
  (def cols (set (ds/column-names pbmc-ds)))
  (def exclude #{:barcode})
  (def for-pca (set/difference cols exclude))
  (def my-dr-alg (mm/reduce-dimensions :pca-cov 40 (vec for-pca) {}))
  (def pipeline (ml/pipeline my-dr-alg))
  (def test (pipeline {:metamorph/data pbmc-ds
                       :metamorph/mode :fit}))
  (def xform (pipeline {:metamorph/data pbmc-ds
                        :metamorph/mode :transform})))
