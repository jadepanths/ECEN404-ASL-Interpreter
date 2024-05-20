from keras.models import Sequential
from keras.layers import LSTM, Dense, Dropout, TimeDistributed, Bidirectional, GRU
from keras.utils import to_categorical, plot_model
from keras.regularizers import l2


input_shape = (93, 186)  # Replace this with the actual input shape
class_folders = ['class1', 'class2']  # Replace this with the actual class folders
l2_reg = l2(0.01)

model = Sequential([
    Bidirectional(LSTM(32, return_sequences=True, activation='tanh', kernel_regularizer=l2_reg, dropout=0.2, recurrent_dropout=0.2), input_shape=input_shape),
    Bidirectional(LSTM(64, return_sequences=True, activation='tanh', kernel_regularizer=l2_reg, dropout=0.2, recurrent_dropout=0.2)),
    Bidirectional(LSTM(32, return_sequences=False, activation='tanh', kernel_regularizer=l2_reg, dropout=0.2, recurrent_dropout=0.2)),
    Dropout(0.5),
    Dense(32, activation='relu'),
    Dropout(0.5),
    Dense(len(class_folders), activation='softmax')
])

# Create a diagram of the model
plot_model(model, to_file='model_diagram.png', show_shapes=True, show_layer_names=True)