# Integrating the BlueKai SDK
## Download the BlueKai SDK for Android

- [Full SDK](http://199.204.23.142/bk-mobile/BlueKai_Android_SDK-20131122.zip)

### Obtain BlueKai site ID

For any demo projects a site id of "2" can be used.


### Add BlueKai SDK to Project

Copy the downloaded SDK (jar file) to your local folder. In your project, create a folder named “libs” if it does not exist already

   ![Screenshot](http://199.204.23.142/bk-mobile/android/image001.png)

Copy bluekai.jar to the libs folder. Right click on your project and select “Properties”

   ![Screenshot](http://199.204.23.142/bk-mobile/android/image003.png)

Select “Java Build Path” from the left side of the properties window and select “Libraries” tab from the right side

   ![Screenshot](http://199.204.23.142/bk-mobile/android/image005.png)

Click “Add JARs” button. Select “bluekai.jar” from project/libs/ folder and select “OK” button

   ![Screenshot](http://199.204.23.142/bk-mobile/android/image007.png)

Goto “Order and Export” tab on the properties window and check “bluekai.jar” and click “OK”

   ![Screenshot](http://199.204.23.142/bk-mobile/android/image009.png)

### Edit AndroidManifest.xml

BlueKai SDK needs the following permissions to work properly. Add these permissions in `AndroidManifest.xml`

```xml
<uses-permission android:name="android.permission.INTERNET"/>
<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
```

To show BlueKai in-built user opt-in opt-out screen, add the following activity in `AndroidManifest.xml`. This is required only if BlueKai in-built opt-in opt-out screen has to be shown

```xml
<activity android:name="com.bluekai.sdk.SettingsActivity" />
```

### Obfuscating

If your project is using proguard to obfuscate, BlueKai SDK must be excluded from getting obfuscated or shrinked. To do this, add the following to your `proguard-project.txt` or proguard configuration file

```
-keep class com.bluekai.** {*;}
```

BlueKai SDK uses Android support library. If your project does not use Android support library, proguard should be configured not to warn about support library. Add the following line to `proguard-project.txt` or proguard configuration file if it does not exist already

```
-dontwarn android.support.**
```

### Import the SDK 

In `MainActivity` add import statement to include the SDK

```java
import com.bluekai.sdk.BlueKai;
```

### Optionally add notification support 

Import the following interface/listener to receive status notifications when data is posted to BlueKai server

```java
import com.bluekai.sdk.listeners.DataPostedListener;
```

and add the following code to `MainActivity` class declaration

```java
implements DataPostedListener
```

Example:

```java
public class MainActivity extends Activity implements DataPostedListener{
}
```

### Creating instance

Instance to BlueKai SDK can be created anywhere as required. Usually it is created in `onCreate()` method of `MainActivity`

```java
BlueKai bk = BlueKai.getInstance(activity, context, devMode, siteId, appVersion, listener, handler);
```

The `getInstance()` method accepts the following params:

* Activity activity
* Context appContext
* Boolean devMode
* String siteId
* String version. This value can be “[app name]-[app version]”
* BKViewListener listener
* Handler handler

### Passing a value

```java
bk.put(key, value);
```

### Passing multiple values

Create a `Map<String, String>` and populate the map with key and values. Pass the map to BlueKai using

```java
bk.put(map);
```

### Resuming data post

The `resume()` method should be invoked on the calling activity's `onResume()` callback. This should be done in order to send out any queued data, which could not be sent because the application was closed or due to network issues. Override the `onResume()` method and call the `resume()` method as follows

```java
@Override
protected void onResume() {
   super.onResume();
   bk.resume();
}
```

### Monitoring post status (optional)

To get notifications about the status of data posting, implement the following callback method in `MainActivity`

```java
@Override
public void onDataPosted(boolean success, String message){
}
```

### Public methods

Return | Method | Description
--- | --- | ---
static BlueKai | getInstance() |Convenience method to initialize and get instance of BlueKai without arguments.
static BlueKai | getInstance(Activity activity, Context context, boolean devMode, String siteId,String appVersion, DataPostedListener listener, Handler handler) |Method to get BlueKai instance
void | put(Map map) |Convenience method to send a bunch of key-value pairs to BlueKai
void | put(String key, String value) |Method to send data to BlueKai. Accepts a single key-value pair
void | resume() |Method to resume BlueKai process after calling application resumes. To use in onResume() of the calling activity
void | setActivity(Activity activity) |Set the calling activity reference
void | setAppContext(Context context) |Set the calling application context
void | setAppVersion(String appVersion) |Set the calling application's version
void | setDataPostedListener(DataPostedListener listener) |Set the DataPostedListener to get notifications about status of a data posting. Calling activity should implement this interface
void | setDevMode(boolean devMode) |Set developer mode (True or False)
void | setFragmentManager(FragmentManager fm) |Set the fragment manager from calling FragmentActivity. Used when devMode is enabled to show webview in a popup dialog. Calling activity should be FragmentActivity
void | setOptIn(boolean optin) |Method to set user opt-in or opt-out preference
void | setSiteId(String siteId) |Set BlueKai site id
void | setHandler(Handler handler) |Set Handler to get data posting updates
void | showSettingsScreen(SettingsChangedListener listener) |Method to show BlueKai in-built opt-in screen. Requires `<activity android:name="com.bluekai.sdk.SettingsActivity" />` in AndroidManifest.xml to be present

# Sample Application 

A sample application is available in the main repository.  After
building you should see the following: 

![Screenshot](http://199.204.23.142/bk-mobile/android/BlueKaiActivity-release-unsigned.png)

You can also test this directly by installing 

  http://199.204.23.142/bk-mobile/android/BlueKaiActivity-release-unsigned.apk
   
If monitoring network requests you would see something like: 

```
67.136.221.74 - - [09/Aug/2013:00:52:19 +0400] "GET /site/2?ret=html&phint=k2%3Dv2&phint=appVersion%3D4.1.6&phint=identifierForVendor%3D201308081644420464&phint=mobile%3Dapp&bkfpd=TF1;015;;;;;;;;;;;;;;;;;;;;;;Mozilla;Netscape;5.0%20%28Linux%3B%20U%3B%20Android%204.3%3B%20en-us%3B%20sdk%20Build/JWR66V%29%20AppleWebKit/534.30%20%28KHTML%2C%20like%20Gecko%29%20Version/4.0%20Safari/534.30;20030107;undefined;true;;true;Linux%20armv7l;undefined;Mozilla/5.0%20%28Linux%3B%20U%3B%20Android%204.3%3B%20en-us%3B%20sdk%20Build/JWR66V%29%20AppleWebKit/534.30%20%28KHTML%2C%20like%20Gecko%29%20Version/4.0%20Safari/534.30;en-US;Latin-1;199.204.23.142;undefined;undefined;undefined;undefined;true;true;1375995139684;-5;Tue%20Jun%2007%202005%2021%3A33%3A44%20GMT-0400%20%28EDT%29;800;1280;;;;;;;21;300;240;Thu%20Aug%2008%202013%2016%3A52%3A19%20GMT-0400%20%28EDT%29;16;800;1280;0;0;;;;;;;;;;;;;;;;;;;19;&bknms=1,800128016,1375995139545,240,0,0,1&bkrid=981067699&r=37344997 HTTP/1.1" 200 102 "http://199.204.23.142/m/2?k2=v2&appVersion=4.1.6&identifierForVendor=201308081644420464" "Mozilla/5.0 (Linux; U; Android 4.3; en-us; sdk Build/JWR66V) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30"
```

