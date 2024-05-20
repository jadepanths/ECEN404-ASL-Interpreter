import cv2
import time
import mediapipe as mp
import argparse
from matplotlib import pyplot as plt

# Default setting
# input option (0=video, 1=image, 2=webcam)
# input_option = 0
# modelComplexity = 0


def full_estimation(input_option, input_model_complexity):
    # for FPS calculation
    p_time = 0

    if input_option < 0 or input_option > 2:
        input_option = 1
        print("invalid input option: change to default (1)")

    if input_option == 0:
        print("Video Option")
        cap = cv2.VideoCapture('pose/pose1.mp4')

    elif input_option == 1:
        print("Currently not supporting images")
        cap = cv2.VideoCapture('pose/pose1.mp4')

    elif input_option == 2:
        print("Webcam Option")
        cap = cv2.VideoCapture(0)

    mp_drawing = mp.solutions.drawing_utils
    mp_drawing_styles = mp.solutions.drawing_styles
    mp_holistic = mp.solutions.holistic

    with mp_holistic.Holistic(
            model_complexity=input_model_complexity,
            min_detection_confidence=0.5,
            min_tracking_confidence=0.5,
            smooth_landmarks=True) as holistic:

        while cap.isOpened():
            success, image = cap.read()

            # the end of the video
            if not success:

                if input_option == 0:
                    print("end of video")
                    # If loading a video, use 'break' instead of 'continue'.
                    break
                elif input_option == 2:
                    print("Ignoring empty camera frame.")
                    continue

            # To improve performance, optionally mark the image as not writeable to
            # pass by reference.
            image.flags.writeable = False
            image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
            results = holistic.process(image)

            # Draw landmark annotation on the image.
            image.flags.writeable = True
            image = cv2.cvtColor(image, cv2.COLOR_RGB2BGR)

            mp_drawing.draw_landmarks(
                image,
                results.pose_landmarks,
                mp_holistic.POSE_CONNECTIONS,
                landmark_drawing_spec=mp_drawing_styles
                .get_default_pose_landmarks_style())

            mp_drawing.draw_landmarks(
                image,
                results.left_hand_landmarks,
                mp_holistic.HAND_CONNECTIONS,
                landmark_drawing_spec=mp_drawing_styles
                .get_default_hand_landmarks_style())

            mp_drawing.draw_landmarks(
                image,
                results.right_hand_landmarks,
                mp_holistic.HAND_CONNECTIONS,
                landmark_drawing_spec=mp_drawing_styles
                .get_default_hand_landmarks_style()
            )

            # printing landmarks
            for id, lm, in enumerate(results.pose_landmarks.landmark):
                print(id, lm)

            """ 
            for id, lm, in enumerate(results.left_hand_landmarks.landmark):
                print(id, lm) 
            """

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

    cap.release()
    print("released")


input_type = input("enter input type: video, image, webcam: ")
full_estimation(int(input_type), 0)
# cmd.exe "/K" C:\ProgramData\Anaconda3\Scripts\activate.bat C:\Users\jadep\.conda\envs\ECEN403
