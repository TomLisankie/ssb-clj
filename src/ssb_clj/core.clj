(ns ssb-clj.core
  (:require [caesium.crypto.sign :as sign]
            [byte-streams :as bs]))

(def keypair (sign/keypair!))

(defn save-keypair
  "saves keypair to ~/.ssb-clj/secret"
  [pair file-path]
  (let [public-key-string (bs/to-string (:public keypair))
        secret-key-string (bs/to-string (:secret keypair))]
    (spit file-path (str "Public Key: " public-key-string))
    (spit file-path (str "\nPrivate Key: " secret-key-string) :append true)))
