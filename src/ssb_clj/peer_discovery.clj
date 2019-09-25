(ns ssb-clj.peer-discovery
  (:import (org.apache.tuweni.scuttlebutt Identity)
           (org.apache.tuweni.scuttlebutt.discovery LocalIdentity))
  (:require [crypto.random :as cr]))

(defn make-local-identity
  "Creates an identity for use during peer discovery"
  [ip port identity]
  (LocalIdentity. ip port identity))
