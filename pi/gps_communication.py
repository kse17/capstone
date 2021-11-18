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

mac = hex(get_mac())

def getMAC(interface='eth0'):
     # Return the MAC address of the specified interface
   try:
      str = open('/sys/class/net/%s/address' %interface).read()
   except:
      str = "00:00:00:00:00:00"
   return str[0:17]

def sendGPSValue(lat, lon):
    db.child('carriers').child(getMAC()).update({"Carrier_lat":lat})
    db.child('carriers').child(getMAC()).update({"Carrier_lon":lon})
    return
