var map, infoWindow, gps, firebase;

//Connection var to firebase
var config = {
    apiKey: "<API_KEY>",
    authDomain: "<PROJECT_ID>.firebaseapp.com",
    databaseURL: "https://<DATABASE_NAME>.firebaseio.com",
    projectId: "<PROJECT_ID>",
    storageBucket: "<BUCKET>.appspot.com",
    messagingSenderId: "<SENDER_ID>",
};

function charge() {
    log();
}
// Save a change in database as log
// Select all your database field you need to watch as log in your app
function log() {
    //change /database/field, to your API URI
    var starCountRef = firebase.database().ref('/database/field');
    starCountRef.on('value', function (snapshot) {
        document.getElementById("log").innerHTML = document.getElementById("log").innerHTML + '<br>' + "Camera: " + snapshot.val();
    });
}
//Change GPS status
function gps() {
    //Only change database/gps_field with your API URI to get GPS
    var starCountRef = firebase.database().ref('database/gps_field');
    starCountRef.on('value', function (snapshot) {
        gps = snapshot.val().split(';');
        var botPosition = new google.maps.LatLng(gps[0], gps[1]);
        //Creates the map into the div map centering into the bot position and zoom of 20
        map = new google.maps.Map(document.getElementById('map'), {
            center: botPosition,
            zoom: 20
        });
        //Creates the red market locator for gps Change project name
        marker = new google.maps.Marker({
            position: botPosition,
            title: "Project name"
        });
        marker.setMap(map);
    });
}

function handleLocationError(browserHasGeolocation, infoWindow, pos) {
    infoWindow.setPosition(pos);
    infoWindow.setContent(browserHasGeolocation ?
        'Error: The Geolocation service failed.' :
        'Error: Your browser doesn\'t support geolocation.');
    infoWindow.open(map);
}