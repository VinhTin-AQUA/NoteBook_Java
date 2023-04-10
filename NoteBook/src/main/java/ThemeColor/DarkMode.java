package ThemeColor;

public enum DarkMode {
    HEADER_cOLOR("#222222"),
    BODY_cOLOR("#222137"),
    SETTING_COLOR("#252525"), 
    BUTTON("#");

    private final String rgb;

    private DarkMode(String rgb) {
        this.rgb = rgb;
    }
    
    public String getRGB(){
        return this.rgb;
    }
}
