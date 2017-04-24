package com.xiaomai.geek;

import com.xiaomai.geek.presenter.password.EditAccountPresenter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by XiaoMai on 2017/4/6 13:30.
 */

public class EditAccountPresenterTest {

    @Test
    public void testGeneratePassword() {
        EditAccountPresenter presenter = new EditAccountPresenter();
        assertEquals(presenter.generatePassword(EditAccountPresenter.TYPE_ALL, 10).length(), 10);
        assertEquals(presenter.generatePassword(EditAccountPresenter.TYPE_LETTER, 10).length(), 10);
        assertEquals(presenter.generatePassword(EditAccountPresenter.TYPE_NUM, 10).length(), 10);
        assertEquals(presenter.generatePassword(EditAccountPresenter.TYPE_NUM_LETTER, 10).length(), 10);
    }
}
