#!/bin/bash
export PORT=9999
export DB_PORT=6432
export DB_USER=mine
export DB_PWD=mine

nohup python prod.py &
