# usr	-> es el ID del usuario 
# items	-> es un vector de IDS de items para ordenar segun la prediccion del modelo SVN
# algo	-> modelo SVN previamenten entrenado 
# ejemplo	-> recomendacionesSVN(101, [10,13,48,55,591],algo)

def recomendacionesSVN(usr, items, algo)
	from collections import defaultdict
	lista = defaultdict(list)
	for i in items:
		prdc=algo.predict(usuario,i)
		lista[usuario].append((i, prdc.est))
	for uid, user_ratings in lista.items():
		user_ratings.sort(key=lambda x: x[1], reverse=True)
		lista[uid] = user_ratings
	return lista
