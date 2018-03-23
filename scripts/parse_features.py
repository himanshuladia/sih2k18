import csv
from tqdm import tqdm

with open('features.csv', 'w') as f:
    csvWriter = csv.writer(f)
    i = 1
    for i in tqdm(range(1, 4888)):
        filename = str(i) + '.csv'
        with open(filename) as g:
            # Parse values
            line = g.readlines()[-1]
            values = line.split(',')
            values.pop(0)
            if values[7] == "":
                values.pop(7)
            valueAt4, valueAt5 = values[4].split()
            values.pop(4)
            values.insert(4, valueAt4)
            values.insert(5, valueAt5)
            if values[-1] == '\n':
                values.pop()
            else:
                values[-1] = values[-1][0:-1]

            # Write to CSV
            csvWriter.writerow(values)
