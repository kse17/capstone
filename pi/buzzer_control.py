import time
import buzzer

while True:
      # Buzzer Setting State
      bs = buzzer.recvBuzSet()
      print("Buzzer set state: ", bs)
      if (bs == '1'):
         print('Buzzer Set: ON')
         # Buzzer Value
         bv = buzzer.recvBuzValue()
         if (bv == '1'):
            print('Buzzer Value: ON')
            buzzer.buz()
         else:
            print('Buzzer Value: OFF')
      if (bs == "0"):
         print('Buzzer Set: OFF')