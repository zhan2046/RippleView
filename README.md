<!--lang: java-->
####效果如图：

![](https://github.com/ruzhan123/RippleView/raw/master/gif/ripple.gif)

</br>

我的博客：[详解](https://ruzhan123.github.io/2016/07/02/2016-07-02-18-00-RippleView%E6%B0%B4%E6%B3%A2%E7%BA%B9%EF%BC%8C%E6%B6%9F%E6%BC%AA%E6%95%88%E6%9E%9C/)

一个模拟涟漪，水波纹的自定义View。核心类就一个：RippleView.java


使用方式如下：



1，把RippleView.java拷贝到你的工程里，布局文件中直接使用（注意宽度和高度，特殊需求请自行设置好）：


```java

	    <RelativeLayout
	        android:layout_width="match_parent"
	        android:layout_height="match_parent"
	        android:layout_weight="1"
	        android:background="@color/blue_bg">
	
	        <com.zhan.rippleview.widget.RippleView
	            android:id="@+id/root_rv"
	            android:layout_width="match_parent"
	            android:layout_height="match_parent" />
	
	        <TextView
	            android:id="@+id/root_tv"
	            android:text="0%"
	            android:gravity="center"
	            android:textSize="16sp"
	            android:textColor="@android:color/white"
	            android:layout_centerInParent="true"
	            android:layout_width="wrap_content"
	            android:layout_height="wrap_content" />
	    </RelativeLayout>


```


2，找到RippleView对象，设置状态监听。

```java
	
		mRippleView = (RippleView) findViewById(R.id.root_rv);
		mRippleView.setRippleStateListener(this);


		 @Override
	    public void startRipple() {
	        
	    }
	
	    @Override
	    public void stopRipple() {
	
	    }
	
	    @Override
	    public void onRippleUpdate(ValueAnimator animation) {

	    }


```

3，你可以使用以下方法进行设置，也可以自行按需求添加新的方法：

```java

	    /** 水波纹状态接口对象注入 */
	    public void setRippleStateListener(RippleStateListener listener) 
	
		/** 开启水波纹 */
		public void startRipple()
	
	    /** 关闭水波纹 */
	    public void stopRipple() 
	
	    /** 设置圆半径 */
	    public void setRadius(int radius) 
	
	    /** 设置圆边线宽度 */
	    public void setStrokeWidth(int strokeWidth) 
	
	    /** 设置动画时间 */
	    public void setDuration(int duration) 
	
	    /** 设置动画次数 */
	    public void setRepeatCount(int repeatCount) 
	
	    /** 设置圆心 */
	    public void setCirclePoint(float x ,float y) 
	
	    /** 设置空心外圆颜色 */
	    public void setOutStrokePaintColor(int color) 
	
	    /** 设置空心内圆颜色 */
	    public void setInStrokePaintColor(int color) 
	
	    /** 设置实心内圆颜色 */
	    public void setInPaintColor(int color) 


```