(ns ssb-clj.identity-gen
  (:import (org.apache.tuweni.scuttlebutt Identity
                                          Ed25519KeyPairIdentity)
           (org.apache.tuweni.crypto.sodium Signature
                                            Signature$KeyPair))
  (:require [crypto.random :as cr]))

(defn generate-new-identity
  "generates a new Ed25519 Scuttlebutt identity (public and secret key)"
  []
  (let [random-byte-seed (cr/bytes 32)
        key-pair (Signature$KeyPair/random)]
    (Identity/fromKeyPair key-pair)))
