#!/usr/bin/python

version = ""
debug = False


def afficher():

    if debug:
        import os
        os.system("java -jar ../desktop/build/libs/desktop-1.0.jar " + version)

    else:
        html = open("html/intro.html").read()
        config = open("../android/assets/data/misc/config.properties").read()
        config = {c[0] : c[1] for c in [l.split("=") for l in config.split("\n")]}
        intro = config["INTRO#"+version].replace("\\n","<br/>")

        html = html.replace("$INTRO", intro)
        html = html.replace("$VERSION", version)

        print(html)

def traitement(form):
    return True

if __name__ == "__main__":
    import cgi
    print("Content-type: text/html; charset=utf-8\n")
    form = cgi.FieldStorage()
    version = form.getvalue("version")
    debug = form.getvalue("debug")
    afficher()