# wordify

A Clojure library designed to convert numbers into the language word equivalents 
(i.e 1024 = "one thousand and twenty four").

## Usage

Example:

```
(wordify/wordify-number 50472)
=> "fifty thousand four hundred and seventy two"

(wordify/wordify-number "50472")
=> "fifty thousand four hundred and seventy two"

(wordify/wordify-number 50.472)
=> "fifty and four hundred and seventy two thousandths"
```

## Installation

Build the jar file and add it to the local repo:
```
lein install
```

Add the following to your project :dependencies:
```
[wordify "0.1.0"]
```

And add to your namespace:
```
(:require [wordify.core :as wordify])
```

## License

Copyright © 2021 Russell Davey

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
