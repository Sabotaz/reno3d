# usage:
# blender foobar.blend --background --python fbxexporter.py -- foobar.fbx

import bpy
import sys

argv = sys.argv
argv = argv[argv.index("--") + 1:] # get all args after "--"

fbx_out = argv[0]

bpy.ops.export_scene.fbx(filepath=fbx_out, axis_forward='-Y', axis_up='Z')