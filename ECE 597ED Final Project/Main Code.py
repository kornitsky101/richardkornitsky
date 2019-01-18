#ECE587ED: Final Project, By: Richard Kornitky, Cassius Peter, Ryan McAuliffe
import pandas as pd
from shapely.geometry import Point, Polygon
import geopandas
import matplotlib.pyplot as plt
import random

from random import randint
from math import radians, sin, cos, sqrt, asin
from mpl_toolkits.basemap import Basemap

"""
    use haversine formula to determine distance between locations in KILOMETERS
    haversine factors in the curvature of the earth
    geopandas distance operator is NOT the same
    https://rosettacode.org/wiki/Haversine_formula#Python
"""

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



########################Main Code########################

#############################################
##### Section Written by Ryan MacAuliffe ####
#############################################


#100322 locations
locations = pd.read_pickle('597EDlocs.pck')
consumption_df = pd.read_pickle('597Dconsumption.pkl')
energyProfile_df = pd.read_csv('Energy use data.csv')

#Information from all radii
radiiInfo_df = pd.DataFrame({
        'centLat' : pd.Series(),
        'centLon' : pd.Series(),
        'numStruct' : pd.Series(),
        'dist' : pd.Series(),
        'wireCost' : pd.Series(),
        'production' : pd.Series(),
        'consumption' : pd.Series(),
        'netEffect' : pd.Series()
        })

mainIndex = -1
trials = 0
while trials < 10:
    #random.uniform creates a random float
    ycoordone = random.uniform(-.74, -.54)
    ycoordtwo = random.uniform(-.74, -.54)
    xcoordone = random.uniform(34.02, 34.42)
    xcoordtwo = random.uniform(34.02, 34.42)
    
    #create rectangle of boundaries
    #fixed error wherein x and y values were flipped
    boundary = geopandas.GeoSeries([Polygon([(34.02, -0.74), (34.02, -0.54), (34.42, -0.54), (34.42, -.74)]), Polygon([(xcoordone,ycoordone), (xcoordone,ycoordtwo), (xcoordtwo,ycoordtwo), (xcoordtwo,ycoordone)])])
    boundarydf = geopandas.GeoDataFrame({'geometry': boundary[0], 'df1':[1,1]})
    minirec = geopandas.GeoDataFrame({'geometry': boundary[1], 'df2':[1,1]})
    
    
    #create list of location coordinates
    output = dict()
    output ['lon'] = []
    output ['lat'] = []
    
    #put data into lists
    for x in range(1, 50161):
        output['lat'].append(locations.gps_y.iloc[x])
        output['lon'].append(locations.gps_x.iloc[x])
    
    coords = pd.DataFrame(output)
    coords['Coordinates'] = list(zip(coords.lon, coords.lat))
    #convert to point
    coords['Coordinates'] = coords['Coordinates'].apply(Point)
    """
    #Displays rectangle that was generated
	ax = boundarydf.plot(color='red');
	minirec.plot(ax=ax, color='green', alpha=0.5);
	"""
    
