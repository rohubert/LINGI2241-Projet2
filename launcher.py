import sys
from threading import Thread
import subprocess
import time

class Client(Thread):

	"""This thread is charged to launch the knockkncokClient java program"""

	def __init__(self, id, ip, port):
		Thread.__init__(self)
		self.id = id
		self.ip = ip
		self.port = port

	def run(self):
		try:
			running_java = subprocess.run("java KnockKnockClient {} {} {}".format(self.ip, self.port, self.id),shell=True, stdout=subprocess.PIPE)
		except OSError as e:
			flag = "os error"
			print("Execution failed:", e, file=stderr)
			
		

# Program
# Create 10 threads with id
ipValue = sys.argv[1]
portValue = sys.argv[2]
threads = []
for i in range(9):
	threads.append(Client(i, ipValue, portValue))
	threads[i].start()

# Wait for the threads to end
for i in range(len(threads)):
	threads[i].join()