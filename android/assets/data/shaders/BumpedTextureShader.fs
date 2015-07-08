
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
varying vec3 v;

varying vec3 N;
varying vec3 T;
varying vec3 B;

// https://github.com/mattdesl/lwjgl-basics/wiki/ShaderLesson6

void main() {

    // normal from normal map
    vec3 normal = normalize(texture2D(u_texture_normal, v_texCoords).rgb * 2.0 - 1.0);

    // light in tangent space
    // /!\ directionnal light -> light_direction = -light_position
    float x_light = -dot(u_light_direction, T);
    float y_light = -dot(u_light_direction, B);
    float z_light = -dot(u_light_direction, N);

    vec3 L = normalize(vec3(x_light, y_light, z_light));

    // Light Color * max(dot(N,L), 0.0) = Diffuse
    vec3 diffuse = u_light_color.rgb * max(dot(normal, L), 0.0);

    // Ambiant + diffuse * attenuation = intensity
    vec3 intensity = u_ambient_color.rgb + diffuse;

    // DiffuseColor * intensity = final color
    vec4 texColor = texture2D(u_texture_diffuse, v_texCoords);
    vec3 finalColor = texColor.rgb * intensity;

    gl_FragColor = vec4(finalColor, texColor.a);

    if (u_is_tinted) {
        gl_FragColor = 0.8 * gl_FragColor + 0.2 * u_tint;
    }

}