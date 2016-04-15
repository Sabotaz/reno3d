#!/usr/bin/python

id = ""
version = ""

def afficher():

    html = open("html/end.html").read()

    print(html)

def traitement(form):
    return True

if __name__ == "__main__":
    import cgi
    import demographie as last
    print("Content-type: text/html; charset=utf-8\n")
    form = cgi.FieldStorage()
    id = form.getvalue("id")
    version = form.getvalue("version")
    afficher() if last.traitement(form) else last.afficher()