#!/usr/bin/python
#coding: utf-8

import random
import re

id = ""
version = ""

items = {
    "fen" : [
        "Simple vitrage",
        "Survitrage",
        "Double vitrage",
        "Triple vitrage"
    ],
    "vol" : [
        "Volet roulant métal",
        "Volet battant bois",
        "Volet roulant PVC",
    ],
    "iso" : [
        "Laine de verre",
        "Laine de roche",
        "Polystyrène",
        "Polyuréthane",
        "Fibre de bois",
        "Laine de coton",
    ],
    "vmc" : [
        "Ventilation naturelle",
        "VMC autoréglable",
        "VMC hygrorégable",
        "VMC double flux"
    ],
    "cha" : [
        "Chaudière gaz standard",
        "Chaudière gaz basse température",
        "Chaudière gaz à condensation",
        "Chaudière électrique"
    ],
    "dep" : [
        "Toiture",
        "Isolation des murs",
        "Fuites d'air",
        "Vitrage",
        "Sols"
    ],
}

results = dict()

shuffled = False
def shuffle(id):
    global shuffled
    if not shuffled:
        random.seed(id)
        for type, values in items.items():
            random.shuffle(values)
    shuffled = True

def afficher():

    html = open("html/neps2.html").read()


    html = html.replace("$ID", id)
    html = html.replace("$VERSION", version)

    shuffle(id)

    for type, values in items.items():
        for i, value in enumerate(values):

            if value in results:
                m = re.search('(\<select name\="'+"\$" + type + str(i+1)+'".*?\>\\n(?:.*\<option\>.\\n)+)', html, re.MULTILINE)
                if m:
                    found = m.group(1)
                    found2 = found.replace("<option>" + results[value], "<option selected=\"selected\">" + results[value])
                    html = html.replace(found, found2)

            html = html.replace("$" + type + str(i+1), value)

    print(html)

def traitement(form):
    id = form.getvalue("id")

    shuffle(id)

    bad = False

    for type, values in items.items():
        count = []
        for i, value in enumerate(values):
            results[value] = form.getvalue(value)
            if results[value] in count:
                bad = True
            count.append(results[value])


    if None not in results.values() and not bad:
        with open("log/"+id, "a") as log:
            for type, value in results.items():
                log.write(type+";"+value)
                log.write("\n")

    return None not in results.values() and not bad

if __name__ == "__main__":
    import cgi
    import neps as last
    print("Content-type: text/html; charset=utf-8\n")
    form = cgi.FieldStorage()
    id = form.getvalue("id")
    version = form.getvalue("version")
    afficher() if last.traitement(form) else last.afficher()