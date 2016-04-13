#!/usr/bin/python

def afficher():

    html = open("html/intro.html").read()
    config = open("../android/assets/data/misc/config.properties").read()
    config = {c[0] : c[1] for c in [l.split("=") for l in config.split("\n")]}
    intro = config["INTRO#"+config["VERSION"]].replace("\\n","<br/>")

    html = html.replace("$INTRO", intro)

    print(html)

def traitement(form):
    return True

if __name__ == "__main__":
    import cgi
    print("Content-type: text/html; charset=utf-8\n")
    afficher()