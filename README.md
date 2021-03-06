# Integrating the BlueKai SDK

## Download the BlueKai SDK for Android

- [Full SDK](http://bluekai.github.io/bluekai_android_sdk-v2.zip)
      
The current version of the SDK is 2.0.0.

## Updating the SDK 

Update, unless otherwise indicated, can be done by just copying over
the previous version. 

### Obtain BlueKai site ID

For any demo projects a site id of "2" can be used.

### Add BlueKai SDK to Project

Copy the downloaded SDK (jar file) to your local folder. In your
project, create a folder named “libs” if it does not exist already.

   ![Screenshot](http://bluekai.github.io/images/android/image001.png)

Copy bluekai.jar to the libs folder. Right click on your project and
select “Properties”.

   ![Screenshot](http://bluekai.github.io/images/android/image003.png)

Select “Java Build Path” from the left side of the properties window
and select “Libraries” tab from the right side.

   ![Screenshot](http://bluekai.github.io/images/android/image005.png)

Click “Add JARs” button. Select “bluekai.jar” from project/libs/
folder and select “OK” button.

   ![Screenshot](http://bluekai.github.io/images/android/image007.png)

Goto “Order and Export” tab on the properties window and check
“bluekai.jar” and click “OK”.

   ![Screenshot](http://bluekai.github.io/images/android/image009.png)

### Edit AndroidManifest.xml

BlueKai SDK needs the following permissions to work properly. Add
these permissions in `AndroidManifest.xml`.


```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

To show BlueKai in-built user opt-in opt-out screen, add the following
activity in `AndroidManifest.xml`. This is required only if BlueKai
in-built opt-in opt-out screen has to be shown.


```xml
<activity android:name="com.bluekai.sdk.SettingsActivity" />
```

### Obfuscating

If your project is using proguard to obfuscate, BlueKai SDK must be
excluded from getting obfuscated or shrinked. To do this, add the
following to your `proguard-project.txt` or proguard configuration
file.


```
-keep class com.bluekai.** {*;}
```

### Import the SDK 

In `MainActivity` add import statement to include the SDK

```java
import com.bluekai.sdk.BlueKai;
```

### Optionally add notification support 

Import the following interface/listener to receive status
notifications when data is posted to BlueKai server


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

Instance to BlueKai SDK can be created anywhere as required. Usually
it is created in `onCreate()` method of `MainActivity`


```java
BlueKai bk = BlueKai.getInstance(activity, context, devMode, httpsEnabled, siteId, appVersion, listener, handler, useWebView);
```

The `getInstance()` method accepts the following params:

* Activity activity
* Context appContext
* boolean devMode - Flag to enable devMode for debugging. If this is on, then actual calls are not made but the URLs are displayed in toast
* boolean httpsEnabled - Flag to determine if https or http would be used to connect
* String siteId - The site ID
* String version - This value can be “[app name]-[app version]”
* DataPostedListener listener - Listener callback implementation to be called when data is sent
* Handler handler - Handler object from UI Thread
* boolean useWebView - Flag determines if a web view would be used or direct calls would be made to tags server. It is recommended to set this value to true

**NOTE:** For all the calls, irrespective of whether using webview or making a direct call, the SDK will try to fetch the Advertising ID and send it as 'adid' query parameter. If user has limited ad tracking enabled from Google Settings or it is not supported by the device then the value will not be sent. 

### Passing a value

```java
bk.put(key, value);
```
**NOTE:** For data to be sent, if using BlueKai's SettingsActivity to show the opt-in screen, user should have the 'Allow BlueKai to receive my data' setting checked. This setting can also be set/unset programmatically using the following method. Setting it to true allows collection and sending of data, whereas if it's false, there's no collection or sending of data.

```java
bk.setOptInPreference(true);
```

### Passing multiple values

Create a `Map<String, String>` and populate the map with key and
values. Pass the map to BlueKai using

```java
bk.putAll(map);
```

### Resuming data post

The `resume()` method should be invoked on the calling activity's
`onResume()` callback. This should be done in order to send out any
queued data, which could not be sent because the application was
closed or due to network issues. Override the `onResume()` method and
call the `resume()` method as follows

```java
@Override
protected void onResume() {
   super.onResume();
   bk.resume();
}
```

### Monitoring post status (optional)

To get notifications about the status of data posting, implement the
following callback method in `MainActivity`


```java
@Override
public void onDataPosted(boolean success, String message){
}
```

### Public methods

Return | Method | Description
--- | --- | ---
static BlueKai | getInstance() |Convenience method to initialize and get instance of BlueKai without arguments. Should be used to get the instance after it has already been created using other overloaded methods with parameters.
static BlueKai | getInstance(Activity activity, Context context, boolean devMode, boolean httpsEnabled, String siteId,String appVersion, DataPostedListener listener, Handler handler) |*Deprecated* in favor of overloaded getInstance method which also takes flag for useWebView. Method to get BlueKai instance.
static BlueKai | getInstance(Activity activity, Context context, boolean devMode, String siteId,String appVersion, DataPostedListener listener, Handler handler) |*Deprecated* in favor of overloaded getInstance method which also takes flag for useWebView. Method to get BlueKai instance. httpsEnabled flag will default to false. useWebView flag defauls to true for backwards compatibility.
static BlueKai | getInstance(Activity activity, Context context, boolean devMode, boolean httpsEnabled, String siteId, String appVersion, DataPostedListener listener, Handler handler, boolean useWebView) | **Recommended** method to get Bluekai instance. The flag useWebView can be set to false to make direct calls to tags server without using web view. If it's true, then web view would be used. 
void | putAll(Map map) |Convenience method to send a bunch of key-value pairs to BlueKai
void | put(String key, String value) |Method to send data to BlueKai. Accepts a single key-value pair
void | resume() |Method to resume BlueKai process after calling application resumes. To use in onResume() of the calling activity
void | setActivity(Activity activity) |Set the calling activity reference
Activity | getActivity() | Get calling activity reference
void | setAppContext(Context context) |Set the calling application context
Context | getContext() | Get the calling application context
void | setAppVersion(String appVersion) |Set the calling application's version
String | getAppVersion() | Get calling application's version
void | setDataPostedListener(DataPostedListener listener) |Set the DataPostedListener to get notifications about status of a data posting. Calling activity should implement this interface
DataPostedListener | getDataPostedListener() | Get the configured DataPostedListener
void | setDevMode(boolean devMode) |Set developer mode (True or False)
boolean | isDevMode() | Get developer mode
void | setHttpsEnabled(boolean httpsEnabled) |Set https enabled (True or False)
boolean | isHttpsEnabled() | Get httpsEnabled flag
void | setOptIn(boolean optin) |Method to set user opt-in or opt-out preference. *Deprecated* in favor of setOptInPreference method
void | setOptInPreference(boolean optin) |Method to set user opt-in or opt-out preference
boolean | getOptInPreference() | Method to get user opt-in or opt-out preference  
void | setSiteId(String siteId) |Set BlueKai site id
String | getSiteId() | Get BlueKai site id
void | setHandler(Handler handler) |Set Handler to get data posting updates
Handler | getHandler() | Get Handler configured to get data posting updates
void | showSettingsScreen(SettingsChangedListener listener) |Method to show BlueKai in-built opt-in screen. Requires `<activity android:name="com.bluekai.sdk.SettingsActivity" />` in AndroidManifest.xml to be present

# Sample Application 

A sample application is available in the main repository.  After
building you should see the following: 

![Screenshot](http://bluekai.github.io/images/android/BlueKaiActivity-release-unsigned.png)

You can also test this directly by installing 

- [BlueKai Sample Application](http://bluekai.github.io/images/android/BlueKaiActivity-release-unsigned.apk)
