#!/usr/bin/python

import cgi

form = cgi.FieldStorage()
print("Content-type: text/html; charset=utf-8\n")

html = open("html/intro.html").read()
config = open("../android/assets/data/misc/config.properties").read()
config = {c[0] : c[1] for c in [l.split("=") for l in config.split("\n")]}
intro = config["INTRO#"+config["VERSION"]].replace("\\n","<br/>")

html = html.replace("$INTRO", intro)

print(html)