# Ejemplo apliación web Java

Tomado de
http://websystique.com/spring-boot/spring-boot-angularjs-spring-data-jpa-crud-app-example/

utiliza spring-boot, jpa y angularjs

para compilar

	mvn clean compile

para ejecutar en pruebas:

	mvn spring-boot:run

para generar artefacto ejecutable final:

	mvn clean package


Para ejecutar la aplicación

	export MAVEN_OPTS=-Xmx1024m -XX:MaxPermSize=128M
	java -jar target/taller01-1.0.0.jar –spring.profiles.active=local

Producción:

	java -jar target/taller01-1.0.0.jar –spring.profiles.active=prod

Url Pruebas locales
 
	http://localhost:8080/taller01/
	
Url base producción

	http://example.com/#/


## Servicio rest para recomendación de usuarios

Url de ejemplo
	
	GET
	http://localhost:8080/taller01/api/recommendation/user?similarityAlgorithm=J&neighbors=5&userID=1&numberOfRecommendations=10

Donde:

	similarityAlgorithm : 	
		'J' : ALGORITHM_JACCARD
	    'C' : ALGORITHM_COSINE
		'P' : ALGORITHM_PEARSON

Ejemplo del JSON que genera como resultado:

	{
	   "executionTime":"00:00:03.295",
	   "recommendations":[
	      {
	         "value":3.0922384,
	         "artist":{
	            "id":48734,
	            "gid":"c07f0676-9143-4217-8a9f-4c26bd636f13",
	            "name":"My Chemical Romance",
	            "comment":null,
	            "photoUrl":"https://lastfm-img2.akamaized.net/i/u/174s/68ab65fe1ae74ae199f3b08a5b9eb26c.png",
	            "totalPlays":17494,
	            "totalUsers":294,
	            "wiki":null
	         }
	      },
	      {
	         "value":2.9317074,
	         "artist":{
	            "id":1968,
	            "gid":"b0799818-22cb-4564-8e68-3c410d0722ee",
	            "name":"Silverchair",
	            "comment":"Australian rock band",
	            "photoUrl":null,
	            "totalPlays":5483,
	            "totalUsers":167,
	            "wiki":null
	         }
	      },
	      {
	         "value":2.6410077,
	         "artist":{
	            "id":51703,
	            "gid":"d8354b38-e942-4c89-ba93-29323432abc3",
	            "name":"Thirty Seconds to Mars",
	            "comment":null,
	            "photoUrl":"https://lastfm-img2.akamaized.net/i/u/174s/88b3fdef5e6b9a78727d2f6f4d86de7a.png",
	            "totalPlays":10039,
	            "totalUsers":211,
	            "wiki":null
	         }
	      },
	      {
	         "value":2.5691788,
	         "artist":{
	            "id":112461,
	            "gid":"516cef4d-0718-4007-9939-f9b38af3f784",
	            "name":"Fall Out Boy",
	            "comment":"US pop-punk band",
	            "photoUrl":"https://lastfm-img2.akamaized.net/i/u/174s/ea4dd5a09f71f7d68d28ce1bcc45f52e.png",
	            "totalPlays":17505,
	            "totalUsers":272,
	            "wiki":null
	         }
	      },
	      {
	         "value":2.5512195,
	         "artist":{
	            "id":218249,
	            "gid":"b7a05028-423c-49f2-a7ac-68591c7cf78d",
	            "name":"Fightstar",
	            "comment":null,
	            "photoUrl":null,
	            "totalPlays":3588,
	            "totalUsers":34,
	            "wiki":null
	         }
	      },
	      {
	         "value":2.5230596,
	         "artist":{
	            "id":12389,
	            "gid":"8bfac288-ccc5-448d-9573-c33ea2aa5c30",
	            "name":"Red Hot Chili Peppers",
	            "comment":null,
	            "photoUrl":"https://lastfm-img2.akamaized.net/i/u/174s/13c90c61d0bc47fd9f391831552a94b2.png",
	            "totalPlays":45544,
	            "totalUsers":572,
	            "wiki":"The Red Hot Chili Peppers are a funk rock band based in Hollywood, California and were formed in 1983, in Los Angeles, California. The band currently consists of founding members Anthony Kiedis (vocals) and Michael \"Flea\" Balzary (bass) along with a longtime member Chad Smith (drums). Guitarist John Frusciante quietly left the band on good terms in late 2008. On January 2, 2010, Josh Klinghoffer was announced as Frusciante's replacement.\n\nThe band initially formed for a one-off support act for a friend's band <a href=\"https://www.last.fm/music/Red+Hot+Chili+Peppers\">Read more on Last.fm</a>"
	         }
	      },
	      {
	         "value":2.482866,
	         "artist":{
	            "id":495,
	            "gid":"b202beb7-99bd-47e7-8b72-195c8d72ebdd",
	            "name":"Christina Aguilera",
	            "comment":null,
	            "photoUrl":"https://lastfm-img2.akamaized.net/i/u/174s/76db9ab6d0924d4facc6a3248e80d543.png",
	            "totalPlays":11693,
	            "totalUsers":279,
	            "wiki":null
	         }
	      },
	      {
	         "value":2.2543437,
	         "artist":{
	            "id":187983,
	            "gid":"1fda852b-92e9-4562-82fa-c52820a77b23",
	            "name":"The Pussycat Dolls",
	            "comment":null,
	            "photoUrl":"https://lastfm-img2.akamaized.net/i/u/174s/14b182b07a0c40debdb18119b4be9ad6.png",
	            "totalPlays":6120,
	            "totalUsers":182,
	            "wiki":"The Dolls began as a burlesque troupe based in Los Angeles, California, U.S., and expanded to Caesars Palace in Las Vegas. The group, which was founded by choreographer Robin Antin in 1995, eventually got a record deal in 2003 with A&M Records and Interscope Records. They were transferred and moulded into a music group and by 2005 they had recorded their first album PCD.\n\nPCD, was released September 12th 2006 in the UK and Septemeber 13th in the US. <a href=\"https://www.last.fm/music/The+Pussycat+Dolls\">Read more on Last.fm</a>"
	         }
	      },
	      {
	         "value":2.0801394,
	         "artist":{
	            "id":315753,
	            "gid":"122d63fc-8671-43e4-9752-34e846d62a9c",
	            "name":"Katy Perry",
	            "comment":null,
	            "photoUrl":"https://lastfm-img2.akamaized.net/i/u/174s/fd2007e3cd0b7b06b63e066c6c122088.png",
	            "totalPlays":4459,
	            "totalUsers":187,
	            "wiki":"Katy Perry (born Katheryn Elizabeth Hudson on 25 October 1984) is a singer/songwriter from Santa Barbara, California, United States. Since the release of her 2007 debut single \"Ur So Gay\", Perry has released four albums - 2008's \"One of the Boys\", 2010's \"Teenage Dream\", 2013's \"Prism\", and 2017's \"Witness\".- and has had nine Billboard Hot 100 #1 hit singles.\n\nUnder the name Katy Hudson, she released a self-titled gospel album in 2001, which was unsuccessful. <a href=\"https://www.last.fm/music/Katy+Perry\">Read more on Last.fm</a>"
	         }
	      },
	      {
	         "value":2.0461192,
	         "artist":{
	            "id":58875,
	            "gid":"09885b8e-f235-4b80-a02a-055539493173",
	            "name":"The All‐American Rejects",
	            "comment":null,
	            "photoUrl":"https://lastfm-img2.akamaized.net/i/u/174s/09ccf6a09cbc4f06813a75590d7ead61.png",
	            "totalPlays":6655,
	            "totalUsers":200,
	            "wiki":null
	         }
	      }
	   ],
	   "userId":1,
	   "numberOfUserRatings":8,
	   "algorithm":"J",
	   "neighbors":6
	}



