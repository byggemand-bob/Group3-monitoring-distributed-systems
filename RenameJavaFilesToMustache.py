#Imports
import os
import shutil
from typing import List

#Used to store the soruce and dest file path
source = os.path.join(os.getcwd(), 'Monitor-Client', 'src' , 'main', 'java')
dest = [ os.path.join(os.getcwd(), 'Client', 'src', 'main' , 'resources', 'cstTemplates', ''),  os.path.join('.', 'Server', 'src', 'main' , 'resources', 'cstTemplates', '')]
autogen = os.path.join(os.getcwd(), 'Monitor-Client', 'target' , 'generated-sources', 'openapi', 'src', 'gen' , 'java', 'main')
autogen_folders = [os.path.join('org', 'openapitools', 'client' , 'api'), os.path.join('org', 'openapitools', 'client' , 'model')]
exclude_path = os.path.join(os.getcwd(), 'Monitor-Client', 'src', 'main', 'java', 'com', 'group3', 'monitorClient')

def check_include_list(path: str, include_list: List) -> str:
  if include_list == None:
    print('Include by default for path: ' + path)
    return None

  allow = False
  for dir in include_list:
    if path.find(dir) != -1:
      print('Path <' + path + '> allowed because it contains dir <' + dir + '>')
      allow = True
      return dir
      break
  
  return None

#Function that does:
  # Rename .java files to mustache files
  # Copy and move them into the desired target location
def CopyRenameMove(path: str, file_name: str, sourceL: str, dir_structure_rem_idx: int) -> None:
  #Rules out the files that does not end with .java
  if file_name.endswith('.java'):
    
    if path == exclude_path: 
      print('Excluded <' + file_name + '>...')
      return

    subpath = path[len(sourceL) + 1:]

    #Spilt the path into a list containing mutitple strings used to generate the folder structure
    folder_structure = subpath.split(os.path.sep)

    #Igonre the the first 3 string in the array
    del folder_structure[0:dir_structure_rem_idx]

    #Remove the files that starts with Test
    source_file = os.path.join(path, file_name)
    #Skip empty strings in array
    if len(source_file) == 0:
      return

    #Rename the files from .java to .mustache
    dest_file_name = file_name.replace('.java', '.mustache')

    #Create the dest path for both project (server, client) for each file.
    for dst in dest:
      dst_path = dst
      dst_path += os.path.sep.join(folder_structure) + os.path.sep if len(folder_structure) > 0 else ''
      
      #Adding file name to each of the dest path for both projects.
      dst_path = dst_path + dest_file_name

      #Varibles to store the real path for soruce and dest
      realpath_dest = os.path.realpath(dst_path)
      realpath_source = os.path.realpath(source_file)

      #Create folders if they do not exists
      os.makedirs(os.path.dirname(realpath_dest), exist_ok=True)

      #Copy files from soruce to dest
      shutil.copy(realpath_source, realpath_dest)
      print('Converted and copied <' + realpath_source + '> to <' + realpath_dest + '>...')

#For loop to get the root, dirs and files in the soruce target.
print('Starting converting Java classes to mustache templates...')
for root, dirs, files in os.walk(source, topdown=True):
  for name in files:
    CopyRenameMove(root, name, source, 3)

# Remember to get the necessary api and models from the autogen folders
for autogen_dir in autogen_folders:
  for root, dirs, files in os.walk(autogen, topdown=True):
    for name in files:
        #check_val: str = check_include_list(root, autogen_folders)
        #if not check_val:
          #continue
        CopyRenameMove(root, name, autogen, 2)

print('Finished converting Java classes to mustache templates...')