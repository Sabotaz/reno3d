varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords);

    if (texColor.a > 0.5) {
        gl_FragColor = texColor;
    } else {
        discard;
    }
}
