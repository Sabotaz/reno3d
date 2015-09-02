varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

uniform vec4 u_firstColor;
uniform vec4 u_secondColor;
uniform float u_ratio;

void main() {
        vec4 tex = texture2D(u_texture, v_texCoords);
        float intensity = (tex.r + tex.g + tex.b) / 3.0;
        if (intensity >= u_ratio) {
                gl_FragColor = vec4(u_firstColor.rgb, tex.a);
        }
        else {
                gl_FragColor = vec4(u_secondColor.rgb, tex.a);
        }
}