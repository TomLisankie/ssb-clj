(ns ssb-clj.identity-utils
  (:import (org.apache.tuweni.scuttlebutt Identity
                                          Ed25519KeyPairIdentity)
           (org.apache.tuweni.crypto.sodium Signature
                                            Signature$KeyPair
                                            Signature$PublicKey
                                            Signature$SecretKey)
           (org.apache.tuweni.bytes Bytes))
  (:require [crypto.random :as cr]))
