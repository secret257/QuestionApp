package jp.android.questionapp.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ReissueMenuModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ReissueMenuModel() {
        mText = new MutableLiveData<>();
        mText.setValue("認証情報を入力してください。");
    }

    public LiveData<String> getText() {
        return mText;
    }
}