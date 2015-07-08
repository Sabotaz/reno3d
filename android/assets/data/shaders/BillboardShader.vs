attribute vec3 a_position;
attribute vec2 a_texCoord0;
attribute vec2 a_texCoords;

uniform vec4 u_textureUV;

uniform mat4 u_proj;
uniform mat4 u_model;
uniform mat4 u_view;

varying vec2 v_texCoords;

void main() {
    v_texCoords = a_texCoords;
    /*
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
    */
    /*
    float scl = u_model[3][3];
    // scale position
    vec4 pos = vec4(scl * a_position,1.0);
    // unscale matrix
    mat4 model = u_model;
    model[3][3] = 1.0;
    // apply model rotation
    pos = model * pos;
    // move camera
    vec3 cam = (u_view * model * vec4(0.0, 0.0, 0.0, 1.0)).xyz;
    // apply cam position
    pos += vec4(cam, 0.0);
    // project
    gl_Position = u_proj * pos;
    */
/*
    mat4 ModelView = u_model * u_view;

    // Column 0:
    ModelView[0][0] = 1.0;
    ModelView[0][1] = 0.0;
    ModelView[0][2] = 0.0;

    // Column 1:
    ModelView[1][0] = 0.0;
    ModelView[1][1] = 1.0;
    ModelView[1][2] = 0.0;

    // Column 2:
    ModelView[2][0] = 0.0;
    ModelView[2][1] = 0.0;
    ModelView[2][2] = 1.0;

    //scale
    ModelView[3][3] = 1.0;

    gl_Position = u_proj * ModelView * vec4(a_position, 1.0);
*/

    v_texCoords = u_textureUV.xy + a_texCoords * u_textureUV.zw;

    vec4 pos = u_model * vec4(a_position,1.0);
    vec3 center = u_model[3].xyz;
    pos += vec4((u_view * vec4(center,1.0)).xyz, 0.0);

    gl_Position = u_proj * pos;
}