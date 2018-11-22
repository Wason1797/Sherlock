# Sherlock

## Project overview

The main goal of this project is to have a robot with the full power of the cloud and serverless arquitecture.

We aim to built in as many features as we can, so you can use this repo as an example for future builds and projects.

The features we aim to build into Sherlock:

  1. Real time GPS updates using the NEO 6M-0-001 module.
  1. Live video streaming to a rtmp server using the raspberry pi camera module.
  1. Remote controll from the cloud using a real time database such as Firebase.

All of the avove will be code using Kotlin and Android Things as our os for the rpi.

Some features of the remote controll center used to operate Sherlock.

  1. Control Sherlock remotely using a 360 Xbox controller as our main input device.
  1. Built in a web dashboard, capable of visualizing the live video streaming and gps updates from anywhere in the world.
  1. Analize images captured by the rpi camera module with Google Vision API.

## Project Developement

1. **The Arquitecture:** As a serverless arquitecture we aim to controll Sherlock remotely without the need of added infraestructure, this is where firebase comes in. The controll proces goes as follows:
1. **Tools**: Android things, Python, Node.js, Kotlin, JavaScript
1. **Developement Process**: TODO

## Network diagrams

TODO

## Wiring

TODO

## Recomendations

TODO

## Using and building your own Sherlock

TODO

## Used Repos

- [GPS driver](https://github.com/androidthings/contrib-drivers/tree/master/gps)
- [GPS example](https://github.com/androidthings/drivers-samples/tree/master/gps)

-[Xbox360 Controller](https://github.com/martinohanlon/XboxController)

- TODO (add more of the used repos)