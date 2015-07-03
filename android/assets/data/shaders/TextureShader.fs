
uniform sampler2D u_texture;
uniform vec4 u_ambient_color;
uniform vec4 u_light_color;
uniform vec3 u_light_direction;
uniform vec4 u_color;

uniform bool u_is_tinted;
uniform vec4 u_tint;

varying vec2 v_texCoords;
varying vec3 N;
varying vec3 v;

void main() {
    vec4 texColor = texture2D(u_texture, v_texCoords);

    vec4 Idiff = (0.1 * u_light_color +  0.9 * texColor) * max(-dot(N,u_light_direction), 0.0);
    vec4 ambient = (0.5 * u_ambient_color +  0.5 * u_color);
    gl_FragColor = 0.7 * Idiff+ 0.3 * ambient;

    if (u_is_tinted) {
        gl_FragColor = 0.8 * gl_FragColor + 0.2 * u_tint;
    }
}