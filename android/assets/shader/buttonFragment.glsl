#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif
varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_powerlevel;

void main()
{
    vec4 color = texture2D(u_texture, v_texCoords);
    float upper = (0.517+0.256)/1.024;
    float lower = (0.517)/1.024;
    float range = upper - lower;
    float point =  upper - v_texCoords.y;
    float final = point / range;
    if (final < u_powerlevel){
        gl_FragColor = v_color * color;
    } else {
        float normalize = (color.x+color.y+color.z+color.w)/4.0;
        gl_FragColor = vec4(normalize,normalize,normalize,color.w);
    }
}