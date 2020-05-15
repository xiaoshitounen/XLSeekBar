# XLSeekBar

详细内容博客地址:[自定义View-XLSeekBar](https://fanandjiu.com/%E8%87%AA%E5%AE%9A%E4%B9%89View-XLSeekBar/#more)

简介：
自定义的进度条以及滑动条的一体版本。
不仅可以是进度条还可以是滚动条，不仅可以水平放置，还可以竖直放置。

app模块是使用例子，其运行效果：
<img src="https://android-1300729795.cos.ap-chengdu.myqcloud.com/project/Self_View/XLSeekBar/XlSeekBar.jpg" style="zoom:67%;" />


### 1. 添加依赖

Step 1. Add the JitPack repository to your build file

Add it in your root build.gradle at the end of repositories:

~~~
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
~~~

Step 2. Add the dependency

~~~
dependencies {
  implementation 'com.github.xiaoshitounen:XLSeekBar:1.0.1'
}
~~~

### 2. Xml文件中静态添加使用

~~~xml
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <swu.xl.xlseekbar.XLSeekBar
        android:layout_marginTop="30dp"
        android:background="#999999"
        android:layout_width="match_parent"
        android:layout_height="30dp"
        app:currentProgress="80"
        app:maxProgress="100"
        app:lineStyle="PROGRESS"
        />

    <swu.xl.xlseekbar.XLSeekBar
        android:id="@+id/seek_bar_hor"
        android:layout_marginTop="90dp"
        android:background="#999999"
        android:layout_width="match_parent"
        android:layout_height="30dp"
        app:currentProgress="50"
        app:maxProgress="100"
        app:lineStyle="SLIDE"
        />

    <swu.xl.xlseekbar.XLSeekBar
        android:id="@+id/seek_bar_ver"
        android:layout_centerInParent="true"
        android:background="#999999"
        android:layout_width="30dp"
        android:layout_height="300dp"
        app:currentProgress="50"
        app:maxProgress="100"
        app:lineStyle="SLIDE"
        app:lineColorBg="#ffffff"
        app:lineColorProgress="@color/colorAccent"
        />

</RelativeLayout>
~~~

#### ① 属性

- lineSize：线条的宽度
- lineColorBg：线条的背景颜色
- lineColorProgress：线条的进度颜色
- lineStyle：线条的样式，PROGRESS是进度条，SLIDE是滑动条
- currentProgress：当前的进度
- maxProgress：总的进度

#### 4. 滑动条回调进度

回调的`currentPage`是页面的索引值而不是序列值。

~~~java
MySeekBar mySeekBarHor = findViewById(R.id.seek_bar_hor);
MySeekBar mySeekBarVer = findViewById(R.id.seek_bar_ver);

mySeekBarHor.setProgressChangeListener(new MySeekBar.OnProgressChangeListener() {
    @Override
    public void progressChanged(float progress) {
        Toast.makeText(MainActivity.this, "横向滑动条的进度:"+progress, Toast.LENGTH_SHORT).show();
    }
});

mySeekBarVer.setProgressChangeListener(new MySeekBar.OnProgressChangeListener() {
    @Override
    public void progressChanged(float progress) {
        Toast.makeText(MainActivity.this, "竖向滑动条的进度:"+progress, Toast.LENGTH_SHORT).show();
    }
});
~~~

### 3. Java代码中动态添加

注意Java代码创建在构造方法中需要传入预设置的值。
