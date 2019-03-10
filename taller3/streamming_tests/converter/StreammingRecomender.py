# coding: utf-8

from flurs.data.entity import User, Item, Event

import numpy as np
import pandas as pd
import time
import os
from calendar import monthrange
from datetime import datetime, timedelta
from converter.converter import Converter
from flurs.recommender.fm import FMRecommender
from flurs.recommender.mf import MFRecommender

class StreammingRecomender:

    def __init__(self):
        #esto iria en el init
        dataset = "taller3"
        k =  5
        self.data = Converter().convert(dataset=dataset)
        
        self.recommender1 = FMRecommender(p=28, k=2)
        self.recommender2 = MFRecommender(k=30) 
        self.recommender1.initialize()
        self.recommender2.initialize()
        
        for item in self.data.items:
        	self.recommender1.register_item(item)
        	self.recommender2.register_item(item)

    def updateStream(self,file='converter/test/test1.txt'):
        test1=pd.read_csv(file,sep=',', names=['userid','movieid','rating','timestamp','rating_date','imdb_tconst','id'], header=0)
        
        cand=[]
        
        for index, row in test1.iterrows():
        	# Calcular el contexto del evento
        	date = datetime(*time.localtime(row['timestamp'])[:6])
        	weekday_vec = np.zeros(7)
        	weekday_vec[date.weekday()] = 1
        	others = weekday_vec
        	
        	itemsito=self.data.items[int(row['movieid'])].index
        	if itemsito not in cand:
        		cand.append(itemsito)
        	
        	evt = Event(self.data.users[int(row['userid'])], self.data.items[int(row['movieid'])], 1., others)
        	
        	#Factorization Machine
        	if self.recommender1.is_new_user(self.data.users[int(row['userid'])].index):
        		self.recommender1.register_user(self.data.users[int(row['userid'])])
        
        	self.recommender1.update(evt)
            
            #Matrix Factorization
        	if self.recommender2.is_new_user(self.data.users[int(row['userid'])].index):
        		self.recommender2.register_user(self.data.users[int(row['userid'])])
        
        	self.recommender2.update(evt)
        
        self.candidates= np.array(cand)

    def recommend(self,userID='126622', numRecom=20):
        usersito=self.data.users[userID]
        context = np.zeros(7)
        
        recsFM = self.recommender1.recommend(usersito, self.candidates, context )
        recsMF = self.recommender2.recommend(usersito, self.candidates )

        pelisFM, scoreFM =recsFM
        pelisMF, scoreMF =recsMF

        recoListFM=list(reversed(pelisFM[-numRecom:]))
        recoListMF=list(reversed(pelisMF[-numRecom:]))

        return recoListFM, recoListMF
