#read input file
fin = open("/home/travis/build/byggemand-bob/Group3-monitoring-distributed-systems/Server/config.yaml", "rt")
#read file contents to string
data = fin.read()
#replace all occurrences of the required string
data = data.replace('templateDir: src/main/resources/cstTemplates', 'templateDir: Server/src/main/resources/cstTemplates')
#close the input file
fin.close()
#open the input file in write mode
fin = open("/home/travis/build/byggemand-bob/Group3-monitoring-distributed-systems/Server/config.yaml", "wt")
#overrite the input file with the resulting data
fin.write(data)
#close the file
fin.close()