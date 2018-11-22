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
1. **Tools**: Android things, Python, Node.js, Kotlin, and JavaScript
1. **Development Process**: TODO

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
