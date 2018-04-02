import os
import csv
import numpy as np
import pandas as pd
import geopandas as gpd
import matplotlib.pyplot as plt
import random
from sklearn.cluster import KMeans

censusData = pd.read_csv('AndhraPradeshCensusData.csv')

GPlist = ['PEDDA GANAGALLAPETA',
          'MANDASA',
          'KONDARAGOLU',
          'LUKALAM',
          'DHARAPADU',
          'BHAVANAPADU',
          'KAVALI',
          'URJAM',
          'KAKARAPALLE',
          'GORINTA']


def findAbsoluteScores(roadScoreList):

    max_score = np.array(roadScoreList).max()
    for i in range(len(roadScoreList)):
        roadScoreList[i] = (roadScoreList[i] / max_score) * 100

    return roadScoreList


def educationScore(GPname):

    GPdata = censusData[censusData['Gram Panchayat Name'] == GPname]

    GPpopulation = GPdata['Total Population of Village'].sum()

    iPSchildren = 0.107 * GPpopulation
    iMSchildren = 0.246 * GPpopulation
    iSSchildren = 0.097 * GPpopulation
    iSSSchildren = 0.087 * GPpopulation

    itotalChildren = iPSchildren + iMSchildren + iSSchildren + iSSSchildren

    iPSclasses = iPSchildren / 41
    iMSclasses = iMSchildren / 34
    iSSclasses = iSSchildren / 32
    iSSSclasses = iSSSchildren / 33

    classesInEachSchool = 10

    iPS = round(iPSclasses / classesInEachSchool)
    iMS = round(iMSclasses / classesInEachSchool)
    iSS = round(iSSclasses / classesInEachSchool)
    iSSS = round(iSSSclasses / classesInEachSchool)

    iTotalSchools = iPS + iMS + iSS + iSSS

    priorityPS = iPSchildren / itotalChildren
    priorityMS = iMSchildren / itotalChildren
    prioritySS = iSSchildren / itotalChildren
    prioritySSS = iSSSchildren / itotalChildren

    aPSinside = (GPdata['Govt Pre-Primary School (Nursery/LKG/UKG) (Numbers)'] + GPdata['Private Pre-Primary School (Nursery/LKG/UKG) (Numbers)'] + GPdata['Govt Primary School (Numbers)'] + GPdata['Private Primary School (Numbers)']).sum()
    aMSinside = (GPdata['Govt Middle School (Numbers)'] + GPdata['Private Middle School (Numbers)']).sum()
    aSSinside = (GPdata['Govt Secondary School (Numbers)'] + GPdata['Private Secondary School (Numbers)']).sum()
    aSSSinside = (GPdata['Govt Senior Secondary School (Numbers)'] + GPdata['Private Senior Secondary School (Numbers)']).sum()

    suggestions = []

    if aPSinside < iPS:
        suggestions.append(iPS - aPSinside)
    else:
        suggestions.append(0)

    if aMSinside < iMS:
        suggestions.append(iMS - aMSinside)
    else:
        suggestions.append(0)

    if aSSinside < iSS:
        suggestions.append(iSS - aSSinside)
    else:
        suggestions.append(0)

    if aSSSinside < iSSS:
        suggestions.append(iSSS - aSSSinside)
    else:
        suggestions.append(0)

    def nearestSchools(x):
        if x == 'a':
            p = 0.8
        elif x == 'b':
            p = 0.6
        elif x == 'c':
            p = 0.2
        else:
            p = 1
        return p

    PriorityPSnearest = GPdata['(If Pre-Primary School (Nursery/LKG/UKG not available within the village, the distance range code of nearest place where facility is available is given viz; a for < 5 Kms, b for 5-10 Kms and c for 10+ kms ). '].apply(nearestSchools).mean()
    PriorityMSnearest = GPdata['(If Middle School not available within the village, the distance range code of nearest place where facility is available is given viz; a for < 5 Kms, b for 5-10 Kms and c for 10+ kms ). '].apply(nearestSchools).mean()
    PrioritySSnearest = GPdata['(If Secondary School not available within the village, the distance range code of nearest place where facility is available is given viz; a for < 5 Kms, b for 5-10 Kms and c for 10+ kms ). '].apply(nearestSchools).mean()
    PrioritySSSnearest = GPdata['(If Senior Secondary School not available within the village, the distance range code of nearest place where facility is available is given viz; a for < 5 Kms, b for 5-10 Kms and c for 10+ kms ). '].apply(nearestSchools).mean()

    EducationScore = (((priorityPS * PriorityPSnearest * aPSinside) + (priorityMS * PriorityMSnearest * aMSinside) + (prioritySS * PrioritySSnearest * aSSinside) + (prioritySSS * PrioritySSSnearest * aSSSinside)) / iTotalSchools) * 100

    return EducationScore, suggestions


