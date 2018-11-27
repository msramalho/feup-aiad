import os
import pandas as pd

path_to_csv = 'data/'
csv_files = ["%s%s" % (path_to_csv, pos_csv) for pos_csv in os.listdir(path_to_csv) if pos_csv.endswith('.csv')]

print("Found %d csv files" % len(csv_files))

from tqdm import tqdm  # progress bar
pbar = tqdm(total=len(csv_files), mininterval=0.1, unit=" results")
df = pd.read_csv(csv_files[0])
for j in csv_files[1:]:
    df = df.append(pd.read_csv(j))
    pbar.update()
pbar.close()
df.reset_index(inplace=True)

df.to_csv("merged_all.csv",index=False)
print("Exported to csv")