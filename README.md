# XLSeekBar
自定义的进度条以及滑动条的一体版本。

运行效果如下：
![](https://github.com/xiaoshitounen/XLSeekBar/blob/master/XlSeekBar.jpg)

使用方式：
~~~
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
~~~

~~~
dependencies {
  implementation 'com.github.xiaoshitounen:XLSeekBar:1.0.0'
}
~~~


# PageController 页面控制器 

详细内容博客地址:[自定义View-PageController](https://fanandjiu.com/%E8%87%AA%E5%AE%9A%E4%B9%89View-PageController/#more)

简介：

用于控制页面之间的切换，你可以自定义页面控制点的样式，页面控制点之间动画切换的效果，回调当前页面值。

app模块是使用例子，其运行效果：

![](https://github.com/xiaoshitounen/PageController/blob/master/self_view_page_controller.gif)

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
        implementation 'com.github.xiaoshitounen:PageController:1.0.3'
}
~~~

### 2. Xml文件中静态添加使用

~~~
<?xml version="1.0" encoding="utf-8"?>
<RelativeLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    tools:context=".MainActivity">

    <swu.xl.pagecontroller.PagerController
        android:id="@+id/page_controller"
        android:layout_centerInParent="true"
        android:background="#000000"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:numberOfPage="5"
        app:pageResource="@drawable/page_controller_shape"
        app:pagePadding="30"
        android:paddingTop="20dp"
        android:paddingBottom="20dp"
        />

</RelativeLayout>
~~~

#### ① 属性

- numberOfPage：设置有多少个页面
- pagePadding：设置每个页面之间的间距
- pageResource：设置页面选中以及没有选中的样式

#### ② 页面样式

页面样式是一个xml文件，你可以自己定义你想要的样式。

例子：
~~~
<?xml version="1.0" encoding="utf-8"?>
<selector xmlns:android="http://schemas.android.com/apk/res/android">

    <!--enable:false的情况-->
    <item android:state_enabled="false">
        <shape android:shape="oval">
            <!--大小-->
            <size android:width="40dp"
                android:height="40dp"
                />

            <!--颜色-->
            <solid android:color="#ff0000"/>
        </shape>
    </item>

    <!--enable:true的情况，也是默认的情况-->
    <item android:state_enabled="true">
        <shape android:shape="oval">
            <!--大小-->
            <size android:width="40dp"
                android:height="40dp"
                />

            <!--颜色-->
            <solid android:color="#666666"/>
        </shape>
    </item>
</selector>
~~~

#### ③ 页面之间动画切换

在Java代码里面创建动画切换类，并实现动画切换的方法。
你可以对上一个页面和当前页面控制点进行动画。

~~~
//初始化
PagerController pagerController = findViewById(R.id.page_controller);

//设置动画
pagerController.setPageChangeAnimation(new PagerController.PageChangeAnimation() {
    @Override
    public void changeAnimation(View last_dot, View current_dot) {
        
    }
});
~~~

例子：

~~~
//初始化
PagerController pagerController = findViewById(R.id.page_controller);

//设置动画
pagerController.setPageChangeAnimation(new PagerController.PageChangeAnimation() {
    @Override
    public void changeAnimation(View last_dot, View current_dot) {
        //上一个点不做动画

        //针对当前点的动画
        ObjectAnimator scale = ObjectAnimator.ofFloat(current_dot, "scaleX", 1.0f, 1.2f, 1.0f);
        scale.setDuration(500);
        scale.start();
    }
});
~~~

#### 4.回调当前页面

回调的`currentPage`是页面的索引值而不是序列值。

~~~
//监听页面切换
pagerController.setPageChangeListener(new PagerController.PageChangeListener() {
    @Override
    public void pageHasChange(int currentPage) {
        System.out.println("当前页面："+(currentPage+1));
    }
});
~~~

### 3. Java代码中动态添加

需要注意一个地方，注意调用各个set方法的顺序，注意setNumberOfPage的位置。
