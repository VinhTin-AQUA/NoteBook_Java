
package Models;

public class Photo {
    private int photoId;
    private byte[] data;
    

    public Photo(int photoId, byte[] data) {
        this.photoId = photoId;
        this.data = data;
    }
    
    public Photo(Photo photo) {
        this.photoId = photo.photoId;
        this.data = photo.data;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public int getPhotoId() {
        return photoId;
    }

    public byte[] getData() {
        return data;
    }
    
}
