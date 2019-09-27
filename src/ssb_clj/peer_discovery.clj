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
  [port source-ip multicast-ip]
  (ScuttlebuttLocalDiscoveryService. (io.vertx.core.Vertx/vertx)
                                     (.getLogger (org.logl.LoggerProvider/nullProvider)
                                                 "test")
                                     port
                                     source-ip
                                     multicast-ip))

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
