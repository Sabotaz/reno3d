#!/usr/bin/python

def afficher():

    html = open("html/start.html").read()

    print(html)

def traitement(form):
    return True

if __name__ == "__main__":
    import cgi
    print("Content-type: text/html; charset=utf-8\n")
    afficher()