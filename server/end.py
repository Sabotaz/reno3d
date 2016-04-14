#!/usr/bin/python

id = ""
version = ""

def afficher():

    import os
    os.popen("java -jar ../desktop/build/libs/desktop-1.0.jar " + version + " \"" + id + "\"").read()

    html = open("html/end.html").read()

    print(html)

def traitement(form):
    return True

if __name__ == "__main__":
    import cgi
    import neps2 as last
    print("Content-type: text/html; charset=utf-8\n")
    form = cgi.FieldStorage()
    id = form.getvalue("id")
    version = form.getvalue("version")
    afficher() if last.traitement(form) else last.afficher()