# ASLator

# Ray Cook | 525003962

This software was developed in a WAMP Stack using XAMPP. To build the app on your own device, please refer to the following steps:

1. Download and install a WAMP or LAMP stack of your choosing. (Note, documentation will only be provided for WAMP. Differences should be minimal in terms of implementation)
2. Extract the htdocs from this folder to your server stack.
3. Ensure that your ports are properly forwarded. (This project used 81 instead of 80 due to difficulties with my ISP)
4. Install python 3.8 with the following dependencies:  
	CV2: pip3 install opencv-python  
	mediapipe: pip3 install mediapipe (Only Python versions 3.8-3.10 supported)  
	sklearn: pip3 install scikit-learn  
	keras: pip3 install keras  
	tensorflow: pip3 install tensorflow (Ensure Windows Long File Path Support is enabled)  
	keras-preprocessing: pip3 install Keras-Preprocessing