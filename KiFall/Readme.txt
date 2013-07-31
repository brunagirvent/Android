
KiFall: Fall Detection System Using an Android Phone and the Kinect Sensor

*The app is in Testing and Debugging Phase. Currently working on the interface and app design. 

The fall detection system consists on:
1. The Android detect if a fall has taken place. The detection algorithm is based on the acceleration and orientation sensor values
2. If the fall is detected, the Android connects to the Kinect through a TCP server.
3. The Kinect verifies if the fall has taken place. The verification consists on track some joints (head, righHand and lefHand) and calculates the distance from the floor. 
4. If the fall is verified, the Kinect takes a picture. 
5. If the fall is verified, the server sends the verification message and the picture to the Android. 
6.The Android sends an MMS or SMS to the emergency contact containing the time when the fall took place, the location, a link to Google Maps and the picture of the person laying down.