#-+- coding: utf-8 -*-

import RPi.GPIO as GPIO
import time

GPIO.setmode(GPIO.BOARD)

GPIO.setup(11, GPIO.IN, pull_up_down=GPIO.PUD_UP)

def btnPressed():
	input_state = GPIO.input(11)
	print("pressed state: ", input_state)
	return input_state
