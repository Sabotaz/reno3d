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
varying vec3 v;


varying vec3 N;
varying vec3 T;
varying vec3 B;

void main() {

    v_texCoords = u_textureUV.xy + a_texCoord0 * u_textureUV.zw;

    gl_Position =  u_projTrans * u_worldTrans * a_position;

    v = vec3(u_projTrans * u_worldTrans * a_position);


    // tangent space calculation

    N = normalize(u_normal_matrix * a_normal);

    vec3 c1 = cross(a_normal, vec3(0.0,0.0,1.0));
    vec3 c2 = cross(a_normal, vec3(0.0,1.0,0.0));
    if (length(c1) > length(c2)) {
        T = c1;
    }
    else {
        T = c2;
    }
    T = normalize(u_normal_matrix * T);
    B = normalize(cross(N, T));
}