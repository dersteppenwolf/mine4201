# listado de servicios

url base

    http://example.com/

## 100 usuarios con màs reviews

retorna los 100 usuarios con más reviews en el dataset

  Url:  http://localhost:5001/user/popular?

resultado

    [
        {
            "name": "Stefany",
            "id": 2508,
            "total_reviews": 1630
        },
        {
            "name": "Norm",
            "id": 33021,
            "total_reviews": 1275
        },

## categorias

retorna las 100 categorias más populares del dataset
Url:  http://localhost:5001/categories?

ejemplo respuesta

    [
        {
            "id": 1144,
            "title": "Restaurants",
            "total": 11953
        },
        {
            "id": 1220,
            "title": "Shopping",
            "total": 10952
        },
        {
            "id": 662,
            "title": "Home Services",
            "total": 9754
        },

# lugares


retorna todos los lugares del dataset

Url: http://localhost:5001/places?

ejemplo respuesta:

    [
        {
            "city": "Las Vegas",
            "country": "United States of America",
            "id": 71,
            "latitude": 36.1318193198863,
            "longitude": -115.197726012067,
            "state": "NV",
            "total_businesses": 20654
        },
        {
            "city": "Phoenix",
            "country": "United States of America",
            "id": 813,
            "latitude": 33.5298095000574,
            "longitude": -112.059116710615,
            "state": "AZ",
            "total_businesses": 13266
        },

# negocio

obtiene los detalles de un negocio

Url: http://localhost:5001/business?id=60


    {
        "attributes": {
            "AcceptsInsurance": true,
            "BikeParking": true,
            "BusinessAcceptsCreditCards": true,
            "BusinessParking": {
                "garage": false,
                "lot": false,
                "street": false,
                "valet": false,
                "validated": false
            },
            "ByAppointmentOnly": false,
            "RestaurantsPriceRange2": 1,
            "WheelchairAccessible": true
        },
        "business_id": "ozn76OaW1HA5mJDgQLg1cA",
        "categories": [
            {
                "category": "Shopping",
                "id": 468687,
                "id_category": 1220
            },
            {
                "category": "Optometrists",
                "id": 468688,
                "id_category": 946
            },
            {
                "category": "Health & Medical",
                "id": 468689,
                "id_category": 621
            },
            {
                "category": "Eyewear & Opticians",
                "id": 468690,
                "id_category": 945
            }
        ],
        "category_count": 4,
        "checkins_by_partofday": [
            {
                "day": "Saturday",
                "part_of_day": "Night",
                "total": 8
            },
            {
                "day": "Friday",
                "part_of_day": "Night",
                "total": 7
            },
            {
                "day": "Wednesday",
                "part_of_day": "Night",
                "total": 4
            },
            {
                "day": "Tuesday",
                "part_of_day": "Night",
                "total": 2
            },
            {
                "day": "Friday",
                "part_of_day": "Evening",
                "total": 1
            },
            {
                "day": "Tuesday",
                "part_of_day": "Evening",
                "total": 1
            },
            {
                "day": "Tuesday",
                "part_of_day": "Morning",
                "total": 1
            },
            {
                "day": "Wednesday",
                "part_of_day": "Evening",
                "total": 1
            },
            {
                "day": "Thursday",
                "part_of_day": "Night",
                "total": 1
            },
            {
                "day": "Monday",
                "part_of_day": "Evening",
                "total": 1
            },
            {
                "day": "Monday",
                "part_of_day": "Night",
                "total": 1
            },
            {
                "day": "Saturday",
                "part_of_day": "Evening",
                "total": 1
            }
        ],
        "city": "Las Vegas",
        "country": "United States of America",
        "id": 60,
        "id_place": 71,
        "latitude": 36.1443829,
        "longitude": -115.201615,
        "name": "True Eyecare",
        "review_count": 26,
        "similar_by_fm": [
            {
                "id_business_similar": 86153,
                "rank": 1
            },
            {
                "id_business_similar": 75164,
                "rank": 2
            },
            {
                "id_business_similar": 57040,
                "rank": 3
            },
            {
                "id_business_similar": 68284,
                "rank": 4
            },
            {
                "id_business_similar": 87885,
                "rank": 5
            }
        ],
        "stars": 5.0,
        "state": "NV",
        "total_checkins": 12,
        "total_tips": 1
    }


# negocio con información de todos los tips y reviews

obtiene los detalles de un negocio incluyendo tips y reviews

    Url: http://localhost:5001/business?id=60&full=true

      {
          "attributes": {
              "AcceptsInsurance": true,
              "BikeParking": true,
              "BusinessAcceptsCreditCards": true,
              "BusinessParking": {
                  "garage": false,
                  "lot": false,
                  "street": false,
                  "valet": false,
                  "validated": false
              },
              "ByAppointmentOnly": false,
              "RestaurantsPriceRange2": 1,
              "WheelchairAccessible": true
          },
          "business_id": "ozn76OaW1HA5mJDgQLg1cA",
          "categories": [
              {
                  "category": "Shopping",
                  "id": 468687,
                  "id_category": 1220
              },
              {
                  "category": "Optometrists",
                  "id": 468688,
                  "id_category": 946
              },
              {
                  "category": "Health & Medical",
                  "id": 468689,
                  "id_category": 621
              },
              {
                  "category": "Eyewear & Opticians",
                  "id": 468690,
                  "id_category": 945
              }
          ],
          "category_count": 4,
          "checkins_by_partofday": [
              {
                  "day": "Saturday",
                  "part_of_day": "Night",
                  "total": 8
              },
              {
                  "day": "Friday",
                  "part_of_day": "Night",
                  "total": 7
              },
              {
                  "day": "Wednesday",
                  "part_of_day": "Night",
                  "total": 4
              },
              {
                  "day": "Tuesday",
                  "part_of_day": "Night",
                  "total": 2
              },
              {
                  "day": "Friday",
                  "part_of_day": "Evening",
                  "total": 1
              },
              {
                  "day": "Tuesday",
                  "part_of_day": "Evening",
                  "total": 1
              },
              {
                  "day": "Tuesday",
                  "part_of_day": "Morning",
                  "total": 1
              },
              {
                  "day": "Wednesday",
                  "part_of_day": "Evening",
                  "total": 1
              },
              {
                  "day": "Thursday",
                  "part_of_day": "Night",
                  "total": 1
              },
              {
                  "day": "Monday",
                  "part_of_day": "Evening",
                  "total": 1
              },
              {
                  "day": "Monday",
                  "part_of_day": "Night",
                  "total": 1
              },
              {
                  "day": "Saturday",
                  "part_of_day": "Evening",
                  "total": 1
              }
          ],
          "city": "Las Vegas",
          "country": "United States of America",
          "id": 60,
          "id_place": 71,
          "latitude": 36.1443829,
          "longitude": -115.201615,
          "name": "True Eyecare",
          "review_count": 26,
          "reviews": [
              {
                  "date": "2017-07-18",
                  "id_user": 139847,
                  "stars": 5.0,
                  "text": "Very professional and caring staff! They are very knowledgeable and offer the most reasonable prices. DJ was super friendly and patient with all of our questions. At the end of our visit, we were very pleased with our selection. We will definitely return for all of our vision care needs!",
                  "useful": 0
              },
              {
                  "date": "2017-07-18",
                  "id_user": 152348,
                  "stars": 5.0,
                  "text": "True Eyecare makes service their top priority. The doctor is not only very kind and patient, but also incredibly knowledgeable. They take their time explaining anything that you might ask. I will definitely be returning.",
                  "useful": 0
              },
              {
                  "date": "2017-07-11",
                  "id_user": 142950,
                  "stars": 5.0,
                  "text": "Ms. Virginia and Dr. Keller were really nice and thorough. Ms. Virginia's very patient with explaining everything we needed clarified regarding our insurance benefits. Much cheaper than other places I've been to. My husband's new eyeglasses were just a little over $200, and he has astigmatism and opted for the transition lenses. Highly recommended and we will definitely come back here again!",
                  "useful": 0
              },
              {
                  "date": "2017-06-17",
                  "id_user": 154850,
                  "stars": 5.0,
                  "text": "My wife needed new glasses. Been over 5 years. Last time went to a chain and spent over $600 for exam and 2 pairs. Today will be half that amount. Can't thank Virginia enough for the wonderful help and service. Learned about her and the reason behind opening their family owned shop. Not only will they have us as customers, we are going to get our family to come here too.",
                  "useful": 0
              },
              {
                  "date": "2017-06-12",
                  "id_user": 131228,
                  "stars": 5.0,
                  "text": "5 * from every aspect.\nReasonably priced, a genuinely caring Dr and Ms Virginia, the lovely lady that assisted me while waiting for the Dr.",
                  "useful": 0
              },
              {
                  "date": "2017-05-17",
                  "id_user": 33710,
                  "stars": 5.0,
                  "text": "I needed new prescription for my glasses; & Virginia made this visit a pleasant one..\nShe made sure I picked the right glasses for my personality & budget, & was very patient, helpful & courteous.\n\nDr. Keller was very knowledgable & thorough with my tests.  He took the time to make sure I'm comfortable with my new prescription & double checked everything.  \nThey all make you feel like family.\nThank you for such excellent service!",
                  "useful": 0
              },
              {
                  "date": "2017-02-24",
                  "id_user": 152788,
                  "stars": 5.0,
                  "text": "Best place ever nobody can beat their prices. Exelent customer service thank you Virginia Zamora",
                  "useful": 0
              },
              {
                  "date": "2017-01-31",
                  "id_user": 92399,
                  "stars": 5.0,
                  "text": "The doctor was very efficient & was more than willing to share details of how the eyesight works in general.  Virginia was awesome who set up the appointment.  Very personable and patient !",
                  "useful": 0
              },
              {
                  "date": "2017-01-28",
                  "id_user": 138606,
                  "stars": 5.0,
                  "text": "Very great service from Virginia! I love the variety of glasses and the deals available. Found a perfect pair of glasses and was excited that it will only take a few days before I get them! Virginia made the experience wonderful. Definitely love this place due to their selection, deals, and service towards everyone that walks in. Would recommend for anyone considering.",
                  "useful": 0
              },
              {
                  "date": "2017-01-23",
                  "id_user": 131629,
                  "stars": 4.0,
                  "text": "This place was such a good experience compared to my last visit at another eye doctors! \nVirginia was able to get me fit in for a same day apt. Needed my glasses for school and found the cutest pair. Although I didn't take the offer, they had a BOGO sale.\nThey do same day glasses pick up, but since my apt was later in the day I wasn't able to take them home with me. But other than that I would recommend this place to anyone .",
                  "useful": 0
              },
              {
                  "date": "2016-12-26",
                  "id_user": 129330,
                  "stars": 5.0,
                  "text": "Walked in and was greeted kindly by DJ. Love the staff! Very friendly and helpful. They got me all set up with my classes and contacts. I am very satisfied :)",
                  "useful": 0
              },
              {
                  "date": "2016-12-18",
                  "id_user": 155611,
                  "stars": 5.0,
                  "text": "Took my son to them after being the only ones that would see him short notice. Best dicision ever. The staff was amazing. Family owned and really cared. Won't go anywhere else. Thank you for your service. See you soon.",
                  "useful": 0
              },
              {
                  "date": "2016-11-27",
                  "id_user": 154406,
                  "stars": 5.0,
                  "text": "This is by far the best eye doctor that I've been to in a very long time. The doctor was so thorough yet he didn't take forever. Neither did Virginia. Who was also a gem. :)  They stand out from other establishments who just go through the motions in life. Keep up the great work True Eyecare. I'll definitely be recommending you to friends, family & coworkers.",
                  "useful": 0
              },
              {
                  "date": "2016-11-26",
                  "id_user": 142200,
                  "stars": 5.0,
                  "text": "Best experience I've had at an optometrist. They have really good prices for eye exams. I got a prescription written for glasses and two brands of contacts. The woman in the front was very friendly and very helpful and I was in and out within an hour. Very good prices and service.",
                  "useful": 0
              },
              {
                  "date": "2016-08-12",
                  "id_user": 7530,
                  "stars": 5.0,
                  "text": "This optical shop is a hidden gem! The manager is so friendly and helpful. I support small family businesses and this is one of them. Despite being a small business their prices are way better than the corporate chains I called! I have no insurance which also made this place a good find. They have a decent selection of eyewear at great prices! I ordered contact lenses at online prices. Their optician is very good. He is focused so don't expect too much conversation unless you're asking tons of questions. My girlfriend also decided to get her prescription renewed here and ordered a new pair of glasses. We will definitely return.",
                  "useful": 0
              },
              {
                  "date": "2016-07-27",
                  "id_user": 139591,
                  "stars": 5.0,
                  "text": "These people are the best!!!! Bought a pair of glasses for $500 at Pearl Vision and the glasses went bad before a year was up. They wanted $525.00 for new lenses with no warranty. Virginia at True Care sold me new lenses for $264 with a warranty and it only took 7 days to process. PLEASE, before you go to Pearl or anywhere else, check with the people at True Care.",
                  "useful": 1
              },
              {
                  "date": "2016-07-05",
                  "id_user": 120810,
                  "stars": 5.0,
                  "text": "Awesome service! I'm only in town until tomorrow but am able to pickup my glasses within less than 24 hours. Doctor was informative and friendly. Staff was efficient and friendly too. Prices were good too.",
                  "useful": 0
              },
              {
                  "date": "2016-06-17",
                  "id_user": 128470,
                  "stars": 5.0,
                  "text": "WOW!!! I just left and have to leave this now! What a beautiful company, what sweet amazing honest hardworking people! I've worn glasses since 2nd grade, I'm 25. I have had eye exams almost every year since & I have never had a doctor re-examine the clarity of the contacts after the initial exam. Usually they just do one exam, give me some trial contacts & call it a day. Dr. Keller gave me two trial pairs & reexamined my eyes maybe 3 or 4 times? We are in Vegas for EDC weekend, we have some time & money for me to actually get an eye exam knocked out, and traveled here from 12 hours away. I called maybe 10 places that were in route to Vegas, only one other place was as sweet as Virginia here at True Eyecare. So literally the best place I found from Vegas to North California. They're prices are also the best & their glasses selection was awesome!! So if it's important to you to support good people need look no further, get your bum over there! Beautiful people over at True Eyecare, thank you for everything!",
                  "useful": 1
              },
              {
                  "date": "2016-06-10",
                  "id_user": 155608,
                  "stars": 5.0,
                  "text": "I read the reviews on Yelp and decided to give this place a go. I was not disappointed. The service is amazing and prices are also very good.They have a nice selection of glasses. Virginia helped me and she was so nice , she did not pressure me one bit but helped me decide costs between glasses and frames. It's a private family owned business.Highly recommend this place for all your contacts, eyeglasses and eye exams!",
                  "useful": 2
              },
              {
                  "date": "2016-03-17",
                  "id_user": 123712,
                  "stars": 5.0,
                  "text": "Fast and great service! I received my prescription eyeglasses in two days. Virginia helped all three of us from understanding costs to taking measurements. Overall very glad with the service and quality of glasses.",
                  "useful": 3
              },
              {
                  "date": "2016-01-20",
                  "id_user": 152805,
                  "stars": 5.0,
                  "text": "Virginia and DJ was so nice and  professional.  I took my parents here for their spcial lens recommended by costco eye care team.  They were able to make for good price and super fast! Once they have special order lens,  they will cut there on site, so we dont have to wait forever like other places.  I been to lots other places but no one can beat their service and prices in town.  This place is the best!",
                  "useful": 1
              },
              {
                  "date": "2016-01-06",
                  "id_user": 131584,
                  "stars": 5.0,
                  "text": "I initially walked in because of the high yelp reviews, and I was not disappointed. Virginia was the technician that helped me out. She was very friendly and even called my insurance company in order to confirm that a reimbursement would be possible. She explained the BOGO deal and was happy to answer the questions I had about the EYEmed form and polycarbonate lens. I got 2 frames for ~$80, excluding the $200 allowance from my insurance company, so the total came out to ~$40 for each glasses. They have both generic and high ended brands like Coach. I was leaving for college the next day, so I was pleasantly surprised to know that they made the glasses on the spot for me! They didn't have enough material for the 2nd frame so it will be shipped to me when it's ready for a $10 fee. I was out of the door in less than 2 hours with a pair of new glasses. Overall I am extremely pleased with both customer service and glasses. I will be coming back again and I definitely recommend this place to anyone looking to purchase new glasses!",
                  "useful": 3
              },
              {
                  "date": "2015-11-27",
                  "id_user": 154907,
                  "stars": 5.0,
                  "text": "Great place. Ask for Dr. Keller for your eye exam. He is very helpful and accommodating.",
                  "useful": 1
              },
              {
                  "date": "2015-10-08",
                  "id_user": 131392,
                  "stars": 5.0,
                  "text": "We purchased the Anti-Reflective coating glasses with Coach frame for our teen daughter couple of weeks ago, and she totally LOVES it!! Her vision highly improved and received so many cool compliments that I just have to rave about you guys! Thank you True Eyecare for recommending this! I would've never thought of it. Your expertise and knowledge..your friendliness and concern about my daughter's vision's needs went beyond our expectations. You just made us as your loyal customers and definitely will be coming back to your store.",
                  "useful": 2
              },
              {
                  "date": "2015-08-26",
                  "id_user": 133467,
                  "stars": 5.0,
                  "text": "I've been going to True Eyecare since they opened their doors and would never go anywhere else again. The staff is knowledgeable, friendly, and value my time and business. They have a large selection of frames and products.",
                  "useful": 0
              },
              {
                  "date": "2015-05-04",
                  "id_user": 144490,
                  "stars": 5.0,
                  "text": "Been wearing glasses for many years and never have had customer service like this before. The minute you walk through the door, it feels warm and welcoming with no pressure to take out a second mortgage on your new glasses. There is a large frame selection to choose from and the staff was always so very helpful in making recommendations. After all said and done and for the amount of money I spent, I was ready to wait two weeks for them to be ready. Then to top it off, the guy tells me my glasses will be ready in a hour!!!! Boom!!  I'm never going anywhere else again! Love this place and will definitely be back soon.",
                  "useful": 2
              }
          ],
          "similar_by_fm": [
              {
                  "id_business_similar": 86153,
                  "rank": 1
              },
              {
                  "id_business_similar": 75164,
                  "rank": 2
              },
              {
                  "id_business_similar": 57040,
                  "rank": 3
              },
              {
                  "id_business_similar": 68284,
                  "rank": 4
              },
              {
                  "id_business_similar": 87885,
                  "rank": 5
              }
          ],
          "stars": 5.0,
          "state": "NV",
          "tips": [
              {
                  "date": "2016-03-11",
                  "id_user": 123712,
                  "likes": 0.0,
                  "text": "Fast and great service! I received my prescription eyeglasses in two days. Virginia helped all three of us from understanding costs to taking measurements. Overall very glad with the service and quality of glasses."
              }
          ],
          "total_checkins": 12,
          "total_tips": 1
      }




# usuario

obtiene los detalles del usuario  incluyendo recomendaciones contextualizadas

todos los parámetros son obligatorios

    Url: http://localhost:5001/user?id=60&lat=36.41948&lon=-115.2424431

descripción recs:

    recs_by_fm :  recomendaciones por factorization machines
    recs_by_fm_with_context : recomendaciones por factorization machines con contexto distancia
                (decay:  recíproco de la distancia   1/distancia )

    recs_by_svd: recomendaciones según svd            
    recs_by_svd_with_context: recomendaciones por svd ajustadas por distancia (igual que las fm)

respuesta :

    {
        "fans": 42,
        "id": 60,
        "name": "Vladmir                         ",
        "recs_by_fm": [
                {
                "id_business": 45676,
                "latitude": 36.0541948,
                "longitude": -115.2424431,
                "name": "Brew Tea Bar",
                "rank": 1,
                "score": 4.53849
            },
            ...
        ],
        "recs_by_fm_with_context": [
                        {
                 "id_business": 136074,
                 "latitude": 36.2382459749,
                 "longitude": -115.150921382,
                 "name": "Poke Express",
                 "rank": 15,
                 "rank_adjusted": 1,
                 "rank_change": 14,
                 "score": 4.27292,
                 "score_adjusted": 0.69
                }, ....

        ],
        "recs_by_svd": [
                    {
                   "id_business": 92240,
                   "latitude": 33.3373851,
                   "longitude": -111.8690947,
                   "name": "Cool Expressions Window Tinting",
                   "rank": 1,
                   "score": 5.0
               }, .....
        ],
        "recs_by_svd_with_context": [
                    {
                   "id_business": 63399,
                   "latitude": 33.407997,
                   "longitude": -111.887898,
                   "name": "Princess Mediterranean Market & Deli",
                   "rank": 20,
                   "rank_adjusted": 1,
                   "rank_change": 19,
                   "score": 4.7642,
                   "score_adjusted": 0.044
               }, ...
        ],
        "user_id": "3R_dB9VQ_D3WPJEw7pmorA",
        "years_elite": 2,
        "yelping_since": "2014-09-01"
    }    


# lugares populares

retorna los 10  lugares mas populares según cantidad de checkins, cantidad de reviews, y promedio de estrellas

Url: http://localhost:5001/popular?lat=36.4248&lon=-115.2424431


ejemplo respuesta

    {
        "popular_by_checkins": [
          {
                "id_business": 40021,
                "latitude": 36.346359,
                "longitude": -115.216182,
                "name": "Clark County Shooting Complex",
                "rank": 1,
                "score": 23
            }, .....


        ],
        "popular_by_review_count": [
                  {
                  "id_business": 40021,
                  "latitude": 36.346359,
                  "longitude": -115.216182,
                  "name": "Clark County Shooting Complex",
                  "rank": 1,
                  "score": 158
              }, .......
        ],
        "popular_by_stars": [
                  {
                 "id_business": 128341,
                 "latitude": 36.3656399,
                 "longitude": -115.2244853,
                 "name": "Real Estate Assistant Solutions",
                 "rank": 1,
                 "score": 5.0
             }, .....
        ]
    }
    .


# recomendación por búsqueda en elasticsearch

retorna los 10  lugares mas relevantes según la palabra clave y ubicación

Url: http://localhost:5001/search?lat=36.1318&lon=-115.1977&q=kolumbian

Notas:
- highlight : texto resaltado donde encontró el término (esto demora más la consulta, pero queda más claro donde lo encontró)
- distance : distancia del punto del parámetro a la ubicación del lugar  

Resultado :

    [
      {
        "city": "Las Vegas",
        "name": "Oiga, Mire, Vea Colombian Cuisine",
        "id_business": 60818,
        "longitude": -115.207747,
        "state": "NV",
        "score": 102.98384,
        "rank": 1,
        "stars": 3.5,
        "latitude": 36.142935,
        "highlight": {
          "tips.text": [
            "Great Columbian food and coffee",
            "Too bad they don't have Colombian beer",
            "Columbian style....;)",
            "Salsa dancing and Colombian food till 5 am"
          ],
          "categories.category": [
            "Colombian"
          ],
          "reviews.text": [
            "Probably not a Colombian cook\nService was just ok they forget about costumers. Food was tasteless",
            "I haven't had delicious Colombian food since I lived in Queens, NY. Man did this place satisfy my",
            ". The service was wonderful as well. This will definitely be my go to place in the future when I'm craving great Colombian food.",
            "So good! My husband and I were slumming it at the Savers, when he noticed a sign for Colombian food",
            ". I love all the food from South America, but I've only had Columbia food via Florida. Florida's"
          ],
          "name": [
            "Oiga, Mire, Vea Colombian Cuisine"
          ]
        },
        "id": 60818,
        "distance": 1.5320177986568
      },...
