package com.cjj.dragmenu;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 
 * 主界面
 * @author cjj
 *
 */
public class DMMainLayout extends RelativeLayout{
	private DragMenu dm; 

	public DMMainLayout(Context context) {
		super(context);
	}

	public DMMainLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public DMMainLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setDragLayout(DragMenu dm) {
		this.dm = dm;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (dm.getStatus() != DragMenuStatusEnum.Close) {
			return true;
		}
		return super.onInterceptTouchEvent(event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (dm.getStatus() != DragMenuStatusEnum.Close) {
			if (event.getAction() == MotionEvent.ACTION_UP) {
				dm.close(true);
			}
			return true;
		}
		return super.onTouchEvent(event);
	}
	

}
