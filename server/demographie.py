#!/usr/bin/python
#coding: utf-8

id = ""
version = ""
email = ""
age = ""
sexe = ""
studies = ""
francais = ""

def afficher():

    html = open("html/demographie.html").read()

    html = html.replace("$VERSION", version)

    if id is not None:
       html = html.replace('name="id" value=""','name="id" value="' + id + '"')

    if email is not None:
       html = html.replace('name="email" value=""','name="email" value="' + email + '"')

    if age is not None:
       html = html.replace('name="age" value=""','name="age" value="' + email + '"')

    if sexe is not None:
       html = html.replace('value="' + sexe + '"','value="' + sexe + '" checked')

    if studies is not None:
        html = html.replace('value="' + studies + '"','value="' + studies + '" checked')

    if francais is not None:
        html = html.replace('value="' + francais + '"','value="' + francais + '" checked')

    print(html)

def traitement(form):
    
    global id
    global email
    global age
    global sexe
    global studies
    global francais

    id = form.getvalue("id")
    email = form.getvalue("email")
    age = form.getvalue("age")
    sexe = form.getvalue("sexe")
    studies = form.getvalue("studies")
    francais = form.getvalue("français")
    version = form.getvalue("version")

    if email and age and sexe and studies and francais:
        id = form.getvalue("id")
        with open("log/"+id, "a") as log:
            log.write("version;"+version)
            log.write("\n")
            log.write("email;"+email)
            log.write("\n")
            log.write("age;"+age)
            log.write("\n")
            log.write("sexe;"+sexe)
            log.write("\n")
            log.write("studies;"+studies)
            log.write("\n")
            log.write("français;"+francais)
            log.write("\n")
        return True
    return False

if __name__ == "__main__":
    import cgi
    import intro as last
    print("Content-type: text/html; charset=utf-8\n")
    form = cgi.FieldStorage()
    version = form.getvalue("version")
    afficher() if last.traitement(form) else last.afficher()
