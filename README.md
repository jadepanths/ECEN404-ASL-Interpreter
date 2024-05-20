# ASL interpreter APP
### Jadepan Thedthong, Ray Cook
<br />
This project aims to connect individuals with disabilities who rely on American 
Sign Language (ASL) for communication and those who are unfamiliar with 
sign language, ultimately bridging the communication gap between them.

This app includes:

- an account system to keep track of the users' progress and attributes
- lesson plans to teach our users some basic ASL communications 
- basic word-level translation from a video 

## Machine Learning Subsystem (Jadepan Thedthong)
- The hand-estimation-old directory is the first tracking model that is being used with poor accuracy. Thus, we decided to use open-source like openPose or mediaPipe. We resorted
to mediaPipe due to its better documentation.

- Key-points-estimation is when we first learned how to use and 
implement mediaPipe by testing with various input and output types.

- Sign-level directory includes all the programs used to train the translator model.
  - The wlasl2000processing subdirectory is the first iteration of the model training
  where we found an open-source dataset with 2000 words and around 10-15 videos per word.
  The dataset was unorganized, so the implemented programs were there to process all the
  dataset. 
  - The Lists subdirectory is used for labeling used for training the model
  and for model prediction, so the client gets the readable text response 
  instead of the index of the predictions. 
  - augmentation.py is for augment and increase the size of our dataset when we
  decided to construct our own dataset. To speed the data processing, we added
  multithreading to the algorithm. The augmentation includes:
    - horizontal flipping - helping our model translating both left and right hands.
    - randomly rotating - helping our model generalizing with different camera angles.
    - randomly adjust the speed - helping our model generalizing with different signing speed.
  - drawmodel.py and heatmap.py are used for drawing the visual overview of our models.
  - keypoint_extraction.py is used for data processing. It extracts all the keypoints in 
  a given directory and store them in a .npy for each video for faster reading speed and minimal space.
  The program is implemented with mediaPipe, we also run a basic classification to select only
  essential keypoints.
  - lstm_asl.py is the heart of our machine learning model. This program construct the model architecture
  and train the model with the .npy files extracted with our keypoint_extraction.py This program includes
  checkpoint and early termination to prevent losing progress or over fitting when validation loss has not
  improved in 10 consecutively epochs.
  - lstm_asl_test.py and lstm_asl_test2.py are the testing and validating program. Our model tested with
  50 newly recorded samples per word, so we can test how well our model generalizing with new unseen dataset.
  The test2 program is the improved version of the first one by adding more metrics like precision, recall,
  and F1 score using scikit-learn.
  - model.py is the backend of our server. A script will be called to run this program with 2 input parameters:
  input video, output directory. The program will extract the keypoints from the given video and pass those
  information into our machine-learning model. The output translation will be saved as a txt file with the
  same name as the input video in the desired directory.

Note: All the dataset used in the process will be given as a google-drive link: [DATASET](https://drive.google.com/drive/folders/1_5bQydfRTnOee_teYRaSk1NvtSBo646O?usp=sharing)



