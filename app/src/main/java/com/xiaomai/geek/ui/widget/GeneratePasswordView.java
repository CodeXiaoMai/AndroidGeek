package com.xiaomai.geek.ui.widget;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.xiaomai.geek.R;

import java.util.Random;

/**
 * Created by XiaoMai on 2017/11/16.
 */

public class GeneratePasswordView extends FrameLayout {

    public static final int TYPE_ALL = 0;

    public static final int TYPE_NUM = 1;

    public static final int TYPE_LETTER = 2;

    public static final int TYPE_NUM_LETTER = 3;

    @IntDef({
            TYPE_ALL, TYPE_NUM, TYPE_LETTER, TYPE_NUM_LETTER
    })
    public @interface PasswordType {
    }

    private char[] nums = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'
    };

    private char[] letters = {
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q',
            'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
            'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private char[] numAndLetters = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
            'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O',
            'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private char[] all = {
            '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '-', '=', '[', ']',
            '{', '}', ':', ';', '"', '\'', '|', '\\', '<', '>', ',', '.', '?', '/', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A',
            'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    private Callback mCallback;

    private int mLength = 6;

    private int mPasswordType = TYPE_ALL;

    private String mPassword;

    public GeneratePasswordView(@NonNull Context context) {
        this(context, null);
    }

    public GeneratePasswordView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GeneratePasswordView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        View rootView = LayoutInflater.from(context).inflate(R.layout.generate_password_view, this, true);
        final TextView tvPassword = (TextView) rootView.findViewById(R.id.tv_password);
        SeekBar seekBar = (SeekBar) rootView.findViewById(R.id.seek_bar);
        final TextView tvLength = (TextView) rootView.findViewById(R.id.tv_length);
        Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);
        View refresh = rootView.findViewById(R.id.refresh);
        final View cancel = rootView.findViewById(R.id.bt_cancel);
        View ok = rootView.findViewById(R.id.bt_ok);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mLength = progress + 6;
                tvLength.setText("密码长度：" + mLength);
                mPassword = getPassword();
                tvPassword.setText(mPassword);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        mPasswordType = TYPE_ALL;
                        break;
                    case 1:
                        mPasswordType = TYPE_NUM_LETTER;
                        break;
                    case 2:
                        mPasswordType = TYPE_NUM;
                        break;
                    case 3:
                        mPasswordType = TYPE_LETTER;
                        break;
                }
                mPassword = getPassword();
                tvPassword.setText(mPassword);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPassword = getPassword();
                tvPassword.setText(mPassword);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback == null) {
                    return;
                }

                mCallback.onCancel();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallback == null) {
                    return;
                }

                mCallback.onConfirm(mPassword);
            }
        });
    }

    private String getPassword() {
        switch (mPasswordType) {
            case TYPE_ALL:
                return getPassword(all, mLength);
            case TYPE_NUM:
                return getPassword(nums, mLength);
            case TYPE_LETTER:
                return getPassword(letters, mLength);
            case TYPE_NUM_LETTER:
                return getPassword(numAndLetters, mLength);
            default:
                return "";
        }
    }

    private String getPassword(char[] source, int length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        while (i < length) {
            stringBuilder.append(source[random.nextInt(source.length)]);
            i++;
        }
        return stringBuilder.toString();
    }

    public void setCallback(Callback callback) {
        mCallback = callback;
    }

    public interface Callback {
        void onCancel();

        void onConfirm(String content);
    }
}
