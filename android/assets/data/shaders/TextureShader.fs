varying vec4 vColor;
varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords);
    gl_FragColor = texColor
}