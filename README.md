# Applying NSGA-G[2,3] to estimate parameters in SIDARTHE[1] model for COVID-19
It is an application of NSGA-G for the problem which has entire data or lack data of variables in SIDARTHE.
# Using:
NSGA-G should be use and complied with MOEA framework[4]
# Data:
The data is taken from the website[5].
# Experiment:
1. With Italia: src/sida/ExperiementPeriod.java
2. With kazakhstan: src/sida/ExperimentTotal.java
# Simualtion: 
1. Italia: matlab/ita/2020_05_05_14_22_49_NSGAS_P_100_Evolution_1000000/Sidarthe_Simulation_Dung45.m
2. Kazakhstan: matlab/kaz/2020_05_11_00_23_50_NSGAS_P_100_Evolution_1000000/Sidarthe_Simulation_Dung39.m
# Caution
1. These code are using to publish into a journal. Hence, you are not allowed to reuse them to publish to anywhere.
2. You are ot allowed to commercialize or monetize this code in any form.
# Reference
1. https://www.nature.com/articles/s41591-020-0883-7
2. https://ieeexplore.ieee.org/document/8622148
3. http://ceur-ws.org/Vol-2324/Paper21-TLe.pdf
4. http://moeaframework.org/
5. https://data.humdata.org/
