import firebase_admin
from firebase_admin import credentials
from firebase_admin import db

from uuid import getnode as get_mac

mac = hex(get_mac())

# Certificate firebase database
db_url = 'https://smart-carrier-3cbba-default-rtdb.firebaseio.com'
if not firebase_admin._apps:
	cred = credentials.Certificate('./smart-carrier-3cbba-firebase-adminsdk-agw57-40c0e0f432.json')
	default_app = firebase_admin.initialize_app(cred, {'databaseURL' : db_url})

def recvServiceState(service):
	# buzzer service
	if service == 'buzzer':
		ref = db.reference('carriers/0001/buzzer')
		return ref.get()
