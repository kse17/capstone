#!/usr/bin/env python
# -*- coding: utf-8 -*-
import pyrebase

import time
import board
import busio
from digitalio import DigitalInOut, Direction
import adafruit_fingerprint


import RPi.GPIO as GPIO
import time
# GPIO setup
GPIO.setmode(GPIO.BCM)
GPIO.setup(26, GPIO.OUT)
GPIO.setup(19, GPIO.IN)

import serial
uart = serial.Serial("/dev/ttyUSB0", baudrate=57600, timeout=1)
finger = adafruit_fingerprint.Adafruit_Fingerprint(uart)

firebaseConfig={
        "apiKey": "AIzaSyBZiOhs9fF1DnzXQCXfVUodXq_v1J9zkRM",
        "authDomain": "smart-carrier-3cbba.firebaseapp.com",
        "databaseURL": "https://smart-carrier-3cbba-default-rtdb.firebaseio.com",
        "projectId": "smart-carrier-3cbba",
        "storageBucket": "smart-carrier-3cbba.appspot.com",
        "messagingSenderId": "568751095343",
        "appId": "1:568751095343:android:86a8d550a95bd784eed0da"
       }


firebase=pyrebase.initialize_app(firebaseConfig)
db=firebase.database()

##########################################################################

def getMAC(interface='eth0'):
     # Return the MAC address of the specified interface
   try:
      str = open('/sys/class/net/%s/address' %interface).read()
      str = str.upper()
   except:
      str = "00:00:00:00:00:00"
   str = "DC:A6:32:F8:A3:AD"
   return str[0:17]

###################################################################################

def get_fingerprint():
    """Get a finger print image, template it, and see if it matches!"""
    print("Waiting for image...")
    while finger.get_image()!=adafruit_fingerprint.OK:
        pass
    print("Templating...")
    if finger.image_2_tz(1) != adafruit_fingerprint.OK:
        return False
    print("Searching...")
    if finger.finger_search() != adafruit_fingerprint.OK:
        return False
    return True

###################################################################################

def get_fingerprint_detail():
    """Get a finger print image, template it, and see if it matches!
    This time, print out each error instead of just returning on failure"""
    print("Getting image...", end="", flush=True)
    i = finger.get_image()
    if i == adafruit_fingerprint.OK:
        print("Image taken")
    else:
        if i == adafruit_fingerprint.NOFINGER:
            print("No finger detected")
        elif i == adafruit_fingerprint.IMAGEFAIL:
            print("Imaging error")
        else:
            print("Other error")
        return False

    print("Templating...", end="", flush=True)
    i = finger.image_2_tz(1)
    if i == adafruit_fingerprint.OK:
        print("Templated")
    else:
        if i == adafruit_fingerprint.IMAGEMESS:
            print("Image too messy")
        elif i == adafruit_fingerprint.FEATUREFAIL:
            print("Could not identify features")
        elif i == adafruit_fingerprint.INVALIDIMAGE:
            print("Image invalid")
        else:
            print("Other error")
        return False

    print("Searching...", end="", flush=True)
    i = finger.finger_fast_search()
    # pylint: disable=no-else-return
    # This block needs to be refactored when it can be tested.
    if i == adafruit_fingerprint.OK:
        print("Found fingerprint!")
        return True
    else:
        if i == adafruit_fingerprint.NOTFOUND:
            print("No match found")
        else:
            print("Other error")
        return False
            
###################################################################################

