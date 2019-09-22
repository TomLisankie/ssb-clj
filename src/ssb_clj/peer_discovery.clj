(ns ssb-clj.peer-discovery
  (:import (java.net
            DatagramSocket
            DatagramPacket
            InetSocketAddress)
           org.apache.tuweni.scuttlebutt.discovery.ScuttlebuttLocalDiscoveryService
           org.apache.tuweni.scuttlebutt)
  (:require [ssb-clj.identity-gen :as id-gen]
            [clojure.string :as clj-str]
            [overtone.at-at :as at-at]))

(def local-discovery-service (ScuttlebuttLocalDiscoveryService.
                              (Vertex/vertx) (Logger/nullLogger)
                              8008 "0.0.0.0"  "255.255.255.255"))

((.addIdentityToBroadcastList ) local-discovery-service)
(.join (.start local-discovery-service))

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

(def socket (DatagramSocket. 8008))
(defn send-packet
  "sends a UDP packet"
  [packet]
  (.send socket packet))

(def broadcast-thread-pool (at-at/mk-pool))
(defn broadcast-id-to-local
  "Broadcasts a packet containing the user's public key to the local network (255.255.255.255:8008) once per second"
  []
  (let [id-packet (generate-udp-packet "255.255.255.255" 8008 public-key)]
    (at-at/every 1000 #(send-packet id-packet) broadcast-thread-pool)))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;


