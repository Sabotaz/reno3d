import os
import re

path = '.'

properties = {}

import os
for root, dirs, files in os.walk(path):
    for f in files:
        if f == "PluginFurnitureCatalog.properties":
            properties[root] = {}
            for name in os.listdir(root):
                if name.endswith(".properties"):
                    lang = "base"
                    if name != "PluginFurnitureCatalog.properties":
                        lang = name[23:-11]

                    properties[root][lang] = {}
                    for line in open(os.path.join(root, name)):
                        if not line.startswith('#'):
                            split = line.strip().split('=')
                            if len(split) == 2 and split[0].find('#') != -1:
                                properties[root][lang][split[0]] = split[1]


correspondances = {}
newProperties = {"base":{}}
index = 1

existance = {}

for directory in properties.keys():
    correspondances = {}
    existance[directory] = {}

    for key, value in properties[directory]["base"].items():
        n = int(re.search('#([0-9]+)',key).group(1))

        #check if model && icon exists
        if n not in existance[directory]:
            model = properties[directory]["base"]["model#"+str(n)]
            icon = properties[directory]["base"]["icon#"+str(n)]
            existance[directory][n] = os.path.isfile(os.path.join(directory, model[1:])) and os.path.isfile(os.path.join(directory, icon[1:]))

        if not existance[directory][n]:
            continue

        newN = 0
        if n in correspondances:
            newN = correspondances[n]
        else:
            newN = index
            correspondances[n] = newN
            index += 1
            newProperties["base"][newN] = {}

        newkey = key.replace('#'+str(n), '#'+str(newN),1)
        newProperties["base"][newN][newkey] = value

        for lang in properties[directory].keys():
            if lang != "base":
                if lang not in newProperties:
                    newProperties[lang] = {}
                if key in properties[directory][lang]:
                    if newN not in newProperties[lang]:
                        newProperties[lang][newN] = {}
                    newProperties[lang][newN][newkey] = properties[directory][lang][key]

for lang in newProperties.keys():
    f = None
    if lang == "base":
        f = open("PluginFurnitureCatalog.properties", "w")
    else:
        f = open("PluginFurnitureCatalog_" + lang + ".properties", "w")
    for n in newProperties[lang].keys():
        for key, value in newProperties[lang][n].items():
            f.write(key + "=" + value + "\n")
        f.write("\n")
    f.close()
