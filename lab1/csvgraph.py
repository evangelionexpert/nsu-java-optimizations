#!/usr/bin/env python3

import pandas as pd
import matplotlib.pyplot as plt
import sys

df = pd.read_csv(sys.stdin)
df.plot()
plt.show()

# print(df.info())
