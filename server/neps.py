#!/usr/bin/python

neps = []
id = ""
version = ""

def fsdkml():
    
    import win32gui
    import win32con
    import time
    time.sleep(2)
	
    def enumHandler(hwnd, lParam):
        if win32gui.IsWindowVisible(hwnd):
            if 'Mozilla' in win32gui.GetWindowText(hwnd) or 'Chrome' in win32gui.GetWindowText(hwnd):
                win32gui.SetWindowPos(hwnd, win32con.HWND_BOTTOM, 0, 0, 1920, 1080, win32con.SWP_NOMOVE | win32con.SWP_NOSIZE)
    win32gui.EnumWindows(enumHandler, None)
    
    

def thread():

    import threading
    threading.Thread(None, fsdkml).start()
	
    import os
    os.popen("java -jar jar/desktop-1.0.jar " + version + " \"" + id + "\"").read()


def afficher():

    html = open("html/neps.html").read()

    html = html.replace("$ID", id)
    html = html.replace("$VERSION", version)

    for i, nep in enumerate(neps):
        if nep is not None:
            html = html.replace('id="' + str(i+1) + '_' + nep + '"','id="' + str(i+1) + '_' + nep + '" checked')
        else:
            html = html.replace('<table id="Neps_'+str(i+1)+'">', '<table id="Neps_'+str(i+1)+'" bgcolor="#FFD289">')


    print(html)

def traitement(form):
    global neps
    global id
    global version
    id = form.getvalue("id")
    version = form.getvalue("version")
    neps = [form.getvalue("Neps_"+str(i))for i in range(1,16)]
    if None not in neps:
        with open("log/"+id, "a") as log:
            for i, nep in enumerate(neps):
                log.write("Neps_"+str(i)+";"+nep)
                log.write("\n")
            log.flush()
    return None not in neps

if __name__ == "__main__":
    import cgi
    import consigne as last
    print("Content-type: text/html; charset=utf-8\n")
    form = cgi.FieldStorage()
    id = form.getvalue("id")
    version = form.getvalue("version")

    import threading
    threading.Thread(None, thread).start()

    afficher() if last.traitement(form) else last.afficher()