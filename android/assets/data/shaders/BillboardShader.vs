attribute vec3 a_position;
attribute vec2 a_texCoords;

uniform mat4 u_proj;
uniform mat4 u_model;
uniform mat4 u_view;

varying vec2 v_texCoords;

void main() {
    v_texCoords = a_texCoords;
    float scl = u_model[3][3];
    vec4 pos;
    if (scl != 1.0) {
        pos = vec4(scl * a_position,1.0);
        mat4 model = u_model;
        model[3][3] = 1.0;
        pos = model * pos;
        pos += vec4((u_view * model * vec4(0.0, 0.0, 0.0, 1.0)).xyz, 0.0);
    } else {
        pos = u_model * vec4(a_position,1.0);
        pos += vec4((u_view * u_model * vec4(0.0, 0.0, 0.0, 1.0)).xyz, 0.0);
    }
    gl_Position = u_proj * pos;
    //gl_Position = u_proj * (vec4((u_view * u_model)[3].xyz + (mv * vec4(a_position,1.0)).xyz, 1.0));
}