#############################################
#### Section Written by Richard Kornitsky ###
#############################################
    
    #find all the points that intersect the random rectangle
    numStruct = 0
    cntr = 0
    for x in coords['Coordinates']:
        if boundary[1].intersects(x):
            numStruct = numStruct + 1
        else:
            coords.drop(cntr,inplace=True)
        if(cntr%5000 == 0):
            print(int((cntr/500)/2),"%")
        cntr = cntr+1
    coords = coords.reset_index()
    
    print ("The number of structures in rectangle:",numStruct)
    
    
    
   
    """
       maxiumum distance between connections set to 40m, see report for info
       
       On at minimum in Kenya, it is found that:
        delta(longitude)= .001° ~ 100m
        delta(latitude) = .001° ~ 100m
        Therefore any data point < .001° lat or long may be thrown out
    """
    
    #1000 random samples taken, therefore 1000+ structures needed
    if numStruct<1000:
        print("Too few structures, selecting new area")
    else:
        #Temp DF for calculating local information
        tempRadius_df = pd.DataFrame({
                'lon' : pd.Series(),
                'lat' : pd.Series(),
                'dist' : pd.Series()
                })
            
        #Checking information on 100 different Radii
        numTrials = 1000
        print('Begin Rectangle',(trials+1),"/ 10")
        for x in range(0,numTrials):###########CHANGE BACK TO 100############
            tempIndex = 0
            mainIndex = mainIndex + 1
            center = randint(0,len(coords)-1)
            tempCenterLon = coords.loc[center,'lon']
            tempCenterLat = coords.loc[center,'lat']
            
            #Removing Datapoints that are father than .001° (~100m) away
            minLon = tempCenterLon - .001
            maxLon = tempCenterLon + .001
            minLat = tempCenterLat - .001
            maxLat = tempCenterLat + .001
            for y in range(0,len(coords)):
                tempLon = coords.loc[y,'lon']
                tempLat = coords.loc[y,'lat']
                if floatRange(tempLon, minLon, maxLon):
                    pass
                elif floatRange(tempLat, minLat, maxLat):
                    pass
                elif tempLon==tempCenterLon and tempLat==tempCenterLat:
                    pass
                else:
                    tempRadius_df.loc[tempIndex] = [tempLon, tempLat, haversine(tempLat, tempLon, tempCenterLat, tempCenterLon)]
                    tempIndex = tempIndex + 1
            
            #Saving spacial and usage data into the radiiInfo Data Structure
            wireDist = 0
            production = 0
            consumption = 0
            netEffect = 0
            #Assigning energy profile to center house
            profileIndex = randint(0,28)
            production = production + energyProfile_df.loc[profileIndex,'Production data']
            consumption = consumption + energyProfile_df.loc[profileIndex, 'Consumption data']
            netEffect = netEffect + energyProfile_df.loc[profileIndex,'Net effect']
            for z in range(0,len(tempRadius_df)):
                if(tempRadius_df.loc[z,'dist'] > .04):
                    tempRadius_df.drop(z,inplace=True)
                    tempRadius_df.reset_index()
                else:#Assigning energy profile to other houses
                    profileIndex = randint(0,28)
                    production = production + energyProfile_df.loc[profileIndex,'Production data']
                    consumption = consumption + energyProfile_df.loc[profileIndex, 'Consumption data']
                    netEffect = netEffect + energyProfile_df.loc[profileIndex,'Net effect']
                    wireDist = wireDist + tempRadius_df.loc[z,'dist']
            tempRadius_df.reset_index()
            #Price of Wire = $5/m = $5,000/km (USD)
            radiiInfo_df.loc[mainIndex] = [ tempCenterLat, tempCenterLon, int((len(tempRadius_df)+1)), wireDist, ((wireDist*1000)*5), production, consumption, netEffect]
            tempRadius_df = tempRadius_df.iloc[0:0]
            if(x%(numTrials/10)) == 0 and x/20 is not 0:
                print(int(50+x/20),'%')
        print('End Rectangle',(trials+1),"/ 10")
        trials = trials + 1
        
radiiInfo_df.to_csv('dataOut.csv')
print("Data Processed, Printing Map")

#Drawing the map of all centerpoints
map = Basemap(llcrnrlon=33.92,llcrnrlat=-0.87,urcrnrlon=35.12,urcrnrlat=-0.28, resolution = 'h', epsg=4210)

map.drawmapboundary(fill_color='#0a42a1')
#Fill the continents with the land color
map.fillcontinents(color='#16ab37',lake_color='#0a42a1')
map.drawcoastlines()

map.readshapefile('County Shape/County', 'comarques')

for o in range(0,len(radiiInfo_df)):
    lon = radiiInfo_df.loc[o, 'centLon']
    lat = radiiInfo_df.loc[o, 'centLat']
    x,y = map(lon,lat)
    map.plot(x,y, marker=',',color='r')
    if (o%1000) == 0:
        print(o/10,'%')
    
plt.show()


