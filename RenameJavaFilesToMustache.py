#Imports
import os
import shutil
from typing import List

#Used to store the soruce and dest file path
source = './Monitor-Client/src/main/java'
dest = ['./Client/src/main/resources/cstTemplates/', './Server/src/main/resources/cstTemplates/']
autogen = './Monitor-Client/target/generated-sources/openapi/src/gen/java/main'
autogen_folders = ['\\org\\openapitools\\client\\api', '\\org\\openapitools\\client\\model']
exclude_path = './Monitor-Client/src/main/java\\com\\group3\\monitorClient'

def check_include_list(path: str, include_list: List) -> bool:
  if include_list == None:
    print('Include by default for path: ' + path)
    return True

  allow = False
  for dir in include_list:
    if path.find(dir) != -1:
      print('Path <' + path + '> allowed because it contains dir <' + dir + '>')
      allow = True
      break
  
  return allow

#Function that does:
  # Rename .java files to mustache files
  # Copy and move them into the desired target location
def CopyRenameMove(path: str, file_name: str, include_dir_list: List = None) -> None:
  #Rules out the files that does not end with .java
  if file_name.endswith('.java'):
    
    if not check_include_list(path, include_dir_list): return
    if path == exclude_path: 
      print('Excluded <' + file_name + '>...')
      return

    subpath = path[len(source) + 1:]

    #Spilt the path into a list containing mutitple strings used to generate the folder structure
    folder_structure = subpath.split('\\')

    #Igonre the the first 3 string in the array
    del folder_structure[0:3]

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
      dst_path += '\\'.join(folder_structure) + '\\' if len(folder_structure) > 0 else ''
      
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
    CopyRenameMove(root, name)

# Remember to get the necessary api and models from the autogen folders
for autogen_dir in autogen_folders:
  for root, dirs, files in os.walk(autogen, topdown=True):
    for name in files:
        CopyRenameMove(root, name, autogen_folders)

print('Finished converting Java classes to mustache templates...')