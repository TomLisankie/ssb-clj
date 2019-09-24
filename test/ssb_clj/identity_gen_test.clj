(ns ssb-clj.identity-gen-test
  (:require [clojure.test :refer :all]
            [ssb-clj.identity-gen :refer :all]))

(deftest test-random-ids
  "Tests to make sure two random IDs are not equal"
  (let [id1 (generate-new-identity)
        id2 (generate-new-identity)]
    (is (not= id1 id2))
    (is (not= (hash-code-for-id id1) (hash-code-for-id id2)))))

(deftest test-same-keypair-id-equality
  "Tests two separate IDs based off of the same keypair to make sure they are equal"
  (let [key-pair (random-key-pair)
        id1 (generate-new-identity key-pair)
        id2 (generate-new-identity key-pair)]
    (is (= id1 id2))))

(deftest test-to-string
  "Tests to see if an ID is properly being converted to a string"
  (let [key-pair (random-key-pair)
        id (generate-new-identity key-pair)
        public-key-string (str "@" (->> key-pair .publicKey .bytes .toBase64String) ".ed25519")]
    (is (= public-key-string (id-as-string id)))))
