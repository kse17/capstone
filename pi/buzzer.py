#-+- coding: utf-8 -*-

import RPi.GPIO as GPIO 
import time 
import communication

GPIO.setmode(GPIO.BCM) 
gpio_pin = 12 
scale = [ 261, 294, 329, 349, 392, 440, 493, 523 ] 

GPIO.setup(gpio_pin, GPIO.OUT) 
list = [4, 4, 4, 4, 4] 

def buz():
   try:
      while True:
         p = GPIO.PWM(gpio_pin, 100) 
         p.start(100) 
         p.ChangeDutyCycle(90) 
         while True:
            for i in range(5) :
               p.ChangeFrequency(scale[list[i]])
               if i == 6: 
                  time.sleep(1) 
               else : 
                  time.sleep(0.5) 
         #p.stop()
            if recvBuzValue() == '0':
               GPIO.cleanup()
               break
    
   except KeyboardInterrupt:
    pass
   GPIO.cleanup()
   
def recvBuzSet():
   return communication.recvServiceState('sbuzzer')

def recvBuzValue():
   return communication.recvValue('buzzer')

