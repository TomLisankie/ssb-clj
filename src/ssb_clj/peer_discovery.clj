(ns ssb-clj.peer-discovery
  (:import (org.apache.tuweni.scuttlebutt Identity)
           (org.apache.tuweni.scuttlebutt.discovery LocalIdentity))
  (:require [crypto.random :as cr]))