educationScorelist = []
educationSuggestionsList = []

for GPname in GPlist:
    edscore, sug = educationScore(GPname)
    educationScorelist.append(edscore)
    educationSuggestionsList.append(list(map(int, sug)))


def healthScore(GPname):

    GPdata = censusData[censusData['Gram Panchayat Name'] == GPname]

    nearestHCweight = 0.2
    nearestVCweight = 0.1
    nearestMHCweight = 0.1

    hcInside = (GPdata['Community Health Centre (Numbers)'] + GPdata['Primary Health Centre (Numbers)'] + GPdata['Primary Health Sub Centre (Numbers)']).sum()
    vcInside = GPdata['Veterinary Hospital (Numbers)'].sum()
    mhcInside = GPdata['Mobile Health Clinic (Numbers)'].sum()

    def nearestHospitals(x):
        if x == 'a':
            p = nearestHCweight
        elif x == 'b':
            p = nearestVCweight
        elif x == 'c':
            p = nearestMHCweight
        else:
            p = 0
        return p

    nearestHC = (GPdata['(If Community Health Centre not available within the village, the distance range code of nearest place where facility is available is given viz; a for < 5 Kms, b for 5-10 Kms and c for 10+ kms ). '].apply(nearestHospitals) + GPdata['(If Primary Health Centre not available within the village, the distance range code of nearest place where facility is available is given viz; a for < 5 Kms, b for 5-10 Kms and c for 10+ kms ). '].apply(nearestHospitals) + GPdata['(If Primary Health Sub Centre not available within the village, the distance range code of nearest place where facility is available is given viz; a for < 5 Kms, b for 5-10 Kms and c for 10+ kms ). '].apply(nearestHospitals)).sum()
    nearestVC = GPdata['(If Veterinary Hospital not available within the village, the distance range code of nearest place where facility is available is given viz; a for < 5 Kms, b for 5-10 Kms and c for 10+ kms ). '].apply(nearestHospitals).sum()
    nearestMHC = GPdata['(If Mobile Health Clinic not available within the village, the distance range code of nearest place where facility is available is given viz; a for < 5 Kms, b for 5-10 Kms and c for 10+ kms ). '].apply(nearestHospitals).sum()

    idealHospitals = (2 + 2 + 1) + nearestHCweight * (1 + 1 + 1) + nearestVCweight * (1 + 1 + 1) + nearestMHCweight * (1 + 1 + 1)

    actualHospitals = hcInside + vcInside + mhcInside + nearestHC + nearestVC + nearestMHC

    healthScore = (actualHospitals / idealHospitals) * 100

    return healthScore


healthScorelist = []

for GPname in GPlist:
    healthScorelist.append(healthScore(GPname))


def roadScore(i):

    road = pd.read_csv('dummy_data/road_dummy_data/' + 'road' + str(i) + '.csv')
    panchayat = pd.read_csv('dummy_data/panchayat_dummy_data/' + 'panchayat' + str(i) + '.csv')

    nationalHighway = road[road['status'] == 'National Highway']['Shape_Leng'].sum()
    stateHighway = road[road['status'] == 'State Highway']['Shape_Leng'].sum()
    districtRoad = road[road['status'] == 'District Road']['Shape_Leng'].sum()
    ruralRoad = (road[road['status'] == 'Village Road']['Shape_Leng']).sum() + (road[road['status'] == 'Cart Track']['Shape_Leng']).sum()

    panchayatArea = panchayat['Shape_Area'].sum()

    roadRelativeScore = panchayatArea / ((0.0213 * nationalHighway) + (0.0397 * stateHighway) + (0.1408 * districtRoad) + (0.798 * ruralRoad))

    return roadRelativeScore


roadScorelist = []

for i in range(len(GPlist)):
    roadScorelist.append(roadScore(i + 1))

roadScorelist = findAbsoluteScores(roadScorelist)


