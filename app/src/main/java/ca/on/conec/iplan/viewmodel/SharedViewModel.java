/**
 * FileName : SharedViewModel.java
 * Purpose
 * Revision History :
 *      2021.04.18 Sean    Create
 *      2021.04.20 Sean    Add edit flag
 */
package ca.on.conec.iplan.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ca.on.conec.iplan.entity.Todo;

/**
 * Purpose : This is for editing DayTodo
 */
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
