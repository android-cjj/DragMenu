package com.cjj.dragmenu;

import android.os.Bundle;

public class CjjTestActivity extends BaseActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		findView();
	}

	private void findView() {
		setDragMenu((DragMenu)findViewById(R.id.dm));
		initDragMenu();
	}
}
