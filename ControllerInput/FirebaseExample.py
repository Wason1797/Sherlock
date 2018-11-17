import pyrebase
import json

with open('authconfig.json') as handle:
    config = json.loads(handle.read())

firebase = pyrebase.initialize_app(config)
db = firebase.database()
users = db.child("sherlock").get()
print(users.val())