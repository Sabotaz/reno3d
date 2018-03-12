#!/usr/bin/python

version = ""
id = ""

def thread():
    import os
    os.popen("vlc video/tutostromo"+version+".mp4").read()

def afficher():
    import threading
    threading.Thread(None, thread).start()

    html = open("html/consigne.html").read()
    intro = open("text/intro"+version).read()

    html = html.replace("$INTRO", intro)
    html = html.replace("$VERSION", version)
    html = html.replace("$ID", id)

    print(html)

def traitement(form):
    global  id
    id = form.getvalue("id")
    return True

if __name__ == "__main__":
    import cgi
    import tutostromo as last
    print("Content-type: text/html; charset=utf-8\n")
    form = cgi.FieldStorage()
    version = form.getvalue("version")
    id = form.getvalue("id")
    afficher() if last.traitement(form) else last.afficher()