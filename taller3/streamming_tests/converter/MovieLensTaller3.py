# coding: utf-8

from flurs.data.entity import User, Item, Event

import numpy as np
import pandas as pd
import time
import os
from calendar import monthrange
from datetime import datetime, timedelta


class MovieLensTaller3Converter:

    def __init__(self):
        here = os.path.dirname(__file__)
        self.path = {
            'items': os.path.join(here, 'data/movies.csv')
        }

        # contexts in this dataset
        # 20 genres
        # 1 rating, 7 for day of week
        self.contexts = {'others': 1 + 7, 'item': 20, 'user': 0}

        self.can_repeat = False

    def convert(self):
    	"""Create a list of samples and count number of users/items.
    	"""
    	
    	user_ids = self.__load_users()
    	
    	movies, movie_titles = self.__load_movies()
    	
    	item_ids = []
    	
    	self.samples = []
    	self.items = []
    	self.users = []
    
    	for mov in movies:
    		if mov not in item_ids:
    			item_ids.append(mov)
    			i_index = item_ids.index(mov)
    			item = Item(i_index, movies[mov])
    			self.items.append(item)
    			
    	for u_id in user_ids:
    		u_index = user_ids.index(u_id)
    		user = User(u_index)
    		self.users.append(user)
    	

    def __load_movies(self):
    	"""Load movie genres as a context.
    	Returns:
        	dict of movie vectors: item_id -> numpy array (n_genre,)
    	"""

    	all_movies=pd.read_csv(self.path['items'],sep=',', names=['movie_id','title','genres'], header=0)

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
    
    	movies = {}
    	movie_titles = {}
    	for index, row in all_movies.iterrows():
    		movie_vec = np.zeros(n_genre)
    		for genre in row['genres'].split('|'):
    			i = all_genres.index(genre)
    			movie_vec[i] = 1.
    		item_id = int(row['movie_id'])
    		movies[item_id] = movie_vec
    		movie_titles[item_id] = row['title']

    	return movies, movie_titles

		# total users = 270896
    def __load_users(self):
    	"""Load user demographics as contexts.User ID -> {sex (M/F), age (7 groupd), occupation(0-20; 21)}
    	
    	Returns:
    		dict of user vectors: user_id -> numpy array (1+1+21,); (sex_flg + age_group + n_occupation, )
    	
    	"""

    	users = [i for i in range(270900)]

    	return users

