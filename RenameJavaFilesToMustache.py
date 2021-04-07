#Imports
import os
import shutil

#Used to store the soruce and dest file path
source = './Monitor-Client/src/main/java'
dest = ['./Client/src/main/resources/cstTemplates/', './Server/src/main/resources/cstTemplates/']

#Function that does:
  # Rename .java files to mustache files
  # Copy and move them into the desired target location
def CopyRenameMove(path, file_name):

  #Rules out the files that does not end with .java
  if file_name.endswith('.java'):
    subpath = path[len(source) + 1:]

    #Spilt the path into a list containing mutitple strings used to generate the folder structure
    folder_structure = subpath.split('\\')

    #Igonre the the first 3 string in the array
    del folder_structure[0:3]

    #Remove the files that starts with Test
    source_file = os.path.join(path, file_name) if not (file_name.startswith('Test')) else ''
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

#For loop to get the root, dirs and files in the soruce target.
for root, dirs, files in os.walk(source, topdown=True):
  for name in files:
    CopyRenameMove(root, name)
