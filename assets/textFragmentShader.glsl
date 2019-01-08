varying vec2 f_texcoord;
varying mat4 transform;
uniform sampler2D texture;
void main() {
        gl_FragColor = vec4(texture2D(texture, f_texcoord));
}
