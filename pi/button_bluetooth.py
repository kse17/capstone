import time
import button
import bluetoothctl

while True:
   bp = button.btnPressed()
   if (bp == 0):
      print('Button Pressed: ON')
      bluetoothctl.start()
   else:
      print('Button Pressed: OFF')
   time.sleep(0.5)