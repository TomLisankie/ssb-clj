(ns ssb-clj.core
  (:require [caesium.crypto.sign :as sign]
            [byte-streams :as bs]))

(def keypair (sign/keypair!))
(def public-key-string (bs/to-string (:public keypair)))
(def secret-key-string (bs/to-string (:secret keypair)))
