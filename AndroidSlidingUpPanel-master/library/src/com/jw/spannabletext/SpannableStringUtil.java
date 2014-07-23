package com.jw.spannabletext;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;

public class SpannableStringUtil {

	/**
	 * 获取特定颜色的文字
	 * @param text
	 * @param color Color.RGB()
	 */
	public SpannableString getColorSpannableString(String text, int color) {
		SpannableString spanString = new SpannableString(text);
		ForegroundColorSpan span = new ForegroundColorSpan(color);
		spanString.setSpan(span, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanString;
	}
	
	public SpannableString getSizeSpannableString(String text, int size) {  
	    SpannableString spanString = new SpannableString(text);  
	    AbsoluteSizeSpan span = new AbsoluteSizeSpan(size); 
	    spanString.setSpan(span, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	    return spanString;
	}  
	
	
	/**
	 * 粗体span
	 * @param text
	 * @return
	 */
	public SpannableString getBoldSpannableString(String text) {
		SpannableString spanString = new SpannableString(text);  
		StyleSpan bold = new StyleSpan(Typeface.BOLD);
		spanString.setSpan(bold, 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return spanString;
	}
}
