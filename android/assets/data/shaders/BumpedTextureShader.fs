
uniform sampler2D u_texture_diffuse;
uniform sampler2D u_texture_bump;
uniform sampler2D u_texture_normal;

uniform vec4 u_ambient_color;
uniform vec4 u_light_color;
uniform vec3 u_light_direction;
uniform vec4 u_color;
uniform mat3 u_normal_matrix;

uniform bool u_is_tinted;
uniform vec4 u_tint;

varying vec2 v_texCoords;
varying vec3 N;
varying vec3 v;

void main() {

    vec3 normal = normalize(texture2D(u_texture_normal, v_texCoords).rgb);
    float diffuse = max(-dot(u_normal_matrix * normal, u_light_direction), 0.0);

    vec4 texColor = texture2D(u_texture_diffuse, v_texCoords);

    vec3 Idiff = (0.2 * u_light_color.rgb + 0.8 * texColor.rgb) * diffuse;

    gl_FragColor = 0.8 * vec4(Idiff, 1.0) + 0.2 * u_ambient_color;

    if (u_is_tinted) {
        gl_FragColor = 0.8 * gl_FragColor + 0.2 * u_tint;
    }
}