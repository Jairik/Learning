
import pandas as pd
import numpy as np
from tensorflow import keras
from tensorflow.keras import layers
from sklearn.model_selection import train_test_split
from sklearn.datasets import load_breast_cancer


# Splitting the data
dataset = load_breast_cancer()
df = pd.DataFrame(dataset.data, columns=dataset.feature_names)
X =  dataset['data']
Y = dataset['target']
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.3, random_state=42)

print(X_train.shape)

# Defining the model
model_better = keras.Sequential([
    layers.Dense(32, input_shape=(30,), activation='relu'),  # Change input shape to 30
    layers.Dense(64, activation='relu'),
    layers.Dense(32, activation='relu'),
    layers.Dense(2, activation='softmax')
])


# Compiling the model
model_better.compile(optimizer='adam',
                     loss=keras.losses.SparseCategoricalCrossentropy(),
                     metrics=['accuracy'])

# Fitting the model
model_better.fit(X_train, Y_train, epochs=2690, batch_size=50)

