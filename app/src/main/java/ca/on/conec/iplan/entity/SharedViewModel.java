package ca.on.conec.iplan.entity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

// Inherits from ViewModel, different from TodoViewModel
// This is for editing DayTodo
public class SharedViewModel extends ViewModel {

    private final MutableLiveData<Todo> selectedItem = new MutableLiveData<>();

    public void selectItem(Todo todo) {
        selectedItem.setValue(todo);
    }

    public LiveData<Todo> getSelectedItem() {
        return selectedItem;
    }
}
