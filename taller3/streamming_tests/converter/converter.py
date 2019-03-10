# coding: utf-8

from .MovieLens1M import MovieLens1MConverter
from .MovieLens100k import MovieLens100kConverter
from .MovieLensTaller3 import MovieLensTaller3Converter


class Converter:

    def __init__(self):
        pass

    def convert(self, dataset='taller3'):
        """Convert a specified dataset to be used by experiments.

        Args:
            dataset (str): Dataset name.

        Returns:
            instance of a converter: Its instance variables are used by experiments.

        """
        if dataset == 'ML1M':
            c = MovieLens1MConverter()
        elif dataset == 'ML100k':
            c = MovieLens100kConverter()
        elif dataset == 'taller3':
            c = MovieLensTaller3Converter()

        c.convert()
        return c
