(ns ssb-clj.identity-utils
  (:import (org.apache.tuweni.scuttlebutt Identity
                                          Ed25519KeyPairIdentity)
           (org.apache.tuweni.crypto.sodium Signature
                                            Signature$KeyPair
                                            Signature$PublicKey
                                            Signature$SecretKey)
           (org.apache.tuweni.bytes Bytes))
  (:require [crypto.random :as cr]))

;; this state is here because unfortunately it seems it's impossible to access
;; the private/secret key from an identity after it's been created. And I need
;; to do that in order to write the private key to disk
(def global-key-pair (atom nil))

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
    (generate-new-identity key-pair)))
  ([key-pair]
   (reset! global-key-pair key-pair)
   (Identity/fromKeyPair key-pair)))

(defn public-key-ed25519
  "Gets the Ed25519 version of the public key"
  [identity]
  (.ed25519PublicKey identity))

(defn hash-code-for-id
  "Gets hash code for specified ID"
  [identity]
  (.hashCode identity))

(defn sign-message
  "Signs a message with a public key from an ID"
  [id message]
  (.sign id message))

(defn verify-signature-for-message
  "Verifies that the message was actually signed by an ID"
  [id signature message]
  (.verify id signature message))

(defn write-identity-to-disk
  "Writes an identity to ~/.ssb/secret"
  [identity]
  (let [public-key-string (.publicKeyAsBase64String identity)
        secret-key-string (.toBase64String (.bytes (.secretKey @global-key-pair)))
        secret-key-dir (str (System/getProperty "user.home") "/.ssb/secret")]
    (if (not (.exists (clojure.java.io/as-file secret-key-dir)))
      (do (clojure.java.io/make-parents secret-key-dir)
          (spit secret-key-dir (str "Public Key: " public-key-string
                                    "\nPrivate Key: " secret-key-string))
          (reset! global-key-pair nil))
      (do (spit secret-key-dir (str "Public Key: " public-key-string
                                    "\nPrivate Key: " secret-key-string))
          (reset! global-key-pair nil)))))

(defn read-identity-from-disk
  "Reads in the file at '~/.ssb/secret' and constructs a new identity from those keys"
  []
  (let [slurped-content (slurp (str (System/getProperty "user.home") "/.ssb/secret"))
        split-on-new-line (.split slurped-content "\n")
        public-key-string (second (.split (first split-on-new-line) " "))
        secret-key-string (second (.split (second split-on-new-line) " "))
        public-key-bytes (Bytes/fromBase64String public-key-string)
        secret-key-bytes (Bytes/fromBase64String secret-key-string)
        public-key (Signature$PublicKey/fromBytes public-key-bytes)
        secret-key (Signature$SecretKey/fromBytes secret-key-bytes)
        key-pair (Signature$KeyPair. public-key secret-key)]
    (Identity/fromKeyPair key-pair))))
