import os
import pandas as pd
import csv
from tqdm import tqdm  # progress bar
from random import randint as rnd

path_to_csv = 'data/'
csv_files = ["%s%s" % (path_to_csv, pos_csv) for pos_csv in os.listdir(path_to_csv) if pos_csv.endswith('.csv')]

print("Found %d csv files" % len(csv_files))

pbar = tqdm(total=len(csv_files), mininterval=0.1, unit=" results")
df_train = pd.read_csv(csv_files[0])
df_test = pd.read_csv(csv_files[1])

for j in csv_files[2:]:
	if rnd(0, 10) <= 8: df_train = df_train.append(pd.read_csv(j))
	else: df_test = df_test.append(pd.read_csv(j))
	pbar.update()
pbar.close()

df_train.reset_index(inplace=True)
df_test.reset_index(inplace=True)

df_train.to_csv("merged_split_train.csv", index=False, quoting=csv.QUOTE_NONNUMERIC)
df_test.to_csv("merged_split_test.csv",  index=False, quoting=csv.QUOTE_NONNUMERIC)
print("Train: ", df_train.shape)
print("Test: ", df_test.shape)
