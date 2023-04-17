package ThemeColor;

public enum DarkMode {
    HEADER_cOLOR("#222222"),
    BODY_cOLOR("#222222"),
    SETTING_COLOR("#252525"), 
    TEXT_COLOR("#333333"),
    BUTTON("#CCFFFF"),
    WHITE("#FFFAFA");
    private final String rgb;

    private DarkMode(String rgb) {
        this.rgb = rgb;
    }
    
    public String getRGB(){
        return this.rgb;
    }
}
