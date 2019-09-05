(ns ssb-clj.core
  (:require [caesium.crypto.sign :as sign]))

(defn byte-buffer-to-string
  "Converts the contents of a java.nio.ByteBuffer to a string."
  [buffer]
  (let [bytes (.array buffer)
        string-from-byte-array (String. bytes)]
    string-from-byte-array))

(def keypair (sign/keypair!))
(def public-key-string (byte-buffer-to-string (:public keypair)))
(def secret-key-string (byte-buffer-to-string (:secret keypair)))
