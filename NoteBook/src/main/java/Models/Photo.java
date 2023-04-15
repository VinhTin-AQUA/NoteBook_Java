
package Models;

import java.util.Arrays;

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

    @Override
    public int hashCode() {
        int hash = 5;
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
        final Photo other = (Photo) obj;
        if (this.photoId != other.photoId) {
            return false;
        }
        return Arrays.equals(this.data, other.data);
    }
    
}
