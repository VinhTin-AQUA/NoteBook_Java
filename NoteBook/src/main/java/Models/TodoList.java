package Models;

public class TodoList {

    private int todoListId;
    private String item;
    private boolean check;

    public TodoList(int todoListId, String item, boolean check) {
        this.todoListId = todoListId;
        this.item = item;
        this.check = check;
    }
    
    public TodoList(TodoList todos) {
        this.todoListId = todos.todoListId;
        this.item = todos.item;
        this.check = todos.check;
    }

    public int getTodoListId() {
        return todoListId;
    }

    public String getItem() {
        return item;
    }

    public boolean isCheck() {
        return check;
    }

    public void setTodoListId(int todoListId) {
        this.todoListId = todoListId;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }
}
