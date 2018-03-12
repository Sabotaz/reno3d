#!/usr/bin/python
#coding: utf-8

id = ""
version = ""
debug = False

def afficher():

    if debug:
        with open("log/debug", "a") as log:
            log.write("version;"+version)
            log.write("\n")
            log.flush()

        import os
        os.system("java -jar jar/desktop-1.0.jar " + version)

    else:
        html = open("html/intro.html").read()

        html = html.replace("$VERSION", version)

        print(html)

def traitement(form):
    
    global id
    global version
    id = form.getvalue("id")
    version = form.getvalue("version")

    if id:
        return True
    return False

if __name__ == "__main__":
    import cgi
    import start as last
    print("Content-type: text/html; charset=utf-8\n")
    form = cgi.FieldStorage()
    version = form.getvalue("version")
    debug = form.getvalue("debug")
    afficher() if last.traitement(form) else last.afficher()
