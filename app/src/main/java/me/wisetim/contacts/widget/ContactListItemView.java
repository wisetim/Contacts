package me.wisetim.contacts.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Objects;

import me.wisetim.R;
import me.wisetim.contacts.util.DensityUtil;

public class ContactListItemView extends LinearLayout implements Checkable {
    private static final int MODE_NORMAL = 0;
    private static final int MODE_MULTI_CHOICE = 1;

    private TextView mContactCatalog;
    private TextView mContactLine;
    private CheckedTextView mContactNameCTV;
    private TextView mContactNameTV;

    //attrs
    private String mContactNameText;
    private int mTextColor;
    private int mBackground;
    private Drawable mCheckMark;
    private Drawable mContactIcon;
    private int mItemMode;

    public ContactListItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ContactListItemView);
        mContactNameText = ta.getString(R.styleable.ContactListItemView_contactNameText);
        mTextColor = ta.getColor(R.styleable.ContactListItemView_textColor, 0x000000);
        mBackground = ta.getColor(R.styleable.ContactListItemView_contactBackground, 0xffffff);
        mCheckMark = ta.getDrawable(R.styleable.ContactListItemView_checkMark);
        mContactIcon = ta.getDrawable(R.styleable.ContactListItemView_contactIcon);
        Objects.requireNonNull(mContactIcon).setBounds(0, 0, 96, 96);
        mItemMode = ta.getInt(R.styleable.ContactListItemView_itemMode,
                MODE_NORMAL);
        ta.recycle();
        mContactCatalog = new TextView(context);
        addCatalogView(context);
        mContactLine = new TextView(context);
        addLineView(context);
        if (mItemMode == MODE_MULTI_CHOICE) {
            mContactNameCTV = new CheckedTextView(context);
            addContactNameCheckedTextView(context);
        } else if (mItemMode == MODE_NORMAL) {
            mContactNameTV = new TextView(context);
            addContactNameTextView(context);
        }
    }

    private void addCatalogView(Context context) {
        DensityUtil util = new DensityUtil(context);
        LayoutParams catalogParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        mContactCatalog.setBackgroundColor(context.getResources()
                .getColor(R.color.catalogBackground));
        mContactCatalog.setPadding(
                util.dp2px(10),
                util.dp2px(5),
                util.dp2px(10),
                util.dp2px(5));
        mContactCatalog.setTextColor(context.getResources()
                .getColor(R.color.catalogTextColor));
        addView(mContactCatalog, catalogParams);
    }

    private void addLineView(Context context) {
        LayoutParams lineParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                1);
        mContactLine.setBackground(context.getResources()
                .getDrawable(R.drawable.bg_cut_line));
        addView(mContactLine, lineParams);
    }

    private void addContactNameCheckedTextView(Context context) {
        DensityUtil util = new DensityUtil(context);
        LayoutParams nameParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        mContactNameCTV.setCheckMarkDrawable(mCheckMark);
        mContactNameCTV.setBackgroundColor(mBackground);
        mContactNameCTV.setCompoundDrawablesRelative(mContactIcon, null, null, null);
        mContactNameCTV.setCompoundDrawables(mContactIcon, null, null, null);
        mContactNameCTV.setEllipsize(TextUtils.TruncateAt.END);
        mContactNameCTV.setGravity(Gravity.CENTER_VERTICAL);
        mContactNameCTV.setPadding(
                util.dp2px(16),
                util.dp2px(4),
                util.dp2px(16),
                util.dp2px(4));
        mContactNameCTV.setTextColor(mTextColor);
        mContactNameCTV.setTextSize(18);
        mContactNameCTV.setText(mContactNameText);
        addView(mContactNameCTV, nameParams);
    }

    private void addContactNameTextView(Context context) {
        DensityUtil util = new DensityUtil(context);
        LayoutParams nameParams = new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        mContactNameTV.setBackgroundColor(mBackground);
        mContactNameTV.setCompoundDrawablesRelative(mContactIcon, null, null, null);
        mContactNameTV.setCompoundDrawables(mContactIcon, null, null, null);
        mContactNameTV.setEllipsize(TextUtils.TruncateAt.END);
        mContactNameTV.setGravity(Gravity.CENTER_VERTICAL);
        mContactNameTV.setPadding(
                util.dp2px(16),
                util.dp2px(4),
                util.dp2px(16),
                util.dp2px(4));
        mContactNameTV.setTextColor(mTextColor);
        mContactNameTV.setTextSize(18);
        mContactNameTV.setText(mContactNameText);
        addView(mContactNameTV, nameParams);
    }

    public void setContactNameText(CharSequence text) {
        if (mItemMode == MODE_MULTI_CHOICE) {
            mContactNameCTV.setText(text);
        } else if (mItemMode == MODE_NORMAL) {
            mContactNameTV.setText(text);
        }
    }

    public void setCatalogLetter(CharSequence text) {
        mContactCatalog.setText(text);
    }

    public void setCatalogAndLineVisibility(int visibility) {
        mContactCatalog.setVisibility(visibility);
        mContactLine.setVisibility(visibility);
    }

    @Override
    public void setChecked(boolean checked) {
        if (mItemMode == MODE_NORMAL) return;
        mContactNameCTV.setChecked(checked);
    }

    @Override
    public boolean isChecked() {
        return mItemMode != MODE_NORMAL
                && mContactNameCTV.isChecked();
    }

    @Override
    public void toggle() {
        if (mItemMode == MODE_NORMAL) return;
        mContactNameCTV.toggle();
    }
}
