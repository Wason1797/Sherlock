function charge() {
    //Change config var with config that firebase provides you
    var config = {
        apiKey: "<API_KEY>",
        authDomain: "<PROJECT_ID>.firebaseapp.com",
        databaseURL: "https://<DATABASE_NAME>.firebaseio.com",
        projectId: "<PROJECT_ID>",
        storageBucket: "<BUCKET>.appspot.com",
        messagingSenderId: "<SENDER_ID>",
      };
      firebase.initializeApp(config);
    log();
    gps();
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
        document.getElementById("gps").innerHTML = snapshot.val();
    });
}