package com.cjj.dragmenu;


import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
/**
 * 
 * 拖拉菜单
 * @author cjj
 *
 */
public class DragMenu extends FrameLayout{
	
	private ViewDragHelper mViewDragHelper;
	
	private GestureDetectorCompat mGestureDetectorCompat;
	
	private int dragLeft;
	
	private int dragWidth;
	
	private int width;
	
	private int height;
	
	private RelativeLayout dm_left_view;
	
	private DMMainLayout dm_main_view;
	
	private DragMenuListener mDragMenuListener;
	
	private DragMenuStatusEnum status = DragMenuStatusEnum.Close;
	
	public DragMenu(Context context) {
		super(context);
		init();
	}
	

	public DragMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}


	public DragMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		mViewDragHelper = ViewDragHelper.create(this,new DragHelperCallback());
		mGestureDetectorCompat = new GestureDetectorCompat(getContext(),new XScrollDetector());
	}
	
	class XScrollDetector extends SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return Math.abs(distanceY) <= Math.abs(distanceX);
		}
	}
	
	
	
	@Override
	protected void onFinishInflate() {
		dm_left_view = (RelativeLayout) getChildAt(0);
		dm_main_view = (DMMainLayout) getChildAt(1);
		dm_main_view.setDragLayout(this);
		dm_left_view.setClickable(true);
		dm_main_view.setClickable(true);
		super.onFinishInflate();
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		return mViewDragHelper.shouldInterceptTouchEvent(ev) && mGestureDetectorCompat.onTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		try {
			mViewDragHelper.processTouchEvent(e);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		width = dm_left_view.getMeasuredWidth();
		height = dm_left_view.getMeasuredHeight();
		dragWidth = (int) (width * 0.6);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		dm_left_view.layout(0, 0, width, height);
		dm_main_view.layout(dragLeft, 0, dragLeft + width, height);
	}

	
	public void setOnDragMenuListener(DragMenuListener mDragMenuListener) {
		this.mDragMenuListener = mDragMenuListener;
	}
	
	
	public ViewGroup getMainView()
	{
		return dm_main_view;
	}
	
	public ViewGroup getLeftView()
	{
		return dm_left_view;
	}
	
	public void open(boolean isAnim) {
		if (isAnim) {
			if (mViewDragHelper.smoothSlideViewTo(dm_main_view, dragWidth, 0)) {
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			dm_main_view.layout(dragWidth, 0, dragWidth * 2, height);
			dispatchDragEvent(dragWidth);
		}
	}
	
	
	public void close(boolean isAnim) {
		if (isAnim) {
			if (mViewDragHelper.smoothSlideViewTo(dm_main_view, 0, 0)) {
				ViewCompat.postInvalidateOnAnimation(this);
			}
		} else {
			dm_main_view.layout(0, 0, width, height);
			dispatchDragEvent(0);
		}
	}
	
	private void dispatchDragEvent(int dragLeft) {
		if (mDragMenuListener == null) {
			return;
		}
		float percent = dragLeft / (float) dragWidth;
		mDragMenuListener.onDragMenu(percent);
		DragMenuStatusEnum lastStatus = status;
		if (lastStatus != getStatus() && status == DragMenuStatusEnum.Close) {
			dm_left_view.setEnabled(false);
			mDragMenuListener.onClose();
		} else if (lastStatus != getStatus() && status == DragMenuStatusEnum.Open) {
			dm_left_view.setEnabled(true);
			mDragMenuListener.onOpen();
		}
	}
	
	public DragMenuStatusEnum getStatus()
	{
		int mainLeft = dm_main_view.getLeft();
		if (mainLeft == 0) {
			status = DragMenuStatusEnum.Close;
		} else if (mainLeft == dragWidth) {
			status = DragMenuStatusEnum.Open;
		} else {
			status = DragMenuStatusEnum.Drag;
		}
		return status;
	}
	
	@Override
	public void computeScroll() {
		if (mViewDragHelper.continueSettling(true)) {
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}
	
	public class DragHelperCallback extends ViewDragHelper.Callback{

		@Override
		public boolean tryCaptureView(View child, int pointerId) {
			return true;
		}
		
		@Override
		public int getViewHorizontalDragRange(View child) {
			return width;
		}
		
		@Override
		public int clampViewPositionHorizontal(View child, int left, int dx) {
			if (dragLeft + dx < 0) {
				return 0;
			} else if (dragLeft + dx > dragWidth) {
				return dragWidth;
			} else {
				return left;
			}
		}
		
		@Override
		public void onViewReleased(View releasedChild, float xvel, float yvel) {
			if (xvel > 0) {
				open(true);
			} else if (xvel < 0) {
				close(true);
			} else if (releasedChild == dm_main_view && dragLeft > dragWidth * 0.3) {
				open(true);
			} else if (releasedChild == dm_left_view && dragLeft > dragWidth * 0.7) {
				open(true);
			} else {
				close(true);
			}
		}
		
		@Override
		public void onViewPositionChanged(View changedView, int left, int top,
				int dx, int dy) {
			if (changedView == dm_main_view) {
				dragLeft = left;
			} else {
				dragLeft = dragLeft + left;
			}
			if (dragLeft < 0) {
				dragLeft = 0;
			} else if (dragLeft > dragWidth) {
				dragLeft = dragWidth;
			}
			dispatchDragEvent(dragLeft);
			if (changedView == dm_left_view) {
				dm_left_view.layout(0, 0, width, height);
				dm_main_view.layout(dragLeft, 0, dragLeft + width, height);
			}
		}
		
	}
	
	

}
