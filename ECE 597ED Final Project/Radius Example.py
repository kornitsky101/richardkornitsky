#ECE587ED: Final Project, By: Richard Kornitky, Cassius Peter, Ryan McAuliffe
"""
This code is for the is used to print out an example of how radius is drawn
and distances are measured from center point. It utilizes sections from
the main model to display the networking feature

It uses data from the previously run result stored in dataOutOriginal.csv

A point with 10 structures within radius was chosen since it demonstrated
how transmission lines are drawn in model well

"""

#############################################
#### Section Written by Richard Kornitsky ###
#############################################

import pandas as pd
from shapely.geometry import Point, Polygon
import geopandas
import matplotlib.pyplot as plt
import random

from random import randint
from math import radians, sin, cos, sqrt, asin
from mpl_toolkits.basemap import Basemap

def haversine(lat1, lon1, lat2, lon2):
 
  R = 6372.8 # Earth radius in kilometers
 
  dLat = radians(lat2 - lat1)
  dLon = radians(lon2 - lon1)
  lat1 = radians(lat1)
  lat2 = radians(lat2)
 
  a = sin(dLat/2)**2 + cos(lat1)*cos(lat2)*sin(dLon/2)**2
  c = 2*asin(sqrt(a))
 
  return R * c


def floatRange(value, minVal, maxVal):
    if (value < minVal) or (value > maxVal):
        return True
    else:
        return False


#################MAIN CODE#################
df = pd.read_csv('dataOutOriginal.csv')

tempIndex = 0
tempRadius_df = pd.DataFrame({
                'lon' : pd.Series(),
                'lat' : pd.Series(),
                'dist' : pd.Series()
                })
tempCenterLon = df.loc[73,'centLon']
tempCenterLat = df.loc[73,'centLat']
minLon = tempCenterLon - .001
maxLon = tempCenterLon + .001
minLat = tempCenterLat - .001
maxLat = tempCenterLat + .001
for y in range(0,len(df)):
                tempLon = df.loc[y,'centLon']
                tempLat = df.loc[y,'centLat']
                if floatRange(tempLon, minLon, maxLon):
                    pass
                elif floatRange(tempLat, minLat, maxLat):
                    pass
                elif tempLon==tempCenterLon and tempLat==tempCenterLat:
                    pass
                else:
                    tempRadius_df.loc[tempIndex] = [tempLon, tempLat, haversine(tempLat, tempLon, tempCenterLat, tempCenterLon)]
                    tempIndex = tempIndex + 1
tempRadius_df

map = Basemap(llcrnrlon=34.0672,llcrnrlat=-0.7279,urcrnrlon=34.0688,urcrnrlat=-0.7270, resolution = 'h',lat_0=tempCenterLat, lon_0=tempCenterLon, epsg=4210)

map.drawmapboundary(fill_color='white')
#Fill the continents with the land color
map.fillcontinents(color='white',lake_color='#0a42a1')
map.drawcoastlines()

map.readshapefile('County Shape/County', 'comarques')

lonCent = tempRadius_df.loc[1, 'lon']
latCent = tempRadius_df.loc[1, 'lat']

for o in range(0,len(tempRadius_df)):
    lon = tempRadius_df.loc[o, 'lon']
    lat = tempRadius_df.loc[o, 'lat']
    x,y = map(lon,lat)
    map.plot(x,y, marker='o',color='r')
    
    lon = [lon,lonCent]
    lat = [lat,latCent]
    x,y = map(lon,lat)
    map.plot(x,y, marker='.',color='black',alpha = 0.5)
    
x,y = map(lonCent,latCent)
map.plot(x,y, marker='o',color='b')
tempRadius_df.drop(1, inplace = True)
tempRadius_df = tempRadius_df.reset_index()
    
plt.show()