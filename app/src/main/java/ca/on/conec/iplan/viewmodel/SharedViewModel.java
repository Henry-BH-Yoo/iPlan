package ca.on.conec.iplan.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ca.on.conec.iplan.entity.Todo;

// Inherits from ViewModel, different from TodoViewModel
// This is for editing DayTodo
public class SharedViewModel extends ViewModel {

    private final MutableLiveData<Todo> selectedItem = new MutableLiveData<>();
    private boolean isEdit;

    public void selectItem(Todo todo) {

        selectedItem.setValue(todo);
    }

    public LiveData<Todo> getSelectedItem() {

        return selectedItem;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public boolean getIsEdit() {
        return isEdit;
    }
}