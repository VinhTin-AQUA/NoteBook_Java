
package ThemeColor;


public enum LightMode {
    HEADER_cOLOR("#DCD3CB"),
    BODY_cOLOR("#FDFDF4"),
    SETTING_COLOR("#FDFDF4"),
    TEXT_COLOR("#333333");
    
    private final String rgb;
    private LightMode(String rgb) {  
        this.rgb = rgb;
    }
    
    public String getRGB(){
        return this.rgb;
    }
}