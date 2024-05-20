import cv2
import mediapipe as mp
import time
import matplotlib.pyplot as plt
import numpy as np
import os
import sys

from sklearn.preprocessing import LabelEncoder
from keras.models import load_model
from keras_preprocessing.sequence import pad_sequences
from keras.utils import to_categorical

mp_holistic = mp.solutions.holistic
holistic_image = mp_holistic.Holistic(static_image_mode=True, min_detection_confidence=0.5)
holistic_video = mp_holistic.Holistic(static_image_mode=False, min_detection_confidence=0.5,
                                      min_tracking_confidence=0.5, smooth_landmarks=True,
                                      model_complexity=1)

mp_drawing = mp.solutions.drawing_utils
mp_drawing_styles = mp.solutions.drawing_styles


def vid_keypoint(cap_vid, holistic, draw=False, display=False, landmarks=False):
    p_time = 0
    # frame = 0
    data = np.empty((0, 186), float)
    while cap_vid.isOpened():
        success, image = cap_vid.read()
        if not success:
            # if loading a video, use break
            break

        # To improve performance, optionally mark the image as not writeable to
        # pass by reference.
        image.flags.writeable = False
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        results = holistic.process(image)

        if draw:
            image.flags.writeable = True

            if results.pose_landmarks:
                mp_drawing.draw_landmarks(
                    image,
                    results.pose_landmarks,
                    mp_holistic.POSE_CONNECTIONS,
                    landmark_drawing_spec=mp_drawing_styles
                    .get_default_pose_landmarks_style())

            if results.left_hand_landmarks:
                mp_drawing.draw_landmarks(
                    image,
                    results.left_hand_landmarks,
                    mp_holistic.HAND_CONNECTIONS,
                    landmark_drawing_spec=mp_drawing_styles
                    .get_default_hand_landmarks_style())
            if results.right_hand_landmarks:
                mp_drawing.draw_landmarks(
                    image,
                    results.right_hand_landmarks,
                    mp_holistic.HAND_CONNECTIONS,
                    landmark_drawing_spec=mp_drawing_styles
                    .get_default_hand_landmarks_style()
                )

        if landmarks:
            if results.pose_landmarks:
                pose = np.array([[res.x, res.y, res.z, res.visibility] for res in results.pose_landmarks.landmark])
                # print("raw pose landmarks (33):", len(pose))
                # print("Shape: ", pose.shape)
                """
                process pose array for only keypoints we want (0-14)
                # Mediapipe index (0, 2, 5, 7-16, 23, 24)
                """
                extracted_pose = []
                for element in range(len(pose)):
                    if element == 0:
                        extracted_pose.append(pose[element])
                    elif element == 2:
                        extracted_pose.append(pose[element])
                    elif element == 5:
                        extracted_pose.append(pose[element])
                    elif element in range(7, 17):
                        extracted_pose.append(pose[element])
                    elif element == 23:
                        extracted_pose.append(pose[element])
                    elif element == 24:
                        extracted_pose.append(pose[element])

                extracted_pose = np.array(extracted_pose).flatten()
            else:
                extracted_pose = np.zeros(60)

            """=============== LEFT HAND ==============="""
            if results.left_hand_landmarks:
                lh = np.array([[res.x, res.y, res.z] for res in results.left_hand_landmarks.landmark])
                lh = np.array(lh).flatten()
            else:
                lh = np.zeros(21 * 3)

            """=============== RIGHT HAND ==============="""
            if results.right_hand_landmarks:
                rh = np.array([[res.x, res.y, res.z] for res in results.right_hand_landmarks.landmark])
                rh = np.array(rh).flatten()
            else:
                rh = np.zeros(21 * 3)

            extracted = np.concatenate([extracted_pose, lh, rh])
            # print("Len:", len(extracted))

            # row = np.hstack((frame, extraction))
            data = np.vstack((data, extracted))
            # frame += 1
            # print("extraction:", extraction)
            # print("Len:", len(extraction))

        if display:
            # timing counting FPS
            c_time = time.time()
            fps = 1 / (c_time - p_time)
            p_time = c_time

            # printing FPS
            cv2.putText(image, str(int(fps)), (70, 50), cv2.FONT_HERSHEY_PLAIN, 3,
                        (255, 0, 0), 3)

            cv2.imshow("Combined Key-points", image)
            '''
            # if imshow is not working:
            image2 = image[:, :, ::-1]
            plt.imshow(image2)
            '''

            if cv2.waitKey(5) & 0xFF == ord('q'):
                break

    if landmarks:
        # write the np_array
        # print(data)
        return data

    cap_vid.release()
    print("released")
    return None


def predict(keypoint, model, label_encoder, max_length):
    # keypoint = np.load(npy_file)
    X = np.array([keypoint])
    X_padded = pad_sequences(X, maxlen=max_length, dtype='float32', padding='post')
    probabilities = model.predict(X_padded)
    predicted_class = np.argmax(probabilities)
    predicted_label = label_encoder.inverse_transform([predicted_class])[0]
    return predicted_label, probabilities[0, predicted_class]


def read_classes_from_file(file_path):
    with open(file_path, 'r') as f:
        classes = [line.strip() for line in f]
    return classes


def create_txt(input_video_path, output_dir, content):
    base_file = os.path.splitext(os.path.basename(input_video_path))[0]

    # create a txt file name and path
    txt_name = f"{base_file}.txt"
    txt_output_dir = os.path.join(output_dir, txt_name)

    with open(txt_output_dir, "w") as f:
        f.write(content)


def main():
    sequence = []
    sentence = []
    predictions = []
    confidence_threshold = 0.3

    n = len(sys.argv)
    print("Total arguments passed:", n)

    # first argument is the python file
    # the rests are the passed in arguments

    input_file = sys.argv[1]
    output_dir = sys.argv[2]

    print(input_file)
    print(output_dir)

    if not os.path.isfile(input_file):
        print("invalid file")
        return

    # Create the output directory if not exist
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    cap = cv2.VideoCapture(input_file)
    key_point = vid_keypoint(cap, holistic_video, draw=False, display=False, landmarks=True)

    if np.count_nonzero(key_point) == 0:
        print("invalid Video (cant detect signer)")
        create_txt(input_file, output_dir, "invalid Video (cant detect signer)")
        return
    elif key_point is None:
        print("null keypoints")
        create_txt(input_file, output_dir, "null keypoints")
        return

    if key_point is not None:

        # model = load_model("models/model11/best_model_epoch_33.h5")
        model = load_model("models/model12/best_model_epoch_70.h5")

        # Prepare the label encoder with known classes
        # Read the known classes from the file
        # classes_file = "lists/ASL10.txt"
        classes_file = "lists/ASL40.txt"

        known_classes = read_classes_from_file(classes_file)

        le = LabelEncoder()
        le.fit(known_classes)

        # Set the known max_length
        max_length = 138  # Replace this with the actual known max_length

        # Make a prediction using the predict_asl_label function
        predicted_label, predicted_prob = predict(key_point, model, le, max_length)

        if predicted_prob < confidence_threshold:
            print(f"Error: Confidence too low ({predicted_prob * 100:.2f}%)")
            create_txt(input_file, output_dir, f"error: Confidence too low ({predicted_prob * 100:.2f}%)")
        else:
            print(f"Predicted label: {predicted_label}", predicted_prob * 100)
            create_txt(input_file, output_dir, predicted_label)

        # print(f"Predicted label: {predicted_label}")
        # create_txt(input_file, output_dir, predicted_label)


if __name__ == "__main__":
    main()
