#!/usr/bin/python

usability = []
id = ""
version = ""

def afficher():

    html = open("html/usability.html").read()

    html = html.replace("$ID", id)
    html = html.replace("$VERSION", version)

    for i, nep in enumerate(usability):
        if nep is not None:
            html = html.replace('id="' + str(i+1) + '_' + nep + '"','id="' + str(i+1) + '_' + nep + '" checked')
        else:
            html = html.replace('<table id="Usa_'+str(i+1)+'">', '<table id="Usa_'+str(i+1)+'" bgcolor="#FFD289">')

    print(html)

def traitement(form):
    global usability
    global id
    global version
    id = form.getvalue("id")
    version = form.getvalue("version")
    usability = [form.getvalue("Usa_"+str(i))for i in range(1,13)]
    if None not in usability:
        with open("log/"+id, "a") as log:
            for i, usa in enumerate(usability):
                log.write("Usa_"+str(i)+";"+usa)
                log.write("\n")
            log.flush()
    return None not in usability

if __name__ == "__main__":
    import cgi
    import neps as last
    print("Content-type: text/html; charset=utf-8\n")
    form = cgi.FieldStorage()
    id = form.getvalue("id")
    version = form.getvalue("version")
    afficher() if last.traitement(form) else last.afficher()