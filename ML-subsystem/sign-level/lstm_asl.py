import numpy as np
import os
from sklearn.preprocessing import LabelEncoder
from sklearn.model_selection import train_test_split
from keras.models import Sequential
from keras.layers import LSTM, Dense, Dropout, TimeDistributed, Bidirectional, GRU
from keras.utils import to_categorical
from keras.optimizers import Adam
from keras.callbacks import ModelCheckpoint, EarlyStopping, ReduceLROnPlateau
from keras.regularizers import l2
import matplotlib.pyplot as plt
from sklearn.utils.class_weight import compute_class_weight

def plot_history(history):
    fig, (ax1, ax2) = plt.subplots(1, 2, figsize=(12, 5))

    # Plot accuracy
    ax1.plot(history.history['accuracy'])
    ax1.plot(history.history['val_accuracy'])
    ax1.set_title('Model accuracy')
    ax1.set_ylabel('Accuracy')
    ax1.set_xlabel('Epoch')
    ax1.legend(['Train', 'Test'], loc='upper left')

    # Plot loss
    ax2.plot(history.history['loss'])
    ax2.plot(history.history['val_loss'])
    ax2.set_title('Model loss')
    ax2.set_ylabel('Loss')
    ax2.set_xlabel('Epoch')
    ax2.legend(['Train', 'Test'], loc='upper left')

    plt.show()


# Load data
data_dir = "extraction"
list_dir = "lists/ASL40.txt"
class_folders = []
# class_folders = ["book", "hello", "guitar"]
with open(list_dir, 'r') as f:
    for line in f:
        words = line.split()
        class_folders.extend(words)


data = {}
for folder in class_folders:
    class_path = os.path.join(data_dir, folder)
    numpy_files = os.listdir(class_path)
    data[folder] = [np.load(os.path.join(class_path, np_file)) for np_file in numpy_files]

# Preprocess data
X = []
y = []
for label, keypoints in data.items():
    for keypoint in keypoints:
        X.append(keypoint)
        y.append(label)

X = np.array(X)
y = np.array(y)

# Encode labels
le = LabelEncoder()
y_encoded = le.fit_transform(y)

# One-hot encode labels
y_onehot = to_categorical(y_encoded)

# Pad sequences
from keras_preprocessing.sequence import pad_sequences

max_length = max([len(seq) for seq in X])
X_padded = pad_sequences(X, maxlen=max_length, dtype='float32', padding='post')

# Train-test split
X_train, X_test, y_train, y_test = train_test_split(X_padded, y_onehot, test_size=0.2, random_state=42)

# Create LSTM model
input_shape = (X_train.shape[1], X_train.shape[2])
print(input_shape)

# LOCATION
path = "models/model12"

# For checkpoint
filepath = path + "/best_model_epoch_{epoch:02d}.h5"
checkpoint = ModelCheckpoint(filepath, monitor='val_loss', save_best_only=True, mode='min')

l2_reg = l2(0.001)

model = Sequential()
model.add(Bidirectional(LSTM(32, return_sequences=True, activation='tanh', kernel_regularizer=l2_reg, dropout=0.2, recurrent_dropout=0.2), input_shape=input_shape))
model.add(Bidirectional(LSTM(64, return_sequences=True, activation='tanh', kernel_regularizer=l2_reg, dropout=0.2, recurrent_dropout=0.2)))
model.add(Bidirectional(LSTM(32, return_sequences=False, activation='tanh', kernel_regularizer=l2_reg, dropout=0.2, recurrent_dropout=0.2)))
# model.add(Bidirectional(LSTM(16, return_sequences=False, activation='tanh', kernel_regularizer=l2_reg, dropout=0.2, recurrent_dropout=0.2)))
model.add(Dropout(0.5))
model.add(Dense(32, activation='relu'))
model.add(Dropout(0.5))
model.add(Dense(len(class_folders), activation='softmax'))

learning_rate = 0.001
optimizer = Adam(lr=learning_rate, clipnorm=1.0)

model.compile(loss='categorical_crossentropy', optimizer=optimizer, metrics=['accuracy'])
early_stopping = EarlyStopping(monitor='val_loss', patience=10, restore_best_weights=True)
reduce_lr_on_plateau = ReduceLROnPlateau(monitor='val_loss', factor=0.1, patience=5, min_lr=1e-6)

# Calculate class weights
y_train_labels = np.argmax(y_train, axis=1)
unique_classes = np.unique(y_train_labels)
class_weights = compute_class_weight(class_weight='balanced', classes=unique_classes, y=y_train_labels)


# Train the model
history = model.fit(X_train, y_train, validation_data=(X_test, y_test), epochs=500, batch_size=32,
                    callbacks=[checkpoint, early_stopping])
plot_history(history)

# Save the trained model
model.save(path + "/asl_model_final_epoch.h5")

