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

    @Override
    public int hashCode() {
        int hash = 3;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Content other = (Content) obj;
        if (this.contentId != other.contentId) {
            return false;
        }
        return Arrays.equals(this.text, other.text);
    }
    
    
}
