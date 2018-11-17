import Xbox360 as x360
import pyrebase
import json


if __name__ == '__main__':

  with open('..\\auth_files\\authconfig.json') as handle:
    config = json.loads(handle.read())
  firebase = pyrebase.initialize_app(config)
  db = firebase.database()

  

  def controll_remote_db(parameter, command):
    db.child("sherlock").update({parameter: command})
    
  def controlCallBack(xboxControlId, value):        
    pass

  def button_a_pressed(aValue):
    if aValue == 1:
      controll_remote_db("camera_state", "on")
      print("A pressed")
      

  def button_b_pressed(bValue):
    if bValue == 1:
      controll_remote_db("camera_state", "off")
      print("B pressed")     

  def leftThumbX(xValue):

    if int(xValue) == 100:
        print("left")
        controll_remote_db("movement_command", "right")
    elif int(xValue) == -100:
        print("right")
        controll_remote_db("movement_command", "left")
    elif int(xValue) == 0:
        print("still")
        controll_remote_db("movement_command", "still")

  def leftThumbY(yValue):
      
    if int(yValue) == 100:
        print("forward")
        controll_remote_db("movement_command", "forward")
    elif int(yValue) == -100:
        print("backwards")
        controll_remote_db("movement_command", "backwards")
    elif int(yValue) == 0:
        print("still")
        controll_remote_db("movement_command", "still")
      # print ("LY {}".format(yValue))

  # setup xbox controller, set out the deadzone and scale, also invert the Y Axis (for some reason in Pygame negative is up - wierd!
  xboxCont = x360.XboxController(
    controlCallBack, deadzone=30, scale=100, invertYAxis=True)

  # setup the left thumb (X & Y) callbacks
  xboxCont.setupControlCallback(xboxCont.XboxControls.LTHUMBX, leftThumbX)
  xboxCont.setupControlCallback(xboxCont.XboxControls.LTHUMBY, leftThumbY)
  xboxCont.setupControlCallback(xboxCont.XboxControls.A, button_a_pressed)
  xboxCont.setupControlCallback(xboxCont.XboxControls.B, button_b_pressed)

  try:
      # start the controller
    xboxCont.start()
    print("xbox controller running")
    xboxCont.run()

  # Ctrl C
  except KeyboardInterrupt:
    print("User cancelled")
    xboxCont.stop()

  # error
  except:
    print("Unexpected error:", sys.exc_info()[0])
    raise

  finally:
    # stop the controller
    xboxCont.stop()
