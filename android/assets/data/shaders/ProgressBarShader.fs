varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform mat4 u_projTrans;

uniform vec4 u_firstColor;
uniform vec4 u_secondColor;
uniform float u_ratio;

uniform float u_time;

void main() {
        vec4 tex = texture2D(u_texture, v_texCoords);
        float intensity = (tex.r + tex.g + tex.b) / 3.0;

        float v = (sin(v_texCoords.x*50.0 - 5.0 * u_time + 3.0 * v_texCoords.y) + 1.0 ) * 0.5 * 0.3; // 0.0 -> 0.2

        vec3 noir = vec3(0.0, 0.0, 0.0);

        vec3 final_color = u_secondColor.rgb * (1.0-v) + noir * v;

        gl_FragColor = mix(vec4(u_firstColor.rgb, tex.a),vec4(final_color.rgb, tex.a), step(u_ratio, intensity));


}