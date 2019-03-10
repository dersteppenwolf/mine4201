# coding: utf-8

import numpy as np
import pandas as pd
import time, logging, os

from calendar import monthrange
from datetime import datetime, timedelta
from collections import deque
from flurs.data.entity import User, Item, Event
from flurs.recommender.fm import FMRecommender
from flurs.recommender.mf import MFRecommender

class StreammingRecomender:

    def __init__(self):
        logging.debug("######################################################################")
        logging.debug("######################################################################")
        logging.debug("StreammingRecomender  - init")
        logging.debug("######################################################################")
        logging.debug("######################################################################")
        #esto iria en el init
        dataset = "taller3"
        k =  5
        #self.data = Converter().convert(dataset=dataset)
        #self.data = MovieLensTaller3Converter()
        #self.data.convert()

        all_movies=pd.read_csv('data/movies.csv',sep=',', names=['movie_id','title','genres'], header=0)

        all_genres = ['Action',
                      'Adventure',
                      'Animation',
                      "Children",
                      'Comedy',
                      'Crime',
                      'Documentary',
                      'Drama',
                      'Fantasy',
                      'Film-Noir',
                      'Horror',
                      'IMAX',
                      'Musical',
                      'Mystery',
                      'Romance',
                      'Sci-Fi',
                      'Thriller',
                      'War',
                      'Western',
                      '(no genres listed)']
        n_genre = len(all_genres)

        self.number_of_events = 0
        self.movies = {}

        for index, row in all_movies.iterrows():
        	movie_vec = np.zeros(n_genre)
        	for genre in row['genres'].split('|'):
        		i = all_genres.index(genre)
        		movie_vec[i] = 1.
        	item_id = int(row['movie_id'])
        	self.movies[item_id] = movie_vec

        self.itemsFlurs=[]
        self.usersFlurs=[]

        self.item_ids=[]
        self.user_ids=[]

        self.candidates= deque(maxlen=8000)

        self.recommender1 = FMRecommender(p=28, k=5)
        self.recommender2 = MFRecommender(k=40)
        self.recommender1.initialize()
        self.recommender2.initialize()
        logging.debug("######################################################################")
        logging.debug("######################################################################")


    def recommend(self,userID='126622', numRecom=20):
        logging.debug("************************************************************")
        logging.debug("StreammingRecomender  recommend")
        logging.debug("************************************************************")
        logging.debug(userID)
        logging.debug(numRecom)
        usersito=self.usersFlurs[self.user_ids.index(userID)]
        context = np.zeros(7)

        itemsCandidatos=np.array(self.candidates)

        recsFM = self.recommender1.recommend(usersito, itemsCandidatos, context )
        recsMF = self.recommender2.recommend(usersito, itemsCandidatos )

        pelisFM, scoreFM =recsFM
        pelisMF, scoreMF =recsMF

        recoListFM=list(reversed(pelisFM[-numRecom:]))
        scoreListFM=list(reversed(scoreFM[-numRecom:]))

        recoListMF=list(reversed(pelisMF[-numRecom:]))
        scoreListMF=list(reversed(scoreMF[-numRecom:]))
        d1=[]
        d2=[]
        d3=[]

        for i in range(numRecom):
        	d1.append([i+1,recoListFM[i],scoreListFM[i]])
        	d2.append([i+1,recoListMF[i],scoreListMF[i]])

        	d3.append([recoListFM[i],scoreListFM[i]])
        	d3.append([recoListMF[i],scoreListMF[i]])

        itemsUnicos=[]
        d3final=[]
        d3.sort(key=lambda item: item[1], reverse=True)

        i=0
        ranking=1
        while len(d3final)<numRecom:
        	if d3[i][0] not in itemsUnicos:
        		d3final.append([ranking,d3[i][0],d3[i][1]])
        		ranking=ranking+1
        		itemsUnicos.append(d3[i][0])
        	i=i+1
        recomm = {'svd': d1, 'fm': d2, 'mixed': d3final}
        logging.debug("************************************************************")
        logging.debug("StreammingRecomender  recommend   END")
        logging.debug("************************************************************")
        return recomm

    '''
    def updateEvent(self, userID, itemID, timestamp):
        logging.debug("****************************")
        logging.debug("StreammingRecomender  updateEvent")
        logging.debug("****************************")

        self.updateUserItem(userID,itemID)
        # Calcular el contexto del evento
        date = datetime(*time.localtime(timestamp)[:6])
        weekday_vec = np.zeros(7)
        weekday_vec[date.weekday()] = 1
        others = weekday_vec

        itemsito=self.itemsFlurs[self.item_ids.index(itemID)].index
        if itemsito not in self.candidates:
        	self.candidates.append(itemsito)

        evt = Event(self.usersFlurs[self.user_ids.index(userID)], self.itemsFlurs[self.item_ids.index(itemID)], 1., others)

        #Factorization Machine
        if self.recommender1.is_new_user(self.usersFlurs[self.user_ids.index(userID)].index):
        	self.recommender1.register_user(self.usersFlurs[self.user_ids.index(userID)])

        if self.recommender1.is_new_item(self.itemsFlurs[self.item_ids.index(itemID)].index):
        	self.recommender1.register_item(self.itemsFlurs[self.item_ids.index(itemID)])

        self.recommender1.update(evt)

        #Matrix Factorization
        if self.recommender2.is_new_user(self.usersFlurs[self.user_ids.index(userID)].index):
        	self.recommender2.register_user(self.usersFlurs[self.user_ids.index(userID)])

        if self.recommender2.is_new_item(self.itemsFlurs[self.item_ids.index(itemID)].index):
        	self.recommender2.register_item(self.itemsFlurs[self.item_ids.index(itemID)])

        self.recommender2.update(evt)
        logging.debug("************************************************************")
        logging.debug("************************************************************")
    '''

    def updateStreamByArray(self, arreglo):
        logging.debug("************************************************************")
        logging.debug("StreammingRecomender  updateStreamByArray")
        logging.debug("************************************************************")

        for userID, itemID, timestamp in arreglo:
            self.updateUserItem(userID,itemID)
            # Calcular el contexto del evento
            date = datetime(*time.localtime(timestamp)[:6])
            weekday_vec = np.zeros(7)
            weekday_vec[date.weekday()] = 1
            others = weekday_vec

            itemsito=self.itemsFlurs[self.item_ids.index(itemID)].index
            if itemsito not in self.candidates:
                self.candidates.append(itemsito)

            evt = Event(self.usersFlurs[self.user_ids.index(userID)], self.itemsFlurs[self.item_ids.index(itemID)], 1., others)

            #Factorization Machine
            if self.recommender1.is_new_user(self.usersFlurs[self.user_ids.index(userID)].index):
                self.recommender1.register_user(self.usersFlurs[self.user_ids.index(userID)])

            if self.recommender1.is_new_item(self.itemsFlurs[self.item_ids.index(itemID)].index):
                self.recommender1.register_item(self.itemsFlurs[self.item_ids.index(itemID)])

            self.recommender1.update(evt)

            #Matrix Factorization
            if self.recommender2.is_new_user(self.usersFlurs[self.user_ids.index(userID)].index):
                self.recommender2.register_user(self.usersFlurs[self.user_ids.index(userID)])

            if self.recommender2.is_new_item(self.itemsFlurs[self.item_ids.index(itemID)].index):
                self.recommender2.register_item(self.itemsFlurs[self.item_ids.index(itemID)])

            self.recommender2.update(evt)


            self.number_of_events += 1
        logging.debug("************************************************************")
        logging.debug("StreammingRecomender END  updateStreamByArray")
        logging.debug("************************************************************")

    def updateUserItem(self, userID, itemID):
        #logging.debug("************************************************************")
        #logging.debug("StreammingRecomender  updateUserItem : "+str(userID))
        #logging.debug("************************************************************")

        if itemID not in self.item_ids:
            self.item_ids.append(itemID)
            i_index = self.item_ids.index(itemID)
            item = Item(i_index, self.movies[itemID])
            self.itemsFlurs.append(item)

        if userID not in self.user_ids:
            self.user_ids.append(userID)
            u_index = self.user_ids.index(userID)
            user = User(u_index)
            self.usersFlurs.append(user)
        #logging.debug("************************************************************")
        #logging.debug("************************************************************")
