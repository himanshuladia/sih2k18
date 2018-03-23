import csv

with open('gram_panchayat_scores.csv', 'r') as f:
    labels = []
    csvReader = csv.reader(f)
    i = 1
    for line in csvReader:
        labels.append(line[-2])
        if i == 1000:
            break
        i += 1

    with open('labels.csv', 'w') as g:
        csvWriter = csv.writer(g)
        csvWriter.writerow(labels)