def AddOperation(location):
    
    for fingerimg in range(1, 3):
        if fingerimg == 1:
            print("Place finger on sensor...", end="", flush=True)
        else:
            print("Place same finger again...", end="", flush=True)

        while True:
            i = finger.get_image()
            if i == adafruit_fingerprint.OK:
                print("Image taken")
                break
            if i == adafruit_fingerprint.NOFINGER:
                print(".", end="", flush=True)
            elif i == adafruit_fingerprint.IMAGEFAIL:
                print("Imaging error")
                db.child('carriers').child(getMAC()).child('carrierOperation').update({'addOperation':"0"})
                return False
            else:
                print("Other error")
                db.child('carriers').child(getMAC()).child('carrierOperation').update({'addOperation':"0"})
                return False

        print("Templating...", end="", flush=True)
        i = finger.image_2_tz(fingerimg)
        if i == adafruit_fingerprint.OK:
            print("Templated")
        else:
            if i == adafruit_fingerprint.IMAGEMESS:
                print("Image too messy")
            elif i == adafruit_fingerprint.FEATUREFAIL:
                print("Could not identify features")
            elif i == adafruit_fingerprint.INVALIDIMAGE:
                print("Image invalid")
            else:
                print("Other error")
                db.child('carriers').child(getMAC()).child('carrierOperation').update({'addOperation':"0"})
            return False

        if fingerimg == 1:
            print("Remove finger")
            time.sleep(1)
            while i != adafruit_fingerprint.NOFINGER:
                i = finger.get_image()

    print("Creating model...", end="", flush=True)
    i = finger.create_model()
    if i == adafruit_fingerprint.OK:
        print("Created")
    else:
        if i == adafruit_fingerprint.ENROLLMISMATCH:
            print("Prints did not match")
        else:
            print("Other error")
            db.child('carriers').child(getMAC()).child('CarrierOperation').update({'addOperation':"0"})
        return False

    print("Storing model #%d..." % location, end="", flush=True)
    i = finger.store_model(location)
    if i == adafruit_fingerprint.OK:
        db.child('carriers').child(getMAC()).update({'fpNum':str(location)})
        db.child('carriers').child(getMAC()).child('carrierOperation').update({'addOperation':"0"})
        db.child('carriers').child(getMAC()).update({'fpValue':"1"})
        print("Stored")
    else:
        if i == adafruit_fingerprint.BADLOCATION:
            print("Bad storage location")
        elif i == adafruit_fingerprint.FLASHERR:
            print("Flash storage error")
        else:
            print("Other error")
            db.child('carriers').child(getMAC()).child('carrierOperation').update({'addOperation':"0"})
        return False

    
    return True
    
###################################################################################

def DeleteOperation():
    
    if get_fingerprint():
        if finger.delete_model(finger.finger_id) == adafruit_fingerprint.OK:
            db.child('carriers').child(getMAC()).child('carrierOperation').update({'deleteOperation':"0"})
            db.child('carriers').child(getMAC()).update({'fpValue':'0'})
            print("Deleted!")
    else:
        db.child('carriers').child(getMAC()).child('carrierOperation').update({'deleteOperation':"0"})
        print("Failed to delete")

###################################################################################
        
def FindOperation():
    if get_fingerprint():
        db.child('carriers').child(getMAC()).child('carrierOperation').update({'findOperation':"0"})
        db.child('carriers').child(getMAC()).update({'fpLocation':(finger.finger_id)})
        db.child('carriers').child(getMAC()).update({'fpConfidence':finger.confidence})
        print("Detected #", (finger.finger_id), "with confidence", finger.confidence)
    else:
        db.child('carriers').child(getMAC()).child('carrierOperation').update({'findOperation':'0'})
        print("Finger not found")

###################################################################################    
def AddFPLocation():
    """Use input() to get a valid number from 1 to 127. Retry till success!"""
    try:
        FPNum = db.child('carriers').child(getMAC()).child('fpNum').get().val()
        intFPNum = int(FPNum) + 1
          
    except ValueError:
        pass
   
    return intFPNum
    
####################################################################################   
def FP_Location():
    """Use input() to get a valid number from 1 to 127. Retry till success!"""
    i = 0
    while (i > 127) or (i < 1):
        try:
            i = int(input("Enter ID # from 1-127: "))
        except ValueError:
            pass
    return i

################################################################################### 

while True:
    AddOperationValue = db.child('carriers').child(getMAC()).child("carrierOperation").child('addOperation').get().val()
    DeleteOperationValue = db.child('carriers').child(getMAC()).child("carrierOperation").child('deleteOperation').get().val()
    FindOperationValue = db.child('carriers').child(getMAC()).child("carrierOperation").child('findOperation').get().val()
    if AddOperationValue == "1" :
        AddOperation(AddFPLocation())
    if DeleteOperationValue == "1" :
        DeleteOperation()
    if FindOperationValue == "1" :
        FindOperation()
    
    


