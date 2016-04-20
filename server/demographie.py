#!/usr/bin/python
#coding: utf-8

id = ""
version = ""
email = ""
age = ""
sexe = ""
proprio = ""
habit = ""
exp = ""
util = ""
studies = ""
francais = ""

def afficher():

    html = open("html/demographie.html").read()

    html = html.replace("$VERSION", version)
    html = html.replace("$ID", id)

    if email is not None:
       html = html.replace('name="email" value=""','name="email" value="' + email + '"')

    if age is not None:
       html = html.replace('name="age" value=""','name="age" value="' + email + '"')

    if sexe is not None:
       html = html.replace('value="' + sexe + '"','value="' + sexe + '" checked')

    if proprio is not None:
        html = html.replace('value="' + proprio + '"','value="' + proprio + '" checked')

    if habit is not None:
        html = html.replace('value="' + habit + '"','value="' + habit + '" checked')

    if exp is not None:
        html=html.replace('value="' + exp + '"', 'value="' + exp + '" checked')

    if util is not None:
        html = html.replace('util="' + util + '"', 'value="' + util + '" checked')

    if studies is not None:
        html = html.replace('value="' + studies + '"','value="' + studies + '" checked')

    if francais is not None:
        html = html.replace('value="' + francais + '"','value="' + francais + '" checked')

    print(html)

def traitement(form):
    
    global id
    global version
    global email
    global age
    global sexe
    global proprio
    global habit
    global exp
    global util
    global studies
    global francais

    id = form.getvalue("id")
    email = form.getvalue("email")
    age = form.getvalue("age")
    sexe = form.getvalue("sexe")
    proprio = form.getvalue("proprio")
    habit = form.getvalue("habit")
    exp = form.getvalue("exp")
    util = form.getvalue("util")
    studies = form.getvalue("studies")
    francais = form.getvalue("français")
    version = form.getvalue("version")

    if email and age and sexe and studies and francais:
        id = form.getvalue("id")
        with open("log/"+id, "a") as log:
            log.write("email;"+email)
            log.write("\n")
            log.write("age;"+age)
            log.write("\n")
            log.write("sexe;"+sexe)
            log.write("\n")
            log.write("proprio;"+proprio)
            log.write("\n")
            log.write("habit;"+habit)
            log.write("\n")
            log.write("exp;"+exp)
            log.write("\n")
            log.write("util;"+util)
            log.write("\n")
            log.write("studies;"+studies)
            log.write("\n")
            log.write("français;"+francais)
            log.write("\n")
        return True
    return False

if __name__ == "__main__":
    import cgi
    import neps as last
    print("Content-type: text/html; charset=utf-8\n")
    form = cgi.FieldStorage()
    id = form.getvalue("id")
    version = form.getvalue("version")
    afficher() if last.traitement(form) else last.afficher()
