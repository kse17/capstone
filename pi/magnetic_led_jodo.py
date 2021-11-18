import smbus
import RPi.GPIO as GPIO
import time
import communication

magnetic = 4
led = 18
GPIO.setmode(GPIO.BCM)
GPIO.setup(led,GPIO.OUT)
GPIO.setup(magnetic, GPIO.IN, pull_up_down = GPIO.PUD_UP)
address = 0x48
AIN0 = 0x40

bus = smbus.SMBus(1)
time.sleep(0.5)


def sendValue(n):
   return communication.sendOpenValue(n)

try:
   while True:
      bus.write_byte(address, AIN0)
      value = bus.read_byte(address)

      time.sleep(0.1)
      if GPIO.input(magnetic) is 0:
         print("Door is close")
         sendValue("0")
         GPIO.output(led,GPIO.LOW)
      if GPIO.input(magnetic) is 1:
         sendValue("1")
         print("value: {0}".format(value))
         if value > 200:
            GPIO.output(led,GPIO.HIGH)
         else:
            GPIO.output(led,GPIO.LOW)

except KeyboardInterrupt:
 pass
GPIO.cleanup()


