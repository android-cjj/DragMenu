package com.cjj.dragmenu;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ViewGroup;

import com.nineoldandroids.view.ViewHelper;

public class BaseActivity extends FragmentActivity{
	public DragMenu dm;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public void setDragMenu(DragMenu dm)
	{
		this.dm = dm;
	}

	public void initDragMenu() {
		dm.setOnDragMenuListener(new DragMenuListener() {
			
			@Override
			public void onOpen() {
				
			}
			
			@Override
			public void onDragMenu(float percent) {
				animate(percent);
			}
			
			@Override
			public void onClose() {
				
			}
		});
	}
	
	
	private void animate(float percent) {
		ViewGroup vg_left = dm.getLeftView();
		ViewGroup vg_main = dm.getMainView();

		float f1 = 1 - percent * 0.3f;
		ViewHelper.setScaleX(vg_main, f1);
		ViewHelper.setScaleY(vg_main, f1);
		ViewHelper.setTranslationX(vg_left, -vg_left.getWidth() / 2.2f
				+ vg_left.getWidth() / 2.2f * percent);
		ViewHelper.setScaleX(vg_left, 0.5f + 0.5f * percent);
		ViewHelper.setScaleY(vg_left, 0.6f + 0.4f * percent);
		ViewHelper.setAlpha(vg_left, percent);
	}
}
