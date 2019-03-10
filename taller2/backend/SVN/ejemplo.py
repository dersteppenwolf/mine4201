import matplotlib.pyplot as plt
import pandas as pd
from surprise import SVD
from surprise import Reader
from surprise import Dataset
from surprise import evaluate, print_perf
from surprise import accuracy
import seaborn as sns
import numpy as np

#total_data.tsv es la suma de los 3 dataset para el afinamiento (train, validacion y test)
total_data=pd.read_csv('total_data.tsv',sep='\t', names=['user_id','item_id','rating','timestamp'])

reader = Reader(rating_scale=(1, 5))
total_data = Dataset.load_from_df(total_data[['user_id', 'item_id', 'rating']], reader)
total_data=total_data.build_full_trainset()
total_data_trainset=total_data.build_testset()

# creacion del recomendado SVD
algo=SVD(n_factors=1, n_epochs=50, biased=True, lr_all=0.004, reg_all=0.1, init_mean=0, init_std_dev=0.01,verbose=False)

#entrenamiento del modelo
predictions=algo.train(total_data)

####################################
####################################

# implementacion de la recomendacion
usuario = 101
items = [61385, 79075, 10468, 40549, 107443, 97705, 66278, 88777, 89084]

from recomendacionesSVN import recomendacionesSVN 
recomendaciones = recomendacionesSVN(usuario, items, algo)

####################################
####################################
recomendaciones
>>>>>>>>>>>>>>>
defaultdict(list,
            {101: [(61385, 5),
              (79075, 5),
              (10468, 5),
              (40549, 5),
              (107443, 5),
              (97705, 5),
              (88777, 3.9530037077771221),
              (89084, 3.8361844465017332),
              (66278, 3.8078247164707455)]})