def slopeScore(i):
    slope = pd.read_csv('dummy_data/slope_dummy_data/' + 'slope' + str(i) + '.csv')
    panchayat = pd.read_csv('dummy_data/panchayat_dummy_data/' + 'panchayat' + str(i) + '.csv')
    panchayatArea = panchayat['Shape_Area'].sum()

    vgsArea = slope[slope['class'] == 'Very gently sloping']['Shape_Area'].sum()
    ssArea = slope[slope['class'] == 'Steeply sloping']['Shape_Area'].sum()
    gsArea = slope[slope['class'] == 'Gently sloping']['Shape_Area'].sum()
    mssArea = slope[slope['class'] == 'Moderately steeply sloping']['Shape_Area'].sum()
    msArea = slope[slope['class'] == 'Moderately sloping']['Shape_Area'].sum()
    vssArea = slope[slope['class'] == 'Very steeply sloping']['Shape_Area'].sum()
    lnlArea = slope[slope['class'] == 'Level to nearly level']['Shape_Area'].sum()

    weightedSlopeArea = vgsArea / 3 + ssArea / 50 + gsArea / 8 + mssArea / 30 + msArea / 15 + vssArea / 100 + lnlArea / 1

    slopeRelativeScore = weightedSlopeArea / panchayatArea

    return slopeRelativeScore


slopeScorelist = []

for i in range(len(GPlist)):
    slopeScorelist.append(slopeScore(i + 1))

slopeScorelist = findAbsoluteScores(slopeScorelist)


def waterBodiesScore(i):

    lulc = pd.read_csv('dummy_data/lulc_dummy_data/' + 'lulc' + str(i) + '.csv')
    panchayat = pd.read_csv('dummy_data/panchayat_dummy_data/' + 'panchayat' + str(i) + '.csv')
    panchayatArea = panchayat['Shape_Area'].sum()

    waterQuality = {'Salinity': 0.4, 'Fluoride': 0.8, 'Iron': 0.4, 'Arsenic': 0.1, 'Nitrate': 0.9, 'No Pollution': 1}

    waterArea = lulc[(lulc['dscr1'] == 'Water bodies') & (lulc['dscr2'] == 'Lakes / Ponds')]['Shape_Area'].sum() + lulc[(lulc['dscr1'] == 'Water bodies') & (lulc['dscr2'] == 'River / Stream / Drain')]['Shape_Area'].sum() + lulc[(lulc['dscr1'] == 'Water bodies') & (lulc['dscr2'] == 'Reservoir / Tanks')]['Shape_Area'].sum()
    waterSpread = waterArea / panchayatArea

    waterInfectant = random.choice(list(waterQuality.items()))[0]
    waterValue = random.choice(list(waterQuality.items()))[1]
    waterScore = (((1.53 * waterValue) + (2.22 * waterSpread)) / 3.75) * 100

    return waterScore


waterBodiesScorelist = []

for i in range(len(GPlist)):
    waterBodiesScorelist.append(waterBodiesScore(i + 1))


def builtUpScore(i):

    lulc = pd.read_csv('dummy_data/lulc_dummy_data/' + 'lulc' + str(i) + '.csv')
    builtup = lulc[(lulc['dscr1'] == 'Built up')]

    panchayat = pd.read_csv('dummy_data/panchayat_dummy_data/' + 'panchayat' + str(i) + '.csv')

    panchayatArea = panchayat['Shape_Area'].sum()

    miningArea = builtup[builtup['dscr2'] == 'Mining / industrial']['Shape_Area'].sum()
    miningPercentage = (miningArea / panchayatArea) * 100
    miningPercentage

    villageArea = builtup[builtup['dscr2'] == 'Built up (Rural)']['Shape_Area'].sum()
    villagePercentage = (villageArea / panchayatArea) * 100
    villagePercentage

    def builtPercentage():
        if miningPercentage <= 20:
            builtupPercentage = miningPercentage + villagePercentage
        else:
            builtupPercentage = villagePercentage + 20 - miningPercentage
        return builtupPercentage

    relativeBuiltupScore = builtPercentage()

    return relativeBuiltupScore


builtUpScorelist = []

for i in range(len(GPlist)):
    builtUpScorelist.append(builtUpScore(i + 1))

builtUpScorelist = findAbsoluteScores(builtUpScorelist)


def wastelandScore(i):

    lulc = pd.read_csv('dummy_data/lulc_dummy_data/' + 'lulc' + str(i) + '.csv')
    wasteland = lulc[lulc['dscr1'] == 'Wastelands']
    panchayat = pd.read_csv('dummy_data/panchayat_dummy_data/' + 'panchayat' + str(i) + '.csv')

    panchayatArea = panchayat['Shape_Area'].sum()
    wastelandArea = wasteland['Shape_Area'].sum()
    scrublandArea = wasteland[wasteland['dscr2'] == 'Scrub land']['Shape_Area'].sum()
    wastelandScore = ((scrublandArea / wastelandArea) - (wastelandArea / panchayatArea)) * 100

    return wastelandScore


wastelandScorelist = []

for i in range(len(GPlist)):
    wastelandScorelist.append(wastelandScore(i + 1))


