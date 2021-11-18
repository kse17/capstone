import pyrebase

from uuid import getnode as get_mac


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


def getMAC(interface='eth0'):
     # Return the MAC address of the specified interface
   try:
      str = open('/sys/class/net/%s/address' %interface).read()
      str = str.upper()
   except:
      str = "00:00:00:00:00:00"
   str = "DC:A6:32:F8:A3:AD"
   return str[0:17]
   
# Service State
def recvServiceState(service):
   if service == 'sbuzzer':
      ref = db.child('carriers').child(getMAC()).child('carrierSetting').child('sbuzzer')
   elif service == 'sled':
      ref = db.child('carriers').child(getMAC()).child('carrierSetting').child('sled')
   elif service == 'slock':
      ref = db.child('carriers').child(getMAC()).child('carrierSetting').child('slock')
   return ref.get().val()

# On/Off Value
def recvValue(service):
   if service == 'buzzer':
      ref = db.child('carriers').child(getMAC()).child('carrierControl').child('buzzer')
   elif service == 'lock':
      ref = db.child('carriers').child(getMAC()).child('carrierControl').child('lock')
   return ref.get().val()

def isExist():
   ref = db.child('carriers').child(getMAC())
   if ref.get().val() == None:
      return False
   else:
      return True
      
def sendBuzValue(n):
   db.child('carriers').child(getMAC()).child('carrierControl').update({"buzzer":n})
   
def sendOpenValue(n):
   db.child('carriers').child(getMAC()).child('carrierControl').update({"open":n})

def sendValue(lat, lon):
   db.child('carriers').child(getMAC()).update({"Carrier_lat":lat})
   db.child('carriers').child(getMAC()).update({"Carrier_lon":lon})
   return
