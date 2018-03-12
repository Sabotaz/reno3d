#!/usr/bin/python
import os

version = ""
id = ""

def afficher():

        html = open("html/tutostromo.html").read()

        html = html.replace("$VERSION", version)
        html = html.replace("$ID", id)
        html = html.replace("$PATH", os.getcwd())

        print(html)

def traitement(form):
    global id
    global version
    id = form.getvalue("id")
    version = form.getvalue("version")
    return True

if __name__ == "__main__":
    import cgi
    import intro as last
    print("Content-type: text/html; charset=utf-8\n")
    form = cgi.FieldStorage()
    version = form.getvalue("version")
    id = form.getvalue("id")
    afficher() if last.traitement(form) else last.afficher()()