# usage:
# blender --background --python objtofbx.py -- foobar.obj foobar.fbx

import bpy
import sys

argv = sys.argv
argv = argv[argv.index("--") + 1:] # get all args after "--"

obj_in = argv[0]
fbx_out = argv[1]

# Load a Wavefront OBJ File
bpy.ops.import_scene.obj(filepath=obj_in, use_ngons=True,
use_edges=True, use_smooth_groups=True, use_split_objects=True, use_split_groups=True,
use_groups_as_vgroups=False, use_image_search=True, split_mode='ON',
global_clamp_size=0, axis_forward='-Z', axis_up='Y')

bpy.ops.export_scene.fbx(filepath=fbx_out, axis_forward='-Y', axis_up='Z')