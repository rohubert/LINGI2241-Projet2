import sys
import json
from os import listdir
from os.path import isfile, join

onlyfiles = [f for f in listdir('.') if isfile(join('.', f))]
json_dict = dict()
for filename in onlyfiles:
	id_data = dict()
	if filename.startswith('result'):
		file = open(filename, 'r')
		line = file.readline()
		while(line):
			#Check if the line has the form "ID:x, p:y"
			current_id = ''
			file_content = dict()
			if line.startswith('ID'):
				idline = line.split(', ')
				#Now: idline = {"ID:x","p:y"}
				# id_data['ID:'+str(current_id)] = file_content
				current_id = idline[0].replace('ID:','')
				file_content['ID'] = current_id
				file_content['Power Mean'] = idline[1].replace('p:','').replace('\n','')
				line = file.readline()
			count = 4
			while(count > 0):
				#Check if the line has the form "Response time mean: x, min:y, max:z"
				if line.startswith('Response'):
					idline = line.split(', ')
					#Now: idline = {"Response time mean: x","min:y","max:z"}
					file_content['Response Time Mean'] = idline[0].replace('Response time mean: ','')
					file_content['Responde Time Min'] = idline[1].replace('min:','')
					file_content['Responde Time Max'] = idline[2].replace('max:','').replace('\n','')
					count-=1
					line = file.readline()
				#Check if the line has the form "Request time mean: x, min:y, max:z"
				elif line.startswith('Request'):
					idline = line.split(', ')
					#Now: idline = {"Request time mean: x","min:y","max:z"}
					file_content['Request Time Mean'] = idline[0].replace('Request time mean: ','')
					file_content['Request Time Min'] = idline[1].replace('min:','')
					file_content['Request Time Max'] = idline[2].replace('max:','').replace('\n','')
					count-=1
					line = file.readline()
				#Check if the line has the form "Server computation time mean: x, min:y, max:z"
				elif line.startswith('Server'):
					idline = line.split(', ')
					#Now: idline = {"Server computation time mean: x","min:y","max:z"}
					file_content['Server computation Time Mean'] = idline[0].replace('Server computation time mean: ','')
					file_content['Server computation Time Min'] = idline[1].replace('min:','')
					file_content['Server computation Time Max'] = idline[2].replace('max:','').replace('\n','')
					count-=1
					line = file.readline()
				#Check if the line has the form "Network time: x"
				elif line.startswith('Network'):
					file_content['Network Time Mean'] = line.replace('Network time: ','').replace('\n','')
					count-=1
					line = file.readline()
				else:
					count -= 1
			id_data[current_id] = file_content
			# print(id_data)
		#When out the while loop don"t forget to close the file
		file.close()

		#Then add this file data to the general dict
		json_dict[filename] = id_data

#Now that we have made the dict containing the datas of all file, we can create the json file
#json_string = json.dump(json_dict)

#Get the file name for the new file to write
# filter = "JSON File (*.json)|*.json|All Files (*.*)|*.*||"
# filename = rs.SaveFileName("Save JSON file as", filter)

filename = "values_sheet.json"
# If the file name exists, write a JSON string into the file.
if filename:
    # Writing JSON data
    with open(filename, 'w') as f:
        json.dump(json_dict, f)