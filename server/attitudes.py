#!/usr/bin/python

import cgi

form = cgi.FieldStorage()
print("Content-type: text/html; charset=utf-8\n")

print(form.getvalue("name"))

html = open("html/attitudes.html").read()

print(html)