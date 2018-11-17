import pyrebase
import json

with open('auth_files\\authconfig.json') as handle:
    config = json.loads(handle.read())

firebase = pyrebase.initialize_app(config)
db = firebase.database()
users = db.child("sherlock").get()
print(users.val())

# stream test

def stream_handler(message):
    print(message["event"]) # put
    print(message["path"]) # /-K7yGTTEp7O549EzTYtI
    print(message["data"]) # {'title': 'Pyrebase', "body": "etc..."}

# my_stream = db.child("sherlock").stream(stream_handler)

db.child("sherlock").update({"movement_comand": "backwards"})
