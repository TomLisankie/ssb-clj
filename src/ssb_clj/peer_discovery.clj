(ns ssb-clj.peer-discovery
  (:import (java.net
            DatagramSocket
            DatagramPacket
            InetSocketAddress))
  (:require [ssb-clj.identity-gen :as id-gen]
            [clojure.string :as clj-str]))

(def public-key (first (clj-str/split (second (clj-str/split (id-gen/load-public-key "/home/thomas/.ssb-clj/secret") #": ")) #"\n")))

(defn generate-udp-packet
  "Generates a UDP packet that will be broadcast to the local network"
  [ip port public-key]
  (DatagramPacket. (.getBytes public-key) (count (.getBytes public-key)) (InetSocketAddress. ip port)))

(defn ip-string
  "Gets IP of UDP packet"
  [packet]
  (let [ip-bytes (vec (.getAddress (.getAddress packet)))]
    (loop [ip-string ""
           bytes ip-bytes]
      (if (empty? bytes)
        ip-string
        (if (nil? (second bytes))
          (recur (str ip-string (first bytes)) (rest bytes))
          (recur (str ip-string (first bytes) ".") (rest bytes)))))))

(defn port
  "Gets port of UDP packet"
  [packet]
  (.getPort packet))

(defn public-key-in-packet
  "Gets the content of the UDP packet (in this case, the public key of sender"
  [packet]
  (String. (.getData packet)))
