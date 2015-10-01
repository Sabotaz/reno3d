# Usage :
# blender --background projetVide.blend --python objToObjWithNormals.py -- objDirectory

import bpy
import sys
import os

# Fonctions #
def computeNormalesInObj(obj_path) :
	bpy.ops.import_scene.obj(filepath=obj_path, use_edges=True, use_smooth_groups=True, use_split_objects=True, use_split_groups=True,
use_groups_as_vgroups=False, use_image_search=True, split_mode='ON',
global_clamp_size=0, axis_forward='-Z', axis_up='Y')
	
	bpy.ops.export_scene.obj(filepath=obj_path, check_existing=True, axis_up='Y', axis_forward='-Z', filter_glob="*.obj;*.mtl", use_selection=False, use_animation=False, use_mesh_modifiers=True, use_edges=True, use_smooth_groups=False, use_smooth_groups_bitflags=False, use_normals=True, use_uvs=True, use_materials=True, use_triangles=False, use_nurbs=False, use_vertex_groups=False, use_blen_objects=True, group_by_object=False, group_by_material=False, keep_vertex_order=False, global_scale=1.0, path_mode='AUTO')

# Main #
argv = sys.argv
argv = argv[argv.index("--") + 1:] # get all args after "--"
print("\nDébut du traitement\n")
racine_directory_path = argv[0]
computeNormalesInObj(racine_directory_path)
print("\nFin du traitement")

