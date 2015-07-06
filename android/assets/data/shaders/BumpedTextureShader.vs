attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
uniform mat4 u_worldTrans;
uniform vec4 u_textureUV;
uniform mat3 u_normal_matrix;
uniform vec3 u_light_direction;

varying vec2 v_texCoords;

varying vec3 N;
varying vec3 v;

void main() {

    v_texCoords = u_textureUV.xy + a_texCoord0 * u_textureUV.zw;

    gl_Position =  u_projTrans * u_worldTrans * a_position;

    v = vec3(u_projTrans * u_worldTrans * a_position);
    N = normalize(u_normal_matrix * vec3(0.0,0.0,1.0));
}