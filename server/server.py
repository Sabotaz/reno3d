#!/usr/bin/python
# utf-8

import BaseHTTPServer
import CGIHTTPServer
from os import curdir, sep

PORT = 8888
server_address = ("", PORT)

server = BaseHTTPServer.HTTPServer

class Handler(CGIHTTPServer.CGIHTTPRequestHandler):
    def do_GET(self):

        file = self.path.split("?")[0]
        try:
            #Check the file extension required and
            #set the right mime type

            sendReply = False
            if file.endswith(".py"):
                self.do_POST()
                return
            if file.endswith(".html"):
                mimetype='text/html'
                sendReply = True
            if file.endswith(".jpg"):
                mimetype='image/jpg'
                sendReply = True
            if file.endswith(".gif"):
                mimetype='image/gif'
                sendReply = True
            if file.endswith(".png"):
                mimetype='image/png'
                sendReply = True
            if file.endswith(".js"):
                mimetype='application/javascript'
                sendReply = True
            if file.endswith(".css"):
                mimetype='text/css'
                sendReply = True
            if file.endswith(".xml"):
                mimetype='text/xml'
                sendReply = True
            if file.endswith(".mp4"):
                mimetype='video/mp4'
                sendReply = True

            if sendReply == True:
                #Open the static file requested and send it
                f = open(curdir + sep + "tutostromo" + sep + file)
                self.send_response(200)
                self.send_header('Content-type',mimetype)
                self.end_headers()
                self.wfile.write(f.read())
                f.close()

            else:
                raise IOError()

            return

        except IOError:
            self.send_error(404,'File Not Found: %s' % file)

handler = Handler
handler.cgi_directories = ["/"]
print "Serveur actif sur le port :", PORT

httpd = server(server_address, handler)
httpd.serve_forever()