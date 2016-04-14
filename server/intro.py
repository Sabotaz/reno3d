#!/usr/bin/python

version = ""
debug = False


def afficher():

    if debug:
        with open("log/debug", "a") as log:
            log.write("version;"+version)
            log.write("\n")
        import os
        os.system("java -jar jar/desktop-1.0.jar " + version)

    else:
        html = open("html/intro.html").read()
        intro = open("text/intro"+version).read()

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