def agriculturalScore(i):

    lulc = pd.read_csv('dummy_data/lulc_dummy_data/' + 'lulc' + str(i) + '.csv')
    panchayat = pd.read_csv('dummy_data/panchayat_dummy_data/' + 'panchayat' + str(i) + '.csv')
    panchayatArea = panchayat['Shape_Area'].sum()

    agriculturalLand = lulc[lulc['dscr1'] == 'Agricultural land']
    agriculturalArea = agriculturalLand['Shape_Area'].sum()
    agriculturalScore = (agriculturalArea / panchayatArea) * 100
    agriculturalScore

    return agriculturalScore


agriculturalScorelist = []

for i in range(len(GPlist)):
    agriculturalScorelist.append(agriculturalScore(i + 1))


def sanitationScore(i):

    files = [x for x in os.listdir('.') if 'sbm' in x]
    csvs = [pd.read_csv(x) for x in files]
    sanitationAP = pd.concat(csvs, axis=0)
    sanitationAP.reset_index(inplace=True)
    sanitationAP.drop('index', axis=1, inplace=True)

    IHHLtotal = sanitationAP.iloc[i]['IHHLTotalAsPerDetails']
    IHHLachieved = sanitationAP.iloc[i]['IHHLTotalAch']
    sanitationscore = (IHHLachieved / IHHLtotal) * 100

    return sanitationscore


sanitationScorelist = []
for i in range(len(GPlist)):
    sanitationScorelist.append(sanitationScore(i + 1))

print(sanitationScorelist)


def literacyRate(i):

    literacy = pd.read_csv('dummy_data/literacy_dummy_data.csv')
    lit = literacy.iloc[i][1].sum()
    return lit


literacyScorelist = []
for i in range(len(GPlist)):
    literacyScorelist.append(literacyRate(i + 1))

scores = np.vstack((np.array(educationScorelist), np.array(healthScorelist), np.array(roadScorelist), np.array(slopeScorelist), np.array(waterBodiesScorelist), np.array(builtUpScorelist), np.array(wastelandScorelist), np.array(agriculturalScorelist), np.array(sanitationScorelist), np.array(literacyScorelist)))
scores = np.transpose(scores)
scores = np.round(scores, 0)
scores = pd.DataFrame(scores, columns=['Education', 'Health', 'Road', 'Slope', 'Water Bodies', 'Built Up', 'Wasteland', 'Agriculture', 'Sanitation', 'Literacy'])


# For clustering

x = np.array(scores)
k = 2
clf = KMeans(n_clusters=k)
clf.fit(x)
labels = clf.labels_

####################

educationWeight = 0.12
healthWeight = 0.10
roadWeight = 0.08
slopeWeight = 0.06
waterWeight = 0.09
builtupWeight = 0.08
wastelandWeight = 0.08
agricultureWeight = 0.15
sanitationWeight = 0.12
literacyWeight = 0.12

totalScore = np.round((scores['Education'] * educationWeight) + (scores['Health'] * healthWeight) + (scores['Road'] * roadWeight) + (scores['Slope'] * slopeWeight) + (scores['Water Bodies'] * waterWeight) + (scores['Built Up'] * builtupWeight) + (scores['Wasteland'] * wastelandWeight) + (scores['Agriculture'] * agricultureWeight) + (scores['Sanitation'] * sanitationWeight) + (scores['Literacy'] * literacyWeight))

# Without geospatial data

educationWeight = 0.261
healthWeight = 0.2175
sanitationWeight = 0.261
literacyWeight = 0.261

censusScore = np.round((scores['Education'] * educationWeight) + (scores['Health'] * healthWeight) + (scores['Sanitation'] * sanitationWeight) + (scores['Literacy'] * literacyWeight))

scores['Census Score'] = censusScore
scores['Total Score'] = totalScore
scores['Label'] = labels
scores.insert(0, 'Name', GPlist)
# scores.to_csv('scoresWithLabels.csv', index=False)

d = {0: 'Primary School', 1: 'Middle School', 2: 'Secondary School', 3: 'Senior Secondary School'}
negativeComment = ""
suggestions = []
for village in educationSuggestionsList:
    for i in range(4):
        if village[i] > 0:
            negativeComment += str(village[i]) + " " + str(d[i]) + ","
    if negativeComment == "":
        negativeComment = "No new schools need to be constructed."
    else:
        negativeComment += "need to be constructed."
    negativeComment += "\nImprove the teacher-student ratio by hiring more teachers.\nGive subsidies and grants for enrollment and consruction of schools."
    suggestions.append(negativeComment)
    negativeComment = ""

# with open("EducationSuggestions.csv", "w") as output:
#     writer = csv.writer(output)
#     for val in suggestions:
#         writer.writerow([val])
