(ns ssb-clj.identity-utils
  (:import (org.apache.tuweni.scuttlebutt Identity
                                          Ed25519KeyPairIdentity)
           (org.apache.tuweni.crypto.sodium Signature
                                            Signature$KeyPair))
  (:require [crypto.random :as cr]))

(defn random-key-pair
  "Generates a random key pair"
  []
  (Signature$KeyPair/random))

(defn id-as-string
  "Shows the public key of an ID as a string in the standard format established in the SSB protocol"
  [identity]
  (.toString identity))

(defn generate-new-identity
  "generates a new Ed25519 Scuttlebutt identity (public and secret key)"
  ([]
   (let [key-pair (random-key-pair)]
    (Identity/fromKeyPair key-pair)))
  ([key-pair]
   (Identity/fromKeyPair key-pair)))

(defn public-key-ed25519
  "Gets the Ed25519 version of the public key"
  [identity]
  (.ed25519PublicKey identity))

(defn hash-code-for-id
  "Gets hash code for specified ID"
  [identity]
  (.hashCode identity))

;;(defn write-identity-to-disk
  ;;"Writes an identity to ~/.ssb/secret"
  ;;[identity]
  ;;(let [public-key-string 

(defn sign-message
  "Signs a message with a public key from an ID"
  [id message]
  (.sign id message))

(defn verify-signature-for-message
  "Verifies that the message was actually signed by an ID"
  [id signature message]
  (.verify id signature message))