## servicio rest para adicionar preferencia a la bd

	GET http://localhost:8080/taller01/api/recommendation/setPreference?userID=1&itemID=1&value=5

	El **value** va entre 1 y 5 (números enteros)



## usuarios

### Servicio para obtener la lista de todos los usuarios

	GET http://localhost:8080/taller01/api/user/

Ejemplo respuesta:

	[{"id":1,"name":"user_000061","age":null,"totalPlays":9,"totalArtists":9,"lastfmUser":null},
	{"id":2,"name":"user_000240","age":null,"totalPlays":2,"totalArtists":2,"lastfmUser":null},...

### Obtener un usuario específico

	http://localhost:8080/taller01/api/user/{id}

Ejemplo

	http://localhost:8080/taller01/api/user/11

Respuesta:

	{"id":11,"name":"user_000004","age":null,"totalPlays":17641,"totalArtists":1623,"lastfmUser":null}

### Crear usuario

POST
si se asigna el parámetro lastfmUser la aplicación trata de importar el top 50 de los artistas que
el usuario tiene en su perfil de lastfm
Ejemplo con
https://www.last.fm/user/riotDollGrrrl

	curl -v -H "Accept: application/json"  -H "Content-Type: application/json" -X POST -d '{"name":"riotDollGrrrl","age":null,"lastfmUser":"riotDollGrrrl"}' http://localhost:8080/taller01/api/user/

retorna el json con el objeto que creó en la bd

	{"id":2005,"name":"my_new_user_000004","age":null,"totalPlays":17641,"totalArtists":1623,"lastfmUser":null}

### Obtener datos de un artista

	GET http://localhost:8080/taller01/api/artist?id=2475

Resultado

	{
	  "id": 2475,
	  "gid": "a505bb48-ad65-4af4-ae47-29149715bff9",
	  "name": "Thievery Corporation",
	  "comment": null,
	  "photoUrl": "https://lastfm-img2.akamaized.net/i/u/174s/4c5f7d1a06a54ddcbb1a268279d524bf.png",
	  "totalPlays": 15269,
	  "totalUsers": 358,
	  "wiki": "Thievery Corporation is a Washington, D.C.-based recording artist and DJ duo consisting of Rob Garza and Eric Hilton and their supporting artists. Their music style mixes elements of dub, acid jazz, Indian classical and Brazilian (such as bossa nova) with a lounge aesthetic.\n\nThievery Corporation is on the Eighteenth Street Lounge record label named after the DC club owned by Eric Hilton, but several of their singles and EP's appear on the 4AD and !K7 labels. <a href=\"https://www.last.fm/music/Thievery+Corporation\">Read more on Last.fm</a>",
	  "lastRecommendedAt": 1505388474072
	}

### Obtener los artistas similares de otro  (item / item)

	GET http://localhost:8080/taller01/api/artist/similar?id=2475

Resultado

	{
	  "executionTime": null,
	  "recommendations": [
	    {
	      "value": 0.6060606,
	      "artist": {
	        "id": 24143,
	        "gid": "c23b637b-97c6-41eb-8ef6-6c724efc80a8",
	        "name": "Zero 7",
	        "comment": null,
	        "photoUrl": "https://lastfm-img2.akamaized.net/i/u/174s/c3ac5b9c9d6348c59cdf602d0f2b22f4.png",
	        "totalPlays": 17644,
	        "totalUsers": 384,
	        "wiki": "Zero 7 is a London, UK downtempo duo, founded in 1999, by Henry Binns & Sam Hardaker. Their songs feature vocalists: Mozez, Sia, Tina Dico, Sophie Barker, José González, Eska Mtungwazi (aka Eska), Jackie Daniels, Martha Tilston & Rowdy Superstar.\n\nAlbums: Début Simple Things (23 Apr 2001, feat' vocalists: Mozez, Sia Furler & Sophie Barker), When It Falls (1 Mar 2004, feat' all \"Simple Things\" vocalists, plus Tina Dico), The Garden (22 May 2006 <a href=\"https://www.last.fm/music/Zero+7\">Read more on Last.fm</a>",
	        "lastRecommendedAt": 1505388471551
	      }
	    },
	    {
	      "value": 0.529976,
	      "artist": {
	        "id": 1742,
	        "gid": "0d4bc6e5-2a3e-4fdf-8bbf-59ad0bc374d7",
	        "name": "Lamb",
	        "comment": "electronica",
	        "photoUrl": "https://lastfm-img2.akamaized.net/i/u/174s/31d20fe530fd4f57ac6ea3fcfd009d2d.png",
	        "totalPlays": 9163,
	        "totalUsers": 280,
	        "wiki": "Lamb is a name of at least five acts:\n\n(1) An electronic duo formed in 1996 in Manchester, England, by Lou Rhodes (singer-songwriter) andAndy Barlow (music/production). They incorporated into their musical style a distinctive mixture of downbeat, trip-hop, jazz, dub, breaks, and drum and bass with a strong vocal element and, in their later works especially, some acoustic influences. Lamb are notable for their highly experimental work, a distinctive production style, Lou's often passionate lyrical style, and their artistic videos.  <a href=\"https://www.last.fm/music/Lamb\">Read more on Last.fm</a>",
	        "lastRecommendedAt": 1505388793890
	      }
	    },
	    {
	      "value": 0.52285194,
	      "artist": {
	        "id": 65,
	        "gid": "8f6bd1e4-fbe1-4f50-aa9b-94c450ec0f11",
	        "name": "Portishead",
	        "comment": null,
	        "photoUrl": "https://lastfm-img2.akamaized.net/i/u/174s/5436fd42cfb54aeeca15e6ed0c7cd1de.png",
	        "totalPlays": 26756,
	        "totalUsers": 475,
	        "wiki": "Portishead are a band from Bristol, United Kingdom, named after a small coastal town twelve miles west of said musical hotbed, in North Somerset. They were initially known for their use of jazz samples and some hip-hop beats along with various synth sounds and the hauntingly beautiful vocals of singer Beth Gibbons. Their current sound drops the samples in favour of a harder, more abrasive edge, but retains Gibbons' vocals.\n\nThe band was formed in 1991 (then later split in 2005 reforming in 2008) <a href=\"https://www.last.fm/music/Portishead\">Read more on Last.fm</a>",
	        "lastRecommendedAt": 1505388464821
	      }
	    },
	    {
	      "value": 0.5218121,
	      "artist": {
	        "id": 911,
	        "gid": "cb67438a-7f50-4f2b-a6f1-2bb2729fd538",
	        "name": "Air",
	        "comment": "French band",
	        "photoUrl": "https://lastfm-img2.akamaized.net/i/u/174s/5f0be5ea3f5f4ac98ab535f2d6c968dd.png",
	        "totalPlays": 36795,
	        "totalUsers": 549,
	        "wiki": "There are at least seven artists with this name:\n1. A popular electronic music duo from France\n2. A jazz trio from the United States\n3. A noise rock band from Japan\n4. An alias of ambient musician Pete Namlook\n5. An alternative pop band from Indonesia\n6. A pop group from the Philippines \n7. A french synth-pop band also known as Air 89\n\n1) The French band Air is a duo consisting of Nicolas Godin and Jean-Benoit Dunckel. They went to school in Versailles, (Lycée Jules Ferry) before forming the band in 1995. <a href=\"https://www.last.fm/music/Air\">Read more on Last.fm</a>",
	        "lastRecommendedAt": 1505388460235
	      }
	    },
	    {
	      "value": 0.5186441,
	      "artist": {
	        "id": 4,
	        "gid": "10adbe5e-a2c0-4bf3-8249-2b4cbf6e6ca8",
	        "name": "Massive Attack",
	        "comment": null,
	        "photoUrl": "https://lastfm-img2.akamaized.net/i/u/174s/f21aba32c5c52f77718b970cb1ebd788.png",
	        "totalPlays": 31997,
	        "totalUsers": 538,
	        "wiki": "Massive Attack are a trip-hop group which formed in Bristol, England in 1988. The group currently consists of Robert \"3D\" Del Naja and Grant \"Daddy G\" Marshall. Third member Andy \"Mushroom\" Vowles left the group in 1999. The band has released five studio albums: \"Blue Lines\" (1991), \"Protection\" (1994), \"Mezzanine\" (1998), \"100th Window\" (2003), and \"Heligoland\" (2010). On 28 January 2016, Massive Attack released a new EP, Ritual Spirit, followed by The Spoils in July. <a href=\"https://www.last.fm/music/Massive+Attack\">Read more on Last.fm</a>",
	        "lastRecommendedAt": 1505388460586
	      }
	    },
	    {
	      "value": 0.5149425,
	      "artist": {
	        "id": 913,
	        "gid": "35723b60-732e-4bd8-957f-320b416e7b7f",
	        "name": "Groove Armada",
	        "comment": null,
	        "photoUrl": "https://lastfm-img2.akamaized.net/i/u/174s/14ba31bdd445447f81ed06c8647a1f3e.png",
	        "totalPlays": 7829,
	        "totalUsers": 301,
	        "wiki": "Groove Armada is an electronic music group from Cambridge, United Kingdom, composed of two members, Andy Cato (real name Andrew Cocup) and Tom Findlay. They first came to fame with a 1997 limited single release, At the River (made famous after being used in various Marks & Spencer advertisements), which sampled \"Old Cape Cod\" by Patti Page. The song has since made its way onto numerous chill-out compilations.\n\nTheir most famous work is the 2000 international hit I See You Baby <a href=\"https://www.last.fm/music/Groove+Armada\">Read more on Last.fm</a>",
	        "lastRecommendedAt": 1505388701703
	      }
	    },
	    {
	      "value": 0.51282054,
	      "artist": {
	        "id": 963,
	        "gid": "067102ea-9519-4622-9077-57ca4164cfbb",
	        "name": "Morcheeba",
	        "comment": null,
	        "photoUrl": "https://lastfm-img2.akamaized.net/i/u/174s/c0e6b929bdad4b74b26b1ff2ab55685e.png",
	        "totalPlays": 10367,
	        "totalUsers": 291,
	        "wiki": "Morcheeba are a British band that mixes influences from rock, trip hop, rhythm and blues and pop. The word \"morcheeba\" means \"the way of marijuana\" (\"MOR\" - the middle of the road & \"Cheeba\" - informal name for cannabis).\n\nFormed in 1995, they consisted of Paul Godfrey (DJ), Ross Godfrey on guitar and keyboards with Skye Edwards as the primary vocalist fronting the band.  While churning up interest through an underground presence mostly in London, the band finally broke through when featured on the Ultra-Chilled series. <a href=\"https://www.last.fm/music/Morcheeba\">Read more on Last.fm</a>",
	        "lastRecommendedAt": 1505388791084
	      }
	    },
	    {
	      "value": 0.5102804,
	      "artist": {
	        "id": 21786,
	        "gid": "7b885d42-3c41-4f43-9944-a5855ec5155e",
	        "name": "Goldfrapp",
	        "comment": null,
	        "photoUrl": "https://lastfm-img2.akamaized.net/i/u/174s/2f276be6cd10df85899cbbd663bc6e44.png",
	        "totalPlays": 18346,
	        "totalUsers": 450,
	        "wiki": "Goldfrapp is an electronic music duo, formed in 1999 in London, England, and made up of Alison Goldfrapp (vocals, songwriter) and Will Gregory (songwriter, producer). Their musical style has changed over time, from a cinematic trip-hop sound, through electropop, to a glam rock influenced sound, folktronica and 80's synthpop.\n\nThe majority of the band's songs are composed by Alison and Will, although they have collaborated with session musician Nick Batt several times. <a href=\"https://www.last.fm/music/Goldfrapp\">Read more on Last.fm</a>",
	        "lastRecommendedAt": 1505388466846
	      }
	    },
	    {
	      "value": 0.5009311,
	      "artist": {
	        "id": 52208,
	        "gid": "1c70a3fc-fa3c-4be1-8b55-c3192db8a884",
	        "name": "Röyksopp",
	        "comment": null,
	        "photoUrl": "https://lastfm-img2.akamaized.net/i/u/174s/90b0067c264e47c0911ef7e7e8e08a98.png",
	        "totalPlays": 23131,
	        "totalUsers": 448,
	        "wiki": "Röyksopp is an electronic music duo formed in 1998 in Tromsø, Norway and currently based in Bergen, Norway. The band consists of Torbjørn Brundtland and Svein Berge, both being songwriters, producers and remixers. They usually rely on the use of other vocalists for their songs and occasionally they perform the vocal parts. The Norwegian word \"røyksopp\" means puffball fungus (literally \"smoke mushroom\"), but the band has stated in interviews that they are actually referring to the mushroom cloud created by atomic bombs. <a href=\"https://www.last.fm/music/R%C3%B6yksopp\">Read more on Last.fm</a>",
	        "lastRecommendedAt": 1505388467146
	      }
	    },
	    {
	      "value": 0.49369746,
	      "artist": {
	        "id": 11367,
	        "gid": "69158f97-4c07-4c4e-baf8-4e4ab1ed666e",
	        "name": "Boards of Canada",
	        "comment": null,
	        "photoUrl": "https://lastfm-img2.akamaized.net/i/u/174s/9d64c6eefade4d27baeb7d897887a4a4.png",
	        "totalPlays": 30796,
	        "totalUsers": 353,
	        "wiki": "Boards of Canada are an electronic music duo from Edinburgh, Scotland, UK, formed in 1985 and comprised of brothers Mike Sandison (born 1 June 1970) and Marcus Eoin (born 21 July 1971). They have released a number of works, most notably the studio albums Music Has the Right to Children (1998), Geogaddi (2002), and The Campfire Headphase (2005) on the pioneering electronic music label Warp. Their fourth album Tomorrow's Harvest was released in June 2013. <a href=\"https://www.last.fm/music/Boards+of+Canada\">Read more on Last.fm</a>",
	        "lastRecommendedAt": 1505388690660
	      }
	    }
	  ],
	  "userId": null,
	  "numberOfUserRatings": null,
	  "algorithm": "\u0000",
	  "neighbors": null,
	  "itemId": 2475
	}


### top 50 de artistas màs populares por cantidad de usuarios dentro del dataset

	GET http://localhost:8080/taller01/api/artist/popular

Retorna

	Array con los 50 artistas más populares (cantidad de usuarios que los tienen en el dataset)
	
	
### Obtener todas los ratings (preferencias) creadas por un usuarrio 

Url 

	http://localhost:8080/taller01/api/user/ratings/{id}

Ejemplo 
	
	http://localhost:8080/taller01/api/user/ratings/2010

Resultado:

	[
		{
		value: 4,
		artist: {
		id: 30696,
		gid: "f181961b-20f7-459e-89de-920ef03c7ed0",
		name: "The Strokes",
		comment: null,
		photoUrl: "https://lastfm-img2.akamaized.net/i/u/174s/f5b08071d97847bd80a268728c39b0db.png",
		totalPlays: 29589,
		totalUsers: 487,
		wiki: "The Strokes are an American rock band from New York City, New York, United States, formed in 1998. The band rose to fame in the early 2000s as a leading group in garage rock/post-punk revival. The band consists of Julian Casablancas (lead vocals), Nick Valensi (lead guitar), Albert Hammond, Jr. (rhythm guitar), Nikolai Fraiture (bass guitar) and Fabrizio Moretti (drums and percussion). Upon the release of their debut album, Is This It, in 2001 <a href="https://www.last.fm/music/The+Strokes">Read more on Last.fm</a>",
		lastRecommendedAt: 1505388464010
		}
	},
	{
		value: 4,
		artist: {
		id: 437457,
		gid: "af37c51c-0790-4a29-b995-456f98a6b8c9",
		name: "Vampire Weekend",
		comment: null,
		photoUrl: "https://lastfm-img2.akamaized.net/i/u/174s/da34e4573b8c444889b148766f041f09.png",
		totalPlays: 6881,
		totalUsers: 215,
		wiki: "Vampire Weekend is an indie rock band which formed in February 2006 in New York City, New York, United States. The band consists of Ezra Koenig (vocals, guitar), Chris Baio (bass) and Christopher Tomson (drums). Rostam Batmanglij has left the band in early 2016 to begin solo career, but will continue to collaborate with Ezra. The band has released three albums: "Vampire Weekend" (2008), "Contra" (2010)", and "Modern Vampires of the City" (2013). The band has gained positive comparisons to such artists as Paul Simon, Haircut 100 and The Walkmen. <a href="https://www.last.fm/music/Vampire+Weekend">Read more on Last.fm</a>",
		lastRecommendedAt: 1505388873014
		}
	},
	{...
	

### Obtener top 5 de los ratings (preferencias) creadas por un usuario. Para cada artista incluye lista de los más similares 

Url 

	http://localhost:8080/taller01/api/user/ratings/top5/{id}

Ejemplo 
	
	http://localhost:8080/taller01/api/user/ratings/top5/2011

Resultado:

	[
		{
		value: 5,
		artist: {
			id: 11,
			gid: "31810c40-932a-4f2d-8cfd-17849844e2a6",
			name: "Squirrel Nut Zippers",
			comment: null,
			photoUrl: "https://lastfm-img2.akamaized.net/i/u/174s/4b407f9211f24113b91ca2781cf3a4d9.png",
			totalPlays: 881,
			totalUsers: 58,
			wiki: "Squirrel Nut Zippers are a big band/jazz/alternative rock band which formed in Chapel Hill, North Carolina, United States in 1993. They split in 2000 and reunited in 2007. They consist of Jim Mathus (vocals, guitar, trombone), Katharine Whalen (banjo, ukulele, vocals), Stuart Cole (bass), Je Widenhouse (cornet), Will Dawson (saxophone), Henry Westmoreland (saxophone) and Chris Phillips (drums). Former members of the band include Ken Mosher (guitar, saxophone), Stacy Guess (trumpet) and Tom Maxwell (guitar, saxophone, clarinet) <a href="https://www.last.fm/music/Squirrel+Nut+Zippers">Read more on Last.fm</a>",
			lastRecommendedAt: 1505392002671
		},
		similarArtists: {
			executionTime: null,
			recommendations: [
				{
				value: 0.22340426,
				artist: {
				id: 4473,...

### Usuarios más similares a otro

Url de ejemplo
	
	GET
	http://localhost:8080/taller01/api/user/similar??similarityAlgorithm=J&neighbors=5&userID=606&numberOfRecommendations=10

Donde:

	similarityAlgorithm : 	
		'J' : ALGORITHM_JACCARD
	    'C' : ALGORITHM_COSINE
		'P' : ALGORITHM_PEARSON

Ejemplo del JSON que genera como resultado:

	{  
	   "executionTime":"00:00:02.401",
	   "recommendations":null,
	   "similarUsers":[  
	      {  
	         "id":2010,
	         "name":"Isabel94",
	         "age":null,
	         "totalPlays":17641,
	         "totalArtists":1623,
	         "lastfmUser":"Isabel94"
	      },
	      {  
	         "id":105,
	         "name":"user_000099",
	         "age":22,
	         "totalPlays":5366,
	         "totalArtists":51,
	         "lastfmUser":null
	      },
	      {  
	         "id":791,
	         "name":"user_000796",
	         "age":null,
	         "totalPlays":728,
	         "totalArtists":48,
	         "lastfmUser":null
	      },
	      {  
	         "id":435,
	         "name":"user_000435",
	         "age":null,
	         "totalPlays":1263,
	         "totalArtists":72,
	         "lastfmUser":null
	      },
	      {  
	         "id":231,
	         "name":"user_000225",
	         "age":null,
	         "totalPlays":4712,
	         "totalArtists":72,
	         "lastfmUser":null
	      },
	      {  
	         "id":958,
	         "name":"user_000965",
	         "age":null,
	         "totalPlays":4643,
	         "totalArtists":106,
	         "lastfmUser":null
	      },
	      {  
	         "id":414,
	         "name":"user_000413",
	         "age":null,
	         "totalPlays":1727,
	         "totalArtists":102,
	         "lastfmUser":null
	      },
	      {  
	         "id":234,
	         "name":"user_000228",
	         "age":22,
	         "totalPlays":5534,
	         "totalArtists":96,
	         "lastfmUser":null
	      },
	      {  
	         "id":860,
	         "name":"user_000866",
	         "age":null,
	         "totalPlays":42,
	         "totalArtists":7,
	         "lastfmUser":null
	      },
	      {  
	         "id":202,
	         "name":"user_000196",
	         "age":22,
	         "totalPlays":13782,
	         "totalArtists":168,
	         "lastfmUser":null
	      }
	   ],
	   "userId":606,
	   "numberOfUserRatings":24,
	   "algorithm":"J",
	   "neighbors":5,
	   "itemId":null
	}
	
### 50 artistas de forma aleatoria
Url 

	http://localhost:8080/taller01/api/artist/random/

Respuesta:

	[
		{
		id: 46983,
		gid: "6f21c5ae-db05-4ae0-9467-7a3a7afb2c4b",
		name: "Ikara Colt",
		comment: null,
		photoUrl: null,
		totalPlays: 433,
		totalUsers: 18,
		wiki: null,
		lastRecommendedAt: 1505476625413
		},
		{
		id: 39926,
		gid: "e188a520-9cb7-4f73-a3d7-2f70c6538e92",
		name: "Adam Ant",
		comment: null,
		photoUrl: "https://lastfm-img2.akamaized.net/i/u/174s/153a3f96a80240bb9f12d7a265f404b5.png",
		totalPlays: 966,
		totalUsers: 52,
		wiki: "Adam Ant is the stage name of Stuart Leslie Goddard (born November 3, 1954, London, England), lead singer of 1980s pop group Adam and the Ants and also a solo artist. Adam was a member of Bazooka Joe, the band that the Sex Pistols opened for in the Pistols' first live appearance. Adam and the Ants started as a punk band, but by the early '80s they had taken a different path and started making pop music. By 1981 they were one of the biggest bands in Britain. <a href="https://www.last.fm/music/Adam+Ant">Read more on Last.fm</a>",
		lastRecommendedAt: 1505392103572
		},	...
		

### Crear Artista

POST

	curl -v -H "Accept: application/json"  -H "Content-Type: application/json" -X POST -d '{"gid":"93c89126-9f86-11e7-abc4-cec278b6b50a","name":"un artista nuevo en la lista 2017"}' http://localhost:8080/taller01/api/artist/										

	
