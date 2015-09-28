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

uniform bool u_is_selected;
uniform float u_time;

void main() {

    vec3 diffuse = u_light_color.rgb * max(-dot(N,u_light_direction), 0.0);

    vec3 intensity = u_ambient_color.rgb + diffuse;
    vec3 finalColor = u_color.rgb * intensity;
    //Idiff = clamp(Idiff, 0.0, 1.0);
    gl_FragColor = vec4(finalColor, 1.0 - u_opacity);
    if (u_is_tinted) {
        gl_FragColor = 0.8 * gl_FragColor + 0.2 * u_tint;
    }

    if (u_is_selected) {
        float s = sin(6.0*u_time) * 0.2 + 0.5;
        float v = sign(s) * s * s;
        vec3 jaune = vec3(1,1,153.0/255.0);
        gl_FragColor = vec4(gl_FragColor.rgb * (1.0-v) + jaune * v, gl_FragColor.a);
    }
}
