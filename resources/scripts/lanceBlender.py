import os

for path, dirs, files in os.walk("."):
	for filename in files:
		if filename.endswith('.obj'):
			os.system("blender --background projetVide.blend --python objToObjWithNormals.py -- " + os.path.join(path,filename))
