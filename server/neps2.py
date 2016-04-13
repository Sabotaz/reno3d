#!/usr/bin/python

neps = []
id = ""

def afficher():

    html = open("html/neps2.html").read()

    html = html.replace("$ID", id)

    for i, nep in enumerate(neps):
        if nep is not None:
            html = html.replace('id="' + str(i+1) + '_' + nep + '"','id="' + str(i+1) + '_' + nep + '" checked')

    print(html)

def traitement(form):
    global  neps
    neps = [form.getvalue("Neps_"+str(i))for i in range(1,16)]

    return None not in neps

if __name__ == "__main__":
    import cgi
    import neps as last
    print("Content-type: text/html; charset=utf-8\n")
    form = cgi.FieldStorage()
    id = form.getvalue("id")
    afficher() if last.traitement(form) else last.afficher()