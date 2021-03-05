def ChangeTemplateInConfig(filePath, replace):
    fin = open(filePath, "rt")
    data = fin.read()
    data = data.replace('templateDir: src/main/resources/cstTemplates', replace)
    fin.close()
    fin = open(filePath, "wt")
    fin.write(data)
    fin.close()

#Change the template file path in Client project
#ChangeTemplateInConfig(
#    "/home/travis/build/byggemand-bob/Group3-monitoring-distributed-systems/Client/config.yaml", 
#    "templateDir: Client/src/main/resources/cstTemplates")

#Change the template file path in Monitor project
#ChangeTemplateInConfig(
#    '/home/travis/build/byggemand-bob/Group3-monitoring-distributed-systems/Monitor/config.yaml', 
#    'templateDir: Monitor/src/main/resources/cstTemplates')

#Change the template file path in Server project
ChangeTemplateInConfig(
    '/home/travis/build/byggemand-bob/Group3-monitoring-distributed-systems/Server/config.yaml', 
    'templateDir: Server/src/main/resources/cstTemplates')
