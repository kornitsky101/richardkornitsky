#ECE587ED: Final Project, By: Richard Kornitky, Cassius Peter, Ryan McAuliffe

#############################################
##### Section Written by Cassius Peter ######
#############################################


import pandas as pd
#from shapely.geometry import Point, Polygon
#import geopandas
import matplotlib.pyplot as plt
import random
import numpy as np
import math

consumption = np.zeros((144,29))
production = np.zeros((144,29))
cnt = np.zeros((144,29))
pnt = np.zeros((144,29))
count = 0
#profiles[70,20] = 7
locations = pd.read_pickle('597Dconsumption.pkl')
#the data is being wierd. apperantly, [x,0] is current in, [x,1] is current out, and [x,4] is voltage
#print(locations.iloc[1,4])
##########################
#Generate energy profiles#
##########################
days = 180
t = -1 
for x in range(4176*days):
    if(x%4176 == 0):
        print(x/4176)
    if (x%29) == 0:
        t = (t+1)%144
    if locations.iloc[x,1] > 0:
        consumption[t,x%29] = (600*locations.iloc[x,1] * locations.iloc[x,4])
        cnt[t,x%29] = cnt[t,x%29] + 1
    if (locations.iloc[x,0]) > 0:
        production[t,x%29] = (600*locations.iloc[x,0] * locations.iloc[x,4])
        pnt[t,x%29] = pnt[t,x%29] + 1
consumption = np.true_divide(consumption, cnt)
production = np.true_divide(production, pnt)
profiles = production - consumption
##########################################
#Find wasted energy of unconnected houses (no storage)#
##########################################
#wasted = np.zeros(29)
#boosh = 0
#for boosh in range(29):
#    psum = production[:,boosh]
#    csum = consumption[:,boosh]
#    temp = np.maximum(psum - csum)
#    wasted[boosh] = sum(temp)

########################################
#Find wasted energy of unconnected houses (infinite storage)
########################################
psum = np.zeros(29)
csum = np.zeros(29)
excess = np.zeros(29)
boosh = 0
for boosh in range(29):
    psum[boosh] = np.sum(production[:,boosh])
    csum[boosh] = np.sum(consumption[:,boosh])
    excess[boosh] = psum - csum

