attribute vec4 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

uniform vec4 u_firstColor;
uniform vec4 u_secondColor;
uniform float u_ratio;

varying vec2 v_texCoords;

void main() {
    v_texCoords = a_texCoord0;
    gl_Position = u_projTrans * a_position;
}