
# coding: utf-8

# In[1]:


# Import all libraries needed for the tutorial

# General syntax to import specific functions in a library: 
##from (library) import (specific library function)
from pandas import DataFrame, read_csv

# General syntax to import a library but no functions: 
##import (library) as (give the library a nickname/alias)
import matplotlib.pyplot as plt
import pandas as pd #this is how I usually import pandas
import sys #only needed to determine Python version number
import matplotlib #only needed to determine Matplotlib version number


# In[2]:


print('Python version ' + sys.version)
print('Pandas version ' + pd.__version__)
print('Matplotlib version ' + matplotlib.__version__)


# In[3]:


# Reading Data
Location = '.\merged_all.csv'
df = pd.read_csv(Location)


# In[4]:


df


# In[5]:


# Check data type of the columns
df.dtypes


# In[6]:


# Reparting data from different experiences with different sizes
lab_10_10 = df.loc[df['xsize'] == 10].loc[df['ysize'] == 10]
lab_50_20 = df.loc[df['xsize'] == 50].loc[df['ysize'] == 20]
lab_20_50 = df.loc[df['xsize'] == 20].loc[df['ysize'] == 50]
lab_60_40 = df.loc[df['xsize'] == 60].loc[df['ysize'] == 40]
lab_40_60 = df.loc[df['xsize'] == 40].loc[df['ysize'] == 60]
lab_100_100 = df.loc[df['xsize'] == 100].loc[df['ysize'] == 100]


# In[49]:


# Getting average of averageSpeed lab_10_10
average_10_10_value = lab_10_10['averageSpeed'].mean()
print('Average in global of 10x10: ' + str(average_10_10_value) + ' in a total of ' + str(lab_10_10.size))
print('')

# Average of different types agents
lab_10_10_BacktrackAgent = lab_10_10.loc[lab_10_10['agentType'] == 'BacktrackAgent']
average_10_10_BacktrackAgent = lab_10_10_BacktrackAgent['averageSpeed'].mean()
size_10_10_BacktrackAgent = lab_10_10_BacktrackAgent.size
print('Average of BacktrackAgent on 10x10: ' + str(average_10_10_BacktrackAgent) + ' in a total of ' + str(size_10_10_BacktrackAgent))

lab_10_10_ForwardAgent = lab_10_10.loc[lab_10_10['agentType'] == 'ForwardAgent']
average_10_10_ForwardAgent = lab_10_10_ForwardAgent['averageSpeed'].mean()
size_10_10_ForwardAgent = lab_10_10_ForwardAgent.size
print('Average of ForwardAgent on 10x10: ' + str(average_10_10_ForwardAgent) + ' in a total of ' + str(size_10_10_ForwardAgent))

lab_10_10_NegotiatingAgent = lab_10_10.loc[lab_10_10['agentType'] == 'NegotiatingAgent']
average_10_10_NegotiatingAgent = lab_10_10_NegotiatingAgent['averageSpeed'].mean()
size_10_10_NegotiatingAgent = lab_10_10_NegotiatingAgent.size
print('Average of NegotiatingAgent on 10x10: ' + str(average_10_10_NegotiatingAgent) + ' in a total of ' + str(size_10_10_NegotiatingAgent))

lab_10_10_RandomAgent = lab_10_10.loc[lab_10_10['agentType'] == 'RandomAgent']
average_10_10_RandomAgent = lab_10_10_RandomAgent['averageSpeed'].mean()
size_10_10_RandomAgent = lab_10_10_RandomAgent.size
print('Average of RandomAgent on 10x10: ' + str(average_10_10_RandomAgent) + ' in a total of ' + str(size_10_10_RandomAgent))

lab_10_10_SwarmAgent = lab_10_10.loc[lab_10_10['agentType'] == 'SwarmAgent']
average_10_10_SwarmAgent = lab_10_10_SwarmAgent['averageSpeed'].mean()
size_10_10_SwarmAgent = lab_10_10_SwarmAgent.size
print('Average of SwarmAgent on 10x10: ' + str(average_10_10_SwarmAgent) + ' in a total of ' + str(size_10_10_SwarmAgent))


# In[90]:


average_10_10 = lab_10_10.groupby(['agentType'], as_index=False).mean()
average_10_10.set_index("agentType",drop=True,inplace=True)
average_10_10['averageSpeed'].plot(kind='bar')


# In[99]:


lab_10_10_BacktrackAgent['averageSpeed'].plot(use_index=False, kind='kde', figsize=(10,4), xlim=(-0.1,1.1), legend= True, label="BacktrackAgent")
lab_10_10_ForwardAgent['averageSpeed'].plot(use_index=False, kind='kde', figsize=(10,4), xlim=(-0.1,1.1), legend= True, label="ForwardAgent")
lab_10_10_NegotiatingAgent['averageSpeed'].plot(use_index=False, kind='kde', figsize=(10,4), xlim=(-0.1,1.1), legend= True, label="NegotiatingAgent")
lab_10_10_RandomAgent['averageSpeed'].plot(use_index=False, kind='kde', figsize=(10,4), xlim=(-0.1,1.1), legend= True, label="RandomAgent")
lab_10_10_SwarmAgent['averageSpeed'].plot(use_index=False, kind='kde', figsize=(10,4), xlim=(-0.1,1), legend= True, label="SwarmAgent")

