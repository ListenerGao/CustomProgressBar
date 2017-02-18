##CustomProgressBar
自定义带有进度显示的（水平方向或圆形）ProgressBar。该进度条可以设置进度条颜色，高度以及字体的大小机颜色。
##效果图
![](http://i.imgur.com/w7YjsI7.gif)

##使用

在你的build.gradle中添加依赖：

    compile 'com.listenergao:customprogressbarlib:1.0.0'

使用方法和普通progressBar一样：

    <ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
	    xmlns:listener="http://schemas.android.com/apk/res-auto"
	    android:layout_width="match_parent"
	    android:background="@color/colorWhite"
	    android:layout_height="match_parent">
	
	    <LinearLayout
	        android:layout_width="match_parent"
	        android:layout_height="wrap_content"
	        android:orientation="vertical">
	
	        <com.listenergao.customprogressbarlib.HorizontalProgressBarWithProgress
	            android:id="@+id/progress"
	            android:layout_width="match_parent"
	            android:layout_height="wrap_content"
	            android:layout_marginTop="30dp"
	            android:padding="15dp"
	            android:progress="50"
	            listener:progress_reach_color="#ffff0000"
	            listener:progress_text_color="#ffff0000"
	            listener:progress_unreach_color="#44ff0000" />
	
	        <com.listenergao.customprogressbarlib.HorizontalProgressBarWithProgress
	            android:layout_width="match_parent"
	            android:layout_height="wrap_content"
	            android:layout_marginTop="30dp"
	            android:padding="15dp"
	            android:progress="30"
	            listener:progress_reach_color="#90EE90"
	            listener:progress_reach_height="8dp"
	            listener:progress_text_color="#90EE90"
	            listener:progress_unreach_color="#4490EE90"
	            listener:progress_unreach_height="6dp" />
	
	        <com.listenergao.customprogressbarlib.RoundProgressBarWithProgress
	            android:id="@+id/round_progress"
	            android:layout_width="match_parent"
	            android:layout_height="wrap_content"
	            listener:radius="40dp"
	            android:layout_marginTop="30dp"
	            android:padding="15dp"
	            android:layout_gravity="center_horizontal"
	            android:progress="30"
	            listener:progress_reach_color="#ffff0000"
	            listener:progress_text_color="#ffff0000"
	            listener:progress_unreach_color="#44ff0000"
	            />
	
	        <com.listenergao.customprogressbarlib.RoundProgressBarWithProgress
	            android:layout_width="match_parent"
	            android:layout_height="wrap_content"
	            listener:radius="20dp"
	            android:layout_marginTop="30dp"
	            android:padding="15dp"
	            android:layout_gravity="center_horizontal"
	            android:progress="30"
	            listener:progress_reach_color="#ffff0000"
	            listener:progress_text_color="#ffff0000"
	            listener:progress_unreach_color="#44ff0000"
	            />
	
	        <com.listenergao.customprogressbarlib.RoundProgressBarWithProgress
	            android:layout_width="match_parent"
	            android:layout_height="wrap_content"
	            listener:radius="30dp"
	            android:layout_marginTop="30dp"
	            android:padding="15dp"
	            android:layout_gravity="center_horizontal"
	            android:progress="80"
	            listener:progress_reach_color="#90EE90"
	            listener:progress_reach_height="8dp"
	            listener:progress_text_color="#90EE90"
	            listener:progress_unreach_color="#4490EE90"
	            listener:progress_unreach_height="6dp"
	            />
	    </LinearLayout>
	</ScrollView>