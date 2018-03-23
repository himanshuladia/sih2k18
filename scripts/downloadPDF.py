import requests
from bs4 import BeautifulSoup
import os

url = "http://missionantyodaya.nic.in/preRankingReportOfGramPanchayat.html?rankingFilter=rankingFilterOfGpNationalWise21To30&startFrom=&endFrom="
download_path = "/home/himanshuladia/Smart India Hackathon 2k18/scraped/"

source = requests.get(url).text
soup = BeautifulSoup(source, 'lxml')

i = 1
for tag in soup.findAll('a', href=True):
    if tag['href'].split('.')[-1] == 'pdf':
        filename = download_path + str(i) + '.pdf'
        urlname = 'http://missionantyodaya.nic.in/' + tag['href']
        print(urlname)
        r = requests.get(urlname)
        with open(filename, 'wb') as f:
            f.write(r.content)
        i += 1
