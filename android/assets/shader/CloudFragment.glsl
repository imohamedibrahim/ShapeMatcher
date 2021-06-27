#ifdef GL_ES
precision mediump float;
#endif

varying vec2 v_texCoord0;
uniform sampler2D u_diffuseTexture;
void main() {
    vec4 diffuse = texture2D(u_diffuseTexture, v_texCoord0);
    //if(diffuse.b > 0.95) {
      //  gl_FragColor.a = 0.0;
    //}else {
        gl_FragColor = mix(vec4(1.0,1.0,1.0,1.0),diffuse, 1.0);
    //}
}