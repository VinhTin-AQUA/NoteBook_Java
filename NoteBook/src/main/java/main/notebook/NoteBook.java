package main.notebook;
import DataConnection.Data;
import Views.App;

public class NoteBook {
    public static void main(String[] args) {
        Data.loadConnection();
        App a= new App();
        a.setVisible(true);
    }
}
