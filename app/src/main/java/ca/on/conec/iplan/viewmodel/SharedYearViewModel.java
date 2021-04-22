package ca.on.conec.iplan.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ca.on.conec.iplan.entity.TodoYear;

public class SharedYearViewModel extends ViewModel {

    private final MutableLiveData<TodoYear> selectedItem = new MutableLiveData<>();
    private boolean isEdit;

    public void selectItem(TodoYear todoYear) {

        selectedItem.setValue(todoYear);
    }

    public LiveData<TodoYear> getSelectedItem() {

        return selectedItem;
    }

    public void setIsEdit(boolean isEdit) {
        this.isEdit = isEdit;
    }

    public boolean getIsEdit() {
        return isEdit;
    }
}
