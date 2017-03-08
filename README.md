
RippleView
===============

A simple ripple view for Android


-----


![](https://github.com/ruzhan123/RippleView/raw/master/gif/ripple.gif)




RippleView use **Animation** change circle radius draw circle

[![](https://jitpack.io/v/ruzhan123/RippleView.svg)](https://jitpack.io/#ruzhan123/RippleView)

Gradle
------

Add it in your root build.gradle at the end of repositories:


```java

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Add the dependency:


```java

	dependencies {
	         compile 'com.github.ruzhan123:RippleView:v1.0'
	}
```

Usage
-----

```xml

	<zhan.rippleview.RippleView
	    android:id="@+id/root_rv"
	    app:radius="200"
	    app:stroke_width="1"
	    app:duration="3000"
	    app:repeat_count="1"
	    app:two_ripple_times="1.5"
	    app:three_ripple_times="2.0"
	    app:max_more_radius_times="1.5"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent" />
```

Developed by
-------

 ruzhan - <a href='javascript:'>dev19921116@gmail.com</a>


License
-------

    Copyright 2017 ruzhan

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
