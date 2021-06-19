#-*- coding:utf-8 -*-

import time
import buzzer
import test
import button
import bluetooth


while (True):
	# Buzzer Setting State
	bs = buzzer.recvBuzSet()
	if (bs == True):
		print('buzzer Set: True')
		#buzzer.buz()
	else:
		print('buzzer Set: False')

	# Button Pressed
	bp = button.btnPressed()
	if (bp == False):
		print('button Pressed: False')
		bluetooth.start()
	else:
		print('button Pressed: True')
	time.sleep(0.5)

