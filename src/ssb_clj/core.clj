(ns ssb-clj.core
  (:require [caesium.crypto.sign :as sign]
            [byte-streams :as bs]
            [clojure.data.codec.base64 :as b64]))

(defn save-keypair
  "saves keypair to ~/.ssb-clj/secret"
  [pair file-path]
  (let [public-key-string (bs/to-string (b64/encode (byte-array (.array (:public pair)))))
        secret-key-string (bs/to-string (b64/encode (byte-array (.array (:secret pair)))))]
    (spit file-path (str "Public Key: " public-key-string))
    (spit file-path (str "\nPrivate Key: " secret-key-string) :append true)))

(defn show-user-id
  "Given a public key as a string, show the public ID of the user according to the format defined in the Scuttlebutt Protocol"
  [public-key-string]
  (str "@" public-key-string ".ed25519"))

(defn create-new-user
  "Creates a new user profile"
  [file-path]
  (let [keypair (sign/keypair!)]
    (save-keypair keypair file-path)
    (show-user-id (bs/to-string (b64/encode (byte-array (.array (:public keypair))))))))
  
