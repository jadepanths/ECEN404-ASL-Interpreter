import cv2
import time
import mediapipe as mp

# Default setting
is_video = True

mpDraw = mp.solutions.drawing_utils
mpPose = mp.solutions.pose
pose = mpPose.Pose()

# cap = cv2.imread("pose/pose1.jpg")
pTime = 0

if is_video:
    cap = cv2.VideoCapture('pose/pose1.mp4')

    while True:
        success, image = cap.read()
        # mpPose only reads RGB (VideoCapture reads in as BGR)
        imgRGB = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)

        # testing the pose process
        results = pose.process(imgRGB)
        # print(results.pose_landmarks)
        
        if results.pose_landmarks:
            mpDraw.draw_landmarks(image, results.pose_landmarks, mpPose.POSE_CONNECTIONS)
            # extracting information for each key points
            for id, lm, in enumerate(results.pose_landmarks.landmark):
                h, w, c = image.shape
                print(id, lm)
                # change the landmark's from image ratio to actual value
                cx, cy = int(lm.x * w), int(lm.y * h)
                cv2.circle(image, (cx, cy), 10, (255, 0, 0), cv2.FILLED)

        cTime = time.time()
        fps = 1/(cTime-pTime)
        pTime = cTime

        # printing FPS
        cv2.putText(image, str(int(fps)), (70, 50), cv2.FONT_HERSHEY_PLAIN, 3,
                    (255, 0, 0), 3)

        cv2.imshow("Image", image)
        if cv2.waitKey(5) & 0xFF == ord('q'):
            break
