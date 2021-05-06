(ns wordify.conversion-utils)

(defn safe-parse-big-int
  "given a string attempts to parse it as an integer"
  [str]
  (when (string? str)
    (try
      (bigint str)
      (catch NumberFormatException _
        nil))))

(defn safe-parse-long
  "given a string attempts to parse it as an integer"
  [str]
  (when (string? str)
    (try
      (Long/parseLong str)
      (catch NumberFormatException _
        nil))))