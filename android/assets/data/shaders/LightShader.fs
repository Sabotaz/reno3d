uniform vec4 u_color;
uniform vec4 u_ambient_color;
uniform vec4 u_light_color;
uniform vec3 u_light_direction;

uniform bool u_is_tinted;
uniform vec4 u_tint;

uniform bool u_is_blended;
uniform float u_opacity;

varying vec3 N;
varying vec3 v;

void main() {
    vec3 Idiff = u_light_color.rgb * max(-dot(N,u_light_direction), 0.0);
    //Idiff = clamp(Idiff, 0.0, 1.0);
    gl_FragColor = 0.2 * vec4(Idiff, 1.0) + 0.2 * u_ambient_color + 0.6 * u_color;
    if (u_is_tinted) {
        gl_FragColor = 0.8 * gl_FragColor + 0.2 * u_tint;
    }
    if (u_is_blended) {
        gl_FragColor.a *= (1.0 - u_opacity);
    }
    //gl_FragColor = Idiff;
}
