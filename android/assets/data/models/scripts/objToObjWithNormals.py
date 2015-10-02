# Usage :
# blender --background projetVide.blend --python objToObjWithNormals.py -- objDirectory

import bpy
import sys
import os

MAX_INDICES = 32767
MIN_THRESOLD = 0.05

# Fonctions #
def computeNormalesInObj(obj_path) :
	import_file(obj_path)
	decimate()
	export_file(obj_path)


def decimate():
	n_indices = {}
	for name in bpy.data.objects.keys():
		indices = 0
		for poly in bpy.data.meshes[name].polygons:
			if len(poly.vertices) >= 3:
				indices += len(poly.vertices) - 2
		n_indices[name] = indices * 3

	total_indices = sum(n_indices.values())
	to_delete = total_indices - MAX_INDICES

	if to_delete <= 0:
		return

	enought_to_decimate = False
	threshold = MIN_THRESOLD
	decimables = {}
	while not enought_to_decimate:
		for name, indices in n_indices.items():
			if indices / total_indices >= threshold:
				decimables[name] = indices

		deletable_indices = sum(decimables.values())
		if deletable_indices < to_delete:
			threshold /= 2
		else:
			enought_to_decimate = True

	ratios = {}
	for name, indices in decimables.items():
		ratios[name] = 1 - ((to_delete * (indices / deletable_indices)) / indices)

	for name, ratio in ratios.items():
		mod = bpy.data.objects[name].modifiers.new(name='decimate', type='DECIMATE')
		mod.ratio = ratio


def import_file(obj_path):
	bpy.ops.import_scene.obj(filepath=obj_path, use_edges=True, use_smooth_groups=True, use_split_objects=True, use_split_groups=True,
use_groups_as_vgroups=False, use_image_search=True, split_mode='ON',
global_clamp_size=0, axis_forward='-Z', axis_up='Y')


def export_file(obj_path):
	bpy.ops.export_scene.obj(filepath=obj_path, check_existing=True, axis_up='Y', axis_forward='-Z', filter_glob="*.obj;*.mtl", use_selection=False, use_animation=False, use_mesh_modifiers=True, use_edges=True, use_smooth_groups=False, use_smooth_groups_bitflags=False, use_normals=True, use_uvs=True, use_materials=True, use_triangles=False, use_nurbs=False, use_vertex_groups=False, use_blen_objects=True, group_by_object=False, group_by_material=False, keep_vertex_order=False, global_scale=1.0, path_mode='AUTO')


# Main #
argv = sys.argv
argv = argv[argv.index("--") + 1:] # get all args after "--"
print("\nDébut du traitement\n")
racine_directory_path = argv[0]
computeNormalesInObj(racine_directory_path)
print("\nFin du traitement")
