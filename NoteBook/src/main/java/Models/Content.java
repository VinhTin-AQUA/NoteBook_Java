package Models;

import java.util.Arrays;

public class Content {

    private int contentId;
    private byte[] text;

    public Content(int contentId, byte[] text) {
        this.contentId = contentId;
        this.text = text;
    }
    
    public Content(Content content) {
        this.contentId = content.contentId;
        this.text = content.text;
    }

    public int getContentId() {
        return contentId;
    }

    public void setContentId(int contentId) {
        this.contentId = contentId;
    }

    public byte[] getText() {
        return text;
    }

    public void setText(byte[] text) {
        this.text = text;
    }

    // xóa mảng byte
    public void clearData() {
        text = null; // bộ thu gom rác tự động giải phóng vùng nhớ
    }
}
