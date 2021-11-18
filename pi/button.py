#-+- coding: utf-8 -*-

import RPi.GPIO as GPIO
import time
import button

GPIO.setmode(GPIO.BCM)

GPIO.setup(11, GPIO.IN, pull_up_down=GPIO.PUD_UP)

def btnPressed():
   input_state = GPIO.input(11)     # 눌리면 0
   print("pressed state: ", input_state)
   return input_state
   

