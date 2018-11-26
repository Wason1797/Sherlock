# Sherlock

## Project overview

The main goal of this project is to have a robot with the full power of the cloud and serverless architecture.

We aim to develop as many features as we can, so you can use this repo as an example for future builds and projects.

The features we aim to build into Sherlock:

  1. Real time GPS updates using the NEO 6M-0-001 module.
  1. Live video streaming to an rtmp server using the raspberry pi camera module.
  1. Remote control from the cloud using a real time database such as Firebase.

All of the above will be code using Kotlin and Android Things as our OS for the Rpi.

Some features of the remote control center used to operate Sherlock.

  1. Control Sherlock remotely using a 360 Xbox controller as our main input device.
  1. Build a web dashboard, capable of visualizing the live video streaming and GPS updates from anywhere in the world.
  1. Analyze images captured by the Rpi camera module with Google Vision API.

## Project Development

1. **The Architecture:** As a serverless architecture we aim to control Sherlock remotely without the need of added infrastructure, this is where firebase comes in. The control process goes as follows:
2. **Tools**: Android things, Python, Node.js, Kotlin, and JavaScript
3. **Development Process**:

    1. [Sherlock GPS](/SherlockGPS): This was our first prove of concept, it is an application that communicates with the GPS module, retrieves data from it and logs it to the console
    1. [Sherlock Video](/SherlockVideo): Then we proceeded to develop a way to stream data from the RPI to a RTMP server, here we used @pedroSG94 library for streaming on android.
    1. [Sherlock Bot](/SherlockBot): With the streaming done, we proceeded to move the robot, controlling it with firebase.
    1. [SherlockBot_Complete](/Sherlock_Bot_Complete): Is the complete project, all the code to make this prototype move, stream live video and update its GPS position on firebase
    1. [Firebase Control](/ControllerInput/RobotControll): Is The python script that runs in a pc, it allows the user to get the Xbox360 controller input, and make changes into firebase so the robot gets the commands.
    1. [Sherlock Page](/SherlockPage): Is the web dashboard that allows the user to watch the stream, view Sherlock's GPS position in a map, request an analysis of an image of the stream and log all the changes made to firebase
    1. [Screen Capture](/ScreenCapture): This REST service made on Django is just a byproduct of our struggle to download an image of the YouTube iframe in which we watch the stream. Our solution was to make a web service that runs on the machine that has the dashboard, and screenshot’s it (this only for development purposes, you could use another streaming service)
    1. [Sherlock Analysis](/SherlockAnalysis): Is the project that allows making image analysis with google vision.

## Network diagrams

TODO

## Wiring

TODO

## Recommendations

TODO

## Using and building your own Sherlock

TODO

## Repos we used as guidance

- [GPS driver](https://github.com/androidthings/contrib-drivers/tree/master/gps)
- [GPS example](https://github.com/androidthings/drivers-samples/tree/master/gps)
- [Xbox360 Controller](https://github.com/martinohanlon/XboxController)
- [Live Streaming](https://github.com/pedroSG94/rtmp-rtsp-stream-client-java) __This one in particular was incredibly helpfull__
- [Android Things Overview](https://github.com/Nilhcem/smarthome-androidthings)

- TODO (add more of the used repos)
