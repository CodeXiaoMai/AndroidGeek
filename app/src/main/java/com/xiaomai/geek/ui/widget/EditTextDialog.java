
package com.xiaomai.geek.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaomai.geek.BuildConfig;
import com.xiaomai.geek.R;

/**
 * Created by XiaoMai on 2017/4/7 13:49.
 */

public class EditTextDialog extends Dialog {

    private EditTextDialog(Context context) {
        super(context, R.style.EditTextDialog);
    }

    public static class Builder {

        private String title;

        private String hint;

        private String editTextContent;

        private Context context;

        private boolean cancelable;

        private boolean isPassword;

        private float fontSize;

        private OnPositiveButtonClickListener listener;

        private View.OnClickListener negativeButtonClickListener;

        public interface OnPositiveButtonClickListener {
            void onClick(EditTextDialog dialog, TextInputLayout textInputLayout, String password);
        }

        public Builder(Context context) {
            this.context = context;
            this.hint = "请输入密码";
            this.cancelable = true;
            this.isPassword = true;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(String title, float fontSize) {
            this.title = title;
            this.fontSize = fontSize;
            return this;
        }

        public Builder setEditTextContent(String editTextContent) {
            this.editTextContent = editTextContent;
            return this;
        }

        public Builder setHint(String hint) {
            this.hint = hint;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setOnPositiveButtonClickListener(OnPositiveButtonClickListener listener) {
            this.listener = listener;
            return this;
        }

        public Builder setOnNegativeButtonClickListener(View.OnClickListener negativeButtonClickListener) {
            this.negativeButtonClickListener = negativeButtonClickListener;
            return this;
        }

        public Builder setIsPassword(boolean isPassword) {
            this.isPassword = isPassword;
            return this;
        }

        public EditTextDialog create() {
            final EditTextDialog mDialog = new EditTextDialog(context);
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_input_password, null);
            final TextInputLayout textInput = (TextInputLayout) view
                    .findViewById(R.id.layout_password);
            textInput.setHint(hint);
            final EditText editText = (EditText) view.findViewById(R.id.edit_password);
            if (BuildConfig.DEBUG) {
                editText.setText("123456");
            }
            if (!isPassword) {
                editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                textInput.setCounterEnabled(true);
                textInput.setCounterMaxLength(16);
            }
            if (!TextUtils.isEmpty(editTextContent)) {
                editText.setText(editTextContent);
                editText.setSelectAllOnFocus(true);
            }
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    textInput.setErrorEnabled(false);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            final TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
            tvTitle.setText(title);
            tvTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, fontSize == 0 ? 15f : fontSize);
            final View cancel = view.findViewById(R.id.bt_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDialog.isShowing())
                        mDialog.dismiss();
                    if (negativeButtonClickListener != null) {
                        negativeButtonClickListener.onClick(cancel);
                    }
                }
            });
            View ok = view.findViewById(R.id.bt_ok);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(mDialog, textInput, editText.getText().toString().trim());
                    }
                }
            });
            mDialog.setCancelable(cancelable);
            mDialog.setContentView(view);
            return mDialog;
        }
    }

}
