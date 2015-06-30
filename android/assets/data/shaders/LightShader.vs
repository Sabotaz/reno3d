attribute vec4 a_position;
attribute vec3 a_normal;
uniform mat4 u_worldTrans;
uniform mat4 u_projTrans;
uniform mat3 u_normal_matrix;
uniform vec3 u_light_direction;
varying vec3 N;
varying vec3 v;

void main() {
    gl_Position =  u_projTrans * u_worldTrans * a_position;
    v = vec3(u_projTrans * u_worldTrans * a_position);
    N = normalize(u_normal_matrix * a_normal);
}
