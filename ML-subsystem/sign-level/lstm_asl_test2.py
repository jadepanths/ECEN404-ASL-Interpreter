import numpy as np
import os
import random
from sklearn.preprocessing import LabelEncoder
from keras.models import load_model
from keras_preprocessing.sequence import pad_sequences
from keras.utils import to_categorical
from sklearn.metrics import precision_score, recall_score, f1_score, classification_report, confusion_matrix
import matplotlib.pyplot as plt


def plot_f1_heatmap(f1_scores, class_names):
    fig, ax = plt.subplots(figsize=(4, 8))
    im = ax.imshow(f1_scores.reshape(-1, 1), cmap='viridis', aspect='auto')

    ax.set_yticks(np.arange(len(class_names)))
    ax.set_yticklabels(class_names)

    ax.set_xticks([0])
    ax.set_xticklabels(["F1 Score"])

    # Loop over data dimensions and create text annotations
    for i in range(len(class_names)):
        text = ax.text(0, i, f"{f1_scores[i]:.2f}",
                       ha="center", va="center", color="w")

    ax.set_title("F1 Score Heatmap")
    plt.show()


# Load the saved model
model = load_model("models/model12/best_model_epoch_67.h5")

# Load and preprocess the test data
data_dir = "raw_data/test40"
list_dir = "lists/ASL40.txt"
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
max_length = 138
X_padded = pad_sequences(X, maxlen=max_length, dtype='float32', padding='post')

# Evaluate the model
loss, accuracy = model.evaluate(X_padded, y_onehot)
print("Loss:", loss)
print("Accuracy:", accuracy)

# Make predictions (optional)
predictions = model.predict(X_padded)
predicted_classes = np.argmax(predictions, axis=1)
predicted_labels = le.inverse_transform(predicted_classes)

# Evaluate the model with additional metrics
y_true = y_encoded
y_pred = predicted_classes

precision = precision_score(y_true, y_pred, average='weighted')
recall = recall_score(y_true, y_pred, average='weighted')
f1 = f1_score(y_true, y_pred, average='weighted')

print("Precision:", precision)
print("Recall:", recall)
print("F1 Score:", f1)

# Calculate the confusion matrix
cm = confusion_matrix(y_true, y_pred)

# Normalize the confusion matrix
cm_normalized = cm.astype('float') / cm.sum(axis=1)[:, np.newaxis]

# Calculate the F1 scores for each class
f1_scores = 2 * (cm_normalized.diagonal() / (cm_normalized.sum(axis=0) + cm_normalized.sum(axis=1)))

# You can also print a more detailed classification report
print("\nClassification Report:")
print(classification_report(y_true, y_pred, target_names=le.classes_))

# Print some predictions for visualization (optional)
num_samples = 10
random_indices = random.sample(range(len(y)), num_samples)
for idx in random_indices:
    print(f"True label: {y[idx]}, Predicted label: {predicted_labels[idx]}")

plot_f1_heatmap(f1_scores, le.classes_)
