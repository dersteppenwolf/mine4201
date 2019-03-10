# listado de servicios

url base

    http://example.com/


## algunas estadísticas de los datos cargados

http://example.com/testdb

    {
    "bipartite_graph_number_of_nodes": 6923,
    flurs_userids: [
          173643,
          251197,
          238754,
          67102,
          .....
    "total_rated_movies": {
      "count": 45793
    },
    "bipartite_graph_number_of_edges": 41136,
    "bipartite_graph_edges_examples": [
    [
      "tt0050212",
      "48364"
    ],
    [
      "tt0050212",
      "44715"
    ],
    [
      "tt0050212",
      "219387"
    ],
    [
      "tt0050212",
      "263115"
    ],
    [
      "tt0050212",
      "120950"
    ],
    [
      "tt0050212",
      "6197"
    ],
    [
      "tt0050212",
      "20678"
    ],
    [
      "tt0050212",
      "95881"
    ],
    [
      "tt0050212",
      "212769"
    ],
    [
      "tt0050212",
      "47408"
    ],
    [
      "tt0050212",
      "237756"
    ]
    ],
    "total_graph_ratings": [
    6401808
    ]
    }

## 100 usuarios con màs ratings

retorna los 100 usuarios con más reviews en el dataset

  Url:  http://localhost:5001/user/popular?

resultado:

    [
        {
            "total_ratings": 3779,
            "userid": 45811
        },
        {
            "total_ratings": 3246,
            "userid": 172224
        },






# movie

obtiene los detalles de un movie

  Url: http://localhost:5001/movie?id=2788  

resultado

    {
        "directors": [
            {
                "birth_year": "1925",
                "death_year": "2002",
                "name": "nm0534084",
                "primary_name": "Ian MacNaughton"
            }
        ],
        "end_year": "\\N",
        "genres": [
            {
                "code": "g_1",
                "genre": "Comedy"
            }
        ],
        "id": 2704,
        "imdb_tconst": "tt0066765",
        "is_adult": "0",
        "movieid": 2788,
        "original_title": "And Now for Something Completely Different",
        "primary_title": "And Now for Something Completely Different",
        "principals": [
            {
                "birth_year": "1943",
                "death_year": "\\N",
                "name": "nm0001385",
                "primary_name": "Eric Idle"
            },
            {
                "birth_year": "1943",
                "death_year": "\\N",
                "name": "nm0001589",
                "primary_name": "Michael Palin"
            },
            {
                "birth_year": "1935",
                "death_year": "\\N",
                "name": "nm0611341",
                "primary_name": "David Muir"
            },
            {
                "birth_year": "\\N",
                "death_year": "\\N",
                "name": "nm0633677",
                "primary_name": "Thom Noble"
            },
            {
                "birth_year": "1939",
                "death_year": "\\N",
                "name": "nm0000092",
                "primary_name": "John Cleese"
            },
            {
                "birth_year": "1925",
                "death_year": "2002",
                "name": "nm0534084",
                "primary_name": "Ian MacNaughton"
            },
            {
                "birth_year": "1940",
                "death_year": "\\N",
                "name": "nm0000416",
                "primary_name": "Terry Gilliam"
            },
            {
                "birth_year": "1941",
                "death_year": "1989",
                "name": "nm0001037",
                "primary_name": "Graham Chapman"
            },
            {
                "birth_year": "1942",
                "death_year": "\\N",
                "name": "nm0001402",
                "primary_name": "Terry Jones"
            },
            {
                "birth_year": "\\N",
                "death_year": "\\N",
                "name": "nm0143513",
                "primary_name": "Patricia Casey"
            }
        ],
        "release_decade": "1970",
        "runtime_minutes": "88",
        "start_year": "1971",
        "tags": [
            {
                "tag": "Monty Python",
                "total": 14
            },
            {
                "tag": "sketch comedy",
                "total": 10
            },
            {
                "tag": "British",
                "total": 9
            },
            {
                "tag": "MontyPython",
                "total": 7
            },
            {
                "tag": "monty python",
                "total": 6
            },
            {
                "tag": "British Humor",
                "total": 6
            },
            {
                "tag": "silly",
                "total": 3
            },
            {
                "tag": "Based on a TV show",
                "total": 3
            },
            {
                "tag": "comedy",
                "total": 2
            },
            {
                "tag": "John Cleese",
                "total": 2
            },
            {
                "tag": "british comedy",
                "total": 2
            },
            {
                "tag": "clip show",
                "total": 2
            },
            {
                "tag": "could've made a better selection from the TV series",
                "total": 2
            }
        ],
        "title": "Monty Python's And Now for Something Completely Different (1971)",
        "title_type": "movie",
        "tmdb_backdrop_path": "https://image.tmdb.org/t/p/w300//qQ2So5TepTJBVnesRH5QWbJEnYd.jpg",
        "tmdb_id": 9267,
        "tmdb_overview": "And Now for Something Completely Different is a film spin-off from the television comedy series Monty Python's Flying Circus featuring favourite sketches from the first two seasons.",
        "tmdb_poster_path": "https://image.tmdb.org/t/p/w342//ajbdFQLvJTlNu4LnVWGnNMb4mZ8.jpg",
        "tmdb_release_date": "1971-09-28",
        "writers": [
            {
                "birth_year": "1943",
                "death_year": "\\N",
                "name": "nm0001385",
                "primary_name": "Eric Idle"
            },
            {
                "birth_year": "1943",
                "death_year": "\\N",
                "name": "nm0001589",
                "primary_name": "Michael Palin"
            },
            {
                "birth_year": "1939",
                "death_year": "\\N",
                "name": "nm0000092",
                "primary_name": "John Cleese"
            },
            {
                "birth_year": "1940",
                "death_year": "\\N",
                "name": "nm0000416",
                "primary_name": "Terry Gilliam"
            },
            {
                "birth_year": "1941",
                "death_year": "1989",
                "name": "nm0001037",
                "primary_name": "Graham Chapman"
            },
            {
                "birth_year": "1942",
                "death_year": "\\N",
                "name": "nm0001402",
                "primary_name": "Terry Jones"
            }
        ]
    }


# recomendaciones por pagerank personalizado

obtiene 10 recomendaciones calculadas por el método de pagerank personalizado

  Url:   http://example.com//user/recs/pprank?id=134988

resultado

    {
        "id": "107664",
        "recs": [
                  {
                  "id": "tt0096895",
                  "score": 0.004193110631824182,
                  "movie_data": {
                    ... datos detallados de la película
                  },
                  {... otras recomendaciones

# explicación de las recomendaciones

dado un usuario y una película trata de dar una explicación a la recomendación generada
(a través de recorrer el grafo de la ontología )

Ejemplos

parámetros: id del usuario e id de imdb

**ejemplo 1:**

  Url: http://localhost:5001/user/recs/explain?userid=172224&movieid=tt0114388

resultado:

    {
        "explanation": [
            "User",
            172224,
            "RATED",
            "Movie",
            "The Exorcism of Emily Rose",
            "AND",
            "Person",
            "Tom Wilkinson",
            "ACTED_IN",
            "Movie",
            "The Exorcism of Emily Rose",
            "AND",
            "Person",
            "Tom Wilkinson",
            "ACTED_IN",
            "Movie",
            "Sense and Sensibility"
        ],
        "movieid": "tt0114388",
        "userid": "172224"
    }

**ejemplo 2:**

url
    http://localhost:5001/user/recs/explain?userid=172224&movieid=tt0203009


resultado ;

      {
          "explanation": [
              "User",
              172224,
              "RATED",
              "Movie",
              "The Exorcism of Emily Rose",
              "AND",
              "Movie",
              "The Exorcism of Emily Rose",
              "BELONGS_TO",
              "Genre",
              "Drama",
              "AND",
              "Movie",
              "Moulin Rouge!",
              "BELONGS_TO",
              "Genre",
              "Drama"
          ],
          "movieid": "tt0203009",
          "userid": "172224"
      }



## streams recientes

últimos 20 streams ejecutados en el sistema

url :   http://localhost:5001/streams/recent?

    [
        {
            "day": "2006-03-07",
            "end_id": 6481114,
            "id": 3611,
            "start_id": 6478720,
            "stream_date": "2017-12-06 01:41:32",
            "total": 2395
        },
        {
            "day": "2006-03-06",
            "end_id": 6478719,
            "id": 3610,
            "start_id": 6476421,
            "stream_date": "2017-12-06 01:40:27",
            "total": 2299
        },
        {
            "day": "2006-03-05",
            "end_id": 6476420,
            "id": 3609,
            "start_id": 6474500,
            "stream_date": "2017-12-06 00:04:11",
            "total": 1921
        },
        {
            "day": "2006-03-04",
            "end_id": 6474499,
            "id": 3608,
            "start_id": 6472463,
            "stream_date": "2017-12-06 00:04:04",
            "total": 2037
        },
        {
            "day": "2006-03-03",
            "end_id": 6472462,
            "id": 3607,
            "start_id": 6471239,
            "stream_date": "2017-12-06 00:03:41",
            "total": 1224
        },
        {
            "day": "2006-03-02",
            "end_id": 6471238,
            "id": 3606,
            "start_id": 6469553,
            "stream_date": "2017-12-06 00:03:11",
            "total": 1686
        },
        {
            "day": "2006-03-01",
            "end_id": 6469552,
            "id": 3605,
            "start_id": 6468526,
            "stream_date": "2017-12-06 00:03:02",
            "total": 1027
        },
        {
            "day": "2006-02-28",
            "end_id": 6468525,
            "id": 3604,
            "start_id": 6466774,
            "stream_date": "2017-12-06 00:02:41",
            "total": 1752
        },
        {
            "day": "2006-02-27",
            "end_id": 6466773,
            "id": 3603,
            "start_id": 6465602,
            "stream_date": "2017-12-06 00:02:15",
            "total": 1172
        },
        {
            "day": "2006-02-26",
            "end_id": 6465601,
            "id": 3602,
            "start_id": 6464086,
            "stream_date": "2017-12-06 00:02:11",
            "total": 1516
        },
        {
            "day": "2006-02-25",
            "end_id": 6464085,
            "id": 3601,
            "start_id": 6462761,
            "stream_date": "2017-12-06 00:01:45",
            "total": 1325
        },
        {
            "day": "2006-02-24",
            "end_id": 6462760,
            "id": 3600,
            "start_id": 6461419,
            "stream_date": "2017-12-06 00:01:41",
            "total": 1342
        },
        {
            "day": "2006-02-23",
            "end_id": 6461418,
            "id": 3599,
            "start_id": 6459952,
            "stream_date": "2017-12-06 00:01:15",
            "total": 1467
        },
        {
            "day": "2006-02-22",
            "end_id": 6459951,
            "id": 3598,
            "start_id": 6459031,
            "stream_date": "2017-12-06 00:01:11",
            "total": 921
        },
        {
            "day": "2006-02-21",
            "end_id": 6459030,
            "id": 3597,
            "start_id": 6457308,
            "stream_date": "2017-12-05 23:53:49",
            "total": 1723
        },
        {
            "day": "2006-02-20",
            "end_id": 6457307,
            "id": 3596,
            "start_id": 6455922,
            "stream_date": "2017-12-05 23:53:45",
            "total": 1386
        },
        {
            "day": "2006-02-19",
            "end_id": 6455921,
            "id": 3595,
            "start_id": 6454117,
            "stream_date": "2017-12-05 23:53:19",
            "total": 1805
        },
        {
            "day": "2006-02-18",
            "end_id": 6454116,
            "id": 3594,
            "start_id": 6452596,
            "stream_date": "2017-12-05 23:53:15",
            "total": 1521
        },
        {
            "day": "2006-02-17",
            "end_id": 6452595,
            "id": 3593,
            "start_id": 6450373,
            "stream_date": "2017-12-05 23:52:49",
            "total": 2223
        },
        {
            "day": "2006-02-16",
            "end_id": 6450372,
            "id": 3592,
            "start_id": 6449170,
            "stream_date": "2017-12-05 23:52:45",
            "total": 1203
        }
    ]


## recomendaciones de flurs


url :   http://localhost:5001/user/recs/flurs?id=173643


retorna las tres categorias 

    {
        "fm": [
            {
                "id": 1497,
                "movie_data": {
                    ...
        "svd": [
            ....

        "mixed": [




##  recomendaciones por ontologia:  por director

utilizando la ontología se determinan los directores favoritos del usuario y a partir de eso se recomiendan películas

  url   http://example.com/user/recs/graph/directors?id=181749



    {
        "id": "181749",
        "recs": [
            {
                "id": [
                    527
                ],
                "movie_data": {
                    "directors": [
                        {
                            "birth_year": "1946",
                            "death_year": "\\N",
                            "name": "nm0000229",
                            "primary_name": "Steven Spielberg"
                        }
                    ],
                    "end_year": "\\N",
                    "genres": [
          .. etc...


##  recomendaciones por ontologia:  por actores

utilizando la ontología se determinan los actores favoritos del usuario y a partir de eso se recomiendan películas

  url     http://localhost:5001/user/recs/graph/actors?id=181749

    {
        "id": "181749",
        "recs": [
            {
                "id": [
                    527
                ],
                "movie_data": {
                    "directors": [
                        {
                            "birth_year": "1946",
                            "death_year": "\\N",
                            "name": "nm0000229",
                            "primary_name": "Steven Spielberg"
                        }
                    ],
                    "end_year": "\\N",
                    "genres": [
                        {
                            "code": "g_5",
                            "genre": "Biography"
                        },
                        {
                            "code": "g_2",
                            "genre": "Drama"
                        },
                        {
                            "code": "g_18",
                            "genre": "History"
                        }
                    ],
                    "id": 524,
                    "imdb_tconst": "tt0108052",
        ... etc
