import cv2
import time
import mediapipe as mp
import matplotlib.pyplot as plt
import numpy as np
import os

# nHD resolution (640*360 16:9)

# Initialize mediapipe pose class
"""
mp_pose = mp.solutions.pose
pose_image = mp_pose.Pose(static_image_mode=True, min_detection_confidence=0.5)
pose_video = mp_pose.Pose(static_image_mode=False, min_detection_confidence=0.7,
                          min_tracking_confidence=0.7)
"""
mp_holistic = mp.solutions.holistic
holistic_image = mp_holistic.Holistic(static_image_mode=True, min_detection_confidence=0.5)
holistic_video = mp_holistic.Holistic(static_image_mode=False, min_detection_confidence=0.5,
                                      min_tracking_confidence=0.5, smooth_landmarks=True,
                                      model_complexity=1)

mp_drawing = mp.solutions.drawing_utils
mp_drawing_styles = mp.solutions.drawing_styles


# Testing with image
def image_keypoints(image_pose, holistic, draw=False, display=False, landmarks=False):
    original_image = image_pose.copy()
    image_rgb = cv2.cvtColor(image_pose, cv2.COLOR_BGR2RGB)
    results = holistic.process(image_rgb)

    if draw:
        if results.pose_landmarks:
            mp_drawing.draw_landmarks(image=original_image, landmark_list=results.pose_landmarks,
                                      connections=mp_holistic.POSE_CONNECTIONS,
                                      landmark_drawing_spec=mp_drawing.DrawingSpec(color=(255, 255, 255),
                                                                                   thickness=3, circle_radius=3),
                                      connection_drawing_spec=mp_drawing.DrawingSpec(color=(49, 125, 237),
                                                                                     thickness=2, circle_radius=2))
        if results.left_hand_landmarks:
            mp_drawing.draw_landmarks(image=original_image, landmark_list=results.left_hand_landmarks,
                                      connections=mp_holistic.HAND_CONNECTIONS,
                                      landmark_drawing_spec=mp_drawing_styles
                                      .get_default_hand_landmarks_style())

        if results.right_hand_landmarks:
            mp_drawing.draw_landmarks(image=original_image, landmark_list=results.right_hand_landmarks,
                                      connections=mp_holistic.HAND_CONNECTIONS,
                                      landmark_drawing_spec=mp_drawing_styles
                                      .get_default_hand_landmarks_style())

    if landmarks:
        """=============== POSE (including Face) ==============="""
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

        print("extraction pose", extracted_pose)
        print("shape:", len(extracted_pose))

        print("extraction lh", lh)
        print("size after flatten", len(lh))

        print("extraction rh", rh)
        print("size after flatten", len(rh))

    if display:
        plt.figure(figsize=[22, 22])
        plt.subplot(121)
        plt.imshow(image_pose[:, :, ::-1])
        plt.title("Input Image")
        plt.axis('off')
        plt.subplot(122)
        plt.imshow(original_image[:, :, ::-1])
        plt.title("Pose detected Image")
        plt.axis('off')

        # show for pycharm
        plt.show()
    else:
        return original_image, results


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


# Extract keypoint 30 frames per keypoint
def webcam_keypoint(holistic, draw=False, dislay=False, landmarks=False):
    no_sequences = 30
    sequence_length = 30
    output_dir = "test/webcam_extraction"
    action_dir = "lists/test.txt"

    # Read the text file containing the list of glosses
    with open(action_dir, 'r') as f:
        gloss_list = [line.strip() for line in f.readlines()]

    # Create the "signs" directory and subdirectories for each gloss
    os.makedirs(output_dir, exist_ok=True)
    for gloss in gloss_list:
        os.makedirs(os.path.join(output_dir, gloss), exist_ok=True)

    for action in gloss_list:
        print(action)



    """
    cap = cv2.VideoCapture(0)
    print("Test")
    while cap.isOpened():
        success, image = cap.read()

        if not success:
            print("Ignoring empty camera frame.")
            continue
    """


def video_extraction():
    input_dir = "raw_data/test_raw10_4"
    output_dir = "raw_data/test_extracted10_4"

    # Create the output directory if not exist
    if not os.path.exists(output_dir):
        os.makedirs(output_dir)

    # loop over subdirectories in input_dir
    for subdir in os.listdir(input_dir):
        # Create the corresponding output directory
        print(subdir)
        output_subdir = os.path.join(output_dir, subdir)
        if not os.path.exists(output_subdir):
            os.makedirs(output_subdir)

        # loop through the video files in the subdirectory
        for video_file in os.listdir(os.path.join(input_dir, subdir)):
            # print(video_file)
            vid_path = input_dir + "/" + subdir + "/" + video_file
            print(vid_path)
            cap = cv2.VideoCapture(vid_path)
            key_point = vid_keypoint(cap, holistic_video, draw=False, display=False, landmarks=True)
            if key_point is not None:
                output_file = os.path.splitext(video_file)[0] + '.npy'
                np.save(os.path.join(output_subdir, output_file), key_point)


if __name__ == "__main__":
    print("main")
    video_extraction()
    # webcam_keypoint(holistic_video, draw=False, display=False, landmarks=False)
