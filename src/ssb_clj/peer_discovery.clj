(ns ssb-clj.peer-discovery
  (:import (org.apache.tuweni.scuttlebutt Identity)
           (org.apache.tuweni.scuttlebutt.discovery LocalIdentity
                                                    ScuttlebuttLocalDiscoveryService))
  (:require [crypto.random :as cr]))

(defn make-local-identity
  "Creates an identity for use during peer discovery"
  [ip port identity]
  (LocalIdentity. ip port identity))

(defn local-id-as-string
  "Converts a local ID to its canonical form as a string"
  [local-identity]
  (.toCanonicalForm local-identity))

(defn create-discovery-service
  "Returns a new instance of the ScuttlebuttLocalDiscoveryService"
  ([port source-ip multicast-ip]
  (ScuttlebuttLocalDiscoveryService. (io.vertx.core.Vertx/vertx)
                                     (.getLogger (org.logl.LoggerProvider/nullProvider)
                                                 "test")
                                     port
                                     source-ip
                                     multicast-ip))
  ([listen-port broadcast-port source-ip multicast-ip validate-multicast]
   (ScuttlebuttLocalDiscoveryService. (io.vertx.core.Vertx/vertx)
                                      (.getLogger (org.logl.LoggerProvider/nullProvider)
                                                  "test")
                                      listen-port
                                      broadcast-port
                                      source-ip
                                      multicast-ip
                                      validate-multicast)))

;; start the service
(defn start-service
  "Starts (and joins) a given ScuttlebuttLocalDiscoveryService instance"
  [service]
  (.join (.start service)))
  

;; broadcast local identity
(defn stop-service
  "Starts (and joins) a given ScuttlebuttLocalDiscoveryService instance"
  [service]
  (.join (.stop service)))

(defn listen
  "Listens for packets (DatagramPacket) containing LocalIdentity info"
  [service packet]
  (.listen service packet))

(defn broadcast
  "Broadcasts a packet to the network containing LocalIdentity info"
  [service]
  (.broadcast service))

(defn append-identity-to-broadcast-list
  "Appends the specified identity to the list of identities to be broadcast"
  [identity]
  (.addIdentityToBroadcastList service identity))

(defn add-listener
  "Adds a listener to be notified when a new packet is received"
  [service listener]
  (.addListener service listener))
