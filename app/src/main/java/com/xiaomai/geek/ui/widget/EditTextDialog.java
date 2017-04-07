
package com.xiaomai.geek.ui.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.xiaomai.geek.R;

/**
 * Created by XiaoMai on 2017/4/7 13:49.
 */

public class EditTextDialog extends Dialog {

    private EditTextDialog(Context context) {
        super(context);
    }

    public static class Builder {

        private String title;

        private String hint;

        private Context context;

        private boolean cancelable;

        private OnPositiveButtonClickListener listener;

        public interface OnPositiveButtonClickListener {
            void onClick(TextInputLayout textInputLayout, String password);
        }

        public Builder(Context context) {
            this.context = context;
            this.hint = "请输入密码";
        }

        public Builder setTitle(String title) {
            this.title = title;
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

        public EditTextDialog create() {
            final EditTextDialog mDialog = new EditTextDialog(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.dialog_input_password, null);
            final TextInputLayout textInput = (TextInputLayout) view.findViewById(R.id.layout_password);
            textInput.setHint(hint);
            final EditText editText = (EditText) view
                    .findViewById(R.id.edit_password);
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
            View cancel = view.findViewById(R.id.bt_cancel);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mDialog.isShowing())
                        mDialog.dismiss();
                }
            });
            View ok = view.findViewById(R.id.bt_ok);
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onClick(textInput, editText.getText().toString().trim());
                    }
                }
            });
            mDialog.setCancelable(cancelable);
            mDialog.setContentView(view);
            return mDialog;
        }
    }
}
