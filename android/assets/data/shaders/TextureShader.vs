attribute vec3 a_position;
attribute vec4 a_color;
uniform mat4 u_projTrans;
varying vec4 vColor;
attribute vec2 a_texCoords;
varying vec2 v_texCoords;

void main() {
    vColor = a_color;
    v_texCoords = a_texCoords;
    gl_Position = u_projTrans * vec4(a_position.xy, 0.0, 1.0);
}