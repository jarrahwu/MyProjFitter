package com.jw.demo;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.jw.view.RoundImageView;

public class MainActivity extends Activity implements OnCheckedChangeListener {

	static class FragmentsHolder {

		final static TabFragment[] fragments = new TabFragment[] {
				new TabFragment("Ferrari", R.drawable.ferrari),
				new TabFragment("Lanbo", R.drawable.lanbo),
				new TabFragment("Koenigsegg", R.drawable.kgg),
				new TabFragment("AstonMatin", R.drawable.astonmartin) };
	}

	
	//tab id
	static final int Ferrari = R.id.tab0;
	static final int Lanbo = R.id.tab1;
	static final int Koenigsegg = R.id.tab2;
	static final int AstonMatin = R.id.tab3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RadioGroup group = (RadioGroup) findViewById(R.id.bottomTabs);
		group.setOnCheckedChangeListener(this);

		initFragmentsAndBar();
		
		test();
	}

	private void test() {
		RoundImageView img = (RoundImageView) findViewById(R.id.img);
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.astonmartin);
		img.setImageBitmap(bm);
		img.setPaddingBorder(100, R.drawable.bg_tab_checked);
	}

	private void initFragmentsAndBar() {
		
		if (!FragmentsHolder.fragments[0].isAdded()) {
			FragmentTransaction trans = getFragmentManager().beginTransaction();
			trans.add(R.id.fragmentContainer, FragmentsHolder.fragments[0]);
			trans.commit();
		}
		
		for (int i = 0; i < FragmentsHolder.fragments.length; i++) {
			setTabName(Ferrari + i, i);
		}
	}

	private void setTabName(int vid, int fgindex) {
		RadioButton rb = (RadioButton) findViewById(vid);
		rb.setText(FragmentsHolder.fragments[fgindex].getTitle());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.remove("android:fragments");//
		super.onSaveInstanceState(outState);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case Ferrari:
			replaceContentFragment(0);
			break;

		case Lanbo:
			replaceContentFragment(1);
			break;

		case Koenigsegg:
			replaceContentFragment(2);
			break;

		case AstonMatin:
			replaceContentFragment(3);
			break;

		default:
			break;
		}
	}

	private void replaceContentFragment(int fIndex) {
		TabFragment tabFragment = FragmentsHolder.fragments[fIndex];
		FragmentTransaction trans = getFragmentManager().beginTransaction();
		
		if(!tabFragment.isAdded())
			trans.addToBackStack(tabFragment.getFragmentName());
		
		trans.replace(R.id.fragmentContainer, tabFragment);
		
		trans.commit();
		
		Log.e("TAG", ""+getFragmentManager().getBackStackEntryCount());
	}
}
