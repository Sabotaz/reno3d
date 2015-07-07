
uniform sampler2D u_texture;
uniform vec4 u_ambient_color;
uniform vec4 u_light_color;
uniform vec3 u_light_direction;

uniform bool u_is_colored;
uniform vec4 u_color;

uniform bool u_is_tinted;
uniform vec4 u_tint;

varying vec2 v_texCoords;
varying vec3 N;
varying vec3 v;

void main() {

    // Light Color * max(dot(N,L), 0.0) = Diffuse
    //vec3 diffuse = u_light_color.rgb * max(-dot(N,u_light_direction), 0.0);
    // it IS the light

    vec3 diffuse = u_light_color.rgb;

    // Ambiant + diffuse * attenuation = intensity
    vec3 intensity = diffuse;

    // DiffuseColor * intensity = final color
    vec4 texColor = texture2D(u_texture, v_texCoords);
    vec3 finalColor = texColor.rgb * intensity;

    gl_FragColor = vec4(finalColor, texColor.a);

}
