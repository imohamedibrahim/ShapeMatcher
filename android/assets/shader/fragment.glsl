#ifdef GL_ES
#define LOWP lowp
#define MED mediump
#define HIGH highp
precision mediump float;
#else
#define MED
#define LOWP
#define HIGH
#endif

#if defined(specularTextureFlag) || defined(specularColorFlag)
#define specularFlag
#endif

#ifdef normalFlag
varying vec3 v_normal;
#endif//normalFlag

#if defined(colorFlag)
varying vec4 v_color;
#endif

#ifdef blendedFlag
varying float v_opacity;
#ifdef alphaTestFlag
varying float v_alphaTest;
#endif//alphaTestFlag
#endif//blendedFlag

#if defined(diffuseTextureFlag) || defined(specularTextureFlag) || defined(emissiveTextureFlag)
#define textureFlag
#endif

#ifdef diffuseTextureFlag
varying MED vec2 v_diffuseUV;
#endif

#ifdef specularTextureFlag
varying MED vec2 v_specularUV;
#endif

#ifdef emissiveTextureFlag
varying MED vec2 v_emissiveUV;
#endif

#ifdef diffuseColorFlag
uniform vec4 u_diffuseColor;
#endif

#ifdef diffuseTextureFlag
uniform sampler2D u_diffuseTexture;
#endif

#ifdef specularColorFlag
uniform vec4 u_specularColor;
#endif

#ifdef specularTextureFlag
uniform sampler2D u_specularTexture;
#endif

#ifdef normalTextureFlag
uniform sampler2D u_normalTexture;
#endif

#ifdef emissiveColorFlag
uniform vec4 u_emissiveColor;
#endif

#ifdef emissiveTextureFlag
uniform sampler2D u_emissiveTexture;
#endif

#ifdef lightingFlag
varying vec3 v_lightDiffuse;

#if    defined(ambientLightFlag) || defined(ambientCubemapFlag) || defined(sphericalHarmonicsFlag)
#define ambientFlag
#endif//ambientFlag

#ifdef specularFlag
varying vec3 v_lightSpecular;
#endif//specularFlag

#ifdef shadowMapFlag
uniform sampler2D u_shadowTexture;
uniform float u_shadowPCFOffset;
varying vec3 v_shadowMapUv;
#define separateAmbientFlag

float getShadowness(vec2 offset)
{
    const vec4 bitShifts = vec4(1.0, 1.0 / 255.0, 1.0 / 65025.0, 1.0 / 16581375.0);
    return step(v_shadowMapUv.z, dot(texture2D(u_shadowTexture, v_shadowMapUv.xy + offset), bitShifts));//+(1.0/255.0));
}

float getShadow()
{
    return (//getShadowness(vec2(0,0)) +
    getShadowness(vec2(u_shadowPCFOffset, u_shadowPCFOffset)) +
    getShadowness(vec2(-u_shadowPCFOffset, u_shadowPCFOffset)) +
    getShadowness(vec2(u_shadowPCFOffset, -u_shadowPCFOffset)) +
    getShadowness(vec2(-u_shadowPCFOffset, -u_shadowPCFOffset))) * 0.25;
}
    #endif//shadowMapFlag

    #if defined(ambientFlag) && defined(separateAmbientFlag)
varying vec3 v_ambientLight;
#endif//separateAmbientFlag

#endif//lightingFlag

