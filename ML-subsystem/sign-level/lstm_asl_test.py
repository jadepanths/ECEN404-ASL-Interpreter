import numpy as np
import os
import random
from sklearn.preprocessing import LabelEncoder
from keras.models import load_model
from keras_preprocessing.sequence import pad_sequences
from keras.utils import to_categorical

# Load the saved model
model = load_model("models/model11/best_model_epoch_33.h5")

# Load and preprocess the test data
data_dir = "raw_data/raw_test_extracted"
list_dir = "lists/ASL10.txt"
class_folders = []

with open(list_dir, 'r') as f:
    for line in f:
        words = line.split()
        class_folders.extend(words)

data = {}
for folder in class_folders:
    class_path = os.path.join(data_dir, folder)
    numpy_files = os.listdir(class_path)
    data[folder] = [np.load(os.path.join(class_path, np_file)) for np_file in numpy_files]

X = []
y = []
for label, keypoints in data.items():
    for keypoint in keypoints:
        X.append(keypoint)
        y.append(label)

X = np.array(X)
y = np.array(y)

# Encode and one-hot encode labels
le = LabelEncoder()
y_encoded = le.fit_transform(y)
y_onehot = to_categorical(y_encoded)

# Pad sequences
max_length = max([len(seq) for seq in X])
max_length = 97
X_padded = pad_sequences(X, maxlen=max_length, dtype='float32', padding='post')

# Evaluate the model
loss, accuracy = model.evaluate(X_padded, y_onehot)
print("Loss:", loss)
print("Accuracy:", accuracy)

# Make predictions (optional)
predictions = model.predict(X_padded)
predicted_classes = np.argmax(predictions, axis=1)
predicted_labels = le.inverse_transform(predicted_classes)

# Print some predictions for visualization (optional)
num_samples = 10
random_indices = random.sample(range(len(y)), num_samples)
for idx in random_indices:
    print(f"True label: {y[idx]}, Predicted label: {predicted_labels[idx]}")
