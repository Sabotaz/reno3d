
uniform sampler2D u_texture;
uniform vec4 u_ambient_color;
uniform vec4 u_light_color;
uniform vec3 u_light_direction;

uniform bool u_is_colored;
uniform vec4 u_color;

uniform bool u_is_selected;
uniform float u_time;

uniform bool u_is_tinted;
uniform vec4 u_tint;

varying vec2 v_texCoords;
varying vec3 N;
varying vec3 v;

void main() {

    vec3 diffuse = u_light_color.rgb * max(-dot(N,u_light_direction), 0.0);

    // Ambiant + diffuse * attenuation = intensity
    vec3 intensity = u_ambient_color.rgb + diffuse;

    // DiffuseColor * intensity = final color
    vec4 texColor = texture2D(u_texture, v_texCoords);
    vec3 finalColor = texColor.rgb * intensity;

    gl_FragColor = vec4(finalColor, texColor.a);

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