#ifdef fogFlag
uniform vec4 u_fogColor;
varying float v_fog;
#endif// fogFlag
#if defined(binormalFlag) || defined(tangentFlag) || defined(normalTextureFlag)
varying vec3 v_binormal;
varying vec3 v_tangent;
#endif
void main() {
    vec3 normal = vec3(1,1,1);
    #if defined(normalFlag) && defined(normalTextureFlag)
    normal = normalize(2.0 * texture2D(u_normalTexture, v_diffuseUV).xyz - 1.0);
    //normal = normalize(texture2D(u_normalTexture, v_diffuseUV).xyz);
    //normal = normalize((v_tangent * normal.x) + (v_binormal * normal.y) + (v_normal * normal.z));
    #elif defined(normalFlag)
    normal = v_normal;
    #elif defined(normalTextureFlag)
    normal = normalize(texture2D(u_normalTexture, v_diffuseUV).xyz);
    #endif // normalFlag
    #if defined(diffuseTextureFlag) && defined(diffuseColorFlag) && defined(colorFlag)
    vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV) * u_diffuseColor;
    #elif defined(diffuseTextureFlag) && defined(diffuseColorFlag)
    vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV) * u_diffuseColor;
    #elif defined(diffuseTextureFlag) && defined(colorFlag)
    vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV);
    #elif defined(diffuseTextureFlag)
    vec4 diffuse = texture2D(u_diffuseTexture, v_diffuseUV);
    #elif defined(diffuseColorFlag) && defined(colorFlag)
    vec4 diffuse = u_diffuseColor;
    #elif defined(diffuseColorFlag)
    vec4 diffuse = u_diffuseColor;
    #elif defined(colorFlag)
    vec4 diffuse = v_color;
    #else
    vec4 diffuse = vec4(1.0);
    #endif

    #if defined(emissiveTextureFlag) && defined(emissiveColorFlag)
    vec4 emissive = texture2D(u_emissiveTexture, v_emissiveUV) * u_emissiveColor;
    #elif defined(emissiveTextureFlag)
    vec4 emissive = texture2D(u_emissiveTexture, v_emissiveUV);
    #elif defined(emissiveColorFlag)
    vec4 emissive = u_emissiveColor;
    #else
    vec4 emissive = vec4(0.0);
    #endif

    #if (!defined(lightingFlag))
    gl_FragColor.rgb = diffuse.rgb + emissive.rgb;
    #elif (!defined(specularFlag))
    #if defined(ambientFlag) && defined(separateAmbientFlag)
    #ifdef shadowMapFlag
    gl_FragColor.rgb = (diffuse.rgb * (v_ambientLight + getShadow() * v_lightDiffuse)) + emissive.rgb;
    //gl_FragColor.rgb = texture2D(u_shadowTexture, v_shadowMapUv.xy);
    #else
    gl_FragColor.rgb = (diffuse.rgb * (v_ambientLight + v_lightDiffuse)) + emissive.rgb;
    #endif//shadowMapFlag
    #else
    #ifdef shadowMapFlag
    gl_FragColor.rgb = getShadow() * (diffuse.rgb * v_lightDiffuse) + emissive.rgb;
    #else
    gl_FragColor.rgb = (diffuse.rgb * v_lightDiffuse) + emissive.rgb;
    #endif//shadowMapFlag
    #endif
    #else
    #if defined(specularTextureFlag) && defined(specularColorFlag)
    vec3 specular = texture2D(u_specularTexture, v_specularUV).rgb * u_specularColor.rgb * v_lightSpecular;
    #elif defined(specularTextureFlag)
    vec3 specular = texture2D(u_specularTexture, v_specularUV).rgb * v_lightSpecular;
    #elif defined(specularColorFlag)
    vec3 specular = u_specularColor.rgb * v_lightSpecular;
    #else
    vec3 specular = v_lightSpecular;
    #endif

    #if defined(ambientFlag) && defined(separateAmbientFlag)
    #ifdef shadowMapFlag
    gl_FragColor.rgb = (diffuse.rgb * (getShadow() * v_lightDiffuse + v_ambientLight)) + specular + emissive.rgb;
    //gl_FragColor.rgb = texture2D(u_shadowTexture, v_shadowMapUv.xy);
    #else
    gl_FragColor.rgb = (diffuse.rgb * (v_lightDiffuse + v_ambientLight)) + specular + emissive.rgb;
    #endif//shadowMapFlag
    #else
    #ifdef shadowMapFlag
    gl_FragColor.rgb = getShadow() * ((diffuse.rgb * v_lightDiffuse) + specular) + emissive.rgb;
    #else
    gl_FragColor.rgb = (diffuse.rgb * v_lightDiffuse) + specular + emissive.rgb;
    #endif//shadowMapFlag
    #endif
    #endif//lightingFlag

    #ifdef blendedFlag
    gl_FragColor.a = diffuse.a * v_opacity;
    #ifdef alphaTestFlag
    if (gl_FragColor.a <= v_alphaTest)
    discard;
    #endif
    #else
    gl_FragColor.a = 1.0;
    #endif
    //gl_FragColor.a = 0.0;
    float v_fog1 = v_fog;
    float varr = 30.0;
    if (v_fog1 < varr){
        v_fog1=1.0;
    } else {
        v_fog1 /= varr;
        v_fog1 = 1.0/v_fog1;
    }
    gl_FragColor.rgb = mix(vec3(0,0,0), gl_FragColor.rgb, v_fog1) ;
    #if defined(normalTextureFlag)
    float NdotL = clamp(dot(normal, vec3(0,-1,0)), 0.0, 1.0);
    vec3 value = vec3(0.2,0.2,0.2) * NdotL;
    gl_FragColor.rgb = gl_FragColor.rgb;
    #endif
    //gl_FragColor.rgb = mix(gl_FragColor.rgb, v_lightDiffuse, 0.1);
    //gl_FragColor.rgb = mix(vec3(1,1,1), gl_FragColor.rgb, 0.1f);
}