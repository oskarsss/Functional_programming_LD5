(defn get-norm [n]
	(if (> n 0) n
	(- n))
	)

(defn get-pos-encrypt [i Key ]
	  (+ i(* 999 (get-norm (- (mod (- i Key) (* 2 Key)) Key)) )) ;; https://en.wikipedia.org/wiki/Triangle_wave
	) 



(defn encrypt-string [string Key map-set i ] 
	(if (empty? string)(print (clojure.string/join(vals(into (sorted-map) map-set))))
	(encrypt-string (rest string) Key (assoc map-set   (get-pos-encrypt i (- 1 Key )) (first string)) (+ 1 i) ))
		)

(defn encrypt [string Key] 
	(def map-set {0 "a"} )
	(encrypt-string (clojure.string/replace string #" " "_") Key map-set 0 ))

(encrypt "WEAREDISCOVEREDRUNATONCE" 4)
(print "\n")

(defn decrypt-line [string jump map-set pos ]

	;;(print "\n")
	;;(print map-set)
	;;(print "\n")
	;;(print string)
	(if (empty? string)map-set
	(decrypt-line (rest string) jump (assoc map-set pos (first string)) (+ pos jump)))
	)

(defn decrypt-middle-lines [string map-set Key  pos]
	(if (empty? string)map-set
	(decrypt-middle-lines 
		(subs string (int(Math/ceil(/ 24  (- Key 1 )))) (count string)) 
		(decrypt-line 
			(subs string 0 (int(Math/ceil(/ 24  (- Key 1 )))))
			(* 2 (- (- Key pos) 1))
			map-set
			pos)
		Key
		(+ pos 1)
	)))



(defn decrypt-string [string Key map-set i]
	(print (clojure.string/join  (vals(into (sorted-map)(decrypt-line ;;decrypt last line
		(subs string (- (count string) (int(Math/floor(/ (count string) (* 2 (- Key 1 )))))) (count string))
		(* 2 (- Key 1))

		(decrypt-line ;;decrypt first line
			(subs string 0  (int(Math/ceil(/ (count string) (* 2 (- Key 1 ))))))  
			(* 2 (- Key 1))
			(decrypt-middle-lines ;;decypt middle lines
				(subs string   (int(Math/ceil(/ (count string) (* 2 (- Key 1 )))))  (- (count string) (int(Math/floor(/ (count string) (* 2 (- Key 1 ))))))  )
				map-set
				Key
				1
				)
			0 )
		(- Key 1) ;;end starting position
		)
	))
	))
	)
	


(defn decrypt [string Key]
 	(def map-set {0 "a"} )
	(decrypt-string (clojure.string/replace string #" " "_") Key map-set 0 )
	)

(decrypt "WECRUOERDSOEERNTNEAIVDAC" 3)




